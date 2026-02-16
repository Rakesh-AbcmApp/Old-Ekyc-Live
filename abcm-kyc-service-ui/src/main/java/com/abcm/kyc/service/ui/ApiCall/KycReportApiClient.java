package com.abcm.kyc.service.ui.ApiCall;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.abcm.kyc.service.ui.dto.KycReportRequestModel;
import com.abcm.kyc.service.ui.dto.MerchantRoutingUiRequest;
import com.abcm.kyc.service.ui.dto.ProductDTO;
import com.abcm.kyc.service.ui.dto.RoutingResponseContainer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;


@Service
public class KycReportApiClient {

	private static final Logger log = LoggerFactory.getLogger(KycReportApiClient.class);
	private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final WebClient kycCountClient;
	private final WebClient productClient;


	@Autowired
	public KycReportApiClient(Environment env) {
		String kycReportUrl = env.getProperty("urls.KYC_REPORT_URL");
		log.info("kyc report ulrl"+kycReportUrl);
		String kycRouteUrl = env.getProperty("urls.KYC_ROUTE_URL");

		this.kycCountClient = WebClient.builder()
				.baseUrl(kycReportUrl)
				.build();

		this.productClient = WebClient.builder()
				.baseUrl(kycRouteUrl)
				.build();
	}


	public String getOkycReport(KycReportRequestModel requestModel, String url) {
		try {
			WebClient webClient = WebClient.builder()
                    .codecs(config -> config.defaultCodecs().maxInMemorySize(30 * 1024 * 1024)) // 10 MB
                    .build();

			log.info("KYC Report URL",url);
			log.info("KYC Report Request Model: {}",requestModel);
			return webClient.post()
					.uri(url) // full URL, not relative to any base URL
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
					//.header("app-id", appId)
					//.header("api-key", apiKey)
					.bodyValue(requestModel)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			//e.printStackTrace();
			log.error("getOkycReport failed: status {}, message {}", e.getStatusCode().value(), e.getResponseBodyAsString());
			return "failed";
		} catch (Exception e) {
			e.printStackTrace();
			log.error("getOkycReport unexpected error", e);
			return "failed";
		}
	}

	
	

	public String kycCountApiCall(String fromDate, String toDate, String merchantId, String product) {
		String formattedStartDate = formatDate(fromDate);
		String formattedEndDate = formatDate(toDate);

		try {
			return kycCountClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/api/v1/kyc-api-count")
							.queryParam("startDate", formattedStartDate)
							.queryParam("endDate", formattedEndDate)
							.queryParam("merchantId", merchantId)
							.queryParam("product", product)
							.build())
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (Exception e) {
			log.error("kycCountApiCall failed", e);
			return null;
		}
	}


	private static String formatDate(String dateStr) {
		return LocalDate.parse(dateStr, INPUT_DATE_FORMAT).format(OUTPUT_DATE_FORMAT);
	}


	public List<ProductDTO> getProducts() {
		try {
			String rawJson = productClient.get()
					.uri("/productList")
					.retrieve()
					.bodyToMono(String.class)
					.block();

			JsonNode rootNode = objectMapper.readTree(rawJson);
			JsonNode dataNode = rootNode.path("data");

			if (!dataNode.isArray()) {
				log.warn("getProducts response missing or invalid 'data' array");
				return List.of();
			}

			return objectMapper.readerForListOf(ProductDTO.class).readValue(dataNode);
		} catch (Exception e) {
			log.error("Error fetching products", e);
			return List.of();
		}
	}


	public String getProductRoutesByMerchantId(String merchantId) {
		try {
			return productClient.get()
					.uri("/merchant/productRoutes/{merchantId}", merchantId)
					.retrieve()
					.bodyToMono(String.class)
					.block();
		} catch (WebClientResponseException e) {
			log.error("getProductRoutesByMerchantId failed: status {}, message {}");
			return "failed";
		} catch (Exception e) {
			log.error("getProductRoutesByMerchantId unexpected error", e);
			return "failed";
		}
	}


	public String saveMerchantRouting(MerchantRoutingUiRequest request) {
		try {
			return productClient.post()
					.uri("/api/merchant-kyc-routing/save")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.bodyValue(request)
					.exchangeToMono(this::handleResponse)
					.block();
		} catch (Exception e) {
			log.error("saveMerchantRouting failed", e);
			return "failed";
		}
	}


	private Mono<String> handleResponse(ClientResponse response) {
		if (response.statusCode().is2xxSuccessful()) {
			return response.bodyToMono(String.class);
		}
		return response.bodyToMono(String.class)
				.defaultIfEmpty("Unexpected response status: " + response.statusCode());
	}


	public RoutingResponseContainer.RoutingResponse fetchRoutingUpdateDetailsApiAsDto(String merchantId) {
		try {
			RoutingResponseContainer.RoutingResponse response = productClient.get()
					.uri(uriBuilder -> uriBuilder
							.path("/api/merchant-kyc-routing/fetch/routes-details")
							.queryParam("mid", merchantId)
							.build())
					.retrieve()
					.bodyToMono(RoutingResponseContainer.RoutingResponse.class)
					.block();



			return response;
		} catch (Exception e) {
			// log.error("fetchRoutingUpdateDetailsApiAsDto failed", e);
			return null;
		}
	}

	public String checkProductRate(String clientId, String productId, String productRate) {
		try {
			Map<String, Object> requestBody = Map.of(
					"clientId", clientId,
					"productId", productId,
					"productRate", productRate
					);
			return productClient.post()
					.uri("/api/merchant-kyc-routing/fetch/merchants/productRate")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.bodyValue(requestBody)
					.exchangeToMono(this::handleResponse)
					.block();
		} catch (Exception e) {
			log.error("checkProductRate failed", e);
			return "failed";
		}
	}




}

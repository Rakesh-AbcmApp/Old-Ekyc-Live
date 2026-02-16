package com.abcm.addhar_service.service;

import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.abcm.addhar_service.Email.CommonUtils;
import com.abcm.addhar_service.Email.SendFailureEmail;
import com.abcm.addhar_service.dyanamicReponse.ResponseDispatcher;
import com.abcm.addhar_service.merchantReponseDto.DigiWebhookReponseToMerchnat;
import com.abcm.addhar_service.repository.AadharRepository;
import com.abcm.addhar_service.util.AadharResponseUpdate;
import com.abcm.addhar_service.util.TimestampUtil;
import com.abcm.addhar_service.util.UpdateBalance;
import com.abcmkyc.entity.KycData;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@Slf4j
public class DigiService {

	@Autowired
	AadharRepository aadharRepository;

	@Autowired
	AadharResponseUpdate aadharResponseUpdate;

	@Autowired
	ResponseDispatcher responseDispatcher;

	@Autowired
	UpdateBalance updateBalance;

	private final WebClient webClient = WebClient.builder().build();

	@Value("${email.webhookfailed.subject}")
	private String webhookfailedsubject;

	@Value("${email.webhookfailed.template.path}")
	private String emailwebhooktempPath;

	@Autowired
	private SendFailureEmail sendFailureEmail;

	public KycData findByWebhookSecurityKey(String WebhookSecurityKey) {
		try {

			KycData entity = aadharRepository.findByWebhookSecurityKey(WebhookSecurityKey);
			return entity;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	public String UpdateWebhookResponse(String payload, String webhookSecurityKey, KycData entity) {

		try {
			JSONObject json = new JSONObject(payload);

			// Root level fields
			String requestId = json.optString("request_id");
			boolean success = json.optBoolean("success");

			String responseCode = json.optString("response_code");
			String responseMessage = json.optString("response_message");

			// Initialize variables
			String status = null;
			String name = null;
			String uri = null;
			String billable = "N";
			String gender = null;
			String parentName = null;
			String dob = null;
			String loc = null;
			String country = null;
			String dist = null;
			String state = null;
			String subdist = null;
			String vtc = null;
			String pc = null;
			String po = null;
			String photo = "";
			String street=null;
			String house=null;
			String landmark=null;
			// Extract from result[0]
			JSONArray result = json.optJSONArray("result");
			if (result != null && !result.isEmpty()) {
				JSONObject first = result.getJSONObject(0);
				status = first.optString("status", "UNKNOWN");

				// Extract name from data_xml
				String dataXml = first.optString("data_xml", "");
				if (!dataXml.isEmpty()) {

					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
						factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
						factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
						factory.setNamespaceAware(true);

						DocumentBuilder builder = factory.newDocumentBuilder();
						Document doc = builder.parse(new InputSource(new StringReader(dataXml)));

						NodeList poiList = doc.getElementsByTagName("Poi");
						if (poiList.getLength() > 0) {
							Element poiElement = (Element) poiList.item(0);
							name = poiElement.getAttribute("name");
							gender = poiElement.getAttribute("gender");
							dob = poiElement.getAttribute("dob");
						}

						NodeList poaList = doc.getElementsByTagName("Poa");
						if (poaList.getLength() > 0) {
							Element poaElement = (Element) poaList.item(0);

							// Extract and clean parent name from 'co' attribute
							String coAttr = poaElement.getAttribute("co");
							if (coAttr != null && coAttr.contains(":")) {
								parentName = coAttr.split(":", 2)[1].trim();
							}

							// Other address fields
							country = poaElement.getAttribute("country");
							dist = poaElement.getAttribute("dist");
							loc = poaElement.getAttribute("loc");
							pc = poaElement.getAttribute("pc");
							po = poaElement.getAttribute("po");
							state = poaElement.getAttribute("state");
							subdist = poaElement.getAttribute("subdist");
							vtc = poaElement.getAttribute("vtc");
							
							//new filed add
							street= poaElement.getAttribute("street");
							house= poaElement.getAttribute("house");
							landmark= poaElement.getAttribute("lm");
							

						}

						NodeList phtList = doc.getElementsByTagName("Pht");

						if (phtList != null && phtList.getLength() > 0 && phtList.item(0) != null) {
							log.info("phtList :" + phtList.item(0).getTextContent().trim());
							photo = phtList.item(0).getTextContent().trim();
							log.info(" Aadhaar Base64 Photo Extracted: {}",
									photo.length() > 30 ? photo.substring(0, 30) + "..." : photo);
						} else {
							log.warn("<Pht> tag not found or empty in XML.");
						}

					} catch (Exception ex) {
						log.error(" Failed to parse data_xml and extract name", ex);
					}

				}
				// Extract URI from issued object
				JSONObject issued = first.optJSONObject("issued");
				if (issued != null) {
					uri = issued.optString("uri");
				}
			}
			// Extract billable metadata
			JSONObject metadata = json.optJSONObject("metadata");
			if (metadata != null) {
				billable = metadata.optString("billable", "N");

				if ("Y".equalsIgnoreCase(billable)) {

					updateBalance.updateWalletBalanceAfterOtp(entity.getMerchantId(), entity.getProductRate());
					log.info("After Baklance update and continue..{}");
				}
			}

			// Logging
			log.info("✅ photo: {}", photo);
			log.info("✅ Metadata - Billable: {}", billable);
			log.info("✅ Request ID: {}", requestId);
			log.info("✅ Response Code: {}", responseCode);
			log.info("✅ Message: {}", responseMessage);
			log.info("✅ Webhook Status: {}", status);
			log.info("✅ Document URI: {}", uri);
			log.info("✅ Customer Name: {}", name);

			//LocalDateTime now = LocalDateTime.now();
			//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = TimestampUtil.getISTTimestamp();

			DigiWebhookReponseToMerchnat response = DigiWebhookReponseToMerchnat.builder().response_code(responseCode)
					.response_message(responseMessage).success(success).referenceId(requestId)
					.track_id(entity.getTrackId()).merchant_id(entity.getMerchantId())
					.response_timestamp(formattedDateTime) // or use actual webhook timestamp if available
					.billable(billable).userFullName(name).userParentName(parentName) // assuming you have this info
					// .userAadhaarNumber(kycEntity.getCu()) // if stored
					.userDob(dob) // if stored
					.userGender(gender) // if stored
					 .house(house)
					 .street(street)
	                .landmark(landmark)
					.loc(loc)
					.po(po)
					.dist(dist)
					.subdist(subdist)
					.vtc(vtc)
					.state(state)
					.country(country)
					.addressZip(pc)
					.userProfileImage(photo)
					.order_id(entity.getOrderId()).build();

			// Store data in DB
			KycData kycEntity = aadharResponseUpdate.updateWebhookResponse(billable, status, requestId,
					webhookSecurityKey, entity, payload, name, response, responseMessage);

			String merchantId = entity.getMerchantId();
			String MerchantEamil = "";
			try {
				MerchantEamil = aadharRepository.findEmailByMid(merchantId);
			} catch (Exception e) {
				log.error("exception when get email", e);
			}
			String merchantApiResponse = sendWebhookTMerchant(response, kycEntity, MerchantEamil);
			log.info("Final Merchant Webhook Response: {}", merchantApiResponse);
			if (success && "FETCHED".equalsIgnoreCase(status) && requestId != null) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			log.error("Exception in updateWebhookResponse: {}", e);
			return "ERROR";
		}
	}

	private String sendWebhookTMerchant(DigiWebhookReponseToMerchnat response, KycData kycData, String email) {
		try {
			log.info("Post Webhook To Merchant{} ;", response.getMerchant_id());
			String responseBody = webClient.post().uri(kycData.getMerchantWebhookUrl())
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).bodyValue(response).retrieve()
					.onStatus(status -> !status.is2xxSuccessful(),
							clientResponse -> clientResponse.bodyToMono(String.class).flatMap(body -> {
								log.warn("❌ Webhook call failed. Status: {}, Body: {}", clientResponse.statusCode(),
										body);
								return Mono
										.error(new RuntimeException("Webhook failed: " + clientResponse.statusCode()));
							}))
					.bodyToMono(String.class)
					.retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
							.doBeforeRetry(retrySignal -> log.warn("🔁 Retrying attempt #{} due to: {}",
									retrySignal.totalRetries() + 1, retrySignal.failure().getMessage())))
					.doOnSuccess(body -> {
						log.info("✅ Webhook delivered successfully. Updating status.");
						updateWebhookStatus(kycData);
					}).doOnError(ex -> {
						log.error("❌ All webhook retry attempts failed. Triggering email alert.");
						CompletableFuture.runAsync(() -> {
							try {

								String mailstring1 = CommonUtils.readUsingFileInputStream(emailwebhooktempPath);
								mailstring1 = mailstring1.replace("{{Product}}", kycData.getProduct());
								mailstring1 = mailstring1.replace("{{Merchant}}", kycData.getMerchantName());
								String Webhooksubject = webhookfailedsubject.replace("{Product}", kycData.getProduct())
										.replace("{Merchant}", kycData.getMerchantName());
								sendFailureEmail.sendAadhaarFailureEmail(mailstring1, "", Webhooksubject, email);
								log.info("Send Mail Success After Webhook Failed");
							} catch (Exception e) {
								log.error("❌ All webhook retry attempts failed. Triggering email alert.",
										e.getMessage());
							}
						});
						// emailSend();
					}).block();

			return responseBody;

		} catch (Exception ex) {
			log.error("🔥 Exception while calling merchant webhook", ex);
			return "ERROR: " + ex.getMessage();
		}
	}

	private void updateWebhookStatus(KycData kycEntity) {
		try {
			KycData updatedEntity = kycEntity.toBuilder().webhookStatusMerchant(true).build();
			aadharRepository.save(updatedEntity);
			log.info("Updated webhook status fields for WebSecurityKey: {}", kycEntity.getWebhookSecurityKey());
		} catch (Exception e) {
			log.error("Exception in updateWebhookStatus: {}", e.getMessage(), e);

		}
	}

//	private String sendWebhookTMerchant(DigiWebhookReponseToMerchnat response, KycData kycData) {
//	    try {
//	        String responseBody = webClient.post()
//	            .uri(kycData.getMerchantWebhookUrl())
//	            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//	            .bodyValue(response)
//	            .retrieve()
//	            .onStatus(
//	                status -> !status.is2xxSuccessful(),
//	                clientResponse -> clientResponse.bodyToMono(String.class)
//	                    .flatMap(body -> {
//	                        log.warn("❌ Webhook call failed. Status: {}, Body: {}", clientResponse.statusCode(), body);
//	                        return Mono.error(new RuntimeException("Webhook failed: " + clientResponse.statusCode()));
//	                    })
//	            )
//	            .bodyToMono(String.class)
//	            .doOnSuccess(body -> {
//	                log.info("✅ Webhook delivered successfully. Updating status.");
//	                updateWebhookStatus(kycData); // only called on success
//	            })
//	            .block();
//
//	        return responseBody;
//
//	    } catch (Exception ex) {
//	        log.error("🔥 Exception while calling merchant webhook", ex);
//	        return "ERROR: " + ex.getMessage();
//	    }
//	}

//
//	private String sendWebhookTMerchant(DigiWebhookReponseToMerchnat response, KycData kycData) {
//	    try {
//	        String apiResponse = webClient.post()
//	                .uri(kycData.getMerchantWebhookUrl())
//	                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//	                .bodyValue(response)
//	                .retrieve()
//	                .bodyToMono(String.class)
//	                .block();  // Blocking for simplicity
//
//	        
//	        
//	        updateWebhookStatus(kycData);
//	        log.info("Redirect API response: {}", apiResponse);
//	        return apiResponse;
//	    } catch (Exception ex) {
//	        log.error(" Error calling Merchantredirect API", ex);
//	        return "ERROR: " + ex.getMessage();
//	    }
//	}

}

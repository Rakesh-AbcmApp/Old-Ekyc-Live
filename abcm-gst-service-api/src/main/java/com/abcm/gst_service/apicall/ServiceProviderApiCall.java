package com.abcm.gst_service.apicall;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.abcm.gst_service.dto.ProductDetailsDto;
import com.abcm.gst_service.util.CommonUtils;
import com.abcm.gst_service.util.SendFailureEmail;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j

public class ServiceProviderApiCall {
	
	
	@Value("${email.serviceDown.subject}")
	private String serviceDownSubject;

	@Value("${email.serviceDown.template.path}")
	private String serviceDownTemplatePath;
	
	@Value("${Env}")
	private String env;

	@Autowired
	private  SendFailureEmail sendFailureEmail;
	
	 @Value("${email.send.to}")
	 private String to;

    private final WebClient webClient;

    public ServiceProviderApiCall() {
        HttpClient httpClient = HttpClient.create()
            .resolver(DefaultAddressResolverGroup.INSTANCE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20000)
            .responseTimeout(Duration.ofSeconds(15))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS))
            );
        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }

    public String providerApiCall(Map<String, Object> requestBody, ProductDetailsDto productDetailsDto) {
        log.info("Calling GSTIN Lite URL: {}");
        return webClient.post()
            .uri(productDetailsDto.getGstliteUrl())
            .headers(headers -> setProviderHeaders(
                headers,
                productDetailsDto.getProviderName(),
                productDetailsDto.getProviderAppId(),
                productDetailsDto.getProviderAppkey()))
            .bodyValue(requestBody)
            .exchangeToMono(response -> {
                int statusCode = response.statusCode().value();
                log.info("Received status code: {}", statusCode);
                return response.bodyToMono(String.class)
                    .map(body -> {
                        if (response.statusCode().is5xxServerError()) {
                            log.error(" 5xx error response: {}", body);
                           
                            return "fail:false";
                        }
                        log.info("response body: {}", body);
                        return body;
                    });
            })
            .onErrorResume(ex -> {
                log.error("Exception calling third-party API: {}", ex.getMessage(), ex);
                return Mono.just("fail:false");
            })
            .block();
    }

    private void setProviderHeaders(HttpHeaders headers, String provider, String appId, String apiKey) {
        log.info("Setting headers for provider: {}", provider);
        switch (provider.toLowerCase()) {
            case "zoop" -> {
                headers.set("app-id", appId);
                headers.set("api-key", apiKey);
            }
            case "cashfree" -> {
                headers.set("x-client-id", appId);
                headers.set("x-client-secret", apiKey);
            }
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        }
    }
}

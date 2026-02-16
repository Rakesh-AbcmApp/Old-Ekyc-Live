package com.abcm.dl_service.apicall;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.abcm.dl_service.dto.ProductDetailsDto;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceProviderApiCall {
	
	

	private final WebClient webClient = buildWebClient();
    private WebClient buildWebClient() {
        HttpClient httpClient = HttpClient.create()
                .resolver(DefaultAddressResolverGroup.INSTANCE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 20_000)
                .responseTimeout(Duration.ofSeconds(15))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }


    public String providerApiCall(Map<String, Object> requestBody, ProductDetailsDto productDetailsDto) {
        log.info("Calling Provider URL: {}"+productDetailsDto.getDrivingLsUrl());
        return webClient.post()
                .uri(productDetailsDto.getDrivingLsUrl())
                .headers(headers -> setProviderHeaders(headers,
                        productDetailsDto.getProviderName(),
                        productDetailsDto.getProviderAppId(),
                        productDetailsDto.getProviderAppkey()))
                .bodyValue(requestBody)
                .exchangeToMono(response -> {
                    int statusCode = response.statusCode().value();
                    log.info("Response status code: {}", statusCode);
                    return response.bodyToMono(String.class)
                            .map(body -> {
                            	
                                if (response.statusCode().is5xxServerError()) {
                                    log.error("server error (5xx), returning fail:false");
                                    
                                    return "fail:false";
                                }
                                return body;
                            });
                })
                .onErrorResume(ex -> {
                    log.error("Exception during third-party API call: {}", ex.getMessage());
                    
                    return Mono.just("fail:false");
                })
                .block();
    }
    
    
    
    

    private void setProviderHeaders(HttpHeaders headers, String provider, String appId, String apiKey) {
        //log.info("Setting headers for provider: {}", provider);
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


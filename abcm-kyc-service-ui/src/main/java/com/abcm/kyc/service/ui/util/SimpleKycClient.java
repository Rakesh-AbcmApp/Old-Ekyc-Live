/*
 * package com.abcm.kyc.service.ui.util;
 * 
 * import java.time.LocalDate; import java.time.format.DateTimeFormatter;
 * 
 * import org.springframework.web.reactive.function.client.WebClient;
 * 
 * import com.abcm.kyc.service.ui.dto.KycApiCountResponseModel;
 * 
 * public class SimpleKycClient {
 * 
 * private static final DateTimeFormatter INPUT_FORMAT =
 * DateTimeFormatter.ofPattern("dd-MM-yyyy"); private static final
 * DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE; //
 * yyyy-MM-dd public static void main(String[] args) { WebClient client =
 * WebClient.create("http://localhost:3081"); String formattedStartDate =
 * formatDate("01-04-2025"); String formattedEndDate = formatDate("31-05-2025");
 * KycApiCountResponseModel response = client.get() .uri(uriBuilder ->
 * uriBuilder .path("/api/v1/kyc-api-count") .queryParam("startDate",
 * formattedStartDate) .queryParam("endDate", formattedEndDate)
 * .queryParam("merchantId", "") .queryParam("product", "") .build())
 * .retrieve() .bodyToMono(KycApiCountResponseModel.class) .block();
 * 
 * System.out.println(response); }
 * 
 * private static String formatDate(String dateStr) { return
 * LocalDate.parse(dateStr, INPUT_FORMAT).format(OUTPUT_FORMAT); } }
 */
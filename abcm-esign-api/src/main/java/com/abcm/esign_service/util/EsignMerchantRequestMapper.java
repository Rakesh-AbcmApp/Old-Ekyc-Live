package com.abcm.esign_service.util;

import java.util.List;

import org.springframework.stereotype.Component;

import com.abcm.esign_service.DTO.EsignMerchantRequest;
import com.abcm.esign_service.DTO.EsignRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EsignMerchantRequestMapper {

	private final ObjectMapper mapper;

	public EsignMerchantRequest mapToFullRequest(EsignRequest  esignRequest, String signersJson, String recipientsJson) {
		try {

			  log.info("Esign client request  Map To original Request:{} ", esignRequest);
			List<EsignMerchantRequest.Signer> signers = mapper.readValue(signersJson,
					new TypeReference<List<EsignMerchantRequest.Signer>>() {
					});

			EsignMerchantRequest esignMerchantRequest = EsignMerchantRequest.builder()
					.merchant_id(esignRequest.getMerchant_id()).consent(esignRequest.getConsent())
					.document_name(esignRequest.getDocument_name()).Order_Id(esignRequest.getOrder_id())
					.webhook_url(esignRequest.getWebhook_url()).link_expiry_min(esignRequest.getLink_expiry_min())
					.signers(signers).build();

			return esignMerchantRequest;

		} catch (Exception e) {
			throw new RuntimeException("Invalid JSON format for signers or recipients", e);
		}

	}
}

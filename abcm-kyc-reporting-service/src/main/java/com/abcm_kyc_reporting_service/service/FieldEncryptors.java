package com.abcm_kyc_reporting_service.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.abcmkyc.util.EncryptionUtil;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Service
@Converter
public class FieldEncryptors implements AttributeConverter<String, String> {

    private static final String ENCRYPTION_KEY = "l7ky8LMTFnG9QHWO6rwl6InGirDv5B0ASqMKr7QXVOc=";

    private static final EncryptionUtil encryptionUtil;

    static {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(ENCRYPTION_KEY);
            encryptionUtil = new EncryptionUtil(keyBytes);
        } catch (Exception e) {
            throw new IllegalStateException("EncryptionUtil init failed", e);
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            return attribute == null ? null : encryptionUtil.encrypt(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return dbData == null ? null : encryptionUtil.decrypt(dbData);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}


package com.abcm_kyc_reporting_service.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.abcmkyc.entity.KycData;

@Repository
public interface KycReportRepository extends JpaRepository<KycData, Long> {

    Page<KycData> findAll(Specification<KycData> spec, Pageable pageable);

 // SINGLE METHOD (optional orderId) — always applies since
    @Query(value = """
            SELECT k.*
            FROM abcm_kyc_ms_db.kyc_data k
            WHERE k.merchant_id = :merchantId
              AND ( :orderId IS NULL OR :orderId = '' OR k.order_id = :orderId )
            ORDER BY k.merchant_request_at DESC
            """, nativeQuery = true)
    List<KycData> findByMerchantAndOptionalOrderIdNative(
        @Param("merchantId") String merchantId,
        @Param("orderId") String orderId
    );


    @Query("SELECT COUNT(k) > 0 FROM KycData k WHERE k.merchantId = :merchantId")
    boolean existsByMerchantId(@Param("merchantId") String merchantId);
    
    
    // 
}

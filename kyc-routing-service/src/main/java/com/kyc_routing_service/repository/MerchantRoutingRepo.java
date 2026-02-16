package com.kyc_routing_service.repository;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.abcmkyc.entity.ClientMaster;
import com.abcmkyc.entity.MerchantRouting;
import com.abcmkyc.entity.Merchant_Master;
import com.abcmkyc.entity.ProductMaster;


@Repository
public interface MerchantRoutingRepo extends JpaRepository<MerchantRouting, Long> {

	List<MerchantRouting> findByMerchantId(Long merchantId);
	
	Optional<MerchantRouting> findByMerchantAndClientAndProduct(Merchant_Master merchant, ClientMaster client,
			ProductMaster product);
	
	Optional<MerchantRouting> findByMerchantAndProduct(Merchant_Master merchant, ProductMaster product);


	List<MerchantRouting> findByMerchantIdAndClientId(Long merchantId, Long clientId);

	List<MerchantRouting> findAll(Specification<MerchantRouting> filter);

	Optional<MerchantRouting> findByMidAndProduct_ProductId(String mid, Long productId);


	List<MerchantRouting> findByMidAndProductProductIdAndIsActiveTrue(String mid, Long productId);


	@Query(value = """
		    SELECT
		        mr.mid AS merchantId,
		        c.id AS clientId,
		        c.service_provider_name AS providerName,
		        p.product_id AS productId,
		        p.product_name AS productName,
		        mc.product_rate AS rate,
		        mr.is_active AS isActive
		    FROM
		        merchant_routing mr
		        JOIN client_master_table c ON mr.provider_id = c.id
		        JOIN product_master p ON mr.product_id = p.product_id
		        LEFT JOIN merchant_charges mc ON mr.route_id = mc.route_id
		    WHERE
		        mr.merchant_id = (SELECT id FROM merchant_master WHERE mid = :mid)
		        AND mr.is_active = true
		    """, nativeQuery = true)
		List<Map<String, Object>> findActiveRoutesByMidNative(@Param("mid") String mid);


	Optional<MerchantRouting> findByMerchantMidAndProductProductId(String mid, Long productId);
	
	
	/*New */
	
	Optional<MerchantRouting> findByProduct(ProductMaster product);

	List<MerchantRouting> findByMerchantAndProductAndIsActiveTrue(Merchant_Master merchant, ProductMaster product);

	List<MerchantRouting> findByMerchantAndProductAndIsActiveTrueAndClientNot(Merchant_Master merchant,
			ProductMaster product, ClientMaster client);

	Optional<MerchantRouting> findByMerchantAndProductAndClientAndIsActiveTrue(Merchant_Master merchant,
			ProductMaster product, ClientMaster client);

	Optional<MerchantRouting> findByMerchantAndProductAndClientAndIsActiveFalse(Merchant_Master merchant,
			ProductMaster product, ClientMaster client);














}

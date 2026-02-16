package com.kyc_routing_service.Utility;



import org.springframework.data.jpa.domain.Specification;
import com.abcmkyc.entity.MerchantRouting;
import com.kyc_routing_service.dto.MerchantRoutingReportRequest;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class MerchantRoutingSpecification {

    public static Specification<MerchantRouting> filter(long id, long clientId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Merchant Id is mandatory
            if (id!=0) {
                predicates.add(cb.equal(root.get("merchant").get("id"), id));
            }

            // Client Id is optional
            if (clientId != 0) {
                predicates.add(cb.equal(root.get("client").get("id"), clientId));
            }

           

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

	
}

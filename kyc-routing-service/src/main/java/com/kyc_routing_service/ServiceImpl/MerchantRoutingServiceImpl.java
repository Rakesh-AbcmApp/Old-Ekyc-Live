package com.kyc_routing_service.ServiceImpl;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.abcmkyc.entity.ClientMaster;
import com.abcmkyc.entity.MerchantCharges;
import com.abcmkyc.entity.MerchantDetailsDto;
import com.abcmkyc.entity.MerchantProductRoute;
import com.abcmkyc.entity.MerchantRouting;
import com.abcmkyc.entity.Merchant_Master;
import com.abcmkyc.entity.ProductMaster;
import com.kyc_routing_service.Entity.MerchantRoutingRequest;
import com.kyc_routing_service.Service.MerchantRoutingService;
import com.kyc_routing_service.Utility.MerchantRoutingSpecification;
import com.kyc_routing_service.dto.MerchantRoutingReportRequest;
import com.kyc_routing_service.dto.MerchantRoutingReportResponseDTO;
import com.kyc_routing_service.dto.ProductRoutesResponseDTO;
import com.kyc_routing_service.dto.ServerResponse;
import com.kyc_routing_service.repository.ClientMasterRepo;
import com.kyc_routing_service.repository.MasterMerchantRepo;
import com.kyc_routing_service.repository.MerchantChargesRepo;
import com.kyc_routing_service.repository.MerchantRoutingRepo;
import com.kyc_routing_service.repository.ProductRepo;
import com.kyc_routing_service.repository.ProviderProductRateRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MerchantRoutingServiceImpl implements MerchantRoutingService {
    private final MerchantRoutingRepo merchantRoutingRepo;
    private final ClientMasterRepo clientMasterRepo;
    private final MasterMerchantRepo masterMerchantRepo;
    private final ProductRepo productRepo;
    private final MerchantChargesRepo chargesRepo;
    private final  ProviderProductRateRepo providerProductRateRepo;
    public MerchantRoutingServiceImpl(
            MerchantRoutingRepo merchantRoutingRepo,
            ClientMasterRepo clientMasterRepo,
            MasterMerchantRepo masterMerchantRepo,
            ProductRepo productRepo,MerchantChargesRepo chargesRepo,ProviderProductRateRepo providerProductRateRepo) {
        this.merchantRoutingRepo = merchantRoutingRepo;
        this.clientMasterRepo = clientMasterRepo;
        this.masterMerchantRepo = masterMerchantRepo;
        this.productRepo = productRepo;
        this.chargesRepo=chargesRepo;
        this.providerProductRateRepo=providerProductRateRepo;
    }
    
    @Override
    @Transactional
    public ServerResponse SaveMerchatntRouting(MerchantRoutingRequest request) {
    	log.info("Routing Save Or Update Request Body"+request);
        if (request == null || request.getRoutingDetails() == null || request.getRoutingDetails().isEmpty()) {
            return new ServerResponse(400, "Invalid request: Routing details are required", null);
        }
        Merchant_Master merchant = masterMerchantRepo.findByMid(request.getMerchantId()).orElse(null);
        if (merchant == null) {
            return new ServerResponse(404, "Merchant not found", null);
        }
        Map<String, List<String>> updatedMap = new LinkedHashMap<>();
        Map<String, List<String>> savedMap = new LinkedHashMap<>();
        for (MerchantRoutingRequest.RoutingDetail detail : request.getRoutingDetails()) {
            ClientMaster client = clientMasterRepo.findById(detail.getClientId()).orElse(null);
            if (client == null) {
                return new ServerResponse(404, "Client ID " + detail.getClientId() + " not found", null);
            }

            ProductMaster product = productRepo.findById(detail.getProductId()).orElse(null);
            if (product == null) {
                return new ServerResponse(404, "Product ID " + detail.getProductId() + " not found", null);
            }

            // 1️⃣ Deactivate active routings for this merchant & product (except if same client)
            List<MerchantRouting> activeRoutingsToDeactivate = merchantRoutingRepo
                .findByMerchantAndProductAndIsActiveTrueAndClientNot(merchant, product, client);

            for (MerchantRouting activeRouting : activeRoutingsToDeactivate) {
                activeRouting.setActive(false);
                activeRouting.setUpdatedBy(detail.getCreatedBy());
                activeRouting.setUpdatedAt(LocalDateTime.now());
                merchantRoutingRepo.save(activeRouting);

                updatedMap.computeIfAbsent(activeRouting.getClient().getServiceProviderName(), k -> new ArrayList<>())
                    .add("[" + product.getProductName() + "(Deactivated)]");
            }

            // 2️⃣ Check if there’s an active routing for this exact merchant+product+client
            Optional<MerchantRouting> existingActiveRoutingOpt = merchantRoutingRepo
                    .findByMerchantAndProductAndClientAndIsActiveTrue(merchant, product, client);

            if (existingActiveRoutingOpt.isPresent()) {
                MerchantRouting activeRouting = existingActiveRoutingOpt.get();

                // Update routing audit info
                activeRouting.setUpdatedBy(detail.getCreatedBy());
                activeRouting.setUpdatedAt(LocalDateTime.now());
                merchantRoutingRepo.save(activeRouting);

                // Convert rate → paise safely
                long newRatePaise = java.math.BigDecimal.valueOf(detail.getRate())
                        .movePointRight(2).setScale(0, java.math.RoundingMode.HALF_UP)
                        .longValueExact();

                // Get latest charge for this routing (simple: last in list)
                MerchantCharges latestCharge = activeRouting.getCharges().isEmpty()
                        ? null
                        : activeRouting.getCharges().get(activeRouting.getCharges().size() - 1);

                if (latestCharge != null) {
                    if (latestCharge.getProductRate() == null || latestCharge.getProductRate() != newRatePaise) {
                        latestCharge.setProductRate(newRatePaise);
                        latestCharge.setModifiedBy(detail.getCreatedBy());
                        latestCharge.setModifiedAt(LocalDateTime.now());
                        chargesRepo.save(latestCharge); // UPDATE existing row

                        savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                                .add("[" + product.getProductName() + " (Rate Updated: " + detail.getRate() + ")]");
                    } else {
                        savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                                .add("[" + product.getProductName() + " (Already Active, Same Rate)]");
                    }
                } else {
                    savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                            .add("[" + product.getProductName() + " (Already Active, No Charge Found)]");
                }

                continue; // Skip to next routing detail
            }


         // 3️⃣ Check if there’s an existing inactive routing for this merchant+product+client (reactivate it)
            Optional<MerchantRouting> existingInactiveRoutingOpt = merchantRoutingRepo
                    .findByMerchantAndProductAndClientAndIsActiveFalse(merchant, product, client);

            if (existingInactiveRoutingOpt.isPresent()) {
                MerchantRouting inactiveRouting = existingInactiveRoutingOpt.get();

                // Reactivate route
                inactiveRouting.setActive(true);
                inactiveRouting.setUpdatedBy(detail.getCreatedBy());
                inactiveRouting.setUpdatedAt(LocalDateTime.now());
                merchantRoutingRepo.save(inactiveRouting);

                // Convert rate → paise
                long newRatePaise = java.math.BigDecimal.valueOf(detail.getRate())
                        .movePointRight(2).setScale(0, java.math.RoundingMode.HALF_UP)
                        .longValueExact();

                // Get latest charge for this routing
                MerchantCharges latestCharge = inactiveRouting.getCharges().isEmpty()
                        ? null
                        : inactiveRouting.getCharges().get(inactiveRouting.getCharges().size() - 1);

                if (latestCharge != null) {
                    if (latestCharge.getProductRate() == null || latestCharge.getProductRate() != newRatePaise) {
                        latestCharge.setProductRate(newRatePaise);
                        latestCharge.setModifiedBy(detail.getCreatedBy());
                        latestCharge.setModifiedAt(LocalDateTime.now());
                        chargesRepo.save(latestCharge); // UPDATE existing

                        savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                                .add("[" + product.getProductName() + " (Reactivated, Rate Updated: " + detail.getRate() + ")]");
                    } else {
                        savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                                .add("[" + product.getProductName() + " (Reactivated, Same Rate)]");
                    }
                } else {
                    savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                            .add("[" + product.getProductName() + " (Reactivated, No Charge Found)]");
                }

                continue;
            }

            // 4️⃣ No existing inactive routing — create a brand new active routing
            MerchantRouting newRouting = new MerchantRouting();
            newRouting.setMerchant(merchant);
            newRouting.setClient(client);
            newRouting.setProduct(product);
            newRouting.setActive(true);
            newRouting.setCreatedBy(detail.getCreatedBy());
            newRouting.setCreatedAt(LocalDateTime.now());
            newRouting.setMid(merchant.getMid());
            MerchantRouting savedRouting = merchantRoutingRepo.save(newRouting);
            // Create charges
            MerchantCharges charge = new MerchantCharges();
            charge.setProductRate((long) (detail.getRate() * 100));
            charge.setCreatedBy(detail.getCreatedBy());
            charge.setCreatedAt(LocalDateTime.now());
            charge.setMerchantRouting(savedRouting);
            charge.setMerchantId(merchant.getMid());
            chargesRepo.save(charge);
            savedRouting.getCharges().add(charge);
            savedMap.computeIfAbsent(client.getServiceProviderName(), k -> new ArrayList<>())
                .add("[" + product.getProductName() + "(" + detail.getRate() + ")]");
        }
        // Build final response message
        StringBuilder message = new StringBuilder();
        if (!updatedMap.isEmpty()) {
            message.append("Deactivated Routing Details: ");
            updatedMap.forEach((provider, products) -> {
                message.append(provider)
                       .append(" -> ")
                       .append(products.stream().collect(Collectors.joining(", ")))
                       .append(" | ");
            });
        }

        if (!savedMap.isEmpty()) {
            message.append("Saved Routing Details: ");
            savedMap.forEach((provider, products) -> {
                message.append(provider)
                       .append(" -> ")
                       .append(products.stream().collect(Collectors.joining(", ")))
                       .append(" | ");
            });
        }

        if (message.length() == 0) {
            message.append("No changes made.");
        } else {
            message.append(" MID -> ").append(merchant.getMid());
        }

        return new ServerResponse(HttpStatus.OK.value(), message.toString().trim(), null);
    }

    
    
    

    @Override
    public ServerResponse FetchMerchnatRouteDetails(String merchantId) {
        try {
        	log.info("Merchnat Details Routing service method");
        	Optional<Merchant_Master> optionalMerchant = masterMerchantRepo.findByMid(merchantId);
        	if (optionalMerchant.isEmpty()) {
        	    return new ServerResponse(404, "Merchant not found", null);
        	}
            Merchant_Master merchant = optionalMerchant.get();
            MerchantDetailsDto dto = mapToMerchantDetailsDto(merchant);
            if (dto.getProductDetails().isEmpty()) {
                return new ServerResponse(HttpStatus.NOT_FOUND.value(), "Product Details not found", dto);
            }
            return new ServerResponse(HttpStatus.OK.value(), "success", dto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ServerResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", null);
        }
    }

    private MerchantDetailsDto mapToMerchantDetailsDto(Merchant_Master merchant) {
        MerchantDetailsDto dto = new MerchantDetailsDto();
        dto.setId(merchant.getId());
        dto.setMerchantId(merchant.getMid());
        dto.setMerchantName(merchant.getName());
        dto.setAppId(merchant.getAppId());
        dto.setApiKey(merchant.getApiKey());
        dto.setOKYC(stringOrNull(merchant.getAadharOkyc()));
        dto.setPAN(stringOrNull(merchant.getPanPro()));
        dto.setGSTIN(stringOrNull(merchant.getGstLit()));
        dto.setDriving_license(stringOrNull(merchant.getDrivingLicense()));
        dto.setVoter_id(stringOrNull(merchant.getVoterId()));
        dto.setActive(merchant.isStatus());
        List<MerchantProductRoute> productRoutes = merchant.getProductRoutes();
        List<MerchantDetailsDto.Product> productDtos = productRoutes == null
            ? Collections.emptyList()
            : productRoutes.stream()
                .map(route -> mapProductDto(route, merchant.getMid()))
                .collect(Collectors.toList()); // Use this instead of .toList() if using Java < 16

        dto.setProductDetails(productDtos);
        return dto;
    }

    private MerchantDetailsDto.Product mapProductDto(MerchantProductRoute  route, String merchantId) {
        MerchantDetailsDto.Product productDto = new MerchantDetailsDto.Product();
        productDto.setProductId(route.getProduct_id());
        productDto.setProductName(route.getProductName());
        List<MerchantRouting> routings = merchantRoutingRepo
            .findByMidAndProductProductIdAndIsActiveTrue(merchantId, route.getProduct_id());
        List<MerchantDetailsDto.Provider> providerDtos = routings.stream()
            .map(this::mapProviderDto)
            .toList();
        productDto.setProvider(providerDtos);
        return productDto;
    }

    private MerchantDetailsDto.Provider mapProviderDto(MerchantRouting routing) {
    	log.info("provider Details Response Set dto");
        MerchantDetailsDto.Provider providerDto = new MerchantDetailsDto.Provider();
        ClientMaster client = routing.getClient();
        providerDto.setProviderId(client.getId());
        providerDto.setProviderName(client.getServiceProviderName());
        providerDto.setProviderAppId(client.getAppId());
        providerDto.setProviderAppkey(client.getApiKey());
        providerDto.setAadhaarOtpSendUrl(client.getApiAadharUrl1());
        providerDto.setAadhaarOtpVerifyUrl(client.getApiAadharUrl2());
        providerDto.setPanUrl(client.getApiPanUrl1());
        providerDto.setGstinUrl(client.getApiGstUrl1());
        providerDto.setDrivingLsUrl(client.getDrivingLsUrl());
        providerDto.setVoterIdUrl(client.getVoterIdUrl());
        List<MerchantDetailsDto.Charges> chargesDtos = routing.getCharges() == null
            ? Collections.emptyList()
            : routing.getCharges().stream()
                .map(charge -> {
                    MerchantDetailsDto.Charges chargeDto = new MerchantDetailsDto.Charges();
                    chargeDto.setRouteId(routing.getRouteId());
                    chargeDto.setMerchnatId(charge.getMerchantId());
                    chargeDto.setProductRate(charge.getProductRate());
                    return chargeDto;
                })
                .toList();

        providerDto.setMerchantcharges(chargesDtos);
        return providerDto;
    }

    private String stringOrNull(Object obj) {
        return obj == null ? null : obj.toString();
    }

	@Override
	public ServerResponse saveProduct(String productName) {
	    try {
	        if (productName == null || productName.trim().isEmpty()) {
	            return new ServerResponse(HttpStatus.BAD_REQUEST.value(), "Product name cannot be empty", null);
	        }
	        Optional<ProductMaster> existing = productRepo.findByProductNameIgnoreCase(productName.trim());
	        if (existing.isPresent()) {
	            return new ServerResponse(HttpStatus.CONFLICT.value(), "Product already exists", null); 
	        }
	        ProductMaster product = new ProductMaster();
	        product.setProductName(productName.trim().toUpperCase()); 

	        ProductMaster saved = productRepo.save(product);
	        return new ServerResponse(HttpStatus.OK.value(), "Product saved successfully", saved.getProductName());

	    } catch (Exception e) {
	        // Log the exception (optional)
	        e.printStackTrace(); // or use a logger
	        return new ServerResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error: " + e.getMessage(), null);
	    }
	}

	@Override
	public ServerResponse SaveProviderProductRate() {
		
		return null;
	}

	
	
	
	
	
	
	
	
	
	

	@Override
	public ServerResponse merchantRoutingReportDetails(MerchantRoutingReportRequest req) {
	    // Check if merchant exists
	    Optional<Merchant_Master> optionalMerchant = masterMerchantRepo.findByMid(req.getMerchantId());
	    if (!optionalMerchant.isPresent()) {
	        return new ServerResponse(HttpStatus.NOT_FOUND.value(), "Merchant not found", null);
	    }

	    // Use Specification to filter
	    List<MerchantRouting> filteredRoutings = merchantRoutingRepo.findAll(
	        MerchantRoutingSpecification.filter(optionalMerchant.get().getId(),req.getProviderId())
	    );

	    // Map entity to DTO to clean response and avoid lazy loading issues
	    List<MerchantRoutingReportResponseDTO> responseList = filteredRoutings.stream().map(routing -> {
	    	MerchantRoutingReportResponseDTO dto = new MerchantRoutingReportResponseDTO();
	        dto.setMerchantId(routing.getMerchant().getMid());
	        dto.setMerchantName(routing.getMerchant().getName());
	        dto.setProviderId(routing.getClient().getId());
	        dto.setProviderName(routing.getClient().getServiceProviderName());
	        //dto.setProductId(routing.getProduct().getProductId());
	        //dto.setProductName(routing.getProduct().getProductName());
	       // dto.setRate(routing.getRate());
	        dto.setCreatedAt(routing.getCreatedAt().toString());
	        dto.setCreatedBy(routing.getCreatedBy());
	        dto.setUpdatedAt(routing.getUpdatedAt().toString());
	        dto.setUpdatedBy(routing.getUpdatedBy());
	        dto.setActive(routing.isActive());
	        return dto;
	    }).collect(Collectors.toList());

	    return new ServerResponse(HttpStatus.OK.value(), "Success", responseList);
	}




	@Override
	public ServerResponse checkRoutingrate(Long clientId, Long productId, Long productRate) {
	    try {
	    	log.info("The checkRoutingrate---"+productRate);
	        Long productMaxRate = providerProductRateRepo.findMaxProviderRate(clientId, productId);

	        // Format result to two decimal places
	        double productMaxRateFormatted = productMaxRate / 100.0;
	        double productRateFormatted = productRate / 100.0;
	        DecimalFormat decimalFormat = new DecimalFormat("0.0");
	        boolean isGreater = productRate >= productMaxRate;
	        String message = isGreater
	                ? "Valid rate is greater than provider rate: " + decimalFormat.format(productMaxRateFormatted)
	                  + " Product Rate: " + decimalFormat.format(productRateFormatted)
	                : "Invalid rate is less than provider rate: " + decimalFormat.format(productMaxRateFormatted)
	                  + " Product Rate: " + decimalFormat.format(productRateFormatted);

	        return new ServerResponse(
	                isGreater ? 200 : 400,
	                isGreater ? "success" : "failed",
	                message
	        );
	    } catch (Exception e) {
	        return new ServerResponse(500, "Internal server error: " + e.getMessage(), null);
	    }
	}

	
	
	
	@Override
	public ServerResponse getProductRoutesByMid(String mid) {
	    log.info("Merchant routes details update Service method Inside {}", mid);
	    List<Map<String, Object>> rawResults = merchantRoutingRepo.findActiveRoutesByMidNative(mid);

	    if (rawResults == null || rawResults.isEmpty()) {
	        return new ServerResponse(400, "Routing details not found", null);
	    }
	    List<ProductRoutesResponseDTO.RoutingDetail> routingDetails = rawResults.stream()
	        .map(row -> {
	            ProductRoutesResponseDTO.RoutingDetail detail = new ProductRoutesResponseDTO.RoutingDetail();
	            detail.setClientId(row.get("clientId") != null ? ((Number) row.get("clientId")).longValue() : null);
	            detail.setProviderName((String) row.get("providerName"));
	            detail.setProductId(row.get("productId") != null ? ((Number) row.get("productId")).longValue() : null);
	            detail.setProductName((String) row.get("productName"));
	            detail.setIsActive(row.get("isActive") != null ? (Boolean) row.get("isActive") : false);
	            detail.setRate(row.get("rate") != null ? ((Number) row.get("rate")).longValue() : 0L);
	            return detail;
	        }).toList();

	    ProductRoutesResponseDTO responseDTO = new ProductRoutesResponseDTO();
	    responseDTO.setRoutingDetails(routingDetails);
	    return new ServerResponse(200, "Routing update fetch details success", responseDTO);
	}



	
	


}

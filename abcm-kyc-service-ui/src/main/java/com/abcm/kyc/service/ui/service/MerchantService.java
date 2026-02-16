package com.abcm.kyc.service.ui.service;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.abcm.kyc.service.ui.dto.DateRange;
import com.abcm.kyc.service.ui.repository.AppUserRepository;
import com.abcm.kyc.service.ui.repository.MerchantRepository;
import com.abcm.kyc.service.ui.repository.WalletRepository;
import com.abcm.kyc.service.ui.util.CommonUtils;
import com.abcm.kyc.service.ui.util.GetFilterDate;
import com.abcm.kyc.service.ui.util.SendFailureEmail;
import com.abcmkyc.entity.Merchant_Master;
import com.abcmkyc.entity.Wallet;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class MerchantService {

	@Autowired
	private MerchantRepository merchantRepository;

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
    private PasswordEncoder passwordEncoder; 
	
	@Autowired
	AppUserRepository appUserRepository;
	
	
	
	@Value("${email.lowbalance.subject}")
	private String lowbalanceSubject;

	@Value("${email.lowBalance.template.path}")
	private String emaillowBalanceTemplatePath;
	
	

	@Autowired
	private  SendFailureEmail sendFailureEmail;
	
	 @Value("${email.send.to}")
	 private String to;
	
	public Merchant_Master getMerchantByUsername(String username) {
		return merchantRepository.findByCredentialsUsername(username).get();
	}

	public Wallet getWalletByMerchantId(String mid) {
		Wallet wallet = walletRepository.findByMerchantId(mid).get();
		return wallet;
	}

	public Integer getKycCount(String merchantId, String filter) {
		System.out.println("Kyc Count Data"+merchantId+"Filter"+filter);
	    // Normalize merchantId to empty string if null or blank
	    if (merchantId == null || merchantId.isBlank()) {
	        merchantId = "";
	    }

	    // If filter is null, blank, or empty, call repository with empty dates (empty strings)
	    if (filter == null || filter.isBlank()) {
	    
	        return merchantRepository.getKycDataCount(merchantId, null, null);
	    }

	    // Otherwise parse date range from filter
	    DateRange dateRange = GetFilterDate.getDateRange(filter);
	    Date startDate = Date.valueOf(dateRange.getStartDate()); // "yyyy-MM-dd"
	    Date endDate = Date.valueOf(dateRange.getEndDate());

	    System.out.println("filter** " + filter);
	    System.out.println("startDate " + startDate);
	    System.out.println("endDate " + endDate);
	    System.out.println("COUNT " + merchantRepository.getKycDataCount(merchantId, startDate, endDate));

	    return merchantRepository.getKycDataCount(merchantId, startDate, endDate);
	}

	public List<Map<String, Object>> getProductUsageDataMonthwise(String merchantId, int year) {
		List<Object[]> results = merchantRepository.getKycDataMonthwise(merchantId, year);
		List<Map<String, Object>> monthwiseList = new ArrayList<>();
		for (Object[] row : results) {
			Map<String, Object> monthData = new HashMap<>();
			monthData.put("monthNumber", row[0]);
			monthData.put("monthName", row[1]);
			monthData.put("count", row[2]);
			monthwiseList.add(monthData);
		}

		return monthwiseList;
	}

	public int getSubscribedServicesCount(Merchant_Master merchant) {
	    int count = 0;
	    count += "ENABLE".equalsIgnoreCase(String.valueOf(merchant.getAadharOkyc())) ? 1 : 0;
	    count += "ENABLE".equalsIgnoreCase(String.valueOf(merchant.getPanPro())) ? 1 : 0;
	    count += "ENABLE".equalsIgnoreCase(String.valueOf(merchant.getGstLit())) ? 1 : 0;

	    return count;
	}
	
	
	
	
	public boolean changePassword(String mid, String newPassword, String confirmPassword) {
	    Optional<Merchant_Master> optionalMerchant = merchantRepository.findByMid(mid);
	    if (!optionalMerchant.isPresent()) {
	        return false;
	    }
	    if (!newPassword.equals(confirmPassword)) {
	        return false;
	    }
	    Merchant_Master merchant = optionalMerchant.get();
	    String newEncodedPassword = passwordEncoder.encode(confirmPassword);
	    merchant.getCredentials().setPassword(newEncodedPassword);
	    merchantRepository.save(merchant);
	    return true;
	}
	
	

    public String getRoleNameByUsername(String username) {
        return appUserRepository.findRoleNameByUsername(username)
                .orElseThrow(() -> new RuntimeException("Role not found for username: " + username));
    }
    
    
    
    public void sendMerchnatLowBalanceEmail(String MerchantName,String email, long balance )
    {
    	
    	CompletableFuture.runAsync(() -> {
			try {
				//String email = dlVerificationRepository.findEmailByMid(merchantId);
				log.info("The email Of then Merchant Id" + email);
				// Step 1: Format balance
				double finalAmount = balance / 100.0;
				String formattedAmount = String.format("%.2f", finalAmount); // → "45.00"
				log.info("Email Template Path: {}", emaillowBalanceTemplatePath);
				String mailstring1 = CommonUtils
						.readUsingFileInputStream(emaillowBalanceTemplatePath);
				mailstring1 = mailstring1.replace("{{Your_Balance}}", formattedAmount);
				mailstring1 = mailstring1.replace("{{MerchantName}}", MerchantName);
				sendFailureEmail.sendEkycFailureEmail(mailstring1, "", lowbalanceSubject, email);

			} catch (Exception e) {
				e.printStackTrace(); // Consider logging instead
			}
		});
    	
    }

}

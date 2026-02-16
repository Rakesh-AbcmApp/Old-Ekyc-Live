package com.abcm.kyc.service.ui.payment;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.abcm.kyc.service.ui.dto.AddBalanceRequest;
import com.abcm.kyc.service.ui.dto.ApiResponseModel;
import com.abcm.kyc.service.ui.dto.OfflineWalletRequest;
import com.abcm.kyc.service.ui.dto.PaymentResponseModel;
import com.abcm.kyc.service.ui.dto.TransactionRequest;
import com.abcm.kyc.service.ui.repository.MerchantRepository;
import com.abcm.kyc.service.ui.repository.TransactionHistoryRepo;
import com.abcm.kyc.service.ui.repository.WalletRepository;
import com.abcmkyc.entity.Merchant_Master;
import com.abcmkyc.entity.TransactionHistory;
import com.abcmkyc.entity.Wallet;
import com.google.gson.Gson;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TransactionHistoryRepo txnHistoryRepo;

	@Value("${ablePayUrl}")
	private String ablePayUrl;

	@Autowired
	private WalletRepository walletRepository;

	@Value("${environment}")
	private String environment;

	@Value("${apiKey}")
	private String apiKey;

	@Value("${salt}")
	private String salt;

	@Value("${returnUrl}")
	private String returnUrl;
	
	private final Environment env;
	
	@Autowired
	MerchantRepository merchantRepository;
	

	public String processPayment(AddBalanceRequest addBalanceRequest, HttpServletRequest request) throws Exception {

		TransactionRequest txnReq = createTransactionRequest(addBalanceRequest);

		// save logs for transaction
		TransactionHistory txHistory = new TransactionHistory();

		try {
			long reqAmount = Long.parseLong(addBalanceRequest.getAmount()) * 100;
			txHistory.setOrderId(txnReq.getOrder_id());
			// txHistory.setRollNumber(txnReq.getUdf4());
			txHistory.setPayUrl(ablePayUrl);
			// txHistory.setBillPayUrl(payBillUrl);
			// txHistory.setAblepayRequest(new Gson().toJson(txnReq));
			// txHistory.setTxnId(txnId);
			// txHistory.setWallet(null);
			// txHistory.setMode();
			txHistory.setInitiateDate(LocalDateTime.now());
			// txHistory.setPaymentDate(LocalDateTime.now());
			txHistory.setAmount(reqAmount);
			txHistory.setRequest(new Gson().toJson(txnReq));
			// transaction.setResponse("Balance added successfully");
			// transaction.setTxnStatus("SUCCESS");
			txHistory.setCreatedBy(addBalanceRequest.getUsername());
			logger.info("txHistory : {}", txHistory.toString());
			txnHistoryRepo.save(txHistory);
		} catch (Exception e) {
			// Log the exception or handle it as needed
			logger.error("Failed to save transaction log entity: {}");
			// throw new CustomException("Error saving transaction log", e);
		}
		setRequestAttributes(request, txnReq);
		logger.info("Transaction hash: {}", txnReq.getHash());
		return "Admin/ForwardToPG";
	}

	public TransactionRequest createTransactionRequest(AddBalanceRequest addBalanceRequest) {
		TransactionRequest txnReq = new TransactionRequest();
		txnReq.setPhone(addBalanceRequest.getMobileNo());
		double baseAmount = Double.parseDouble(addBalanceRequest.getAmount()); 
		log.info("Based Amount:{}"+baseAmount);
        double gstAmount = baseAmount * 0.18;
        double totalAmount = baseAmount + gstAmount;
        log.info("PG Total Amount With GSt:{}"+totalAmount);
		txnReq.setAmount("" + totalAmount);// pg Amount
		txnReq.setName(addBalanceRequest.getMerchantName());
		txnReq.setEmail(addBalanceRequest.getEmail());
		txnReq.setUdf1(addBalanceRequest.getMerchantId());
		 txnReq.setUdf2(""+baseAmount);  // display amount wallet amount
		// txnReq.setUdf3(feeCollectionRequest.getStudentName());
		// txnReq.setUdf4(feeCollectionRequest.getAttributeValue());
		txnReq.setApi_key(apiKey);
		txnReq.setOrder_id(generateTxnId());
		txnReq.setCurrency("INR");
		txnReq.setReturn_url(returnUrl);
		txnReq.setDescription("KYC Add Balance");
		txnReq.setAddress_line_1("");
		txnReq.setAddress_line_2("");
		txnReq.setCountry(addBalanceRequest.getCountry());
		txnReq.setZip_code(addBalanceRequest.getPincode());
		txnReq.setMode(environment);
		txnReq.setCity(addBalanceRequest.getCity());
		txnReq.setState(addBalanceRequest.getState());
		txnReq.setHash(getHashCode(salt, txnReq));
		logger.info("Transaction Request: {}", txnReq);
		return txnReq;
	}
	
	
	

	private void setRequestAttributes(HttpServletRequest request, TransactionRequest txnReq) {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("payUrl", ablePayUrl);
		attributes.put("phone", txnReq.getPhone());
		attributes.put("amount", txnReq.getAmount());
		attributes.put("name", txnReq.getName());
		attributes.put("email", txnReq.getEmail());
		attributes.put("udf1", txnReq.getUdf1());
		attributes.put("udf2", txnReq.getUdf2());
		attributes.put("udf3", txnReq.getUdf3());
		attributes.put("udf4", txnReq.getUdf4());
		attributes.put("udf5", txnReq.getUdf5());
		attributes.put("apiKey", txnReq.getApi_key());
		attributes.put("orderId", txnReq.getOrder_id());
		attributes.put("currency", txnReq.getCurrency());
		attributes.put("returnUrl", txnReq.getReturn_url());
		attributes.put("description", txnReq.getDescription());
		attributes.put("addressLine1", txnReq.getAddress_line_1());
		attributes.put("addressLine2", txnReq.getAddress_line_2());
		attributes.put("country", txnReq.getCountry());
		attributes.put("zipCode", txnReq.getZip_code());
		attributes.put("mode", txnReq.getMode());
		attributes.put("city", txnReq.getCity());
		attributes.put("hash", txnReq.getHash());
		attributes.put("state", txnReq.getState());
		attributes.forEach(request::setAttribute);
	}

	public String getHashCode(String salt, TransactionRequest txnReq) {
		String hashData = buildHashData(salt, txnReq, getTransactionHashColumns());
		return calculateHash(hashData);
	}

	private String calculateHash(String hashData) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] byteData = md.digest(hashData.getBytes(StandardCharsets.UTF_8));
			StringBuilder hashCodeBuffer = new StringBuilder();
			for (byte b : byteData) {
				hashCodeBuffer.append(String.format("%02x", b));
			}
			return hashCodeBuffer.toString().toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error calculating hash: ", e);
			throw new RuntimeException("Error calculating hash", e);
		}
	}

	private String buildHashData(String salt, Object requestObj, String[] columns) {
		StringBuilder hashData = new StringBuilder(salt);
		for (String column : columns) {
			String value = getValue(requestObj, column);
			if (value != null && !value.isBlank()) {
				hashData.append('|').append(value);
			} else {
				logger.info("Missing value for column: {}", column);
			}
		}
		logger.info("Hash data: {}", hashData);
		return hashData.toString();
	}

	private String getValue(Object payload, String fieldName) {
		try {
			return (String) payload.getClass().getDeclaredMethod("get" + capitalize(fieldName)).invoke(payload);
		} catch (Exception e) {
			return null;
		}
	}

	private String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	private String[] getTransactionHashColumns() {
		return new String[] { "address_line_1", "address_line_2", "amount", "api_key", "city", "country", "currency",
				"description", "email", "mode", "name", "order_id", "phone", "return_url", "state", "udf1", "udf2",
				"udf3", "udf4", "udf5", "zip_code" };
	}

	public String generateTxnId() {
		return "ABCMKYC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
	}

	public static LocalDateTime getLocalDateTimeByStringFormated(String paymentDate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try {
			LocalDateTime localPaymentDate = LocalDateTime.parse(paymentDate, formatter);
			return localPaymentDate;
		} catch (DateTimeParseException e) {
			// handle invalid date format
			return null;
		}
	}

	@Transactional
	public PaymentResponseModel handleWalletBalanceUpdate(HttpServletRequest request) {
		try {
			log.info("Payment Gatway Response Balance update Method");
			PaymentResponseModel paymentResponseModel = verifyPaymentHash(request);
			Map<String, String> params = extractRequestParameters(request);

			logger.info("Payment Response: {}", paymentResponseModel);
			logger.info("Parameters: {}", params);
			String orderId = paymentResponseModel.getOrder_id();
			String txnId = paymentResponseModel.getTransaction_id();
			String txnStatus = paymentResponseModel.getResponse_message();
			String paymentMode = paymentResponseModel.getPayment_mode();
			String paymentDate = paymentResponseModel.getPayment_datetime();
			String mid = paymentResponseModel.getUdf1();
			Wallet wallet = null;
			if ("Transaction successful".equalsIgnoreCase(params.get("responseMessage"))) {
				// Find existing wallet or create a new one
				wallet = walletRepository.findByMerchantId(mid).orElseGet(() -> {
					Wallet newWallet = new Wallet();
					newWallet.setMerchantId(mid);
					newWallet.setBalance(0L); // Initialize balance
					newWallet.setTxnDate(LocalDateTime.now());
					newWallet.setCreatedBy(paymentResponseModel.getName());
					return walletRepository.save(newWallet);
				});
				// Convert amount to paise (from rupees)
				BigDecimal amount = new BigDecimal(params.get("udf2"));
				Long Txn_amountInPaise = amount.multiply(BigDecimal.valueOf(100)).longValue();
				log.info("Request Amount" + Txn_amountInPaise);
				log.info("Exsting Wallet Amount{}" + wallet.getBalance());
				long WalletupdateBalance = wallet.getBalance() + Txn_amountInPaise;
				log.info("Total Updated wallet Amount" + WalletupdateBalance);
				long Merchant_Alert_Balance = (long) (WalletupdateBalance * 0.2); // 20% of the updated balance
				log.info("Payment Gatway Txn Wallet Balance 20%{} " + Merchant_Alert_Balance);
				// Update wallet balance
				wallet.setBalance(WalletupdateBalance);
				wallet.setTxnDate(LocalDateTime.now());
				 wallet.setValidity(LocalDateTime.now().plusDays(Long.valueOf(env.getProperty("urls.validity"))));
				wallet.setAlertBalance(Merchant_Alert_Balance);
				wallet.setUpdatedBy(paymentResponseModel.getName());
				walletRepository.save(wallet);
				log.info("Payment Gatway  Wallet Update Sucess{}", WalletupdateBalance,
						"Alert balance{}" + Merchant_Alert_Balance);
			}
			String paymentResponseJson = new Gson().toJson(paymentResponseModel);
			// Update transaction history
			txnHistoryRepo.updateTransactionByOrderIdWithWallet(getLocalDateTimeByStringFormated(paymentDate),
					paymentResponseJson, txnStatus, paymentMode, txnId, wallet, orderId);
			logger.info("Updated transaction history for Order ID: {}", orderId);
			return paymentResponseModel;

		} catch (Exception e) {
			logger.error("Error processing wallet update: {}", e.getMessage(), e);
			return null;
		}
	}

	@Transactional
	public ApiResponseModel offlineBalanceAdd(OfflineWalletRequest request) {
		try {
			log.info("offline Balance Add  update Method",request);
			Wallet wallet = walletRepository.findByMerchantId(request.getMid()).orElseGet(() -> {
				Wallet newWallet = new Wallet();
				newWallet.setMerchantId(request.getMid());
				newWallet.setBalance(0L);
				newWallet.setTxnDate(LocalDateTime.now());
				newWallet.setCreatedBy(request.getUsername());
				
				return walletRepository.save(newWallet);
			});
			long amountInPaise = new BigDecimal(request.getWalletAmount()).multiply(BigDecimal.valueOf(100))
					.longValue();
			log.info("Request Amount{} " + amountInPaise);
			log.info("Exsting Wallet Amount{} " + wallet.getBalance());
			long updateBalance = wallet.getBalance() + amountInPaise;
			log.info("Total Update Wallet Amount{} " + updateBalance);
			long AlertBalance = (long) (updateBalance * 0.2);
			log.info("Offline Balance  Method 20% amount{Total Update Balance} " + AlertBalance);
			wallet.setBalance(updateBalance);
			wallet.setTxnDate(LocalDateTime.now());
			 wallet.setValidity(LocalDateTime.now().plusDays(Long.valueOf(env.getProperty("urls.validity"))));
			wallet.setAlertBalance(AlertBalance);
			wallet.setUpdatedBy(request.getUsername());
			walletRepository.save(wallet);
			log.info("Offline Balance Wallet Update Sucess{}", updateBalance, "Alert balance{}" + AlertBalance);
			TransactionHistory txn = new TransactionHistory();
			txn.setAmount(amountInPaise);
			txn.setInitiateDate(LocalDateTime.now());
			txn.setPaymentDate(LocalDateTime.now());
			txn.setMode(request.getPaymentMode());
			txn.setOrderId(generateTxnId());
			txn.setTxnStatus("Transaction successful");
			txn.setCreatedBy(request.getUsername());
			txn.setWallet(wallet);
			txnHistoryRepo.save(txn);
			log.info("Transaction  Save successfully{}");
			return new ApiResponseModel(200, "Balance added successfully", null);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error processing wallet update: {}", e.getMessage());
			return new ApiResponseModel(500, "Error processing request", null);
		}
	}

	private Map<String, String> extractRequestParameters(HttpServletRequest request) {
	    return Map.of(
	        "responseMessage", request.getParameter("response_message"),
	        "responseCode", request.getParameter("response_code"),
	        "transactionId", request.getParameter("transaction_id"),
	        "udf4", request.getParameter("udf4"),
	        "orderId", request.getParameter("order_id"),
	        "attributeName", request.getParameter("udf3"),
	        "attributeValue", request.getParameter("udf4"),
	        "amount", request.getParameter("amount"),
	        "udf2", request.getParameter("udf2")  // ✅ added missing key
	    );
	}

	public PaymentResponseModel verifyPaymentHash(HttpServletRequest request) throws Exception {
		PaymentResponseModel responseModel = new PaymentResponseModel();
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// String salt = "18e6063d410586se913fa536be8dbf237a6c15ed";
			if (salt != null && !salt.isEmpty()) {
				Map<String, String[]> parameters = new TreeMap<>(request.getParameterMap());
				parameters.remove("hash");
				StringBuilder hashData = new StringBuilder(salt);
				for (String parameter : parameters.keySet()) {
					if (parameters.get(parameter)[0].length() > 0) {
						switch (parameter) {
						case "transaction_id":
							responseModel.setTransaction_id(parameters.get(parameter)[0]);
							break;
						case "country":
							responseModel.setCountry(parameters.get(parameter)[0]);
							break;
						case "amount":
							responseModel.setAmount(parameters.get(parameter)[0]);
							break;
						case "payment_mode":
							responseModel.setPayment_mode(parameters.get(parameter)[0]);
							break;
						case "response_code":
							responseModel.setResponse_code(parameters.get(parameter)[0]);
							break;
						case "city":
							responseModel.setCity(parameters.get(parameter)[0]);
							break;
						case "description":
							responseModel.setDescription(parameters.get(parameter)[0]);
							break;
						case "udf3":
							responseModel.setUdf3(parameters.get(parameter)[0]);
							break;
						case "udf4":
							responseModel.setUdf4(parameters.get(parameter)[0]);
							break;
						case "udf5":
							responseModel.setUdf5(parameters.get(parameter)[0]);
							break;
						case "udf1":
							responseModel.setUdf1(parameters.get(parameter)[0]);
							break;
						case "udf2":
							responseModel.setUdf2(parameters.get(parameter)[0]);
							break;
						case "zip_code":
							responseModel.setZip_code(parameters.get(parameter)[0]);
							break;
						case "response_message":
							responseModel.setResponse_message(parameters.get(parameter)[0]);
							break;
						case "payment_channel":
							responseModel.setPayment_channel(parameters.get(parameter)[0]);
							break;
						case "phone":
							responseModel.setPhone(parameters.get(parameter)[0]);
							break;
						case "payment_datetime":
							responseModel.setPayment_datetime(parameters.get(parameter)[0]);
							break;
						case "name":
							responseModel.setName(parameters.get(parameter)[0]);
							break;
						case "currency":
							responseModel.setCurrency(parameters.get(parameter)[0]);
							break;
						case "state":
							responseModel.setState(parameters.get(parameter)[0]);
							break;
						case "order_id":
							responseModel.setOrder_id(parameters.get(parameter)[0]);
							break;
						case "email":
							responseModel.setEmail(parameters.get(parameter)[0]);
							break;
						}
						hashData.append("|").append(parameters.get(parameter)[0]);
					}
				}

				String calculatedHash = "";
				if (hashData.length() > 0) {
					MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
					byte[] digest = messageDigest.digest(hashData.toString().getBytes());

					StringBuilder hashedPassword = new StringBuilder();
					for (byte b : digest) {
						String hex = Integer.toHexString(0xFF & b);
						if (hex.length() == 1) {
							hex = "0" + hex;
						}
						hashedPassword.append(hex);
					}

					calculatedHash = hashedPassword.toString().toUpperCase();
				}

				responseModel.setCalculatedHash(calculatedHash);

				String receivedHash = request.getParameter("hash");
				responseModel
						.setValid_hash((receivedHash != null && receivedHash.equals(calculatedHash)) ? "Yes" : "No");
			} else {
				responseModel.setValid_hash("Set your salt in the application to perform hash validation.");
			}
		}

		return responseModel;
	}

}

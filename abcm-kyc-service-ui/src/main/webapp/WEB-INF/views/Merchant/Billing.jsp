<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<%@ include file="MerchantHeader.jsp"%>
<meta charset="UTF-8">
<title>eKYC | Billing</title>
<style type="text/css">
/* .modal-header {
	display: flex;
	justify-content: center;
	position: relative;
}

.modal-title {
	margin: 0 auto;
	position: absolute;
	left: 50%;
	transform: translateX(-50%);
}

.btn-close {
	position: absolute;
	right: 1rem;
	top: 68%; 
	transform: translateY(-50%);
} */
</style>
</head>
<body>
	<div class="pc-container">
		<div class="pc-content">
			<!-- [ breadcrumb ] start -->
			<div class="page-header">
				<div class="page-block">
					<div class="row align-items-center">
						<div class="col-md-12">
							<div class="page-header-title">
								<h2 class="mb-0">Billings</h2>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- [ breadcrumb ] end -->
			<div class="row">
				<div class="kyc-low-alert mt-1">
					<c:if test="${wallet.balance < wallet.alertBalance}">
						<div class="alert alert-danger d-flex justify-content-between align-items-center" role="alert">
							<div>⚠️ Your wallet balance is low. Please wallet
								recharge to continue using services.</div>
							
						</div>
				</c:if>
				</div>
				<div class="col-md-12 col-xxl-12">
					<div class="add-balance mb-3">
						<button type="button" class="btn btn-shadow btn-info mr-2 mb-0 rounded-0 btn-sm" data-bs-toggle="modal" data-bs-target=".bd-example-modal-lg"> <i class="ti ti-plus"></i> Add Balance
						</button>

					</div>
					<div class="col-12">
						<div class="card billing-table">
							<div class="card-header p-2 rounded-0">
								<h5 class="mb-0">Plan Details</h5>
							</div>


							<div class="card-body">
								<div class="kyc-wallet">
									<div class="row"
										style="display: flex; justify-content: center;">
										<div class="col-md-6 col-xl-3">
											<label class="form-label" for="example-datemax">Current
												Plan</label>
											<p class="kyc-wallet-details">PREPAID</p>
										</div>
										<div class="col-md-6 col-xl-3">
											<label class="form-label" for="example-datemax">Validity</label>
											<p class="kyc-wallet-details">${validityDays} Days</p>
										</div>
										<div class="col-md-6 col-xl-3">
											<label class="form-label" for="example-datemax">Available
												Balance</label>
											<p class="kyc-wallet-details">
												<i class="fas fa-rupee-sign"></i>
												<fmt:formatNumber value="${wallet.balance/100}"
													type="number" minFractionDigits="2" maxFractionDigits="2" />
											</p>
										</div>
										<%-- <div class="col-md-6 col-xl-3">
											<label class="form-label" for="example-datemax">Alert
												Balance </label>
											<p class="kyc-wallet-details">
												<i class="fas fa-rupee-sign"></i>
												<fmt:formatNumber value="${wallet.alertBalance/100}"
													type="number" minFractionDigits="2" maxFractionDigits="2" />
											</p>
										</div> --%>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>


				<div class="col-md-12 col-xxl-12">
					<div class="col-12">
						<div class="card table-card billing-table">
							<div class="card-header  p-2 rounded-0 d-flex justify-content-between">
								<h5 class="mb-0">Transaction History</h5>
								<i class="fas fa-sync-alt"></i>
							</div>
							<div class="card-body">
								<div class="table-responsive">
									<table class="table table-hover" id="pc-dt-simple">
										<thead>
											<tr>
												<th>Date</th>
												<th>Mode</th>
												<th>Amount</th>
												<th>Status</th>
											</tr>
										</thead>


										<tbody>
											<c:forEach var="txn" items="${wallet.transactions}">
												<tr>
													<td><c:out value="${txn.paymentDate}" /></td>
													<td><c:out value="${txn.mode}" /></td>
													<td><fmt:formatNumber value="${txn.amount/100}"
															type="number" minFractionDigits="2" maxFractionDigits="2" /></td>

													<td><span
														class="badge 
								                        <c:choose>
								                            <c:when test="${txn.txnStatus eq 'Transaction successful'}">text-bg-success</c:when>
								                            <c:when test="${txn.txnStatus eq 'Pending'}">text-bg-warning</c:when>
								                            <c:otherwise>text-bg-secondary</c:otherwise>
								                        </c:choose>">
															<c:out value="${txn.txnStatus}" />
													</span></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>


		<!-- modal billing balance -->
		<div class="modal fade bd-example-modal-lg" tabindex="-1"
			role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content  balance-add rounded-0">
					<div class="modal-header rounded-0">
						<h5 class="modal-title h4" id="myLargeModalLabel"> Add Wallet Balance</h5>
						<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"> </button>
					</div>
					<div class="modal-body">
						<div class="custom-body mb-3">
							<div class="labelbbps">
								<label for="validationTooltip05"
									class="font-bold form-label text-green-400 mb-2"> Enter
									Amount to Add.</label>
							</div>
							<div class="input-group">
								<span class="input-group-text"><i
									class="fas fa-rupee-sign"></i></span> <input type="text"
									class="form-control" id="validationTooltip05" required="">
							</div>
						</div>
						<div class="bbps-wallet  mt-3">
							<div class="d-flex flex-wrap gap-2"
								style="justify-content: center;">
								<button type="button" class="btn btn-outline-info">1000</button>
								<button type="button" class="btn btn-outline-info">2000</button>
								<button type="button" class="btn btn-outline-info">3000</button>
								<button type="button" class="btn btn-outline-info">4000</button>
								<button type="button" class="btn btn-outline-info">5000</button>

							</div>
						</div>

						<!-- Hidden Form -->
						<form id="hiddenAmountForm" method="post"
							action="${url}/app/merchant/processToPayRequest"
							style="display: none;">
							<input type="hidden" name="amount" id="hiddenAmountInput">
							<input type="hidden" name="merchantId" id="merchantId" value="${merchant.mid}">
							 <input type="hidden" name="merchantName" id="merchantName" value="${merchant.name}">
							<input type="hidden" name="email" id="email" value="${merchant.email}">
							 <input type="hidden" name="mobileNo" id="mobileNo" value="${merchant.phoneOne}">
							 <input type="hidden" name="country" id="country" value="${merchant.country}">
							  <input type="hidden" name="state" id="state" value="${merchant.state}">
							   <input type="hidden" name="city" id="city" value="${merchant.city}">
							    <input type="hidden" name="pincode" id="pincode" value="${merchant.pincode}">
							    <input type="hidden" name="username" id="username" value="${merchant.credentials.username}">
						</form>

						<div class="bbps-proceed mt-4 text-center">
							<div id="error-msg" style="color: red;  display: none; margin-bottom: 10px;">Enter
								Valiad Amount</div>
							<button type="button" class="btn btn-shadow btn-info rounded-0 btn-sm" id="" data-bs-dismiss="modal" aria-label="Close">Cancel</button>
							<button type="button" class="btn btn-shadow btn-success rounded-0 btn-sm" id="proceedToPayBtn">Proceed to Pay</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- modal billing balance -->
		
		
		
		<!-- -----------Model Transaction Response -->
		<c:if test="${not empty paymentResponse}">
			<script>
        document.addEventListener("DOMContentLoaded", function () {
            var modal = new bootstrap.Modal(document.querySelector('.bd-payment-response-modal'));
            modal.show();
        });
    </script>
			<div class="modal fade bd-payment-response-modal" tabindex="-1"
				role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
				<div class="modal-dialog modal-md">
					<div class="modal-content balance-add rounded-0">
						<div class="modal-header rounded-0">
							<h5 class="modal-title h4" id="myLargeModalLabel">Payment
								Response</h5>
							<button type="button" class="btn-close" data-bs-dismiss="modal"
								aria-label="Close"></button>
						</div>
						<div class="modal-body m-0 p-0 rounded-0">
                             <div class="p-3"> 
							<!-- Common payment response details -->
							<p>
								<strong>Transaction ID:</strong>
								${paymentResponse.transaction_id}
							</p>
							<p>
								<strong>Amount:</strong> ₹
								<fmt:formatNumber value="${paymentResponse.amount}"
									type="number" minFractionDigits="2" maxFractionDigits="2" />
							</p>
							<p>
								<strong>Available Balance:</strong> ₹
								<fmt:formatNumber value="${wallet.balance / 100}" type="number"
									minFractionDigits="2" maxFractionDigits="2" />
							</p>
							<c:choose>
								<c:when
									test="${fn:containsIgnoreCase(paymentResponse.response_message, 'success')}">
									<p>
								<strong >Status:</strong> <span style="color:green">${paymentResponse.response_message}</span>
							</p>		
								</c:when>
								<c:otherwise>
									<strong >Status:</strong> <span style="color:red">${paymentResponse.response_message}</span>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="text-center mb-3 rounded-0">
							<button type="button" class="btn btn-shadow btn-info  mb-0 rounded-0 btn-sm"
								data-bs-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
	</div>
	<!-- modal billing balance end -->
	</c:if>
<%@ include file="MerchantFooter.jsp"%>
</body>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script type="text/javascript">
// Auto-fill input when amount button is clicked
document.querySelectorAll('.quick-amount').forEach(button => {
    button.addEventListener('click', function () {
        const amount = this.getAttribute('data-amount');
        document.getElementById('validationTooltip05').value = amount;
    });
});

// Proceed to Pay button logic
document.getElementById('proceedToPayBtn').addEventListener('click', function () {
    const amountValue = document.getElementById('validationTooltip05').value.trim();
    if (amountValue === '' || isNaN(amountValue) || Number(amountValue) <= 0 ||  Number(amountValue) < 1000) {
    	 $('#error-msg').text('Amount must not be 0 and should be at least 1000.')
         .fadeIn()
         .delay(4000)
         .fadeOut();
    return;
    }
    document.getElementById('hiddenAmountInput').value = amountValue;
    document.getElementById('hiddenAmountForm').submit();
});

$(document).ready(function() {
    $('.btn-outline-info').on('click', function() {
        // Get the value of the clicked button
        var buttonValue = $(this).text();
        // Set the input field value to the button value
        $('#validationTooltip05').val(buttonValue);
    });
    $('.btn-close').on('click', function () {
        $('#validationTooltip05').val('');
      });
});
</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<%@ include file="MerchantHeader.jsp"%>
<meta charset="UTF-8">
<title>eKYC | Dashboard</title>
<!-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script> -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>

<c:if test="${merchant.dashboard == 'ENABLE'}">
	<div class="pc-container">
		<div class="dh-350"></div>
		<div class="pc-content neg-h">
			<div class="page-header">
				<div class="page-block">
					<div class="row align-items-center">
						<div class="col-md-12">
							<div class="page-header-title">
								<h2 class="mb-0">Dashboard</h2>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- [ breadcrumb ] end -->
			<!-- [ Main Content ] start -->
			<div class="row pt-0">
				<div class="kyc-low-alert mt-1">
					<c:if test="${wallet.balance < wallet.alertBalance}">
						<div class="alert alert-danger d-flex justify-content-between align-items-center" role="alert">
							<p class="m-0">⚠️ Your wallet balance is low. Please wallet recharge to continue using services.</p>
							<button data-bs-toggle="modal" data-bs-target="#rechargeModal" data-bs-backdrop="static" data-bs-keyboard="false"
								class="btn btn-primary shadow-sm btn-sm rounded-0">Recharge Now</button>
						</div>
					</c:if>
				</div>
				<div class="col-md-8">
					<div class="m-boxes">
						<div class="row">
							<div class="col-md-6">
								<div class="card">
									<div class="card-body">
										<div class="d-flex align-items-center">
											<div class="flex-shrink-0">
												<div class="avtar avtar-s bg-light-primary">
													<svg width="24" height="24" viewBox="0 0 24 24" fill="none"
														xmlns="http://www.w3.org/2000/svg">
                                        <path opacity="0.4" d="M13 9H7"
															stroke="#4680FF" stroke-width="1.5"
															stroke-linecap="round" stroke-linejoin="round" />
                                        <path
															d="M22.0002 10.9702V13.0302C22.0002 13.5802 21.5602 14.0302 21.0002 14.0502H19.0402C17.9602 14.0502 16.9702 13.2602 16.8802 12.1802C16.8202 11.5502 17.0602 10.9602 17.4802 10.5502C17.8502 10.1702 18.3602 9.9502 18.9202 9.9502H21.0002C21.5602 9.9702 22.0002 10.4202 22.0002 10.9702Z"
															stroke="#4680FF" stroke-width="1.5"
															stroke-linecap="round" stroke-linejoin="round" />
                                        <path
															d="M17.48 10.55C17.06 10.96 16.82 11.55 16.88 12.18C16.97 13.26 17.96 14.05 19.04 14.05H21V15.5C21 18.5 19 20.5 16 20.5H7C4 20.5 2 18.5 2 15.5V8.5C2 5.78 3.64 3.88 6.19 3.56C6.45 3.52 6.72 3.5 7 3.5H16C16.26 3.5 16.51 3.50999 16.75 3.54999C19.33 3.84999 21 5.76 21 8.5V9.95001H18.92C18.36 9.95001 17.85 10.17 17.48 10.55Z"
															stroke="#4680FF" stroke-width="1.5"
															stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
												</div>
											</div>
											<div class="flex-grow-1 ms-3">
												<h6 class="mb-0">Transactions Volume</h6>
											</div>
											<div class="flex-shrink-0 ms-3">
												<div class="dropdown">
													<a
														class="avtar avtar-s btn-link-secondary dropdown-toggle arrow-none"
														href="${url}/app/merchant/kycReport"
														data-bs-toggle="dropdown" aria-haspopup="true"
														aria-expanded="false"> <i
														class="ti ti-dots-vertical f-18"></i>
													</a>
													<div class="dropdown-menu dropdown-menu-end">
														<a class="dropdown-item" href="#"
															onclick="getKycData('today')">Today</a> <a
															class="dropdown-item" href="#"
															onclick="getKycData('weekly')">Weekly</a> <a
															class="dropdown-item" href="#"
															onclick="getKycData('monthly')">Monthly</a>
													</div>
												</div>

											</div>
										</div>


										<div class="bg-body p-2 mt-1 rounded-0">
											<div class="mt-3">
												<div class="kb-vol"> 
													<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="#e923c4" viewBox="0 0 24 24">
														<path d="M12 2C6.477 2 2 3.79 2 6v12c0 2.21 4.477 4 10 4s10-1.79 10-4V6c0-2.21-4.477-4-10-4zm0 2c4.418 0 8 1.343 8 2s-3.582 2-8 2-8-1.343-8-2 3.582-2 8-2zm0 16c-4.418 0-8-1.343-8-2v-2c1.528.932 4.474 1.5 8 1.5s6.472-.568 8-1.5v2c0 .657-3.582 2-8 2zm0-4c-4.418 0-8-1.343-8-2v-2c1.528.932 4.474 1.5 8 1.5s6.472-.568 8-1.5v2c0 .657-3.582 2-8 2zm0-4c-4.418 0-8-1.343-8-2V8c1.528.932 4.474 1.5 8 1.5s6.472-.568 8-1.5v2c0 .657-3.582 2-8 2z"/>
													</svg>
													<h3 class="mb-1" id="txnVolume"></h3>
												</div>
												<!--  <div class="col-4">
                                        <p class="text-primary mb-0"><i class="ti ti-arrow-up-right"></i> 30.6%</p>
                                    </div> -->
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-md-6">
								<div class="card">
									<div class="card-body">
										<div class="d-flex align-items-center">
											<div class="flex-shrink-0">
												<div class="avtar avtar-s bg-light-warning">
													<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
														<path d="M12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12Z"
																stroke="#4680FF" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" opacity="0.4" />
														<path d="M4 20C4 17 7 14.5 12 14.5C17 14.5 20 17 20 20" stroke="#4680FF" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                    				</svg>
												</div>
											</div>
											<div class="flex-grow-1 ms-3">
												<h6 class="mb-0">Product Subscribed</h6>
											</div>
											
										</div>
										<div class="bg-body p-2 mt-1 rounded-0">
											<div class="mt-3">
												<div class="kb-vol">
													
														<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="#ea222b" viewBox="0 0 16 16">
															<path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zm.104-14.996A1.5 1.5 0 0 1 9.5 2v.086a5.002 5.002 0 0 1 3.5 4.914v2.586l.854.853A.5.5 0 0 1 13.5 11h-11a.5.5 0 0 1-.354-.854L3 9.586V7a5.002 5.002 0 0 1 3.5-4.914V2a1.5 1.5 0 0 1 1.104-1.496z"/>
														</svg>
														
													

													<!-- <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="#ea222b" viewBox="0 0 16 16">
														<path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2zm.104-14.996A1.5 1.5 0 0 1 9.5 2v.086a5.002 5.002 0 0 1 3.5 4.914v2.586l.854.853A.5.5 0 0 1 13.5 11h-11a.5.5 0 0 1-.354-.854L3 9.586V7a5.002 5.002 0 0 1 3.5-4.914V2a1.5 1.5 0 0 1 1.104-1.496z"/>
													</svg> -->
													<h3 class="mb-1">${subscribeCount}</h3>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="card rounded-0 wallet-card">
						<div class="card-body p-0">
							<div class="w-card">
								<div class="w-card-inner">
									<h3>Wallet Balance</h3>
								
									<h5>+91 <b>${merchant.phoneOne}</b></h5>
								</div>
								<div class="wb-kyc">
									<div class="wb-bl">
										<fmt:formatNumber value="${wallet.balance / 100}" type="number" minFractionDigits="2" maxFractionDigits="2" var="formattedBalance" />
<h3 class="mb-1"><i class="fas fa-rupee-sign"></i> ${formattedBalance}</h3>
									</div>
									<!-- <div class="wb-kyc-inner">
										<img src="${url}/assets/images/wallet.png" alt="img" class="img-fluid" />
									</div> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

			
			<div class="row">
				<div class="col-md-5 rounded">
					<div class="card">
						<div class="card-header p-2">
							<h5 class="mb-0">Kyc Services</h5>
						</div>
						<div class="card-body"  style="background: #08508d">
							<!-- <div class=" flex-grow-1 pt-2">
								<h5 class="mb-0 mt-2" style="color: #62ff56"></h5>
							</div> -->

							<div id="kycCarousel" class="carousel slide mt-3 p-1" data-bs-ride="carousel" data-bs-interval="1400">
								<div class="carousel-inner kyc-slider">
									<!-- Slide 1 -->
									<div class="carousel-item active">
										<img src="${url}/assets/images/aadhar-1.jpg" class="d-block w-100" alt="Aadhar">
									</div>

									<!-- Slide 2 -->
									<div class="carousel-item">
										<img src="${url}/assets/images/pan-1.jpg" class="d-block w-100" alt="PAN">
									</div>

									<!-- Slide 3 -->
									<div class="carousel-item">
										<img src="${url}/assets/images/gst-1.jpg" class="d-block w-100" alt="account">
									</div>
								</div>


								<button class="carousel-control-prev" type="button" data-bs-target="#kycCarousel" data-bs-slide="prev">
									<span class="carousel-control-prev-icon" aria-hidden="true"></span>
									<span class="visually-hidden">Previous</span>
								</button>
								<button class="carousel-control-next" type="button" data-bs-target="#kycCarousel" data-bs-slide="next">
									<span class="carousel-control-next-icon" aria-hidden="true"></span>
									<span class="visually-hidden">Next</span>
								</button>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-7">
					<div class="card">
						<div class="card-header p-2">
							<h5 class="mb-0">Transaction Volume Graph</h5>
						</div>
						<div class="card-body">
							<div class="d-flex align-items-center mb-2">
								<div class="flex-grow-1">
									<h5 class="mb-0">Product Usage</h5>
								</div>
								<div class="flex-shrink-0 ms-3">
									<div class="dropdown">
										<a
											class="avtar avtar-s btn-link-secondary dropdown-toggle arrow-none"
											href="#" data-bs-toggle="dropdown" aria-haspopup="true"
											aria-expanded="false" id="productUsage"> <i
											class="ti ti-dots f-18"></i>
										</a>
										<div class="dropdown-menu dropdown-menu-end">
											<a class="dropdown-item" href="#">2024</a> <a
												class="dropdown-item active" href="#">2025</a>
											
											<a class="dropdown-item" href="#">2026</a>
										</div>
									</div>
								</div>
							</div>

							<div id="customer-rate-graph"></div>
						</div>
					</div>
				</div>
			</div>




			<!-- graph data -->

			

			<!-- graph data end -->



		</div>
	</div>




	<div class="modal fade bd-example-modal-lg" id="rechargeModal"
		tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel"
		aria-hidden="true" style="top: 152px;">
		<div class="modal-dialog modal-lg">
			<div class="modal-content  balance-add">
				<div class="modal-header "
					style="position: relative; background-color: #3ec9d6;">
					<h5 class="modal-title h4" id="myLargeModalLabel"
						style="position: absolute; left: 50%; transform: translateX(-50%); margin: 0; color: white;">
						Add Wallet Balance</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"
						style="position: absolute; right: 0rem; top: 94%; transform: translateY(-50%);">
					</button>
				</div>
				<div class="modal-body">
					<div class="custom-body mb-3">
						<div class="labelbbps">
							<label for="validationTooltip05"
								class="font-bold form-label text-green-400 mb-2"> Enter
								Amount to Add to Wallet</label>
						</div>
						<div class="input-group">
							<span class="input-group-text"><i
								class="fas fa-rupee-sign"></i></span> <input type="text"
								class="form-control" id="validationTooltip05">
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
						<input type="hidden" name="merchantId" id="merchantId"
							value="${merchant.mid}"> <input type="hidden"
							name="merchantName" id="merchantName" value="${merchant.name}">
						<input type="hidden" name="email" id="email"
							value="${merchant.email}"> <input type="hidden"
							name="mobileNo" id="mobileNo" value="${merchant.phoneOne}">
					</form>

					<div class="bbps-proceed mt-4 text-center">
						<div id="error-msg"
							style="color: red; font-weight: bold; display: none; margin-bottom: 10px;">Enter
							Valiad Amount</div>

						<button type="button" class="btn btn-shadow btn-info"
							id="proceedToPayBtn" style="color: white">Proceed to Pay</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:if>
<%@ include file="MerchantFooter.jsp"%>


<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
      
  
      function getKycData(filterType) {
	   
          fetch('${url}/app/public/kyc-data-count?filterType=' + filterType+'&merchantId='+'${merchant.mid}')
              .then(response => {
                  if (!response.ok) {
                      throw new Error('Network response was not ok');
                  }
                  return response.json();
              })
              .then(data => {
                  console.log("RESPONSE:", data);
                  if (data && typeof data.count !== "undefined") {
                      document.getElementById('txnVolume').innerHTML = data.count;
                  } else {
                      document.getElementById('txnVolume').innerHTML = '0';
                  }
              })
              .catch(error => {
                  console.error('Error fetching KYC data count:', error);
                  document.getElementById('txnVolume').innerHTML = 'Error';
              });
      }

      function loadKycMonthwiseChart(selectedYear) {
    	  fetch('${url}/app/public/kyc-data-monthwise?merchantId=${merchant.mid}&year=' + selectedYear)
    	    .then(response => response.json())
    	    .then(data => {
    	      const counts = data.map(item => item.count);
    	      //console.log('Fetched monthwise data:', counts);

    	      if (!kycChart) {
    	        renderKycChart(counts); // render for first time
    	      } else {
    	        kycChart.updateSeries([{ name: 'KYC Count', data: counts }]); // update on year change
    	      }
    	    })
    	    .catch(error => console.error('Error fetching KYC data:', error));
    	}

      document.addEventListener('DOMContentLoaded', function () {
    	    // Auto-call for today's data
    	    getKycData('today');

    	    // Auto-call for current year monthwise data
    	    const currentYear = new Date().getFullYear();
    	    loadKycMonthwiseChart(currentYear);

    	    // Set default selected year in dropdown
    	    const dropdownToggle = document.getElementById('productUsage');
    	    if (dropdownToggle) {
    	        dropdownToggle.setAttribute('data-selected-year', currentYear);
    	    }

    	    // Restrict the dropdown year selector to only the Product Usage section
    	    const productUsageDropdown = dropdownToggle?.closest('.dropdown');
    	    const productUsageItems = productUsageDropdown?.querySelectorAll('.dropdown-menu .dropdown-item');

    	    productUsageItems?.forEach(item => {
    	        item.addEventListener('click', function (e) {
    	            e.preventDefault();
    	            const selectedYear = this.textContent.trim();
    	            console.log('Selected year:', selectedYear);

    	            if (dropdownToggle) {
    	                dropdownToggle.setAttribute('data-selected-year', selectedYear);
    	            }

    	            loadKycMonthwiseChart(selectedYear);
    	        });
    	    });
    	});

      
      </script>



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
    /* if (amountValue === '' || isNaN(amountValue) || Number(amountValue) <= 0 ||  Number(amountValue) < 1000) {
    	 $('#error-msg').text('Amount must not be 0 and should be at least 1000.')
         .fadeIn()
         .delay(4000)
         .fadeOut();
return;
    } */
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


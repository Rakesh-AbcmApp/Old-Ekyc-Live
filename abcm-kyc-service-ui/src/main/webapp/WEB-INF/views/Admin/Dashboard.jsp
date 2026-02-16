<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<%@ include file="AdminHeader.jsp"%>
<meta charset="UTF-8">
<title>Insert title here</title>

</head>
<div class="pc-container">
	<div class="dh-350"></div>
	<div class="pc-content neg-h">

		<!-- [ breadcrumb ] start -->
		<div class="page-header">
			<div class="page-block px-1">
				<div class="row align-items-center">
					<div class="col-md-12">
						<div class="page-header-title">
							<h2 class="mb-2">Dashboard</h2>
						</div>
					</div>
					<div class="col-md-4 col-12">
						<label for="midSelect" class="form-label">Select Filter</label> <select
							class="form-select form-select w-100" id="dateRange">
							<option value="">All</option>
							<option value="today">today</option>
							<option value="weekly">weekly</option>
							<option value="monthly">monthly</option>
							<option value="lastmonth">Last 30 days</option>
						</select>
					</div>
					<div class="col-md-4 offset-md-4 col-12">
						<label for="midSelect" class="form-label">Select MID</label> <select
							class="form-select form-select w-100" id="merchantId" name="mid"
							onchange="handleMidChange()">
							<option value="">All</option>
							<c:forEach var="merchant" items="${merchantList}">
								<option value="${merchant.mid}">${merchant.mid}-${merchant.name}</option>
							</c:forEach>
						</select>
					</div>
				</div>
			</div>
		</div>
		<!-- [ breadcrumb ] end -->
		<!-- [ Main Content ] start -->
		<div class="m-boxes">
			<div class="row">
				<!-- transaction box -->
				<div class="col-md-4">
					<div class="card">
						<div class="card-body">
							<div class="d-flex align-items-center">
								<div class="flex-shrink-0">
									<div class="avtar avtar-s bg-light-primary">
										<svg width="24" height="24" viewBox="0 0 24 24" fill="none"
											xmlns="http://www.w3.org/2000/svg">
													<path opacity="0.4" d="M13 9H7" stroke="#4680FF"
												stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" />
													<path
												d="M22.0002 10.9702V13.0302C22.0002 13.5802 21.5602 14.0302 21.0002 14.0502H19.0402C17.9602 14.0502 16.9702 13.2602 16.8802 12.1802C16.8202 11.5502 17.0602 10.9602 17.4802 10.5502C17.8502 10.1702 18.3602 9.9502 18.9202 9.9502H21.0002C21.5602 9.9702 22.0002 10.4202 22.0002 10.9702Z"
												stroke="#4680FF" stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" />
													<path
												d="M17.48 10.55C17.06 10.96 16.82 11.55 16.88 12.18C16.97 13.26 17.96 14.05 19.04 14.05H21V15.5C21 18.5 19 20.5 16 20.5H7C4 20.5 2 18.5 2 15.5V8.5C2 5.78 3.64 3.88 6.19 3.56C6.45 3.52 6.72 3.5 7 3.5H16C16.26 3.5 16.51 3.50999 16.75 3.54999C19.33 3.84999 21 5.76 21 8.5V9.95001H18.92C18.36 9.95001 17.85 10.17 17.48 10.55Z"
												stroke="#4680FF" stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" />
												</svg>
									</div>
								</div>
								<div class="flex-grow-1 ms-3">
									<h6 class="mb-0">Transactions Volume</h6>
								</div>
							</div>

							<div class="bg-body d-flex align-items-center">
								<div class="">
									<div class="">
										<h3 class="m-0" id="txnVolume"></h3>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- transaction box -->

				<!-- product box -->
				<div class="col-md-4">
					<div class="card">
						<div class="card-body">
							<div class="d-flex align-items-center">
								<div class="flex-shrink-0">
									<div class="avtar avtar-s bg-light-warning">
										<svg width="24" height="24" viewBox="0 0 24 24" fill="none"
											xmlns="http://www.w3.org/2000/svg">
													<path
												d="M12 12C14.2091 12 16 10.2091 16 8C16 5.79086 14.2091 4 12 4C9.79086 4 8 5.79086 8 8C8 10.2091 9.79086 12 12 12Z"
												stroke="#4680FF" stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" opacity="0.4" />
													<path d="M4 20C4 17 7 14.5 12 14.5C17 14.5 20 17 20 20"
												stroke="#4680FF" stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" />
												</svg>
									</div>
								</div>
								<div class="flex-grow-1 ms-3">
									<h6 class="mb-0">Product</h6>
								</div>
							</div>
							<div class="bg-body d-flex align-items-center">
								<div class="">
									<div class="">
										<h3 class="mb-0" id="product-count"></h3>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- product box end-->

				<!-- wallet box -->
				<div class="col-md-4">
					<div class="card">
						<div class="card-body">
							<div class="d-flex align-items-center">
								<div class="flex-shrink-0">
									<div class="avtar avtar-s bg-light-primary">
										<svg width="24" height="24" viewBox="0 0 24 24" fill="none"
											xmlns="http://www.w3.org/2000/svg">
													<path opacity="0.4" d="M3 3H5L6 14H18L19 6H6"
												stroke="#4680FF" stroke-width="1.5" stroke-linecap="round"
												stroke-linejoin="round" />
													<circle cx="9" cy="20" r="1" stroke="#4680FF"
												stroke-width="1.5" />
													<circle cx="17" cy="20" r="1" stroke="#4680FF"
												stroke-width="1.5" />
												</svg>
									</div>
								</div>
								<div class="flex-grow-1 ms-3">
									<h6 class="mb-0">Wallet Balance</h6>
								</div>
							</div>

							<div class="bg-body d-flex align-items-center">
								<div class="">
									<div class="">
										<h3 class="mb-0" id="walletBalance">
											<!--i class="fas fa-rupee-sign"></i-->
										</h3>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- wallet box end-->
			</div>
		</div>

		<!-- mboxes end -->







		<div class="card table-card">
			<div
				class="card-header d-flex align-items-center justify-content-between py-3">
				<h5 class="mb-0">Wallet Recharge History</h5>
			</div>
			<div class="card-body">
				<div class="table-responsive">
					<table class="table table-hover auth-table table-bordered"
						id="pc-dt-simple">
						<thead>
							<tr>
								<th>Sr.No</th>
								<th>merchant Id</th>
								<th>Merchant Name</th>
								<th>Balance</th>


							</tr>
						</thead>
						<tbody id="walletReportbody">

						</tbody>
					</table>
				</div>
			</div>
		</div>








		<!-- carousel & graph -->

		<div class="xx-grp-car">
			<div class="row">
				<div class="col-md-5">
					<div class="card">
						<div class="card-header">
							<h5 class="">Our Services</h5>
						</div>
						<div class="card-body p-1">
							<div class="">
								<div id="kycCarousel" class="carousel slide"
									data-bs-ride="carousel" data-bs-interval="1400">
									<div class="carousel-inner kyc-slider">
										<!-- Slide 1 -->
										<div class="carousel-item active">
											<img src="${url}/assets/images/aadhar-1.jpg"
												class="d-block w-100" alt="Aadhar">
										</div>

										<!-- Slide 2 -->
										<div class="carousel-item">
											<img src="${url}/assets/images/pan-1.jpg"
												class="d-block w-100" alt="PAN">
										</div>

										<!-- Slide 3 -->
										<div class="carousel-item">
											<img src="${url}/assets/images/gst-1.jpg"
												class="d-block w-100" alt="account">
										</div>
									</div>

									<!-- Optional Controls -->
									<button class="carousel-control-prev" type="button"
										data-bs-target="#kycCarousel" data-bs-slide="prev">
										<span class="carousel-control-prev-icon" aria-hidden="true"></span>
										<span class="visually-hidden">Previous</span>
									</button>
									<button class="carousel-control-next" type="button"
										data-bs-target="#kycCarousel" data-bs-slide="next">
										<span class="carousel-control-next-icon" aria-hidden="true"></span>
										<span class="visually-hidden">Next</span>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- graph -->
				<div class="col-md-7">
					<div class="card">
						<div class="card-header xx-gcard-header">
							<h5 class="">Product Usage</h5>
							<div class="d-flex align-items-center justify-content-end">
								<div class="flex-shrink-0">
									<div class="dropdown">
										<div class="mt-0">
											<select
												class="form-select form-select w-100 p-1 rounded-0 ml-auto"
												id="selectYear">
												<option value="">Select Year</option>
												<option value="2025">2025</option>
												<option value="2026">2026</option>
												<option value="2027">2027</option>
												<option value="2028">2028</option>
											</select>
										</div>
									</div>
								</div>
							</div>
						</div>
						<div class="card-body p-1">

							<div id="customer-rate-graph"></div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- carousel & graph end -->





	</div>
</div>
<%@ include file="AdminFooter.jsp"%>

<script>
document.addEventListener('DOMContentLoaded', () => {
	  const midSelect   = document.getElementById('merchantId');
	  const selectYear  = document.getElementById('selectYear');
	  const dateRange   = document.getElementById('dateRange');
	  const defaultYear = new Date().getFullYear().toString();

	  // 1) Ensure the year dropdown has a default
	  if (selectYear && !selectYear.value) {
	    selectYear.value = defaultYear;
	  }

	  // 2) Helper that runs _all_ your data‐loading functions
	  function refreshDashboardFor(rawMid) {
	    const mid  = rawMid.trim();         // get the string, trim whitespace
	    const yr   = selectYear.value || defaultYear;

	    // KYC chart (monthwise)
	    loadKycMonthwiseChart(mid, yr);

	    // KYC count (filterType + merchant)
	    getKycData(dateRange, { value: mid });

	    // Wallet balance
	    fetchWalletBalance({ value: mid });

	    // Product count
	    fetchProductCount({ value: mid });

	    // Wallet report table
	    WalletDashboardReport(mid);
	  }

	  // 3) Initial load on page render (blank mid => “all”)
	  refreshDashboardFor(midSelect.value);

	  // 4) Re-run everything whenever the MID changes
	  midSelect.addEventListener('change', () => {
	    refreshDashboardFor(midSelect.value);
	  });
	});


function loadKycMonthwiseChart(mid,selectedYear) {
    var apiUrl = '${url}'+'/app/public/kyc-data-monthwise?merchantId='+mid+'&year='+selectedYear;
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.json();
        })
        .then(data => {
            const counts = data.map(item => item.count);
           // console.log('Fetched monthwise data:', counts);

            if (!kycChart) {
                renderKycChart(counts); // First time render
            } else {
                kycChart.updateSeries([{ name: 'Txn Count', data: counts }]); // Update chart
            }
        })
        .catch(error => console.error('Error fetching KYC data:', error));
}


      
      
function getKycData(filterTypeElement, merchantIdElement) {
   // alert("Call getKycData Functions");
    // Get the actual values from the select/input elements
    const filterTypeValue = filterTypeElement.value;
    const merchantIdValue = merchantIdElement.value;
    const finalUrl = '${url}' + '/app/public/kyc-data-count?filterType=' + encodeURIComponent(filterTypeValue) + '&merchantId=' + encodeURIComponent(merchantIdValue);
   // alert("Fetching URL: " + finalUrl);
    fetch(finalUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log("getKycData Txn Valum:", data);
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



      
      function fetchWalletBalance(merchantId) {
    	  const merchantIdValue = merchantId.value;

    	  fetch('${url}/app/public/wallet-balance-count?merchantId=' + encodeURIComponent(merchantIdValue))
    	    .then(response => {
    	      if (!response.ok) {
    	        throw new Error("Network response was not ok");
    	      }
    	      return response.json(); 
    	    })
    	    .then(data => {
    	      console.log("Wallet Balance Amount:", data);
    	      // Example: display response data
    	      document.getElementById("walletBalance").innerHTML = '<i class="fas fa-rupee-sign"></i>' + (data.data / 100).toFixed(2);
    	      document.getElementById("walletBal").innerHTML = '<i class="fas fa-rupee-sign"></i>' + (data.data / 100).toFixed(2);

    	      

    	    })
    	    .catch(error => {
    	      console.error("Fetch error:", error);
    	    });
    	}
      
      
      
      
      
      function fetchProductCount(merchantId) {
    	  const merchantIdValue = merchantId.value;

    	  fetch('${url}/app/public/product-subscribe-count?merchantId=' + encodeURIComponent(merchantIdValue))
    	    .then(response => {
    	      if (!response.ok) {
    	        throw new Error("Network response was not ok");
    	      }
    	      return response.json(); 
    	    })
    	    .then(data => {
    	      //console.log("product-count:", data);
    	      // Example: display response data
    	      document.getElementById("product-count").innerText = data.data;

    	    })
    	    .catch(error => {
    	      console.error("Fetch error:", error);
    	    });
    	}
      
      
      
      
    

      
      </script>


<script>
function WalletDashboardReport(merchantId) {
    ajaxindicatorstart('💼 Fetching Data... Please wait.');

    var endpoint = '${url}' + '/app/public/wallet-dashboard-report';
    if (merchantId) {
        endpoint += '?merchant_id=' + encodeURIComponent(merchantId);
    }

    $.ajax({
        url: endpoint,
        type: 'GET',
        success: function(response) {
            console.log('Success Wallet Dashboard: ' + JSON.stringify(response));

            var html = "";
            var totalBalance = 0;

            if (response.responseCode === 200 &&
                Array.isArray(response.data) &&
                response.data.length > 0) 
            {
                // build each row and accumulate
                for (var i = 0; i < response.data.length; i++) {
                    var item = response.data[i];
                    totalBalance += item.balance;
                    html += 
                      "<tr>" +
                        "<td>" + (i + 1)                         + "</td>" +
                        "<td>" + item.merchantId                + "</td>" +
                        "<td>" + item.merchantName              + "</td>" +
                        "<td>" + (item.balance / 100).toFixed(2)+ "</td>" +
                      "</tr>";
                }

                // now append the Totals row (spanning first 3 columns)
                html += 
                  "<tr style='font-weight:bold;'>" +
                    "<td colspan='3' style='text-align:center;'>Total Balance</td>" +
                    "<td>" + (totalBalance / 100).toFixed(2) + "</td>" +
                  "</tr>";
            } else {
                html = 
                  "<tr>" +
                    "<td colspan='4' style='text-align:center; color:red;'>" +
                      "No data found" +
                    "</td>" +
                  "</tr>";
            }

            $('#walletReportbody').html(html);
        },
        error: function(xhr, status, error) {
            console.error('Error fetching wallet report:', error);
        },
        complete: ajaxindicatorstop
    });
}


</script>



<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="AdminHeader.jsp"%>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
</head>
<body>
	<div class="pc-container">
		<div class="pc-content">

			<div class="card">
				<div class="card-header p-2">
					<h5 class="mb-0">Wallet Recharge Report & PG Transaction
						Report</h5>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-12">
							<div class="card">
								<div class="card-body p-0">

									<div class="kyc-products kyc-reports"
										style="flex-direction: column;">
										<ul class="nav nav-pills w-100" id="pills-tab" role="tablist">
											<li class="nav-item w-50 text-center"><a
												class="nav-link active" id="pills-home-tab"
												data-bs-toggle="pill" href="#pills-home" role="tab"
												aria-controls="pills-home" aria-selected="true"><i
													class="fas fa-chart-pie"></i>Wallet Recharge History</a></li>
											<li class="nav-item w-50 text-center"><a
												class="nav-link m-0" id="pills-profile-tab"
												data-bs-toggle="pill" href="#pills-profile" role="tab"
												aria-controls="pills-profile" aria-selected="false"><i
													class="fas fa-tv"></i> PG Transaction Report</a></li>

										</ul>
										<div class="row mt-2 p-2">
											<div class="col-md-4 col-12">
												<label for="pc-date_range_picker-2" class="form-label">Select
													Date</label>
												<div class="input-group">
													<span class="input-group-text"><i
														class="feather icon-calendar"></i></span> <input type="text"
														id="pc-date_range_picker-2" class="form-control"
														placeholder="Select date range" name="dateRange" />
												</div>
											</div>
											<div class="col-md-4 col-12">
												<div class="all-products">

													<label for="pc-date_range_picker-2" class="form-label">Select
														Report</label> <select class="form-select form-select w-100"
														id="midSelect" name="mid">
														<option value="all">All</option>
														<c:forEach var="merchant" items="${merchantList}">
															<option value="${merchant.mid}">${merchant.mid}-${merchant.name}</option>
														</c:forEach>
													</select>

												</div>
											</div>
											<div class="col-md-4 col-12">
												<div class="text-center">
													<button type="submit"
														class="btn btn-shadow rounded-0 btn-info btn-md w-50"
														style="margin-top: 1.75rem; padding: 7px 0;"
														onclick="submitReportBasedOnActiveTab()">Submit</button>
												</div>
											</div>
										</div>
									</div>

									<div class="tab-content m-0 p-2" id="pills-tabContent">
										<!-- tab-1 -->
										<div class="tab-pane fade show active" id="pills-home"
											role="tabpanel" aria-labelledby="pills-home-tab">
											<div class="row">
												<div class="card-body p-2">
													<div class="table-responsive">
														<table class="table table-hover" id="pc-dt-simple">
															<thead>
																<tr>
																	<th>Sr.No</th>
																	<th>merchant Id</th>
																	<th>Merchant Name</th>
																	<th>Balance</th>
																	<th>Txn Date</th>
																	<th>Alert balance</th>

																</tr>
															</thead>
															<tbody id="walletReportbody">


															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
										<!-- tab-1 end-->

										<!-- tab-2 -->
										<div class="tab-pane fade" id="pills-profile" role="tabpanel"
											aria-labelledby="pills-profile-tab">
											<div class="row">
												<div class="card-body p-2">
													<div class="table-responsive">
														<table class="table table-hover" id="pc-dt-simple">
															<thead>
																<tr>
																	<th>Sr.No</th>
																	<th>Merchant Id</th>
																	<th>Merchant Name</th>
																	<th>Txn Id</th>
																	<th>Txn Amount</th>
																	<th>Txn Date</th>
																	<th>Txn Mode</th>
																	<th>Txn Status</th>
																</tr>
															</thead>
															<tbody id="TxnReportbody">


															</tbody>
														</table>
													</div>
												</div>
											</div>
										</div>
										<!-- tab-2  end -->
									</div>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	<%@ include file="AdminFooter.jsp"%>
	<script>
function submitReportBasedOnActiveTab() {
  const activeTab = $('.nav-link.active').attr('id');
  if (activeTab === 'pills-home-tab') {
    walletReport();
  } else if (activeTab === 'pills-profile-tab') {
    
    transactionReport();
  }
}
// Dummy functions for illustration
function walletReport() {
	  let selectemid = $('#midSelect').val();
	 let dateRange = $("#pc-date_range_picker-2").val();
     let [formDate, toDate] = dateRange.split(' to ').map(date => date.trim());
//  alert("Calling Wallet Report AJAX");
  $.ajax({
	    url: '${url}'+'/app/public/wallet-report',
	    type: 'POST',
	    contentType: 'application/json',
	    data: JSON.stringify({
	        "merchantId": selectemid,
	        "fromDate": formDate,
	        "toDate": toDate
	    }),
	    beforeSend: function () {
	    	ajaxindicatorstart('💼 Fetching wallet balance... Please wait.');
		    },
	    success: function(response) {
	        console.log('Success:', JSON.stringify(response));
	        let html = "";

	        if (response.responseCode === 200 && Array.isArray(response.data) && response.data.length > 0) {
	            response.data.forEach(function (item, index) {
	                html += "<tr>" +
	                    "<td>" + (index + 1) + "</td>" +
	                    "<td>" + item.merchantId + "</td>" +
	                    "<td>" + item.merchantName + "</td>" +
	                    "<td>" + (item.balance / 100).toFixed(2) + "</td>" +
	                    "<td>" + formatDate(item.txnDate) + "</td>" +
	                    "<td>" + (item.alertBalance/100).toFixed(2) + "</td>" +
	                    "</tr>";
	            });
	        } else {
	            html = "<tr><td colspan='5' style='text-align:center; color:red;'>No data found</td></tr>";
	        }

	        $('#walletReportbody').html(html);
	        ajaxindicatorstop(); // Hide loader after success
	    },
	    error: function(xhr, status, error) {
	        console.error('Error:', error);
	        ajaxindicatorstop(); // Hide loader after success
	        
	    }
	});

}

function transactionReport() {
	let selectemid = $('#midSelect').val();
	//alert(selectemid);
	 let dateRange = $("#pc-date_range_picker-2").val();
    let [formDate, toDate] = dateRange.split(' to ').map(date => date.trim());
// alert("Calling Wallet Report AJAX");
 $.ajax({
	    url: '${url}'+'/app/public/txn-report',
	    type: 'POST',
	    contentType: 'application/json',
	    data: JSON.stringify({
	        "merchantId": selectemid,
	        "fromDate": formDate,
	        "toDate": toDate
	    }),
	    beforeSend: function () {
	    	ajaxindicatorstart('💼 Fetching your transaction report... Please wait.');
		    },
	    success: function(response) {
	        console.log('Success:', response);
	        let html = "";

	        if (response.responseCode === 200 && Array.isArray(response.data) && response.data.length > 0) {
	        	response.data.forEach(function (item, index) {
	        	    html += "<tr>" +
	        	        "<td>" + (index + 1) + "</td>" +
	        	        "<td>" + item.merchantId + "</td>" +
	        	        "<td>"+item.merchantName+" </td>" +
	        	        "<td>" + (item.txnId !== null && item.txnId !== '' ? item.txnId : item.orderId) + "</td>" +
	        	        "<td>" + (item.txnAmount / 100).toFixed(2) + "</td>" +
	        	        "<td>" + formatDate(item.txnDate) + "</td>" +
	        	        "<td>" + item.txnMode + "</td>" +
	        	        "<td style='color:green;'>" + item.status + "</td>" +
	        	        "</tr>";
	        	});
	        } else {
	            html = "<tr><td colspan='7' style='text-align:center; color:red;'>No data found</td></tr>";
	        }

	        $('#TxnReportbody').html(html);
	        ajaxindicatorstop(); // Hide loader after success
	    },
	    error: function(xhr, status, error) {
	        console.error('Error:', error);
	        ajaxindicatorstop(); // Hide loader after success
	        
	    }
	});
}

function formatDate(dateStr) {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) return '';

    const pad = function (n) {
        return n < 10 ? '0' + n : n;
    };
    return pad(date.getDate()) + "-" +
           pad(date.getMonth() + 1) + "-" +
           date.getFullYear() + " " +
           pad(date.getHours()) + ":" +
           pad(date.getMinutes()) + ":" +
           pad(date.getSeconds());
}
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<%@ include file="AdminHeader.jsp"%>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0"> <!-- Important for mobile responsiveness -->
<title>Add Balance Page</title>
<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
<style>
	.add-blnc-kyc {

	}
	/* #addBalance {
		max-width: 440px;
    	margin: auto;
	} */
</style>
</head>

<div class="pc-container mt-4">
	<div class="pc-content">
		<div class="card mt-3">
			<div class="add-blnc-kyc">
				<div class="card-header p-2 rounded-0" style="background: #08508d;">
					<h5 class="mb-0 text-white">Add Wallet Balance</h5>
				</div>
				<div class="card-body">
					<div id="passwordUpdateMessageContainer" class="text-center mb-2">
						<span id="passwordUpdateMessage"></span>
					</div>

					<form id="addBalance">
						<div class="row">
							<div class="col-md-4 col-12">
								<div class="md-4">
									<label for="mid" class="form-label">Select Merchant</label> 
									<select id="mid" class="form-control">
										<option value="">select mid</option>
										<c:forEach var="merchant" items="${midList}">
											<option value="${merchant.mid}">${merchant.mid}-${merchant.name}</option>
										</c:forEach>

									</select>
								</div>
							</div>
							<div class="col-md-4 col-12">
								<div class="md-4">
									<label for="amount" class="form-label">Wallet Balance</label> 
									<input type="text" class="form-control" id="amount" placeholder="Enter Wallet Amount">
								</div>
							</div>
							
							<div class="col-md-4 col-12">
								<div class="md-4">
									<label for="amount" class="form-label">Payment Mode</label> 
									<input type="text" class="form-control" id="mode" placeholder="Enter Payment Mode">
								</div>
							</div>
							<div class="col-12">
								<div class="d-flex justify-content-center mt-3">
									<button type="button" class="btn btn-primary rounded-0 mt-3 shadow-sm" onclick="addBalance()">
										<i class="fa fa-plus"></i> Add Balance
									</button>
								</div>
							</div>
						</div>
					</form>
					
					
				</div>
			</div>
		</div>
	</div>
</div>

<%@ include file="AdminFooter.jsp"%>
<script>
function addBalance() { 
    var mid = $("#mid").val();
    var amount = $("#amount").val();
    var paymentMode=$("#mode").val();
    if (!mid) {
        showMessage('<span class="text-danger fw-bold">Please select a merchant.</span>');
        return;
    }
    if (!amount) {
        showMessage('<span class="text-danger fw-bold">Please enter amount.</span>');
        return;
    }
    var requestData = {
        mid: mid,
        walletAmount: amount,
        paymentMode: paymentMode,
        username: '${username}',
    };
    
    $.ajax({
        url: '${url}' + '/app/public/add-merchant-balance',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        success: function (response) {
        	console.log("Balance added Response"+JSON.stringify(response))
            if (response.responseCode === 200) {
                showMessage('<span class="text-success fw-bold">' + response.message + '</span>');
                $("#mid").val('');
                $("#amount").val('');
                $("#mode").val('');
            } else {	
                showMessage('<span class="text-danger fw-bold">' + response.message + '</span>');
            }
        },
        error: function () {
            showMessage('<span class="text-danger fw-bold">Error while adding balance. Please try again.</span>');
        }
    });
}

function showMessage(message) {
    $("#passwordUpdateMessage").html(message).fadeIn();

    setTimeout(function () {
        $("#passwordUpdateMessage").fadeOut();
    }, 5000);
}
</script>

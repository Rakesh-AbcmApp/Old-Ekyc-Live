<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<%@ include file="AdminHeader.jsp"%>
<meta charset="UTF-8">
<title>eKYC | OnboardReport</title>
<style type="text/css">
.cursor-pointer {
	cursor: pointer;
}

.position-relative i.bi {
	height: 1em !important;
	line-height: 3 !important;
}

#merchantTableBody tr td  button {
	padding: 2px 6px !important;
	font-size: 11px !important;
	border-radius: 10px !important;
}

#merchantTableBody tr td {
	font-size: 13px !important;
}

#ClientDataTableBody tr td  button {
	padding: 2px 6px !important;
	font-size: 11px !important;
	border-radius: 10px !important;
}

#ClientDataTableBody tr td {
	font-size: 13px !important;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>


</head>
<!-- [ Main Content ] start -->
<div class="pc-container">
	<div class="pc-content">
		<!-- [ breadcrumb ] start -->
		<div class="page-header">
			<div class="page-block">
				<div class="row align-items-center">
					<!-- <div class="col-md-12">
                        <ul class="breadcrumb">
                            <li class="breadcrumb-item"><a href="../dashboard/index.html">Dashboard</a></li>
                            <li class="breadcrumb-item"><a href="javascript: void(0)"></a></li>
                            <li class="breadcrumb-item" aria-current="page">Finance</li>
                        </ul>
                        </div> -->
					<div class="col-md-12">
						<div class="page-header-title">
							<h2 class="mb-0">Reports</h2>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- [ breadcrumb ] end -->

		<!-- [ Main Content ] start -->
		<div class="row">
			<div class="col-12">
				<div class="card">
					<div class="card-body">

						<div class="kyc-products kyc-reports">
							<ul class="nav nav-pills w-100" id="pills-tab" role="tablist">
								<li class="nav-item w-50 text-center"><a
									class="nav-link active" id="pills-home-tab"
									data-bs-toggle="pill" href="#pills-home" role="tab"
									aria-controls="pills-home" aria-selected="true"><i
										class="fas fa-chart-pie"></i>Provider Report</a></li>
								<li class="nav-item w-50 text-center"><a class="nav-link"
									id="pills-profile-tab" data-bs-toggle="pill"
									href="#pills-profile" role="tab" aria-controls="pills-profile"
									aria-selected="false"><i class="fas fa-tv"></i> Merchant
										Report</a></li>
								
							</ul>
							
						</div>




						<div class="tab-content" id="pills-tabContent">
							<!-- tab-1 -->
							<div class="tab-pane fade show active" id="pills-home"
								role="tabpanel" aria-labelledby="pills-home-tab">
								<div class="row">
									<div class="card-body">
										<div class="table-responsive">
											<table class="table table-hover" id="pc-dt-simple">
												<thead>
													<tr>
														<th>Sr.No</th>
														<th>Provider Name</th>
														<th>Api Key</th>
														<th>App Id</th>
														<th>Aaddhar OKYC OTP Request</th>
														<th>Aaddhar OKYC OTP Submit</th>
														<th>PAN URL</th>
														<th>GST URL</th>
														<th>DRIVING LICENSE URL</th>
														<th> VOTER-ID  URL</th>
														<th>ENVIROMENT</th>
														<th>Created On</th>
														<th>Status</th>
														<th>Action</th>
													</tr>
												</thead>
												<tbody id="ClientDataTableBody">
													<!-- <tr>
                                                        <td>
                                                          <div class="d-flex align-items-center">
                                                            <div class="flex-shrink-0">
                                                              <img src="assets/images/user/avatar-1.jpg" alt="user image" class="img-radius wid-40" />
                                                            </div>
                                                            <div class="flex-grow-1 ms-3">
                                                              <h6 class="mb-0">Airi Satou</h6>
                                                            </div>
                                                          </div>
                                                        </td>
                                                        
                                                        <td>2023/02/07 span class="text-muted text-sm d-block">09:05 PM</span</td>
                                                        <td>$950.54</td>
                                                        <td><span class="badge text-bg-success">Completed</span></td>
                                                        
                                                      </tr>
                                                      <tr>
                                                        <td>
                                                          <div class="d-flex align-items-center">
                                                            <div class="flex-shrink-0">
                                                              <img src="assets/images/user/avatar-2.jpg" alt="user image" class="img-radius wid-40" />
                                                            </div>
                                                            <div class="flex-grow-1 ms-3">
                                                              <h6 class="mb-0">Ashton Cox</h6>
                                                            </div>
                                                          </div>
                                                        </td>
                                                       
                                                        <td>2023/02/01 </td>
                                                        <td>$520.30</td>
                                                        <td><span class="badge text-bg-success">Completed</span></td>
                                                       
                                                      </tr> -->

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
									<div class="card-body">
										<div class="table-responsive">
											<table class="table table-hover" id="pc-dt-simple">
												<thead>
													<tr>
														<th>Sr.No</th>
														<th>Merchant Id</th>
														<th>Merchant Name</th>
														<th>UserName</th>
														<th>Phone</th>
														<th>Email</th>
														<th>created At</th>
														<th>Api Key</th>
														<th>App Id</th>
														<th>OKYC</th>
														<th>PAN</th>
														<th>GST</th>

														<th>Status</th>
														<th>Action
														<th>
													</tr>
												</thead>
												<tbody id="merchantTableBody">
													

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



		<!-- [ Main Content ] end -->
	</div>




</div>

<div class="modal" id="changePasswordModalAdmin" 
	>
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="background: #08508d;">
				<h5 class="modal-title text-white"
					>Merchant Update Password</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>

			<div class="modal-body">
				<div id="passwordUpdateMessageContainer" class="text-center mb-2">
					<span id="passwordUpdateMessage"></span>
				</div>
				<form id="changePasswordForm">
					<div class="mb-3">
						<label for="mid" class="form-label">Merchant Id</label> <input
							type="text" class="form-control" id="mid" readonly>
					</div>

					<div class="mb-3 position-relative">
						<label for="newPassword" class="form-label">New Password</label> <input
							type="password" class="form-control pe-5" id=newPassword_admin
							placeholder="Enter a new password" required> <i
							class="bi bi-eye position-absolute end-0 top-50 translate-middle-y me-3 cursor-pointer"
							onclick="togglePassword('newPassword_admin', this)"></i>
					</div>

					<div class="mb-3 position-relative">
						<label for="confirmPassword" class="form-label">Confirm
							New Password</label> <input type="password" class="form-control pe-5"
							id="confirmPassword_admin" placeholder="Confirm your new password"
							required> <i
							class="bi bi-eye position-absolute end-0 top-50 translate-middle-y me-3 cursor-pointer"
							onclick="togglePassword('confirmPassword_admin', this)"></i>
					</div>
				</form>
			</div>

			<div class="modal-footer flex-column">
				<div>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary" 
						onclick="changePassword()">Update Password</button>
				</div>
			</div>
		</div>
	</div>
</div>






<div class="modal fade" id="updatebalance" tabindex="-1"
	aria-labelledby="balanceUpdateMessage" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header" style="background: #08508d;">
				<h5 class="modal-title text-white" id="changePasswordModalLabel"
					style="color: #ffffff;">Update Wallate Balance</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<div id="passwordUpdateMessageContainer" class="text-center mb-2">
					<span id="updateBalancemsg"></span>
				</div>
				<form id="addBalance">
					<div class="mb-3">
						<label for="currentPassword" class="form-label">Merchant
							Id</label> <input type="text" class="form-control" id="merchantId"
							readonly="readonly">
					</div>
					<div class="mb-3">
						<label for="currentPassword" class="form-label">Merchant
							Name</label> <input type="text" class="form-control" id="name"
							readonly="readonly">
					</div>
					<div class="mb-3">
						<label for="currentPassword" class="form-label"> Previous
							Wallet Balance</label> <input type="text" class="form-control"
							id="existingBalance" readonly="readonly">
					</div>

					<div class="mb-3">
						<label for="currentPassword" class="form-label">New Wallet
							Balance</label> <input type="text" class="form-control" id="newbalance">
					</div>

					<div class="mb-3">
						<label for="currentPassword" class="form-label">Payment
							Mode</label> <input type="text" class="form-control" id="mode">
					</div>
				</form>
			</div>
			<div class="modal-footer flex-column">

				<div>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">Cancel</button>
					<button type="button" class="btn btn-primary" id="saveBalanceBtn"
						onclick="addBalance()">
						<i class="fa fa-refresh"></i> Update Balance
					</button>
				</div>
			</div>
		</div>
	</div>
</div>


<script>
         $(document).ready(function() {
        	    $.ajax({
        	        url: '${url}'+'/app/public/clientReport',
        	        type: 'GET',
        	        success: function(response) {
        	            //console.log("Client Report: " + JSON.stringify(response));
        	            var html = "";

        	            if (response.responseCode == 200) {
        	                var Data = response.data;
        	                //console.log("Data-----------------------", Data);

        	                Data.forEach(function(item, index) {
        	                	 var Status = item.status
     	                        ? "<span class='badge text-bg-success'>Active</span>"
     	                        : "<span class='badge text-bg-danger'>InActive</span>";
        	                     html += "<tr>" +
        	                                "<td>" + (index + 1) + "</td>" +
        	                                "<td>" + item.serviceProviderName + "</td>" +
        	                                "<td>" +item.apiKey+ "</td>" +
        	                                "<td>" +  item.appId+ "</td>" +
        	                                "<td>" +  item.apiAadharUrl1+ "</td>" +
        	                                "<td>" +  item.apiAadharUrl2+ "</td>" +
        	                                "<td>" +  item.apiGstUrl1+ "</td>" +
        	                                "<td>" +  item.apiPanUrl1+ "</td>" +
        	                                "<td>" +  item.drivingLsUrl+ "</td>" +
        	                                "<td>" +  item.voterIdUrl+ "</td>" +
        	                                "<td>" +  item.environment+ "</td>" +
        	                                "<td>" + item.createdOn + "</td>" +
        	                                "<td>" + Status + "</td>" +
                	                        "<td><button type='submit' class='btn btn-shadow btn-info mt-2' onclick=\"update('" + item.id + "','c')\">Update</button></td>"+   
        	                            "</tr>"; 
        	                });

        	                // Append to tbody
        	                $("#ClientDataTableBody").html(html);
        	            }
        	        },
        	        error: function(xhr, status, error) {
        	            console.log('Error: ' + error);
        	        }
        	    });
        	    
        	    /*All Merchnat Data*/
        	    $.ajax({
        	        url: '${url}'+'/app/public/allMerchnat',
        	        type: 'GET',
        	        success: function(response) {
        	           // console.log("Merchant Report: " + JSON.stringify(response));
        	            var html = "";
        	            if (response.responseCode == 200) {
        	                var Data = response.data;
        	              // console.log("Data-----------------------", Data);

        	                Data.forEach(function(item, index) {
        	                    // Determine display values based on truthy/falsy logic
        	                var aadharStatus = item.aadharOkyc === "ENABLE"
    ? "<span class='badge text-bg-success'>Active</span>"
    : "<span class='badge text-bg-danger'>InActive</span>";
var panStatus = item.panPro === "ENABLE"
    ? "<span class='badge text-bg-success'>Active</span>"
    : "<span class='badge text-bg-danger'>InActive</span>";

var gstStatus = item.gstLit === "ENABLE"
    ? "<span class='badge text-bg-success'>Active</span>"
    : "<span class='badge text-bg-danger'>InActive</span>";
        	                        var Status = item.status
        	                        ? "<span class='badge text-bg-success'>Active</span>"
        	                        : "<span class='badge text-bg-danger'>InActive</span>";
        	                    html += "<tr>" +
        	                        "<td>" + (index + 1) + "</td>" +
        	                        "<td>" + item.mid + "</td>" +
        	                        "<td>" + item.name + "</td>" +
        	                        "<td>" + item.credentials.username + "</td>" +
        	                        "<td>" + item.phoneOne + "</td>" +
        	                        "<td>" + item.email + "</td>" +
        	                        "<td>" + formatDateTime(item.credentials.createdAt) + "</td>" +
        	                        "<td>" + item.apiKey + "</td>" +
        	                        "<td>" + item.appId + "</td>" +
        	                        "<td>" + aadharStatus + "</td>" +
        	                        "<td>" + panStatus + "</td>" +
        	                        "<td>" + gstStatus + "</td>" +
        	                        "<td>"+Status+"</td>" +
        	                        "<td>" +
        	                        "<button type='button' class='btn btn-shadow btn-info mt-2' onclick=\"update('" + item.id + "', 'm')\">Update</button> | " +
        	                        "<button type='button' class='btn btn-shadow btn-warning mt-2' onclick=\"updatePassword('" + item.mid + "')\">Password Update</button> | " +
        	                        "<button type='button' class='btn btn-shadow btn-success mt-2' onclick=\"UpdateBalance('" + item.mid + "', '" + item.name.replace(/'/g, "\\'") + "')\">Update Balance</button>" +        	                        "</td>" +
        	                        "</tr>";
        	                });
        	                // Append to tbody
        	                $("#merchantTableBody").html(html);
        	            }
        	        },
        	        error: function(xhr, status, error) {
        	            console.log('Error: ' + error);
        	        }
        	    });
        	    
        	});
         
           function update(id, type) {
        	    //alert(type);
        	    var baseUrl = '${url}';
        	    var url = type === 'c' 
        	        ? baseUrl + "/app/admin/updateClientMaster/" + id 
        	        : baseUrl + "/app/admin/updateMerchant/" + id;
        	    window.location.href = url;
        	}
           
           function formatDateTime(dateTimeString) {
        	    const date = new Date(dateTimeString);
        	    const pad = num => String(num).padStart(2, '0');

        	    const day = pad(date.getDate());
        	    const month = pad(date.getMonth() + 1); // Months are 0-based
        	    const year = date.getFullYear();

        	    const hours = pad(date.getHours());
        	    const minutes = pad(date.getMinutes());
        	    const seconds = pad(date.getSeconds());

        	    // Concatenate manually without template literals
        	    return day + '-' + month + '-' + year + ' ' + hours + ':' + minutes + ':' + seconds;
        	}

        </script>
        
        
        
        
<script >
function updatePassword(mid) {
	  // set MID
	  document.getElementById('mid').value = mid;

	  // clear only password fields
	  document.getElementById('newPassword_admin').value = '';
	  document.getElementById('confirmPassword_admin').value = '';

	  // show modal
	  const modal = bootstrap.Modal.getOrCreateInstance(
	    document.getElementById('changePasswordModalAdmin'),
	    { backdrop: 'static', keyboard: false }
	  );
	  modal.show();
	}






function changePassword() {
	  const modalEl = document.getElementById('changePasswordModalAdmin');
	  const $modal  = $('#changePasswordModalAdmin');

	  // ---- helper for all messages (same UI everywhere) ----
	  function showPwMsg(isSuccess, text) {
	    const $msg = $modal.find('#passwordUpdateMessage');
	    const icon = isSuccess
	      ? '<i class="bi bi-check-circle-fill me-1"></i> '
	      : '<i class="bi bi-exclamation-circle-fill me-1"></i> ';
	    $msg
	      .stop(true, true)
	      .removeClass('text-success text-danger fw-bold')
	      .addClass((isSuccess ? 'text-success' : 'text-danger') + ' fw-bold')
	      .hide()
	      .html(icon + text)
	      .fadeIn(200)
	      .delay(4000)
	      .fadeOut(200);
	  }

	  // ---- read fields once ----
	  const mid              = (document.getElementById('mid')?.value || '').trim();
	  const newPassword      = (document.getElementById('newPassword_admin')?.value || '').trim();
	  const confirmPassword  = (document.getElementById('confirmPassword_admin')?.value || '').trim();

	  // ---- simple validations ----
	  if (!newPassword || !confirmPassword) {
	    showPwMsg(false, 'Please fill in password & new password password fields.');
	    return;
	  }
	  if (newPassword !== confirmPassword) {
	    showPwMsg(false, 'Passwords do not match.');
	    return;
	  }

	  // ---- build request payload (keep your current field names) ----
	  const requestBody = {
	    mid: mid,
	    newPassword: newPassword,
	    confirmNewPassword: confirmPassword  // NOTE: if your backend expects "confirmPassword", rename here
	  };

	  ajaxindicatorstart('🔐 Updating password... Please wait');
	  $.ajax({
	    url: '${url}' + '/app/public/change-password',
	    method: 'POST',
	    contentType: 'application/json',
	    dataType: 'json',
	    data: JSON.stringify(requestBody),

	    success: function (data) {
	      ajaxindicatorstop();
	      if (data && data.responseCode === 200) {
	        // show success, then hide & reset after the fade finishes
	        const message = data.message || 'Password updated successfully!';
	        const $msg = $modal.find('#passwordUpdateMessage');

	        $msg
	          .stop(true, true)
	          .removeClass('text-danger fw-bold')
	          .addClass('text-success fw-bold')
	          .hide()
	          .html('<i class="bi bi-check-circle-fill me-1"></i> ' + message)
	          .fadeIn(200)
	          .delay(4000)
	          .fadeOut(200, function () {
	            document.activeElement && document.activeElement.blur(); // avoid aria-hidden focus warning
	            bootstrap.Modal.getOrCreateInstance(modalEl).hide();
	            $('#changePasswordForm')[0].reset();
	          });

	      } else {
	        showPwMsg(false, (data && data.message) || 'Error changing password.');
	      }
	    },

	    error: function () {
	      ajaxindicatorstop();
	      showPwMsg(false, 'An error occurred while changing password.');
	    }
	  });
	}



</script>

<script>
function togglePassword(fieldId, btn) {
  const input = document.getElementById(fieldId);
  const icon = btn.querySelector('i');
  if (input.type === "password") {
    input.type = "text";
    icon.classList.remove("bi-eye");
    icon.classList.add("bi-eye-slash");
  } else {
    input.type = "password";
    icon.classList.remove("bi-eye-slash");
    icon.classList.add("bi-eye");
  }
}
</script>



<script>
function UpdateBalance(mid, name) {
    console.log("Fetching existing balance for MID:", mid);
    $.ajax({
        url: '${url}'+'/app/public/merchant-existing-balance',
        type: "GET",
        data: { merchantId: mid },
        success: function(response) {
            if (response.responseCode === 200) {
                console.log("Balance fetched:", response.data);
                $('#merchantId').val(mid);
                $('#name').val(name);
                $('#existingBalance').val((response.data / 100).toFixed(2));
                // Show modal
                const modal = new bootstrap.Modal(document.getElementById('updatebalance'), {
                    backdrop: 'static',
                    keyboard: false
                });
                modal.show();
            } else {
                alert("Failed to fetch balance: " + response.message);
            }
        },
        error: function(xhr, status, error) {
            console.error("Error fetching balance:", error);
            alert("Error fetching balance for merchant.");
        }
    });
}
</script>


<script>
function addBalance() { 
    var mid = $("#merchantId").val();
    var amount = $("#newbalance").val();
    var paymentMode = $("#mode").val();

    // Validation
    if (!mid) {
        showMessage('<span class="text-danger fw-bold">Please select a merchant.</span>');
        return;
    }
    if (!paymentMode) {
        showMessage('<span class="text-danger fw-bold">Please enter payment mode.</span>');
        return;
    }
    if (!amount || isNaN(amount) || Number(amount) <= 0) {
        showMessage('<span class="text-danger fw-bold">Please enter a valid wallet amount.</span>');
        return;
    }

    var requestData = {
        mid: mid,
        walletAmount: amount,
        paymentMode: paymentMode,
        username: '${username}'
    };

    $.ajax({
        url: '${url}/app/public/add-merchant-balance',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        beforeSend: function () {
            ajaxindicatorstart('📊 Adding balance, please wait...');
        },
        success: function (response) {
            console.log("✅ Balance added response:", response);
            if (response.responseCode === 200) {
                showMessage('<span class="text-success fw-bold">' + response.message + '</span>');

                // Clear form fields
                $("#merchantId").val('');
                $("#newbalance").val('');
                $("#mode").val('');
            } else {
                showMessage('<span class="text-danger fw-bold">' + response.message + '</span>');
            }
            ajaxindicatorstop();
        },
        error: function () {
            showMessage('<span class="text-danger fw-bold">❌ Error while adding balance. Please try again.</span>');
            ajaxindicatorstop();
        }
    });
}


function showMessage(message) {
    $("#updateBalancemsg").html(message).fadeIn();

    setTimeout(function () {
        $("#updateBalancemsg").fadeOut();
    }, 5000);
}
</script>



<%@ include file="AdminFooter.jsp"%>




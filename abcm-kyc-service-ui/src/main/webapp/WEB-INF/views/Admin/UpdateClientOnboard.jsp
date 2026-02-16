<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<head>
    <%@ include file="AdminHeader.jsp"%>
    <meta charset="UTF-8" />
    <title>Provider Onboarding</title>
</head>

<div class="pc-container">
    <div class="pc-content">
        <!-- [ breadcrumb ] start -->
        <!-- <div class="page-header">
            <div class="page-block">
              <div class="row align-items-center">
                <div class="col-md-12">
                  <div class="page-header-title">
                    <h2 class="mb-0">Provider Onboard</h2>
                  </div>
                </div>
              </div>
            </div>
          </div> -->
        <!-- [ breadcrumb ] end -->


        <div class="card">
            <div class="card-header p-2">
                <h5 class="mb-0">Provider Onboard</h5>
            </div>
            <div class="card-body">
                <form id="clients_sumt">
            <div class="row">
                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Provider Name</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Provider Name" name="providerName" aria-label="Username" aria-describedby="basic-addon1" value="${ClientMaster.serviceProviderName}" />
                                                                        <input type="hidden" class="form-control" placeholder="Provider Name"  name ="id"aria-label="Username" aria-describedby="basic-addon1" value="${ClientMaster.id}">
                        
                    </div>
                </div>
                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Aaddhar Initiate OTP Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Provider Aaddhar URL1" name="aaddharUrl1" aria-label="Email" aria-describedby="basic-addon1" value="${ClientMaster.apiAadharUrl1}"  />
                    </div>
                </div>
                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Aaddhar OTP Submit Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Aaddhar URL2" name="aaddharUrl2" aria-label="Phone" aria-describedby="basic-addon1" value="${ClientMaster.apiAadharUrl2}"  />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Gst Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Gst Url1" aria-label="Phone" name="gstUrl1" aria-describedby="basic-addon1"  value="${ClientMaster.apiGstUrl1}" />
                    </div>
                </div>

               <!--  <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Gst Url2</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Gst Url2" aria-label="Phone" name="gstUrl2" aria-describedby="basic-addon1" />
                    </div>
                </div> -->

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Pan Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Pan Url1" aria-label="Phone" name="panUrl1" aria-describedby="basic-addon1" value="${ClientMaster.apiPanUrl1}" />
                    </div>
                </div>
                
                 <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Driving License Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Pan Url1" aria-label="Phone" name="drivingLicense" aria-describedby="basic-addon1" value="${ClientMaster.drivingLsUrl}" />
                    </div>
                </div>
                
                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Voter-ID Url</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Pan Url1" aria-label="Phone" name="voterId" aria-describedby="basic-addon1"  value="${ClientMaster.voterIdUrl}"/>
                    </div>
                </div>

               <!--  <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label">Pan Url2</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider Pan Url2" aria-label="Phone" name="panUrl1" aria-describedby="basic-addon1" />
                    </div>
                </div> -->

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Provider Url1</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider  Url1" aria-label="Phone" name="udf1" aria-describedby="basic-addon1" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Provider Url2</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder=" Provider  Url2" aria-label="Phone" name="udf2" aria-describedby="basic-addon1" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Provider Url3</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Provider  Url3" aria-label="Phone" name="udf3" aria-describedby="basic-addon1" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Provider Url4</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Provider  Url4" aria-label="Phone" name="udf4" aria-describedby="basic-addon1" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Provider Url5</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Provider  Url5" aria-label="Phone" name="udf5" aria-describedby="basic-addon1" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> Api Key</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-link"></i></span>
                        <input type="text" class="form-control" placeholder="Api Key" name="apiKey" aria-label="username" aria-describedby="basic-addon1" value="${ClientMaster.apiKey}" />
                    </div>
                </div>
                <div class="col-md-4 col-12">
                    <label for="merchantPhoneNo" class="form-label"> App Id</label>
                    <div class="input-group mb-3">
                        <span class="input-group-text" id="basic-addon1"><i class="fas fa-clock"></i></span>
                        <input type="text" class="form-control" placeholder="App Id" aria-label="Email" name="apiId" aria-describedby="basic-addon1" value="${ClientMaster.appId}" />
                    </div>
                </div>

                <div class="col-md-4 col-12">
    <label class="form-label">Environment</label>
    <div class="input-group mb-3">
        <span class="input-group-text" id="basic-addon1">
            <i class="far fa-hand-pointer"></i>
        </span>
        <select class="form-select" id="inputGroupSelect03" name="environment" aria-label="Environment select">
            <option value="" disabled ${empty ClientMaster.environment ? 'selected' : ''}>Select Environment</option>
            <option value="LIVE" ${ClientMaster.environment == 'LIVE' ? 'selected' : ''}>LIVE</option>
            <option value="TEST" ${ClientMaster.environment == 'TEST' ? 'selected' : ''}>TEST</option>
        </select>
    </div>
</div>
            </div>

            

            <div style="text-align: center;">
                <span id="onboard_msg"></span>
            </div>
            <div class="bill-register mb-3 text-center">
                <button type="submit" class="btn btn-shadow btn-info mt-2">Submit</button>
            </div>
        </form>
            </div>
        </div>
        
    </div>
</div>

<%@ include file="AdminFooter.jsp"%>

<script>
     $(document).ready(function () {
    	  $('#clients_sumt').submit(function (e) {
    	   // alert("call");
    	    e.preventDefault(); // prevent default form submission
    	    // Collect form data into a JSON object
    	    const formData = {};
    	    $('#clients_sumt').serializeArray().forEach(field => {
    	      formData[field.name] = field.value;
    	    });
    	    $.ajax({
    	      url: '${url}'+'/app/public/onboardClient',
    	      type: 'POST',
    	      contentType: 'application/json',
    	      data: JSON.stringify(formData),
    	      beforeSend: function () {
              	  ajaxindicatorstart('⚙️ Update Provider Onboarding... Please Wait.');
          	    },
    	      success: function (response) {
    	    	  if (response.responseCode === 200) {
                  	
                      $("#onboard_msg").empty().append(response.message).css({
                          color: "green",
                          fontWeight: "bold",
                      });
                      ajaxindicatorstop(); // ✅ Stop loader on error
                      //$("#clients_sumt").trigger("reset");
                  } else {
                      $("#onboard_msg").empty().append(response.message).css({
                          color: "red",
                          fontWeight: "bold",
                      });
                      ajaxindicatorstop(); // ✅ Stop loader on error
                      //$("#clients_sumt").trigger("reset");
                  }
    	      },
    	      error: function (error) {
    	    	  $("#onboard_msg").empty().append('error while update provider').css({
                      color: "red",
                      fontWeight: "bold",
                  });
    	       // alert('Error during onboarding!');
    	        console.error('Error:', error);
    	       // $('#clients_sumt').trigger('reset');
    	      }
    	    });
    	  });
    	});
</script>




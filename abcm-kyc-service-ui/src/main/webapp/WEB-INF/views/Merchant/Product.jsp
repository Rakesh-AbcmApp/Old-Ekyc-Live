<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<%@ include file="MerchantHeader.jsp"%>
<meta charset="UTF-8">
<title>eKYC|Product</title>
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
								<h2 class="mb-0">Products</h2>
							</div>
                        </div>
                    </div>
                    </div>
                </div>
                <!-- [ breadcrumb ] end -->

                <!-- [ Main Content ] start -->
                <div class="row">
                    <div class="col-12">
                        <div class="card p-4">
                            <div class="card-body">

                                <div class="kyc-products">
                                    <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                        <li class="nav-item">
                                            <a class="nav-link active" id="pills-home-tab" data-bs-toggle="pill" href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">All</a>
                                        </li>
                                       <!--  <li class="nav-item">
                                            <a class="nav-link" id="pills-profile-tab" data-bs-toggle="pill" href="#pills-profile" role="tab" aria-controls="pills-profile" aria-selected="false">identity</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-contact-tab" data-bs-toggle="pill" href="#pills-contact" role="tab" aria-controls="pills-contact" aria-selected="false">financial</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-ml-tab" data-bs-toggle="pill" href="#pills-ml" role="tab" aria-controls="pills-ml" aria-selected="false">ml</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-esign-tab" data-bs-toggle="pill" href="#pills-esign" role="tab" aria-controls="pills-esign" aria-selected="false">eSign</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-face-tab" data-bs-toggle="pill" href="#pills-face" role="tab" aria-controls="pills-face" aria-selected="false">face Analytics</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-utility-tab" data-bs-toggle="pill" href="#pills-utility" role="tab" aria-controls="pills-utility" aria-selected="false">utility</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-persona-tab" data-bs-toggle="pill" href="#pills-persona" role="tab" aria-controls="pills-persona" aria-selected="false">persona</a>
                                        </li>
                                        <li class="nav-item">
                                            <a class="nav-link" id="pills-fraud-tab" data-bs-toggle="pill" href="#pills-fraud" role="tab" aria-controls="pills-fraud" aria-selected="false">fraud</a>
                                        </li> -->
                                    </ul>
                                    <!-- <div class="all-products">
                                        <select class="form-select rounded-3 form-select-sm w-auto">
                                            <option selected>All Products</option>
                                            <option>adhar card</option>
                                            <option >Voter id lite</option>
                                        </select>
                                    </div> -->
                                </div>
                                
<%-- 								<p>yyyyyyyyyyy ${merchant}</p>
 --%>

                               <div class="tab-content" id="pills-tabContent">
								    <!-- tab-1 -->
								    <div class="tab-pane fade show active" id="pills-home" role="tabpanel" aria-labelledby="pills-home-tab">
								        <div class="row">
								            <!-- Aadhaar -->
											<div class="col-md-6 col-xl-4">
											    <div class="card">
											        <h5 class="card-header p-2">Aadhaar</h5>
											        <div class="card-body p-2">
											            <h5 class="card-title">Verify Your Identity</h5>
											            <p class="card-text">Ensure your Aadhaar is linked and verified for secure transactions.</p>
											            <div class="kyc-btn">
											                <c:choose>
											                    <c:when test="${merchant.aadharOkyc == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
											            </div>
											        </div>
											    </div>
											</div>
								
								            <!-- PAN -->
								            <div class="col-md-6 col-xl-4">
								                <div class="card">
								                    <h5 class="card-header p-2">PAN</h5>
								                    <div class="card-body p-2">
								                        <h5 class="card-title">Tax Details Update</h5>
								                        <p class="card-text">Provide your PAN details to continue with financial operations.</p>
								                        <div class="kyc-btn">
								                            <c:choose>
											                    <c:when test="${merchant.panPro == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
								                        </div>
								                    </div>
								                </div>
								            </div>
								            <!-- GST -->
								            <div class="col-md-6 col-xl-4">
								                <div class="card">
								                    <h5 class="card-header p-2">GST</h5>
								                    <div class="card-body p-2">
								                        <h5 class="card-title">Business Tax Info</h5>
								                        <p class="card-text">Add your GST number to enable B2B services and compliance.</p>
								                        <div class="kyc-btn">
								                            <c:choose>
											                    <c:when test="${merchant.gstLit == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
								                        </div>
								                    </div>
								                </div>
								            </div>
								            
								            
								            <div class="col-md-6 col-xl-4">
								                <div class="card">
								                    <h5 class="card-header p-2">DRIVING_LICENSE</h5>
								                    <div class="card-body p-2">
								                        <h5 class="card-title">Driving License Info</h5>
								                        <p class="card-text">Verify your Driving License as part of the KYC process for secure identity validation.</p>
								                        <div class="kyc-btn">
								                            <c:choose>
											                    <c:when test="${merchant.drivingLicense == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
								                        </div>
								                    </div>
								                </div>
								            </div>
								            
								            
								            <div class="col-md-6 col-xl-4">
								                <div class="card">
								                    <h5 class="card-header p-2">VOTER-ID</h5>
								                    <div class="card-body p-2">
								                        <h5 class="card-title">Verify Your Voter Id</h5>
								                        <p class="card-text">Verify your identity using Voter ID  service for KYC compliance</p>
								                        <div class="kyc-btn">
								                            <c:choose>
											                    <c:when test="${merchant.voterId == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
								                        </div>
								                    </div>
								                </div>
								            </div>
								
								            <!-- GST Advance -->
								           <%--  <div class="col-md-6 col-xl-3">
								                <div class="card">
								                    <h5 class="card-header">GST Advance</h5>
								                    <div class="card-body">
								                        <h5 class="card-title">Advance Credit Verification</h5>
								                        <p class="card-text">Submit GST advance documents for faster credit approval.</p>
								                        <div class="kyc-btn">
								                            <c:choose>
											                    <c:when test="${merchant.gstLit == 'ENABLE'}">
											                        <span class="badge bg-success">Active</span>
											                    </c:when>
											                    <c:otherwise>
											                        <span class="badge bg-secondary">Inactive</span>
											                    </c:otherwise>
											                </c:choose>
								                        </div>
								                    </div>
								                </div>
								            </div> --%>
								        </div>
								    </div>
								    <!-- tab-1 end -->
								</div>

                            </div>
                        </div>
                    </div>
                </div>
                <!-- [ Main Content ] end -->
            </div>
        </div>
        
        
     <%@ include file="MerchantFooter.jsp"%>
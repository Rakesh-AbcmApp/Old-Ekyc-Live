<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<%@ include file="MerchantHeader.jsp"%>
<meta charset="UTF-8">
<title>eKYC | Apps</title>
</head>
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
                            <h2 class="mb-0">Apps</h2>
                        </div>
                        </div>
                    </div>
                    </div>
                </div>
                <!-- [ breadcrumb ] end -->
                <%-- <div class="row">
                    <div class="col-lg-4 col-xxl-3">
                        <div class="card">
                          <div class="card-body position-relative">
                            <div class="text-center mt-3">
                              <div class="chat-avtar d-inline-flex mx-auto">
                                <img class="rounded-circle img-fluid wid-70" src="${url}/assets/images/user/avatar-5.jpg" alt="User image">
                              </div>
                              <h6 class="mb-0">Test App</h6>
                              <p class="text-muted text-sm">March 06, 2025</p>
                              <div class="row g-3">
                                <div class="col-6">
                                  
                                  <small class="text-muted">Key</small>
                                  <h5 class="mb-0">2</h5>
                                </div>
                                <div class="col-6  border-top-0 border-bottom-0">
                                  
                                  <small class="text-muted">Services</small>
                                  <h5 class="mb-0">10</h5>
                                </div>
                              </div>
                              <hr class="my-2 border border-secondary-subtle">
                              <div class="d-inline-flex align-items-center justify-content-between w-100 mb-3">
                                <button class="btn bg-gray-300  gap-2">Trail</button>
                                <button class="btn bg-cyan-200"> More Info</button>
                              </div>
                            </div>
                          </div>
                        </div>
                    </div> --%>
                    <div class="col-lg-4 col-xxl-3">
                        <div class="card">
                          <div class="card-body position-relative">
                            <div class="text-center mt-3">
                              <div class="chat-avtar d-inline-flex mx-auto">
                                <img class="rounded-circle img-fluid wid-70" src="${url}/assets/images/user/avatar-5.jpg" alt="User image">
                              </div>
                              <h6 class="mb-0">Production</h6>
                              <p class="text-muted text-sm">March 06, 2025</p>
                              <div class="row g-3">
                                <div class="col-6">
                                  <small class="text-muted">Key</small>
                                  <h5 class="mb-0">0</h5>
                                </div>
                                <div class="col-6  border-top-0 border-bottom-0">
                                  
                                  <small class="text-muted">Services</small>
                                  <h5 class="mb-0">${subscribeCount}</h5>
                                </div>
                              </div>
                              <hr class="my-2 border border-secondary-subtle">
                              <div class="d-inline-flex align-items-center justify-content-between w-100 mb-3">
                                <button class="btn bg-gray-300  gap-2">Trail</button>
                                <button class="btn bg-cyan-200" data-bs-toggle="modal" data-bs-target="#appDetailsModal"> More Info</button>
                              </div>
                            </div>
                          </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>
        
        
        <!-- Modal -->
		<div class="modal fade" id="appDetailsModal" tabindex="-1" aria-labelledby="appDetailsLabel" aria-hidden="true">
		  <div class="modal-dialog modal-dialog-centered">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="appDetailsLabel">App Details</h5>
		        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
		      </div>
		      <div class="modal-body">
		        <p><strong>App ID:</strong>${merchant.appId} </p>
		        <p><strong>Name:</strong> Production</p>
		        <p><strong>Created At:</strong> ${merchant.credentials.createdAt}</p>
		        <c:set var="productCount" value="0" />

				<c:if test="${merchant.aadharOkyc == 'ENABLE'}">
				    <c:set var="productCount" value="${productCount + 1}" />
				</c:if>
				<c:if test="${merchant.panPro == 'ENABLE'}">
				    <c:set var="productCount" value="${productCount + 1}" />
				</c:if>
				<c:if test="${merchant.gstLit == 'ENABLE'}">
				    <c:set var="productCount" value="${productCount + 1}" />
				</c:if>
		        
		        
		        <p><strong>Services: </strong>${productCount}</p>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
		      </div>
		    </div>
		  </div>
		</div>
        
        
        <%@ include file="MerchantFooter.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="MerchantHeader.jsp"%>
<meta charset="UTF-8">
<title>Insert title here</title>
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
                <div class="col-md-12 col-xxl-12">
                    <div class="add-balance mb-3">
                        <button type="button" class="btn btn-shadow btn-info mr-2 mb-2" data-bs-toggle="modal" data-bs-target=".bd-example-modal-lg"><i class="ti ti-plus"></i> Add Balance</button>
                       
                    </div>
                    <div class="col-12">
                        <div class="card billing-table">
                        <div class="card-header d-flex align-items-center justify-content-between py-3">
                            <h5 class="mb-0">Plan Details</h5> 
                        </div>


                        <div class="card-body">
                            <div class="kyc-wallet">
                                <div class="row">
                                    <div class="col-md-6 col-xl-3">
                                        <label class="form-label" for="example-datemax">Current Plan</label>
                                        <p class="kyc-wallet-details">Lite</p>
                                    </div>
                                    <div class="col-md-6 col-xl-3">
                                        <label class="form-label" for="example-datemax">Validity</label>
                                        <p class="kyc-wallet-details">18 month</p>
                                    </div>
                                    <div class="col-md-6 col-xl-3">
                                        <label class="form-label" for="example-datemax">Available Balance</label>
                                        <p class="kyc-wallet-details"><i class="fas fa-rupee-sign"></i> 15462.00</p>
                                    </div>
                                    <div class="col-md-6 col-xl-3">
                                        <label class="form-label" for="example-datemax">Alert Balance ?</label>
                                        <p class="kyc-wallet-details"><i class="fas fa-rupee-sign"></i> 856844.56</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        
            <div class="col-md-6 col-xxl-6">
                <div class="col-12">
                    <div class="card table-card billing-table">
                        <div class="card-header d-flex align-items-center justify-content-between py-3">
                            <h5 class="mb-0">Payment History </h5><i class="fas fa-sync-alt"></i>
                        </div>


                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover" id="pc-dt-simple">
                                    <thead>
                                        <tr>
                                            <th>Date</th>
                                            <th>Payment Type</th>
                                            <th>Mode</th>
                                            <th>Amount</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>25/03/2025</td>
                                            <td>internal Payment</td>
                                            <td>NA</td>
                                            <td>55850</td>
                                            <td><span class="badge text-bg-success">success</span></td>
                                            
                                        </tr>
                                        <tr>
                                            <td>14/03/2025</td>
                                            <td>Net Banking</td>
                                            <td>NA</td>
                                            <td>25850</td>
                                            <td><span class="badge text-bg-warning">Pending</span></td>
                                        </tr>
                                    
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-xxl-6">
                <div class="col-12">
                    <div class="card table-card billing-table">
                        <div class="card-header d-flex align-items-center justify-content-between py-3">
                            <h5 class="mb-0">Plan History</h5>
                        </div>


                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-hover" id="pc-dt-simple">
                                    <thead>
                                    <tr>
                                        <th>Plan Name</th>
                                        <th>Plan type</th>
                                        <th>Start Date</th>
                                        <th>End Date</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>lite</td>
                                        <td>Prepaid</td>
                                        <td>05/01/2025</td>
                                        <td>04/02/2025</td>
                                        
                                    </tr>
                                    <tr>
                                        <td>lite</td>
                                        <td>Prepaid</td>
                                        <td>05/01/2025</td>
                                        <td>04/02/2025</td>
                                    </tr>
                                    
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      </div>


    <!-- modal billing balance -->
    <div class="modal fade bd-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content  balance-add">
                <div class="modal-header">
                    <h5 class="modal-title h4" id="myLargeModalLabel">Add Custom Amount</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="custom-body mb-3">
                        <div class="labelbbps">
                            <label for="validationTooltip05" class="font-bold form-label text-green-400 mb-2">Add money</label>
                        </div>
                        <div class="input-group">
                            <span class="input-group-text"><i class="fas fa-rupee-sign"></i></span>
                            <input type="text" class="form-control" id="validationTooltip05" required="">
                        </div>
                    </div>
                    <div class="bbps-wallet  mt-3">
                        <div class="d-flex flex-wrap gap-2">
                            <button type="button" class="btn btn-outline-info">50</button>
                            <button type="button" class="btn btn-outline-info">200</button>
                            <button type="button" class="btn btn-outline-info">500</button>
                            <button type="button" class="btn btn-outline-info">1000</button>
                            <button type="button" class="btn btn-outline-info">2000</button>
                            <button type="button" class="btn btn-outline-info">3000</button>
                            <button type="button" class="btn btn-outline-info">5000</button>
                        </div>
                    </div>

                    <div class="bbps-proceed mt-4 text-center">
                        <button type="button" class="btn btn-shadow btn-info">Proceed to Pay </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- modal billing balance end -->


    </div>
<%@ include file="MerchantFooter.jsp"%>
    
</body>
</html>
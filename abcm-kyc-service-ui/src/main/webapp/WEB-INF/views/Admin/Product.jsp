<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="AdminHeader.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>eKYC | Product</title>
</head>
<body>
    <!-- [ Main Content ] start -->
    <div class="pc-container">
        <div class="pc-content">
          

            <!-- [ Main Content ] start -->
            <div class="row">
                <div class="col-12">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">Product Details</h5>
                        </div>
                        <div class="card-body">
                            <!-- <div class="kyc-products">
                                <ul class="nav nav-pills mb-3" id="pills-tab" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="pills-home-tab" data-bs-toggle="pill"
                                           href="#pills-home" role="tab" aria-controls="pills-home" aria-selected="true">
                                            Product Details
                                        </a>
                                    </li>
                                </ul>
                            </div> -->

                            <div class="tab-content" id="pills-tabContent">
                                <!-- tab-1 -->
                                <div class="tab-pane fade show active" id="pills-home" role="tabpanel"
                                     aria-labelledby="pills-home-tab">
                                    <div class="row">
                                        <c:forEach var="product" items="${productList}">
                                            <div class="col-md-6 col-xl-4">
                                                <div class="card">
                                                    <h5 class="card-header">${product.productName}</h5>
                                                    <div class="card-body">
                                                        <h5 class="card-title">
                                                            <c:choose>
                                                                <c:when test="${product.productName == 'PAN'}">
                                                                    Tax Details Update
                                                                </c:when>
                                                                <c:when test="${product.productName == 'GSTIN'}">
                                                                    Business Tax Info
                                                                </c:when>
                                                                <c:when test="${product.productName == 'OKYC'}">
                                                                    Verify Your Identity
                                                                </c:when>
                                                                
                                                                <c:when test="${product.productName == 'DRIVING_LICENSE'}">
                                                                    Verify Your Driving-License
                                                                </c:when>
                                                                
                                                                 <c:when test="${product.productName == 'VOTER-ID'}">
                                                                    Verify Your Voter-Id
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Product Information
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </h5>
                                                        <p class="card-text">
                                                            <c:choose>
                                                                <c:when test="${product.productName == 'PAN'}">
                                                                    Provide your PAN details to continue with financial operations.
                                                                </c:when>
                                                                <c:when test="${product.productName == 'GSTIN'}">
                                                                    Add your GST number to enable B2B services and compliance.
                                                                </c:when>
                                                                <c:when test="${product.productName == 'OKYC'}">
                                                                    Ensure your KYC is linked and verified for secure access.
                                                                </c:when>
                                                                
                                                                
                                                                 <c:when test="${product.productName == 'DRIVING_LICENSE'}">
                                                                    Ensure your Driving_License is linked and verified for secure access.
                                                                </c:when>
                                                                
                                                                <c:when test="${product.productName == 'VOTER-ID'}">
                                                                    Ensure your Voter-Id is linked and verified for secure access.
                                                                </c:when>
                                                                
                                                                <c:otherwise>
                                                                    Please provide the required information.
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>
                                                        <div class="kyc-btn">
                                                            <!-- Action buttons can go here -->
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div> <!-- tab-content -->
                        </div>
                    </div>
                </div>
            </div> <!-- row -->
        </div>
    </div> <!-- container -->

<%@ include file="AdminFooter.jsp" %>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="icon" href="img/fav.png" type="image/x-icon" />
<title>PG Page</title>
</head>
<body>
	<%-- <form action="${payUrl}" method="post">
		<input type="hidden" value="${api_key}"
			name="api_key" /> <input type="hidden"
			value="${encrypted_data}" name="encrypted_data" /> <input
			type="hidden" value="${iv}" name="iv" />
		<!-- Generally instead of showing the submit button do an auto submit using 
javascript onload="document.forms[0].submit()" -->
		<input type="submit" value="Submit">
	</form> --%>
	<form id="paymentform" action="${payUrl}" method="POST">
    <input type="hidden" name="hash" value="${hash}">
    <input type="hidden" name="api_key" value="${apiKey}">
    <input type="hidden" name="return_url" value="${returnUrl}">
    <input type="hidden" name="mode" value="${mode}">
    <input type="hidden" name="order_id" value="${orderId}">
    <input type="hidden" name="amount" value="${amount}">
    <input type="hidden" name="currency" value="${currency}">
    <input type="hidden" name="description" value="${description}">
    <input type="hidden" name="name" value="${name}">
    <input type="hidden" name="email" value="${email}">
    <input type="hidden" name="phone" value="${phone}">
    <input type="hidden" name="address_line_1" value="${addressLine1}">
    <input type="hidden" name="address_line_2" value="${addressLine2}">
    <input type="hidden" name="city" value="${city}">
    <input type="hidden" name="state" value="${state}">
    <input type="hidden" name="zip_code" value="${zipCode}">
    <input type="hidden" name=c value="${country}">
    <input type="hidden" name="udf1" value="${udf1}">
    <input type="hidden" name="udf2" value="${udf2}">
    <input type="hidden" name="udf3" value="${udf3}">
    <input type="hidden" name="udf4" value="${udf4}">
    <input type="hidden" name="udf5" value="${udf5}">
<%--     <input type="hidden" name="payment_options" value="${paymentOptions}">
 --%></form>

		<script>
			document.getElementById("paymentform").submit();
		</script>
</body>

</html>

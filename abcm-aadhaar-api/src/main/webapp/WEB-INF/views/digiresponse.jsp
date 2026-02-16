<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
    
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Response page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

     <style>
    body {
      font-family: Arial, sans-serif;
      background: #dedede;
    }
    .respo-inner{
      max-width: 500px;
      margin: 10rem auto;
    } 
    .response-container {
      background: #fff;
      padding: 40px;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      /* max-width: 500px; */
      text-align: center;
    }
    .response-container h1 {
      color: #2c3e50;
      margin-bottom: 20px;
    }
    .response-container p {
      color: #555;
      font-size: 16px;
      line-height: 1.5;
    }
   .xxdigi-respo {
      padding: 1.5rem;
   }
   .xxdigi-respo img {
    padding-top: 1rem;

   } 


   @media only screen and (max-width:768px){
    .respo-inner {
      max-width: 100% !important;
      padding: 1rem;
    }
    .response-container {
      padding: 40px 5px;
    }
    .xxdigi-respo {
      padding: 0.5rem;
    }
   }
  </style>
</head>
<body>
    <div class="xxdigi-respo">
      <div class="xxdigi-respoimg">
          <img src="https://ekyc.ablepay.in/view-kyc/assets/images/logo.png" class="" alt="logo" width="120"/>
      </div>  
      <div class="respo-inner">
          <div class="response-container">
              <h1>Thank You!</h1>
              <p>${message}.</p>
          </div>
      </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body> 
</html>
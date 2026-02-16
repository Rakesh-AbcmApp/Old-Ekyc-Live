<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<head>
<title>eKYC | Routing</title>

<%@ include file="AdminHeader.jsp"%>

<style>
#listabcm {
	max-width: 1100px;
	width: 1100px;
	margin: auto;
	background: #f0efeac4;
	border: 1px solid #f3f3f3;
	padding: 1rem;
	border-radius: 8px;
	box-shadow: rgba(0, 0, 0, 0.16) 0px 1px
}

@media only screen and (max-width: 600px) {
  #listabcm {
       max-width: 100%;
  }
}

</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<!-- [Head] end -->

<div class="pc-container">
	<div class="pc-content">
		<!-- [ breadcrumb ] start -->
		<div class="page-header">
			<div class="page-block">
				<div class="row align-items-center">
					<div class="col-md-12">
						<div class="page-header-title">
							<h2 class="mb-0">Merchant Routing</h2>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- [ breadcrumb ] end -->
		<div class="row">


			<form id="listabcm" class="p-4">
				<div class="row">
  <div class="col-12 col-md-3">
    <div class="mb-3">
      <label for="merchantId" class="form-label">Merchant Id</label>
      <input type="text" id="merchantId" class="form-control" value="${mid}" placeholder="Enter merchant Id" />
    </div>
  </div>
  <div class="col-12 col-md-3">
    <div class="mb-3">
      <label for="merchantName" class="form-label">Merchant Name</label>
      <input type="text" id="merchantName" class="form-control" value="${Name}" placeholder="Enter merchant Name" />
      <input type="hidden" id="Username" class="form-control" value="${username}" placeholder="Enter merchant Name" />
    </div>
  </div>
</div>

				<div id="merchant-container">
					<div class="list-merchants mb-3">
						<div class="row mb-3 align-items-center">

							<div class="col-12 col-md-3">
								<label class="form-check-label" for="rateCheck1">Select
									Provider</label> <select class="form-select provider-select" id="providerList">
									<option selected>select provider</option>

								</select>
							</div>

							<div class="col-12 col-md-3">
								<label class="form-check-label" for="productSelect">Select
									Product</label> <select class="form-select product-select" id="productList">
									<option selected>select product</option>

								</select>
							</div>

							<div class="col-12 col-md-2">
								<label class="form-check-label" for="rateInput">Rate</label> <input
									type="text" class="form-control rate-input" id="rateInput"
									placeholder="Rate" />
							</div>
							<div class="col-12 col-md-2 d-flex align-items-end">
								<button type="button"
									class="btn btn-danger rounded-0 remove-btn btn-sm">Remove</button>
							</div>

						</div>
					</div>
				</div>


				<div class="btn-groups mt-3 text-center">
				<div>
						</br> <span style="color: rgb(255, 255, 255);" id="route-rate">
						</span>
					</div>
					<button type="button" id="addMerchant"
						class="btn btn-info rounded-0 mt-1">Add More Charges</button>
					<button type="button" 
						onclick="submitMerchantRouting();"
						class="btn btn-primary rounded-0 mt-1">Save Charges</button>
					<div></br>
						<span style="color: red; font-weight: bold;" id="route-msg">  </span>
					</div>
				</div>
			</form>

		</div>
	</div>
</div>
<script>
  window.addEventListener('load', function() {
	  fetch('${url}'+'/app/public/provider-product-list/'+'${mid}')
	    .then(function(res) {
	      return res.json();
	    })
	    .then(function(response) {
	    	//console.log("Stringified JSON:", JSON.stringify(response));
	      var providers = (response.data && response.data.data && response.data.data.providers) || [];
	      var products = (response.data && response.data.data && response.data.data.products) || [];
	      // Fill initial dropdowns
	      fillDropdown(document.getElementById('providerList'), providers, 'serviceProviderName', 'id');
	      fillDropdown(document.getElementById('productList'), products, 'productName', 'productId');
	      // Store data globally for later use
	      window.merchantData = { providers: providers, products: products };
	    })
	    .catch(function(err) {
	      console.error('Fetch error:', err);
	    });
	});
	function fillDropdown(selectElem, items, labelKey, valueKey) {
	  for (var i = 0; i < items.length; i++) {
	    var option = document.createElement('option');
	    option.value = items[i][valueKey];
	    option.textContent = items[i][labelKey];
	    selectElem.appendChild(option);
	  }
	}
	var merchantContainer = document.getElementById('merchant-container');
	var addMerchantBtn = document.getElementById('addMerchant');
	function getMerchantTemplate(providers, products) {
	  var providerOptions = "";
	  for (var i = 0; i < providers.length; i++) {
	    providerOptions += "<option value='" + providers[i].id + "'>" + providers[i].serviceProviderName + "</option>";
	  }

	  var productOptions = "";
	  for (var j = 0; j < products.length; j++) {
	    productOptions += "<option value='" + products[j].productId + "'>" + products[j].productName + "</option>";
	  }
	  var template = 
	    "<div class='list-merchants mb-3'>" +
	      "<div class='row mb-3 align-items-center'>" +

	        "<div class='col-12 col-md-3'>" +
	          "<label class='form-check-label'>Select Provider</label>" +
	          "<select id='providerList' class='form-select provider-select'>" +
	            "<option selected disabled>Select provider</option>" +
	            providerOptions +
	          "</select>" +
	        "</div>" +

	        "<div class='col-12 col-md-3'>" +
	          "<label class='form-check-label'>Select Product</label>" +
	          "<select id='productList' class='form-select product-select'>" +
	            "<option selected disabled>Select product</option>" +
	            productOptions +
	          "</select>" +
	        "</div>" +

	        "<div class='col-12 col-md-2'>" +
	          "<label class='form-check-label'>Rate</label>" +
	          "<input type='text' class='form-control rate-input' id='rateInput' placeholder='Rate' />" +
	        "</div>" +

	        "<div class='col-12 col-md-2 d-flex align-items-end'>" +
	          "<button type='button' class='btn btn-danger rounded-0 remove-btn'>Remove</button>" +
	        "</div>" +

	      "</div>" +
	    "</div>";

	  return template;
	}
	addMerchantBtn.addEventListener('click', function() {
	  if (!window.merchantData) {
	    alert('Data not loaded yet, please wait.');
	    return;
	  }
	  var div = document.createElement('div');
	  div.innerHTML = getMerchantTemplate(window.merchantData.providers, window.merchantData.products);
	  merchantContainer.appendChild(div.firstElementChild);
	});
	merchantContainer.addEventListener('click', function(e) {
	  if (e.target.classList.contains('remove-btn')) {
	    var block = e.target.closest('.list-merchants');
	    if (block) block.remove();
	  }
	});

</script>




<script>
function submitMerchantRouting() {
	console.log("merchnat Routing saved success");
    //alert("call routing");
    var merchantId = $("#merchantId").val();
    const username = document.getElementById('Username').value.trim();
    var container = document.getElementById('merchant-container');
    // Corrected line
    var blocks = container.querySelectorAll('.list-merchants');
    var routingDetails = [];
    blocks.forEach(function(block) {
    var providerSelect = block.querySelector('.provider-select');
    var productSelect = block.querySelector('.product-select');
    var rateInput = block.querySelector('.rate-input');
    if(providerSelect && productSelect && rateInput) {
      var clientId = parseInt(providerSelect.value);
      var productId = parseInt(productSelect.value);
      var rate = parseFloat(rateInput.value);
      if(!isNaN(clientId) && !isNaN(productId) && !isNaN(rate)){
          routingDetails.push({
          clientId: clientId,
          productId: productId,
          isActive: true,
          rate: rate
        });
      }
    }
  });
  var requestBody = {
    merchantId: merchantId,
    username:username,
    routingDetails: routingDetails
  };
  //alert("route rquest body{}"+JSON.stringify(requestBody));

  fetch('${url}'+'/app/public/save-merchant-route', {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Accept": "application/json"
    },
    body: JSON.stringify(requestBody)
  })
  .then(response => { 
    if (!response.ok) {
      throw new Error("Network response was not OK: " + response.status);
    }
    return response.json();
  })
  .then(data => {
    console.log("Response from server:", JSON.stringify(data));
    var ResponseData = data.data;
    console.log("Message:", ResponseData.massage || ResponseData.message);
    $("#route-msg")
      .stop(true, true)
      .empty()
      .append(ResponseData.massage || ResponseData.message || 'Success')
      .show()
      .delay(3000)
      .fadeOut(400);      
  })
  .catch(error => {
    console.error("Fetch error:", error);
    alert("Error occurred while saving merchant routing. Check console for details.");
  });
}
</script>



<script type="text/javascript">
let debounceTimeout;
$(document).on('keyup', '.rate-input', function(e) {
  const keyCode = e.keyCode || e.which;
  // Block backspace, arrow keys
  if ([8, 37, 38, 39, 40].includes(keyCode)) {
    console.log("Blocked key pressed (backspace or arrow), skipping API call.");
    return;
  }
  // Allow only numeric keys
  const key = e.key;
  if (!key.match(/^[0-9]$/)) {
    console.log("Non-numeric key pressed, skipping API call.");
    return; 
  }
  clearTimeout(debounceTimeout);
  debounceTimeout = setTimeout(function() {
    const rateInput = $(e.target);
    const productRate = rateInput.val();
    const parentBlock = rateInput.closest('.list-merchants');
    if (!parentBlock.length) return;
    const clientId = parentBlock.find('.provider-select').val();
    const productId = parentBlock.find('.product-select').val();

    const requestParams = {
      clientId: clientId,
      productId: productId,
      productRate: productRate
    };
    console.log("Request parameters:", requestParams);
    $.ajax({
      url: '${url}' +'/app/public/fetch-product-rate',
      method: 'GET',
      data: requestParams,
      success: function(response) {
        console.log("API response:", response);
        let responseData = JSON.parse(response.data);
        let str = responseData.data;
        // Remove surrounding quotes if present
        if (str.startsWith('"') && str.endsWith('"')) {
          str = str.slice(1, -1);
        }
        console.log("Str is: " + str);
        // Append the text with color based on success/fail and fadeout after 2 seconds
        const $routeRate = $('#route-rate');
        if (responseData.massage && responseData.massage.toLowerCase() === "success") {
          $routeRate
            .removeClass('text-danger')
            .addClass('text-success')
            .stop(true, true)
            .text("Valid Rate !. Please continue to process.")
            .show()
            .delay(2000)
            .fadeOut(400, function() {
              $routeRate.text('').show(); // Clear text after fadeout and show again
            });
        } else {
          $routeRate
            .removeClass('text-success')
            .addClass('text-danger')
            .stop(true, true)
            .text("Invalid Rate !.Please Re-Confirm ")
            .show()
            .delay(2000)
            .fadeOut(400, function() {
              $routeRate.text('').show(); // Clear text after fadeout and show again
            });
        }
      },
      error: function(xhr, status, error) {
        console.error("API error:", xhr.responseText || error);

        const $routeRate = $('#route-rate');
        $routeRate
          .removeClass('text-success')
          .addClass('text-danger')
          .stop(true, true)
          .text('Error occurred, please try again.')
          .show()
          .delay(2000)
          .fadeOut(400, function() {
            $routeRate.text('').show(); // Clear text after fadeout and show again
          });
      }
    });
  }, 300);
});
</script>



<%@ include file="AdminFooter.jsp"%>
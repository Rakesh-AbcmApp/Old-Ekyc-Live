<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:eval
	expression="@environment.getProperty('ContextPath', 'https://default.url')"
	var="url" />

<!doctype html>
<html lang="en">
<head>
<title>ABCM || Kyc Login</title>
<meta charset="utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />

<link rel="icon" href="${url}/assets/images/favicon.png"
	type="image/x-icon" />
<link rel="stylesheet" href="${url}/assets/fonts/inter/inter.css"
	id="main-font-link" />
<!-- <link rel="stylesheet" href="${url}/assets/fonts/phosphor/duotone/style.css" /> -->
<link rel="stylesheet" href="${url}/assets/fonts/tabler-icons.min.css" />
<link rel="stylesheet" href="${url}/assets/fonts/feather.css" />
<link rel="stylesheet" href="${url}/assets/fonts/fontawesome.css" />
<link rel="stylesheet" href="${url}/assets/fonts/material.css" />
<link rel="stylesheet" href="${url}/assets/css/style.css"
	id="main-style-link" />
<link rel="stylesheet" href="${url}/assets/css/style-preset.css" />
<link rel="stylesheet" href="${url}/assets/css/kyc-login.css" />
<!-- <script src="${url}/assets/js/tech-stack.js"></script> -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
@media only screen and (max-width:600px) {
	.auth-form .card .card-body {
		padding: 0;
	}
}
</style>
</head>

<body data-pc-preset="preset-1" data-pc-sidebar-caption="true"
	data-pc-layout="vertical" data-pc-direction="ltr" data-pc-theme="light">
	<div class="loader-bg">
		<div class="loader-track">
			<div class="loader-fill"></div>
		</div>
	</div>

	<div class="auth-main">
		<div class="auth-wrapper login-wrapper v2">
			<div class="auth-sidecontent"
				style="background: #8fb4bc; width: 500px; height: 100vh;">
				<div class="auth-sidecontent text-center">
					<div class="text-center mt-5">
						<a href="#"><img src="${url}/assets/images/ablepay.webp"
							alt="img" style="width: 150px" /></a>
					</div>
					<!-- <img src="${url}/assets/images/login-bg11.svg" alt="images"
						class="img-fluid img-auth-side" style="width: 75%; height: 87vh;" /> -->
					<img src="${url}/assets/images/kyc.png" alt="images"
						class="img-fluid img-auth-side mt-5" style="height: auto;" />
				</div>
			</div>

			<div class="auth-form">
				<div class="card py-5">
					<!-- <div class="card-header">
						<img src="${url}/assets/images/avatar-10.jpg" alt="demo user"/>
					</div> -->
					<div class="card-body">
						<form class="kyc-login-form" id="kycLoginForm"
							action="${url}/login" method="post">

							<!-- Error Message -->

							<div class="mb-3">
								<label class="form-label">Username</label> <input type="text"
									class="form-control" id="floatingInput" name="username"
									placeholder="Enter Username" id="floatingInput" />
							</div>


							<div class="mb-2">
								<label class="form-label">Password</label> <input
									type="password" class="form-control" id="floatingInput1"
									name="password" placeholder="Enter Password" />
							</div>

							<div class="d-flex mt-1 justify-content-between
							">
								<div class="form-check my-2">
									<input class="form-check-input input-primary" type="checkbox"
										id="termsCheckbox"> <label
										class="form-check-label text-muted" for="customCheckc1">I
										agree to all the Terms &amp; Condition</label>
								</div>
							</div>


							<div class="d-grid mt-3 text-center">
								<button type="submit" class="btn btn-primary lgb rounded-0">Login</button>
								<div id="error-msg"
									style="color: white; font-weight: 500; display: none; margin: 10px; background: red; padding: 10px;"></div>
							</div>

							<c:if test="${not empty errorMessage}">
								<div id="error-message"
									style="color: white; font-weight: 500; margin: 10px; background: red; padding: 10px; text-align: center;">
									${errorMessage}</div>
							</c:if>

						</form>
					</div>
				</div>
			</div>

		</div>
		<div class="xx-login-footer"
			style="position: absolute; left: 520px; bottom: 12px;">
			<div class="xx-login-inner">
				<p class="m-0"
					style="text-transform: capitalize; font-size: 13px; font-weight: 600; color: #8fb4bc;">
					&copy; 2025 All Right Reserved Designed & developed by <a
						href="https://www.ablepay.in" target="_blank"> ablepay</a>
				</p>
			</div>
		</div>
	</div>


	<!-- Terms Modal -->
	<div class="modal fade" id="termsModal" tabindex="-1"
		aria-labelledby="termsModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header p-2" style="padding: 10px 20px">
					<h5 class="modal-title" id="termsModalLabel">Terms &
						Conditions</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body" style="height: 71vh;">
					<iframe src="${url}/Agreement/E-KYCServiceAgreement.pdf"
						width="100%" height="100%" style="border: none;"></iframe>
				</div>
				<div class="modal-footer justify-content-center"
					style="padding: 10px 20px">
					<button type="button" class="btn btn-info btn-shadow rounded-0"
						data-bs-dismiss="modal" style="padding: 6px 40px;">Accept</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Scripts -->
	<script src="${url}/assets/js/plugins/popper.min.js"></script>
	<!-- <script src="${url}/assets/js/plugins/simplebar.min.js"></script> -->
	<script src="${url}/assets/js/plugins/bootstrap.min.js"></script>
	<!-- <script src="${url}/assets/js/plugins/i18next.min.js"></script>
	<script src="${url}/assets/js/plugins/i18nextHttpBackend.min.js"></script> -->
	<!-- <script src="${url}/assets/js/icon/custom-font.js"></script> -->
	<script src="${url}/assets/js/script.js"></script>
	<!-- <script src="${url}/assets/js/theme.js"></script> -->
	<!-- <script src="${url}/assets/js/multi-lang.js"></script> -->
	<script src="${url}/assets/js/plugins/feather.min.js"></script>

	<!-- Form Validation Script -->
	<script>
	//When checkbox is clicked, open modal and check it on modal close
	  document.getElementById("termsCheckbox").addEventListener("click", function (e) {
	    // Open modal
	    const modalEl = document.getElementById('termsModal');
	    const modal = new bootstrap.Modal(modalEl);
	    modal.show();

	    // Prevent checkbox from being toggled immediately
	    e.preventDefault();

	    // When modal is hidden, check the checkbox
	    modalEl.addEventListener('hidden.bs.modal', function () {
	      document.getElementById("termsCheckbox").checked = true;
	    }, { once: true });
	  });

	
  document.getElementById("kycLoginForm").addEventListener("submit", function (event) {
    const username = document.getElementById("floatingInput").value.trim();
    const password = document.getElementById("floatingInput1").value.trim();
    const checkbox = document.getElementById("termsCheckbox");
    const errorMsg = document.getElementById("error-msg");
    // Clear previous message and reset opacity
    errorMsg.style.display = "none";
    errorMsg.style.opacity = 1;
    // Validation logic
    if (username === "" || password === "") {
      event.preventDefault();
      errorMsg.textContent = "Username and Password are required.";
      errorMsg.style.display = "block";
    } else if (!checkbox.checked) {
      event.preventDefault();
      errorMsg.textContent = "! Please check this box if we want to proceed.";
      errorMsg.style.display = "block";
    }

    // Fade out logic (only when error message is shown)
    if (errorMsg.style.display === "block") {
      setTimeout(() => {
        let fadeEffect = setInterval(() => {
          if (!errorMsg.style.opacity) {
            errorMsg.style.opacity = 1;
          }
          if (errorMsg.style.opacity > 0) {
            errorMsg.style.opacity -= 0.15;
          } else {
            clearInterval(fadeEffect);
            errorMsg.style.display = "none";
            errorMsg.style.opacity = 1;
          }
        }, 100);
      }, 900);
    }
  });
</script>


	<!-- <script>
  $(document).ready(function () {
    const $usernameInput = $('#floatingInput');
    const $checkboxWrapper = $('#termsCheckbox').closest('.d-flex');
    const $termsCheckbox = $('#termsCheckbox');

    // Hide checkbox by default
    $checkboxWrapper.addClass('d-none');

    function evaluateRole(username) {
      if (username.length >= 3) {
        $.ajax({
          url: '${url}/getRole',
          type: 'GET',
          data: { username: username },
          success: function (response) {
            const role = response?.toUpperCase?.() || "";

            if (role === 'ROLE_ADMIN') {
              $checkboxWrapper.addClass('d-none');
              $termsCheckbox.prop('checked', true);
            } else {
              $checkboxWrapper.removeClass('d-none');
              //$termsCheckbox.prop('checked', true);
            }
          },
          error: function () {
            // If error from API, hide the checkbox
            $checkboxWrapper.addClass('d-none');
            $termsCheckbox.prop('checked', false);
          }
        });
      } else {
        // For empty or short input, always hide
        $checkboxWrapper.addClass('d-none');
        $termsCheckbox.prop('checked', false);
      }
    }

    // Run on input (with debounce)
    let debounceTimer;
    $usernameInput.on('input', function () {
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(() => {
        const username = $(this).val().trim();
        evaluateRole(username);
      }, 400);
    });

    // Run once on page load (for autofilled value)
    const initialVal = $usernameInput.val().trim();
    if (initialVal.length >= 3) {
      evaluateRole(initialVal);
    }
  });
</script>

 -->

	<script>
  window.addEventListener('DOMContentLoaded', function () {
	 // alert();
    const errorBox = document.getElementById("error-message");
    if (errorBox) {
      setTimeout(() => {
        errorBox.style.transition = "opacity 1s ease-out";
        errorBox.style.opacity = 0;
        setTimeout(() => {
          errorBox.style.display = "none";
        }, 1000); // Hide after fade
      }, 4000); // Show for 4 seconds
    }
  });
</script>

</body>
</html>

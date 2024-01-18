<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>+Room 重設密碼</title>
<!-- head -->
<%@ include file="./include/head.jspf"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/cis.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/account.css">

</head>
<body>
	<!-- header -->
	<%@ include file="./include/header.jspf"%>


	<div class="mx-md-5 mx-3">
		<div class="container-fluid p-md-4 p-2 text-center">

			<!-- 重設密碼表單 -->
			<form method="post" action="${pageContext.request.contextPath}/mvc/account/password"
				  class="container bg-white" id="passwordForm" >
				<div class="w-75 pt-5 pb-4 text-start mx-auto">
					<h4 class="fw-bold mb-4">重設密碼</h4>

					<div class="mb-3">
						<label for="oldPassword">舊密碼</label>
						<input type="password" id="oldPassword" name="oldPassword">
					</div>

					<div class="mb-3">
						<label for="newPassword">新密碼</label>
						<input type="password" id="newPassword" name="newPassword">
					</div>

					<div class="mb-3">
						<label for="confirmPassword">再次輸入新密碼</label>
						<input type="password" id="confirmPassword" name="confirmPassword">
					</div>

					<div class="w-100 d-flex justify-content-center text-danger mb-2" style="min-height: 2rem;">${errorMessage}</div>
					
					<div class="w-100 d-flex justify-content-center">
						<button type="submit">重設</button>
					</div>
					
				</div>
			</form>
		</div>
	</div>
	
	<!-- footer -->
	<%@ include file="./include/footer.jspf" %>
</body>

</html>
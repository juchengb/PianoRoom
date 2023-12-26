<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>使用者 +Room 琴房預約系統</title>
<!-- head -->
<%@ include file="./include/head.jspf"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/frontend/css/cis.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/frontend/css/account.css">

</head>
<body>
	<!-- header -->
	<%@ include file="./include/header.jspf"%>


	<div class="mx-md-5 mx-3">
		<div class="container-fluid p-4 text-center">
			<!-- 個人檔案表單 -->
			<form class="container mt-4 bg-white" id="profile-form">
				<div class="w-75 pt-5 pb-4 text-start mx-auto">
					<h4 class="fw-bold mb-4">個人檔案</h4>
					<!-- 大頭貼預覽區域 -->
					<div id="avatar-preview" class="mb-3 text-center">
						<img src="${pageContext.request.contextPath}/mvc/profile/12345.png" alt="">
					</div>

					<!-- 大頭貼上傳區域 -->
					<div class="mb-3">
						<label for="avatar">上傳大頭貼</label>
						<input type="file" id="avatar" accept="image/*"
							   onchange="previewAvatar(this)">
					</div>

					<!-- 其他表單元素 -->
					<div class="mb-3">
						<label for="email">電子信箱（帳號）</label>
						<input type="email" id="email">
					</div>

					<div class="mb-3">
						<label for="name">姓名</label>
						<input type="text" id="name">
					</div>

					<div class="mb-3">
						<label for="major">主修</label>
						<input type="text" id="major">
					</div>

					<div class="w-100 d-flex justify-content-center">
						<button type="submit">更新</button>
					</div>
				</div>
			</form>

			<!-- 重設密碼表單 -->
			<form class="container mt-4 bg-white" id="password-form">
				<div class="w-75 pt-5 pb-4 text-start mx-auto">
					<h4 class="fw-bold mb-4">重設密碼</h4>

					<div class="mb-3">
						<label for="old-password">舊密碼</label>
						<input type="password" id="old-password">
					</div>

					<div class="mb-3">
						<label for="new-password">新密碼</label>
						<input type="password" id="new-password">
					</div>

					<div class="mb-3">
						<label for="confirm-password">再次輸入新密碼</label>
						<input type="password" id="confirm-password">
					</div>

					<div class="w-100 d-flex justify-content-center">
						<button type="submit">重設</button>
					</div>
				</div>
			</form>
		</div>
	</div>

</body>
<script>
	
</script>
</html>
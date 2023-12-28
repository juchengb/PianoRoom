<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>個人檔案 +Room 琴房預約系統</title>
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
		<div class="container-fluid p-md-4 p-2 text-center">
			<!-- preview profile -->
			<sp:form modelAttribute="updateUser" id="accountForm" method="post"
				  action="${pageContext.request.contextPath}/mvc/account" 
				  class="container bg-white">
				<div class="w-75 pt-5 pb-4 text-start mx-auto">
					<h4 class="fw-bold mb-4">個人檔案</h4>
					
					<!-- preview profile image -->
					<div id="imagePreview" class="mb-3 text-center">
						<img src="${pageContext.request.contextPath}/mvc/profile/default.png" alt="我的大頭貼">
					</div>

					<!-- profile image upload -->
					<div class="mb-3">
						<label for="image">上傳大頭貼</label>
						<sp:input type="file" path="image" accept="image/*"
							   onchange="previewImage(this)" />
					</div>

					<!-- else input -->
					<div class="mb-3">
						<label for="email">電子信箱（帳號）</label>
						<sp:input type="email" path="email" value="${user.email}" />
					</div>

					<div class="mb-3">
						<label for="name">姓名</label>
						<sp:input type="text" path="name" value="${user.name}" />
					</div>

					<div class="mb-3">
						<label for="major">主修</label>
						<sp:select path="majorId" type="text" value="${user.majorId}">
							<sp:options items="${majors}" itemLabel="majorName" itemValue="id"></sp:options>		   
						</sp:select>
					</div>
					
					<div class="w-100 d-flex justify-content-center" style="min-height: 1.5rem;">
						<sp:errors path="email" cssClass="text-danger text-nowrap"></sp:errors>
						<sp:errors path="name" cssClass="text-danger text-nowrap"></sp:errors>
						<sp:errors path="majorId" cssClass="text-danger text-nowrap"></sp:errors>
					</div>
					
					<div class="w-100 d-flex justify-content-center">
						<button type="submit">更新</button>
					</div>
				</div>
			</sp:form>

		</div>
	</div>

</body>
<script>
	
</script>
</html>
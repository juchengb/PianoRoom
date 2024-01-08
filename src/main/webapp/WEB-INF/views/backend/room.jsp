<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 後台-修改琴房</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/backend/room.css">
	</head>
	<body>
		<div class="d-flex w-100">
			<!-- sidebar -->
			<%@ include file="./include/sidebar.jspf" %>
			
			<!-- Page Content  -->
        	<div id="content" class="p-4 pb-0">
        		<header class="px-3">
	                <h4 class="fw-bold">修改琴房</h4>
	                <p class="text-end pe-4">副標題：xxxxxxxxxxxxxxxxxxxxxxxxxx</p>
	            </header>
				<div class="container-fluid p-md-4 p-2 text-center">
					<!-- preview img -->
					<sp:form modelAttribute="room" id="roomForm" method="post"
						  action="${pageContext.request.contextPath}/mvc/backend/update-room/${room.id}" 
						  cssClass="container bg-white"
						  enctype="multipart/form-data">
						  
						<div class="w-75 pt-5 pb-4 text-start mx-auto">
							<h4 class="fw-bold mb-4">琴房檔案</h4>
							
							<!-- preview room image -->
							<div id="imagePreview" class="mb-3 text-center border">
								<img src="${pageContext.request.contextPath}/mvc/room-img/${room.image}" alt="琴房照片">
							</div>
		
							<!-- room image upload -->
							<div class="mb-3">
								<label for="image">上傳琴房照片</label>
								<sp:input type="file" path="image" accept=".jpg, .jpeg, .png, .gif" />
							</div>
							
							${room}
							<!-- else input -->
							<div class="mb-3">
								<label for="name">名稱</label>
								<sp:input type="text" path="name" value="${room.name}" />
							</div>
		
							<div class="mb-3">
								<label for="dist">校區</label>
								<sp:input type="text" path="dist" value="${room.dist}" />
							</div>
		
							<div class="mb-3">
								<label for="type">類型</label>
								<sp:input type="text" path="type" value="${room.type}" />
							</div>
							
							<div class="mb-3">
								<label for="latitude">經度</label>
								<sp:input type="number" step="any" path="latitude" value="${room.latitude}" />
							</div>
							
							<div class="mb-3">
								<label for="longitude">緯度</label>
								<sp:input type="number" step="any" path="longitude" value="${room.longitude}" />
							</div>
							
							<div class="mb-3">
								<label for="businessHour">營業時間</label>
								<sp:input type="time" step="3600" path="businessHour" value="${room.businessHour[0].startTime}" />
							</div>
							
							
							<div class="w-100 d-flex justify-content-center" style="min-height: 1.5rem;">
							</div>
							
							<div class="w-100 d-flex justify-content-center">
								<button type="submit">更新</button>
							</div>
						</div>
					</sp:form>
				</div>
		</div>
	</body>
</html>
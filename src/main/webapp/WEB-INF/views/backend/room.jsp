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
	                <h4 class="fw-bold pt-3">修改琴房</h4>
	                <p class="text-end pe-4"></p>
	            </header>
				<div class="container-fluid p-md-4 p-2 text-center">
					<!-- preview img -->
					<sp:form modelAttribute="editRoom" id="roomForm" method="post"
						  action="${pageContext.request.contextPath}/mvc/backend/update-room/${room.id}" 
						  cssClass="container bg-white"
						  enctype="multipart/form-data">
						  
						<div class="w-75 pt-5 pb-4 text-start mx-auto">
							<h4 class="fw-bold mb-5 text-center">琴房檔案</h4>
							
							<!-- preview room image -->
							<div id="imagePreview" class="mb-5 text-center border">
								<img src="${pageContext.request.contextPath}/mvc/room-img/${room.image}" alt="琴房照片">
							</div>
		
							<!-- room image upload -->
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="image" class="col-2 text-center">琴房照片：</label>
								<sp:input type="file" path="image" accept=".jpg, .jpeg, .png, .gif" class="col-10 mb-0" />
							</div>
							
							<!-- else input -->
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="name" class="col-2 text-center">名稱：</label>
								<sp:input type="text" path="name" value="${room.name}" class="col-10 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="dist" class="col-2 text-center">校區：</label>
								<sp:input type="text" path="dist" value="${room.dist}" class="col-10 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="type" class="col-2 text-center">類型：</label>
								<sp:input type="text" path="type" value="${room.type}" class="col-10 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="latitude" class="col-2 text-center">經度：</label>
								<sp:input type="number" step="any" path="latitude" value="${room.latitude}" class="col-10 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="longitude" class="col-2 text-center">緯度：</label>
								<sp:input type="number" step="any" path="longitude" value="${room.longitude}" class="col-10 mb-0" />
							</div>			
							
							<div class="w-100 d-flex justify-content-center" style="min-height: 1.5rem;">
							</div>
							
							<div class="w-100 mb-3 d-flex justify-content-center">
								<button type="submit">更新</button>
							</div>
							
							${room}
						</div>
					</sp:form>
				</div>
		</div>
		</div>
	</body>
</html>
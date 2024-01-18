<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room (後臺) 修改琴房</title>
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
					
					<sp:form modelAttribute="updateRoom" id="roomForm" method="post"
						  action="${pageContext.request.contextPath}/mvc/backend/update-room/${room.id}" 
						  cssClass="container bg-white mb-5"
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
								<sp:input type="file" path="image" accept=".jpg, .jpeg, .png, .gif" cssClass="col-10 mb-0" />
							</div>
							
							<!-- else input -->
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="name" class="col-2 text-center">名稱：</label>
								<sp:input type="text" path="name" value="${room.name}" cssClass="col-10 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="dist" class="col-2 text-center">校區：</label>
								<sp:input type="text" path="dist" value="${room.dist}" cssClass="col-10 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="type" class="col-2 text-center">類型：</label>
								<sp:input type="text" path="type" value="${room.type}" cssClass="col-10 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="latitude" class="col-2 text-center">經度：</label>
								<sp:input type="number" step="any" path="latitude" value="${room.latitude}" cssClass="col-10 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="longitude" class="col-2 text-center">緯度：</label>
								<sp:input type="number" step="any" path="longitude" value="${room.longitude}" class="col-10 mb-0" />
							</div>			
							
							<div class="w-100 d-flex justify-content-center" style="min-height: 1.5rem;">
							</div>
							
							<div class="w-100 mb-3 d-flex justify-content-center">
								<button type="submit">修改</button>
							</div>
						</div>
					</sp:form>
					
					<form id="businessHourForm" class="container bg-white" method="post" 
						  action="${pageContext.request.contextPath}/mvc/backend/update-room/businesshour/${room.id}">
						  
						<div class="w-75 pt-5 pb-4 text-start mx-auto">
							<h4 class="fw-bold mb-5 text-center">營業時間</h4>							
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期一：</label>
								<input type="time" step="3600" name="openingTime" value="${room.businessHour[0].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[0].closingTime}" class="col-4 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期二：</label>
								<input type="time" name="openingTime" value="${room.businessHour[1].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[1].closingTime}" class="col-4 mb-0" />
							</div>
		
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期三：</label>
								<input type="time" name="openingTime" value="${room.businessHour[2].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[2].closingTime}" class="col-4 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期四：</label>
								<input type="time" name="openingTime" value="${room.businessHour[3].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[3].closingTime}" class="col-4 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期五：</label>
								<input type="time" name="openingTime" value="${room.businessHour[4].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[4].closingTime}" class="col-4 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期六：</label>
								<input type="time" name="openingTime" value="${room.businessHour[5].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[5].closingTime}" class="col-4 mb-0" />
							</div>
							
							<div class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期日：</label>
								<input type="time" name="openingTime" value="${room.businessHour[6].openingTime}" class="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<input type="time" name="closingTime" value="${room.businessHour[6].closingTime}" class="col-4 mb-0" />
							</div>
							
							
							<div class="w-100 d-flex justify-content-center" style="min-height: 1.5rem;">
							</div>
							
							<div class="w-100 mb-3 d-flex justify-content-center">
								<button type="submit">修改</button>
							</div>

						</div>
					</form>
				</div>
				
				<!-- footer -->
        		<%@ include file="../frontend/include/footer.jspf" %>
			</div>
		</div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room (後臺) 新增琴房</title>
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
	                <h4 class="fw-bold pt-3">新增琴房</h4>
	                <p class="text-end pe-4"></p>
	            </header>
				<div class="container-fluid p-md-4 p-2 text-center">
	
					<sp:form modelAttribute="addRoom" id="roomForm" method="post"
						action="${pageContext.request.contextPath}/mvc/backend/add-room"
						cssClass="container bg-white mb-5" enctype="multipart/form-data">
	
						<div class="w-75 pt-5 pb-4 text-start mx-auto">
							<h4 class="fw-bold mb-5 text-center">琴房檔案</h4>
	
							<!-- room image upload -->
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="image" class="col-2 text-center">琴房照片：</label>
								<sp:input type="file" path="image"
									accept=".jpg, .jpeg, .png, .gif" cssClass="col-10 mb-0" />
							</div>
	
							<!-- else input -->
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="name" class="col-2 text-center">名稱：</label>
								<sp:input type="text" path="name" cssClass="col-10 mb-0"
									required="required" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="dist" class="col-2 text-center">校區：</label>
								<sp:input type="text" path="dist" cssClass="col-10 mb-0"
									required="required" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="type" class="col-2 text-center">類型：</label>
								<sp:input type="text" path="type" cssClass="col-10 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="latitude" class="col-2 text-center">經度：</label>
								<sp:input type="number" step="any" path="latitude"
									cssClass="col-10 mb-0" required="required" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label for="longitude" class="col-2 text-center">緯度：</label>
								<sp:input type="number" step="any" path="longitude"
									class="col-10 mb-0" required="required" />
							</div>
	
							<div class="w-100 d-flex justify-content-center"
								style="min-height: 1.5rem;"></div>
	
							<h4 class="fw-bold mb-5 ms-3">營業時間</h4>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期一：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期二：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期三：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期四：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期五：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期六：</label>
								<sp:input type="time" step="3600" path="openingTime"
									cssClass="col-4 mb-0" />
								<span class="col-2 text-center">-</span>
								<sp:input type="time" step="3600" path="closingTime"
									cssClass="col-4 mb-0" />
							</div>
	
							<div
								class="mb-4 row d-flex align-items-center justify-content-center">
								<label class="col-2 text-center">星期日：</label> <input type="time"
									step="3600" name="openingTime" class="col-4 mb-0" /> <span
									class="col-2 text-center">-</span> <input type="time"
									step="3600" name="closingTime" class="col-4 mb-0" />
							</div>
	
	
							<div class="w-100 d-flex justify-content-center"
								style="min-height: 1.5rem;"></div>
	
							<div class="w-100 mb-3 d-flex justify-content-center">
								<button type="submit">新增</button>
							</div>
	
						</div>
					</sp:form>
				</div>

				<!-- footer -->
	        	<%@ include file="../frontend/include/footer.jspf" %>
	        </div>
		</div>
	</body>
</html>
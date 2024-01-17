<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 琴房預約系統</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/main.css">
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/check-gps.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>
		
		<div class="mx-md-5 mx-3">
			
			<div class="container-fluid p-4">
				<h4 class="fw-bold mb-4 text-center">首 頁</h4>
                <div class="row">
                    <div class="col-md-7 mb-3">
                    	<!-- check -->
                    	<div class="block h-100">
						    <h5 class="fw-bold">打卡專區</h5>
						    <div class="row d-flex align-items-center mt-3">
						        <div class="col-md-9">
						            下一個預約：
						            <div class="c-primary fw-bold fs-5">${nextReservation}</div>
						        </div>
						        <c:if test="${nextReservation != '查無預約，趕快去預約琴房'}">
							        <div class="col-md-3 d-flex">
							        	<c:choose>
								        	<c:when test="${btnStatus == 0}">
								            	<button id="mute-btn" class="mx-md-auto px-4 py-1 fw-bold text-nowrap bgc-gray" disabled>${btnWord}</button>
								        	</c:when>
								        	<c:when test="${btnStatus == 1}">
								            	<button id="check-btn" class="mx-md-auto px-4 py-1 fw-bold text-nowrap" data-status="${btnStatus}">${btnWord}</button>
								        	</c:when>
								        	<c:when test="${btnStatus == 2}">
								            	<button id="check-btn" class="mx-md-auto px-4 py-1 fw-bold text-nowrap" data-status="${btnStatus}">${btnWord}</button>
								        	</c:when>
							        	</c:choose>
							        </div>
						        </c:if>
						    </div>
						    <c:if test="${nextReservation != '查無預約，趕快去預約琴房'}">
								<p id="status" class="text-secondary mt-2 mb-0 text-md-end">*點擊按鈕以獲取位置並打卡簽到/退</p>
						    </c:if>
						</div>
                    </div>
                    
                    <!-- dashboard -->
                    <div class="col-md-5 mb-3">
                    	<div class="block h-100">
							<h5 class="fw-bold">本月累積</h5>
                    		<%@ include file="./include/dashboard.jspf" %>
                    	</div>
                    </div>
                </div>
           	</div>
			
			
			<!-- room status -->
			<section>
			    <h4 class="text-center fw-bold">目前琴使用狀況</h4>
			    <p class="text-end pe-5 mb-0">更新時間：${updateTime}</p>
			
			    <div class="card-container p-4 justify-content-around">
				    <c:forEach items="${roomStatusList}" var="roomStatus">
				        <div class="card overflow-hidden bg-white justify-content-end m-2">
				            <div class="row g-0">
				                <div class="col-9">
				                    <div class="card-content p-4 pb-2">
				                        <p>${roomStatus.dist}</p>
				                        <p>${roomStatus.type}</p>
				                        <h6 class="fw-bold">${roomStatus.name}</h6>
				                    </div>
				                </div>
				                <div class="col-3">
				                	<c:if test="${roomStatus.status == '空琴房'}">
					                    <div class="card-icon text-center p-3 d-flex align-items-center justify-content-center rounded-circle overflow-hidden"
					                    	 role="button"
					                    	 onclick="window.location.href='./reserve#room${roomStatus.id}'">
				                        	<i class="bi bi-cursor-fill fs-3 c-primary"></i>
				                    	</div>
			                    	</c:if>
				                </div>
				            </div>
				            <div class="card-footer card-empty text-white py-2 bgc-primary" data-status="${roomStatus.status}">${roomStatus.status}</div>
				        </div>
					</c:forEach>
			    </div>
			</section>
			
		</div>
	</body>
</html>
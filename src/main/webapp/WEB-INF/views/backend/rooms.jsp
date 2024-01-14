<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room (後臺) 管理琴房</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
	</head>
	<body>
		<div class="d-flex w-100">
			<!-- sidebar -->
			<%@ include file="./include/sidebar.jspf" %>
			
			<!-- Page Content  -->
        	<div id="content" class="p-4 pb-0">
        		<header class="px-3">
	                <h4 class="fw-bold pt-3">琴房管理</h4>
	                <div class="text-end w-100 pe-5 mb-3">
	                	<button class="text-center" onclick="location.href='${pageContext.request.contextPath}/mvc/backend/add-room'">新增琴房</button>
	                </div>
	            </header>
				
				<!-- rooms area -->
	            <div class="rooms-area w-100 d-flex flex-wrap overflow-auto">
					
					<!-- room -->
					<c:forEach items="${rooms}" var="room">
		                <div id="room${room.id}" class="rooms-row m-2 p-3">
		                    <button class="edit-btn d-flex border-0 p-0 position-absolute align-items-center justify-content-center"
		                    	onclick="window.location.href='./update-room/${room.id}'">
		                        <i class="bi bi-pencil-square"></i>
		                    </button>
		
		                    <div class="room-image overflow-hidden bg-black">
		                        <img src="${pageContext.request.contextPath}/mvc/room-img/${room.image}" class="w-100 h-100">
		                    </div>
		                    <h4 class="my-2">${room.name}</h4>
		                    <div><span class="">ID：</span>${room.id}</div>
		                    <div><span class="">校區：</span>${room.dist}</div>
		                    <div><span class="">類型：</span>${room.type}</div>
		                    <div><span class="">經度：</span>${room.latitude}</div>
		                    <div><span class="">緯度：</span>${room.longitude}</div>
		                    <div><span class="">營業時間：</span>
		                    	<span>星期一 ${room.businessHour[0].openingTime} - ${room.businessHour[0].closingTime}...</span>
		                    </div>
		                </div>
					</c:forEach>
					
				</div>
        	</div>
		</div>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 預約琴房</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/reserve.css">
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>
		
		<!-- table list-->
        <div class="mx-md-5 mx-3">
            <div class="container-fluid p-4">
                <h4 class="text-center fw-bold mb-4">預約琴房</h4>
                <p class="text-end">日期：${currentDate}</p>
                <c:forEach items="${rooms}" var="room">
	                <div id="room${room.id}" class="dashboard mb-5">
	                    <div class="item">
	                        <div class="col-2 col-lg-1">
	                        	<div class="img">
	                        		<img src="${pageContext.request.contextPath}/mvc/room-img/${room.image}" class="bgc-gray">
	                        	</div>
	                        </div>
                        	<div class="col-10 col-lg-11 content py-3 ps-lg-4">
	                            <h5 class="my-3">${room.dist} ${room.type} <span class="fw-bold">${room.name}</span></h5>
	                            <div class="d-flex flex-wrap">
	                            	<c:if test="${empty room.businessHourButtons}">
										<p>未開放，暫停預約</p>
									</c:if>
									<c:forEach var="hourButton" items="${room.businessHourButtons}">
										<button type="button" class="available-btn"
												onclick="location.href='${pageContext.request.contextPath}/mvc/reserve/${room.id}/${hourButton}'">${hourButton}</button>
									</c:forEach>
	                            </div>
	                        </div>
	                    </div>
	                </div>
                </c:forEach>
                
            </div>
        </div>
	</body>
</html>
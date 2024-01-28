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
            <div class="container-fluid pb-0 p-4">
                <h4 class="text-center fw-bold mb-4">預約琴房</h4>
                <p class="text-center c-primary fs-5">日期：${currentDate}</p>
                <div class="search p-3 mb-4">
                	<form method="post" action="${pageContext.request.contextPath}/mvc/reserve" class="row justify-content-center">
					  <div class="col-auto d-flex align-items-center w-25">
					    <label for="dist" class="w-25">校區</label>
					    <select class="bg-white" name="dist" id="distSelect">
					      <option value="all">所有琴房校區</option>
					      <c:forEach items="${dists}" var="dist">
					      	<option value="${dist}" ${ distSelectItem == dist ? 'selected' : ''}>${dist}</option>
					      </c:forEach>
						</select>
					  </div>
					  
					  <div class="col-auto d-flex align-items-center w-25">
					    <label for="type" class="w-25">類型</label>
					    <select class="bg-white" name="type" id="typeSelect">
					      <option value="all">所有琴房類型</option>
					      <c:forEach items="${types}" var="type">
					      	<option value="${type}" ${ typeSelectItem == type ? 'selected' : ''}>${type}</option>
					      </c:forEach>
						</select>
					  </div>
					  
					  <div class="col-auto d-flex align-items-center">
					    <button type="submit">搜尋</button>
					  </div>
					</form>
                </div>
                
                <c:forEach items="${rooms}" var="room">
	                <div id="room${room.id}" class="dashboard mb-5">
	                    <div class="item">
	                        <div class="col-2 col-lg-1">
	                        	<div class="img">
	                        		<img src="${pageContext.request.contextPath}/mvc/room-img/${room.image}" class="bgc-gray room-img">
	                        	</div>
	                        </div>
                        	<div class="col-10 col-lg-11 content py-3 ps-lg-4">
	                            <h5 class="my-3">${room.dist} ${room.type} <span class="fw-bold">${room.name}</span></h5>
	                            <div class="d-flex flex-wrap">
	                            	<c:if test="${empty room.reserveButtonList}">
										<p>未開放，暫停預約</p>
									</c:if>
									<c:forEach var="reserveButton" items="${room.reserveButtonList}">
											<button type="button" class="reserve-btn"
													onclick="location.href='${pageContext.request.contextPath}/mvc/reserve/${room.id}/${reserveButton.buttonString}'"
													data-status="${reserveButton.isBooked}"
													<c:if test="${reserveButton.isBooked == true}">disabled</c:if>
													>${reserveButton.buttonString}</button>
									</c:forEach>
	                            </div>
	                        </div>
	                    </div>
	                </div>
                </c:forEach>
            </div>
        </div>
        
        <!-- footer -->
        <%@ include file="./include/footer.jspf" %>
	</body>
</html>
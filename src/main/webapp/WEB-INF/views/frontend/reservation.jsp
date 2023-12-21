<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 預約琴房</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/reservation.css">
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/update-current-time.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>
		
		<!-- table list-->
        <div class="mx-md-5 mx-3">
            <div class="container-fluid p-4">
                <h4 class="text-center fw-bold mb-4">預約琴房</h4>
                <div id="room1" class="dashboard mb-5">
                    <div class="item">
                        <div class="col-md-2"><div class="img"></div></div>
                        <div class="col-10 content py-3">
                            <span class="">26 December 2019</span>
                            <div class="title mb-2 fw-bold">Lorem Ipsum Dolor</div>
                            <div class="text mb-4">Lorem ipsum dolor sit amet consectetur, adipisicing elit.
                                Recusandae voluptate repellendus magni illo ea animi? </div>
                            <button href="#" type="button" class="available-btn">07:00</button>
                            <button class="reserved-btn">08:00</button><button class="available-btn">09:00</button><button class="available-btn">10:00</button><button class="available-btn">11:00</button><button class="reserved-btn">12:00</button><button class="available-btn">13:00</button><button class="available-btn">14:00</button><button class="available-btn">15:00</button><button class="available-btn">16:00</button><button class="available-btn">17:00</button><button class="available-btn">18:00</button><button class="available-btn">19:00</button><button class="available-btn">20:00</button><button class="available-btn">21:00</button><button class="reserved-btn">22:00</button><button class="available-btn">23:00</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
	</body>
</html>
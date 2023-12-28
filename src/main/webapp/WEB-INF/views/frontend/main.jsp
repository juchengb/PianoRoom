<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    	<%@ include file="./include/nextCheck.jspf" %>
                    </div>
                    <div class="col-md-5 mb-3">
                    	<%@ include file="./include/dashboard.jspf" %>
                    </div>
                </div>
           	</div>
			
			
			<!-- 琴房狀況 -->
			<%@ include file="./include/roomStatus.jspf" %>
			
		</div>
	</body>
</html>
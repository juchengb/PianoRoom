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
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/update-current-time.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>
		
		<div class="mx-md-5 mx-3">
			<section>
				<div class="container-fluid p-4">
	                <div class="row">
	                    <div class="col-md-8 mb-3">
	                    	<%@ include file="./include/nextCheck.jspf" %>
	                    </div>
	                    <div class="col-md-4 mb-3">
	                        <div class="block h-100">
	                            <h5 class="fw-bold">練習累積</h5>
	                            <!-- Add content for the practice summary -->
	                        </div>
	                    </div>
	                </div>
            	</div>
			</section>
			
			<!-- 琴房狀況 -->
			<%@ include file="./include/roomStatus.jspf" %>
			
		</div>
	</body>
</html>
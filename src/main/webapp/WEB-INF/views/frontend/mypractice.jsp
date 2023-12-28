<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 我的練習</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/mypractice.css">
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/update-current-time.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>
		
		
        <div class="mx-md-5 mx-3">
        	<div class="container-fluid p-4">
                <h4 class="fw-bold mb-4 text-center">我的練習</h4>
                <!-- calendar -->

                <div class="row">
                    <div class="col-md-7 mb-3">
	                    <div class="block">
	                        <h5 class="fw-bold">練習月曆</h5>
	                        <div id="calendar"></div>
	                    </div>
	                </div>
                    
                    <div class="col-md-5 mb-3">
                    	<%@ include file="./include/dashboard.jspf" %>
                    </div>
                    
                </div>

                
                
                <!-- Ranking -->
        		<h4 class="text-center fw-bold mb-4">練習排行榜</h4>
        		<%@ include file="./include/ranking.jspf" %>
			</div>
        </div>
	</body>
</html>
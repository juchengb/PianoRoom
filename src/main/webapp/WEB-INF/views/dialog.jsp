<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 琴房預約系統</title>
		<!-- head -->
		<%@ include file="./frontend/include/head.jspf" %>
		
		<style>
	        .dialog {
	            width: 40%;
	            border-radius: 1.5rem;
	            box-shadow: .3rem .3rem .7rem rgba(0, 0, 0, .1);
	        }
	
	        .check-icon i{
	            font-size: 7.5rem;
	        }
	        
	        a:hover{
	        	color: white;
	        }
    	</style>
	</head>
	<body class="d-flex align-items-center justify-content-center">
	    <div class="dialog d-flex flex-column align-items-center bg-white p-5">
	        <h3>+Room 琴房預約系統</h3>
	        <div class="check-icon c-primary">
	            <i class="bi bi-check-circle-fill"></i>
	        </div>
	        <h2 class="my-2">${message}</h2>
	        <a class="my-3 bgc-gray border-0 button text-decoration-none" 
	        	    href="${pageContext.request.contextPath}/mvc${togourl}">${togobtn}</a>
	    </div>
	</body>
</html>
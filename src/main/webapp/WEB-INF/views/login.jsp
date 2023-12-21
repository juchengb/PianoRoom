<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>登入 +Room 琴房預約系統</title>
		<!-- head -->
		<%@ include file="./frontend/include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/login.css">
	</head>
	<body class="d-flex align-items-center">
	    <div class="container align-content-center row bg-white" id="container">
	        <div class="login w-50 h-100">
	            <sp:form modelAttribute="user" method="post"
	            		 action="${pageContext.request.contextPath}/mvc/auth/login"
	            	     class="d-flex align-items-center justify-content-center flex-column h-100">
	                <h1 class="mb-3">登入</h1>
	                <input type="email" id="email" name="email" placeholder="帳號（電子信箱）">
	                <input type="password" id="password" name="password" placeholder="密碼">
	                <!-- Button trigger modal -->
	                <a type="button" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#forgotten">
	                    忘記密碼
	                </a>
	                <div class="mt-3 text-danger" style="min-height: 1.5rem;">${loginMessage}</div>
	                <button class="mt-3" type="submit">登入</button>
	            </sp:form>
	        </div>
	
	        <div class="signup w-50 h-100">
	            <sp:form action="${pageContext.request.contextPath}/mvc/auth/signup" method="post"
	            	  class="d-flex align-items-center justify-content-center flex-column h-100">
	                <h1 class="mb-3">建立帳號</h1>
	                <input type="email" name="email" placeholder="電子信箱（帳號）">
					<input type="password" name="password" placeholder="密碼">
					<input type="text" name="name" placeholder="姓名">
					<input type="text" name="major" placeholder="主修">
	                <button class="mt-3" type="submit">註冊</button>
	            </sp:form>
	        </div>
	        <div class="overlay h-100 w-50 d-flex flex-column align-items-center justify-content-center">
	            <h2 class="text-white mb-4">加入一起練琴</h2>
	            <button class="ghost" id="sign">建立帳號</button>
	        </div>
	    </div>
	    
	    <!-- 忘記密碼 Modal -->
		<%@ include file="./frontend/include/forgottenmodel.jspf" %>
	</body>
	<script>
	    // 在 document ready 時執行相應的功能
	    $(document).ready(function () {
	        overlayMove();
	    });
    	// overlay 相關程式碼
        function overlayMove() {
            let io = true;
            let btn = $('#sign');
            let overlay = $('.overlay');
            let h2 = $('.overlay h2');

            btn.click(function () {
                if (io) {
                    h2.html('歡迎一起練琴');
                    btn.html('返回登入');
                    overlay.css('transition', '.6s');
                    overlay.addClass("move-to-left");
                } else {
                    h2.html('加入一起練琴');
                    btn.html('建立帳號');
                    overlay.removeClass("move-to-left");
                }
                io = !io;
            });
        }
	</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
	    	<!-- login -->
	        <div class="login w-50 h-100">
	            <sp:form  modelAttribute="loginUser"  method="post" action="${pageContext.request.contextPath}/mvc/auth/login"
            	      class="d-flex align-items-center justify-content-center flex-column h-100">
	                <h1 class="mb-3">登入</h1>
	                
	                <sp:input type="email" path="email" placeholder="帳號（電子信箱）"></sp:input>
	                <sp:errors path="email" cssClass="text-danger text-nowrap text-end" />
	                
	                <sp:input type="password" path="password" placeholder="密碼"></sp:input>
	                <sp:errors path="password" cssClass="text-danger text-nowrap text-end" />
	                
	                <!-- Button trigger modal -->
	                <a type="button" class="btn btn-link" data-bs-toggle="modal" data-bs-target="#forgotten">
	                    忘記密碼
	                </a>
	                <div class="mt-3 text-danger" style="min-height: 1.5rem;">${loginMessage}</div>
	                <button class="mt-3" type="submit">登入</button>
	            </sp:form>
	        </div>
			
			<!-- signup -->
	        <%@ include file="./frontend/include/signup.jspf" %>
	        
	        <!-- overlay -->
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
	    
	    function moveToLeft () {
	    	let btn = $('#sign');
	        let overlay = $('.overlay');
	        let h2 = $('.overlay h2');
	    	h2.html('歡迎一起練琴');
            btn.html('返回登入');
            overlay.css('transition', '.6s');
            overlay.addClass("move-to-left");
	    }
	    
	    function moveToRight() {
	    	let btn = $('#sign');
	        let overlay = $('.overlay');
	        let h2 = $('.overlay h2');
            h2.html('加入一起練琴');
            btn.html('建立帳號');
            overlay.removeClass("move-to-left");
	    }
	    
    	// overlay 相關程式碼
        function overlayMove() {
            let io = true;
            let btn = $('#sign');
            btn.click(function () {
                if (io) {
                	moveToLeft();
                } else {
                	moveToRight();
                }
                io = !io;
            });
            if(location.href.indexOf('signup') >=0) {
            	moveToLeft();
            	io = false;
            } else {
            	moveToRight();
            }
        }
	</script>
</html>
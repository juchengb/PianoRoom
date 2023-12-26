<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 我的預約</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/myreservation.css">
		
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/update-current-time.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf" %>

		<!-- list -->
        <div class="mx-md-5 mx-3">
            <div class="container-fluid p-4 text-center">
                <h4 class="fw-bold mb-4">我的預約</h4>
                				
               	<c:choose>
               		<c:when test="${show == 'future'}">
                   		<%@ include file="./include/futureReservations.jspf" %>
                   	</c:when>
                   	
                   	<c:when test="${show == 'past'}">
                   		<%@ include file="./include/pastReservations.jspf" %>
                   	</c:when>
                   	
                   	<c:otherwise>
                   		<p>沒有任何預約紀錄</p>
                   	</c:otherwise>
               	</c:choose>
            </div>
        </div>		
	</body>
	
	<script type="text/javascript">
		function showReservations(){
			$('#future').removeClass('btn-off');
			$('#past').addClass('btn-off');
			window.location.href='${pageContext.request.contextPath}/mvc/myreservation';
		}
		
		function showPastReservations(){
			window.location.href='${pageContext.request.contextPath}/mvc/myreservation/past';
			$('#past').removeClass('btn-off');
			$('#future').addClass('btn-off');
		}
		
		function deleteReservation(id){
			window.location.href='${pageContext.request.contextPath}/mvc/myreservation/delete?id=' + id;
		}
		
	</script>
</html>
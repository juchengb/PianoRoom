<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 我的預約</title>
		<!-- head -->
		<%@ include file="./include/head.jspf"%>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/myreservation.css">
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf"%>
	
		<!-- list -->
		<div class="mx-md-5 mx-3">
			<div class="container-fluid p-4 text-center">
				<h4 class="fw-bold mb-4">我的預約</h4>
	
				<div class="btn-group mb-3 justify-content-between" role="group"
					aria-label="Basic mixed styles example">
	
					<button type="button" id="future" class="me-1" onClick="showReservations()">預約紀錄</button>
					<button type="button" id="past" class="btn-off"
						onClick="showPastReservations()">歷史紀錄</button>
				</div>
	
				<c:choose>
					<c:when test="${show == 'future'}">
						<%@ include file="./include/futureReservations.jspf"%>
					</c:when>
	
					<c:when test="${show == 'future-none'}">
						<p>沒有新預約紀錄</p>
					</c:when>
	
					<c:when test="${show == 'past'}">
						<%@ include file="./include/pastReservations.jspf"%>
					</c:when>
	
					<c:when test="${show == 'past-none'}">
						<p>沒有歷史預約紀錄</p>
					</c:when>
	
	
				</c:choose>
			</div>
		</div>
		
		<!-- footer -->
	    <%@ include file="../frontend/include/footer.jspf" %>
	</body>
	
	<script type="text/javascript">
		$(document).ready(function () {
			showButton();
		});
		
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
		
		function showButton() {
		    if (location.href.indexOf('past') >= 0) {
		    	$('#past').removeClass('btn-off');
				$('#future').addClass('btn-off');
		    } else {
		    	$('#future').removeClass('btn-off');
				$('#past').addClass('btn-off');
		    }
		}
		
	    var cancellationData = {};

	    function prepareCancellation(id, date, startTime, endTime, roomInfo) {
	        // 存儲預約ID和相關資訊
	        cancellationData.id = id;
	        cancellationData.date = date;
	        cancellationData.startTime = startTime;
	        cancellationData.endTime = endTime;
	        cancellationData.roomInfo = roomInfo;

	        // 更新模態框內容
	        document.getElementById('cancellation-details').innerHTML =
	            '是否要刪除該筆預約？<br>日期：' + cancellationData.date +
	            '<br>開始時間：' + cancellationData.startTime +
	            '<br>結束時間：' + cancellationData.startTime +
	            '<br>琴房：' + cancellationData.roomInfo +
	            '<div class="c-accent">*資料刪除後無法復原<div>';
	    }

	    function deleteReservation() {
	        // 在這裡使用 cancellationData.id 傳給後端
	        var idToDelete = cancellationData.id;
	        window.location.href = '${pageContext.request.contextPath}/mvc/myreservation/delete?id=' + idToDelete;
	    }
	</script>
</html>
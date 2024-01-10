<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 後台-管理預約</title>
		<!-- head -->
		<%@ include file="./include/head.jspf" %>
		<script src="https://cdn.jsdelivr.net/npm/ag-grid-community/dist/ag-grid-community.min.js"></script>
	</head>
	<body>
		<div class="d-flex w-100">
			<!-- sidebar -->
			<%@ include file="./include/sidebar.jspf" %>
			
			<!-- Page Content  -->
        	<div id="content" class="p-4 pb-0">
        		<header class="px-3">
	                <h4 class="fw-bold pt-3">預約管理</h4>
	                <div class="text-end w-100 pe-5 mb-3">
	                	<button class="text-center" onclick="location.href='${pageContext.request.contextPath}/mvc/backend/add-user'">新增預約</button>
	                </div>
	            </header>
				
				<!-- reservations area -->
	            <div class="reservations-area w-100">
					<div id="reservationGrid" class="ag-theme-quartz" style="height: 500px"></div>
				</div>
        	</div>
		</div>
	</body>
	<script type="text/javascript">
		// Grid Options: Contains all of the grid configurations
		const gridOptions = {
			// Row Data: The data to be displayed.
	        rowData: [],
	        
	        columnDefs: [
	            {headerName: 'ID', field: 'id', width: 60},
	            {headerName: '預約者', field: 'user.name', width: 120, editable: true},
	            {headerName: '琴房名稱', field: 'room.name', width: 180},
	            {headerName: '琴房校區', field: 'room.dist', width: 180},
	            {headerName: '琴房類別', field: 'room.type', width: 180},
	            {headerName: '開始時間', field: 'startTime', width: 180},
	            {headerName: '結束時間', field: 'endTime', width: 180},
	            {headerName: '簽到時間', field: 'checkin', width: 180, editable: true},
	            {headerName: '簽退時間', field: 'checkout', width: 180, editable: true},
			],
		         
		};
		
		// Create Grid: Create new grid within the #reservationGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#reservationGrid'), gridOptions);
	
		
		fetch('/PianoRoom/mvc/backend/get-reservations')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data))
	</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room (後臺) 管理預約</title>
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
	                <div class="text-end w-100 pe-5 mb-3"></div>
	            </header>
				
				<!-- reservations area -->
	            <div class="reservations-area w-100">
					<div id="reservationGrid" class="ag-theme-quartz" style="height: 550px"></div>
				</div>
				
				<!-- footer -->
	        	<%@ include file="../frontend/include/footer.jspf" %>
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
	            {headerName: '預約者', field: 'user.name', width: 80},
	            {headerName: '琴房名稱', field: 'room.name', width: 110},
	            {headerName: '琴房校區', field: 'room.dist', width: 110},
	            {headerName: '琴房類別', field: 'room.type', width: 110},
	            {headerName: '開始時間', field: 'startTime', width: 185, editable: true},
	            {headerName: '結束時間', field: 'endTime', width: 185, editable: true},
	            {headerName: '簽到時間', field: 'checkin', width: 185, editable: true},
	            {headerName: '簽退時間', field: 'checkout', width: 185, editable: true},
	            {headerName: '刪除', field: 'button', width: 100, cellRenderer: params => {
	                let id = params.data.id;
	                return '<a class="update-button" href="#" ' +
	                		  'onclick="confirmAndRedirect(event, \'delete-reservation/' + id + '\')">刪除</a>';
	           }}
			],
			
			onCellValueChanged: function (params) {
				const changedData = Object.assign({}, params.data,
												  {[params.column.colId]: params.newValue});
				updateBackendData(changedData);
			}      
		};
		
		// Create Grid: Create new grid within the #reservationGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#reservationGrid'), gridOptions);
	
		
		fetch('/PianoRoom/mvc/backend/get-reservations')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data))
		  
		function updateBackendData(changedData) {
		    fetch('/PianoRoom/mvc/backend/update-reservation', {
		        method: 'POST',
		        headers: {
		            'Content-Type': 'application/json',
		        },
		        body: JSON.stringify(changedData),
		    })
		    .then(response => response.json())
		    .then((response) => {
		        if (response.success) {
		            alert('更新成功!');
		        } else {
		            alert('更新失敗!');
		        }
    		})
		}
		
		function confirmAndRedirect(event, url) {
			event.preventDefault();
			console.log(url);
			
			var isConfirmed = window.confirm('是否要刪除預約？');
			
			if (isConfirmed) {
			  window.location.href = url;
			}
		}
	</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 後台-管理使用者</title>
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
	                <h4 class="fw-bold pt-3">使用者管理</h4>
	                <div class="text-end w-100 pe-5 mb-3">
	                	<button class="text-center" onclick="location.href='${pageContext.request.contextPath}/mvc/backend/add-user'">新增使用者</button>
	                </div>
	            </header>
				
				<!-- users area -->
	            <div class="users-area w-100">
					<div id="userGrid" class="ag-theme-quartz" style="height: 500px"></div>
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
	            {headerName: '姓名', field: 'name', width: 120, editable: true},
	            {headerName: '信箱', field: 'email', width: 240, editable: true},
	            {headerName: '密碼', field: 'password', width: 240},
	            {headerName: '主修代碼', field: 'majorId', width: 120},
	            {headerName: '主修', field: 'major.major', width: 120, editable: true, cellEditor: "agSelectCellEditor", cellEditorParams: { values: ["鋼琴", "小提琴", "中提琴", "大提琴", "小號"]}},
	            {headerName: '權限等級', field: 'level', width: 120, filter: true},
	            {headerName: '大頭照', field: 'avator', width: 240},
			],
		         
		};
		
		// Create Grid: Create new grid within the #userGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#userGrid'), gridOptions);

		
		fetch('/PianoRoom/mvc/backend/get-users')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data))
	</script>
</html>
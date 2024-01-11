<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
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
	                <div class="text-end w-100 pe-5 mb-3">xxxx</div>
	            </header>
				
	            <!-- add user area -->
	        	<div class="w-100 mb-3">
					<sp:form modelAttribute="addUser" method="post" class="p-0 m-0" 
							 action="${pageContext.request.contextPath}/mvc/backend/add-user">
						
						<div class="row d-flex align-items-center">
							<sp:label path="name" class="col-1 fs-6">姓名:</sp:label>
							<div class="col-3"><sp:input path="major" type="text" class="form-control" /></div>
							
							<sp:label path="email" class="col-1 fs-6">信箱:</sp:label>
							<div class="col-3"><sp:input path="major" type="text" class="form-control" /></div>
							<sp:label path="password" class="col-1 fs-6">密碼:</sp:label>
							<div class="col-3"><sp:input path="major" type="text" class="form-control" /></div>
						</div>
						
						<div class="row d-flex align-items-center">
							<sp:label path="password" class="col-1 fs-6">密碼:</sp:label>
							<div class="col-3"><sp:input path="major" type="text" class="form-control" /></div>
							
							<sp:label path="majorId" class="col-1 fs-6">主修:</sp:label>
							<div class="col-3"><sp:input path="majorId" type="text" class="form-control" /></div>
							
							<sp:label path="level" class="col-1 fs-6">權限:</sp:label>
							<div class="col-3"><sp:input path="level" type="text" class="form-control col-8" /></div>
						</div>
						
						<div class="row d-flex align-items-center">
							<sp:label path="avator" class="col-1 fs-6">大頭照:</sp:label>
							<div class="col-4"><sp:input path="avator" type="text" class="form-control col-8" /></div>
							
							<div class="col-7 text-end"><button type="submit">新增使用者</button></div>
						</div>

					</sp:form>
				</div>
					
				<!-- users list -->
				<div class="w-100">
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
	            {headerName: '主修', field: 'major.major', width: 120, editable: true, cellEditor: "agSelectCellEditor", cellEditorParams: { values: []}},
	            {headerName: '權限等級', field: 'level', width: 120, editable: true},
	            {headerName: '大頭照', field: 'avator', width: 280},
			],
		         
		};
		
		// Create Grid: Create new grid within the #userGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#userGrid'), gridOptions);

		
		fetch('/PianoRoom/mvc/backend/get-users')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data));
		  
		fetch('/PianoRoom/mvc/backend/get-majors')
		  .then(response => response.json())
		  .then(data => {
			  // 提取每個Major物件的major屬性
			  const majorValues = data.map(major => major.major);
			  gridOptions.columnDefs.find(colDef => colDef.field === 'major.major').cellEditorParams.values = majorValues;
	          
	          // Refresh the grid to apply changes
	          gridApi.refreshCells();
			  console.log(majorValues);
		  })
		  
	</script>
</html>
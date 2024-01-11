<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 後台-管理主修</title>
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
	                <h4 class="fw-bold pt-3">主修管理</h4>
	            </header>
	        	<!-- majors add & update area -->
	        	<div class="w-100">
					<sp:form modelAttribute="major" method="post" class="row d-flex align-items-center p-0 m-0" 
							 action="${pageContext.request.contextPath}/mvc/backend/add-major">
						
						<sp:label path="major" class="col-1 fs-6">主修名稱:</sp:label>
						<div class="col-4"><sp:input path="major" type="text" class="form-control" /></div>
						<div class="col-2"><button type="submit">新增</button></div>
					</sp:form>
				</div>
				
				<!-- majors list -->
				<div class="w-100">
					<div id="majorGrid" class="ag-theme-quartz w-50" style="height: 515px"></div>
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
	            {headerName: 'ID', field: 'id', width: 200},
	            {headerName: '主修名稱', field: 'major', width: 380},
			],
		         
		};
		
		// Create Grid: Create new grid within the #majorGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#majorGrid'), gridOptions);
	
		
		fetch('/PianoRoom/mvc/backend/get-majors')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data))

	</script>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sp" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room (後臺) 管理使用者</title>
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
	                <div class="text-end w-100 pe-5 mb-3"></div>
	            </header>
				
	            <!-- add user area -->
	        	<div class="w-100 mb-3">
					<sp:form modelAttribute="addUser" method="post" class="p-0 m-0" 
							 action="${pageContext.request.contextPath}/mvc/backend/add-user"
							 enctype="multipart/form-data">
						
						<div class="row d-flex align-items-center ">
							<sp:label path="name" class="col-1 fs-6 text-end">姓名:</sp:label>
							<div class="col-2"><sp:input type="text" path="name" class="form-control" required="reduired" /></div>
							
							<sp:label path="email" class="col-1 fs-6 text-end">信箱:</sp:label>
							<div class="col-3"><sp:input type="email" path="email" class="form-control" required="reduired" /></div>
							
							<sp:label path="password" class="col-1 fs-6 text-end">密碼:</sp:label>
							<div class="col-3"><sp:input type="password" path="password" class="form-control" required="reduired" /></div>
							
						</div>
						
						<div class="row d-flex align-items-center">
							<sp:label path="majorId" class="col-1 fs-6 text-end">主修:</sp:label>
							<div class="col-2">
								<sp:select path="majorId" type="text" value="" class="form-select">
									<sp:options items="${majors}" itemLabel="major" itemValue="id"></sp:options>		   
								</sp:select>
							</div>
							
							<sp:label path="avator" class="col-1 fs-6 text-end">大頭照:</sp:label>
							<div class="col-3"><sp:input type="file" path="avator" accept=".jpg, .jpeg, .png, .gif" class="form-control col-8" /></div>
							
							<sp:label path="level" class="col-1 fs-6 text-end" required="reduired">權限:</sp:label>
							<div class="col-1"><sp:input type="number" path="level" min="0" max="2" class="form-control col-8" /></div>
							
							
							<div class="col-2 text-end"><button type="submit">新增使用者</button></div>
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
	            {headerName: '主修', field: 'major.major', width: 120, editable: true, cellEditor: "agSelectCellEditor", 
	            	cellEditorParams: { values: []}},
	            {headerName: '權限等級', field: 'level', width: 120, editable: true},
	            {headerName: '大頭照', field: 'avator', width: 280},
			],
			
			onCellValueChanged: function (params) {

				const changedData = Object.assign({},
												  params.data,
							            		  {[params.column.colId]: params.newValue});
				updateBackendData(changedData);
			}
		         
		};
	
	
		// Create Grid: Create new grid within the #userGrid div, using the Grid Options object
		gridApi = agGrid.createGrid(document.querySelector('#userGrid'), gridOptions);
		
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
		
		fetch('/PianoRoom/mvc/backend/get-users')
		  .then(response => response.json())
		  .then((data) => gridApi.setGridOption('rowData', data));

		 
		function updateBackendData(changedData) {
		    fetch('/PianoRoom/mvc/backend/update-user', {
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
		 
	</script>
</html>
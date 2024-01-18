<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>+Room 我的練習</title>
		<!-- head -->
		<%@ include file="./include/head.jspf"%>
		
		<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/frontend/css/mypractice.css">
		
		<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/resources/frontend/js/practice-chart.js"></script>
	</head>
	<body>
		<!-- header -->
		<%@ include file="./include/header.jspf"%>
	
	
		<div class="mx-md-5 mx-3">
			<div class="container-fluid p-4">
				<h4 class="fw-bold mb-4 text-center">我的練習</h4>
				<!-- calendar -->
				<div class="row">
					<div class="col-md-7 mb-3">
						<div class="block h-100">
							<h5 class="fw-bold">本月練習履歷</h5>
							<div>
								<canvas id="myPracticeChart"></canvas>
							</div>
						</div>
					</div>
	
					<div class="col-md-5 mb-3">
						<div class="block h-100">
							<h5 class="fw-bold mb-4">本月累積</h5>
							<%@ include file="./include/dashboard.jspf"%>
							<div class="h-50 mt-4 d-none d-lg-block">
								<div class="row fs-1"><i class="bi bi-quote c-gray"></i></div>
								<div class="row justify-content-center fs-2 px-3">台上十分鐘，台下十年功</div>
								<div class="row fs-1 text-end"><i class="bi bi-quote c-gray"></i></div>
							</div>
						</div>
					</div>
				</div>
	

				<!-- Ranking -->
				<h4 class="text-center fw-bold my-4">練習排行榜</h4>
				<div class="container-fluid px4">
					<div class="list p-5 pt-0">
						<div class="row d-flex mb-4 fw-bold">
							<div class="col-2 col-md-1"></div>
							<div class="col-1 text-end">排名</div>
							<div class="col-1 text-center">主修</div>
							<div class="col-5 ps-4">使用者</div>
							<div class="col-4 pe-4 text-end">本月練習分鐘數</div>
						</div>
						
						<c:forEach items="${rankingUsers}" var="user">
							<div class="row item d-flex bg-white p-4 mb-4">
								<div class="col-2 col-md-1">
									<img src="${pageContext.request.contextPath}/mvc/profile/${user.avator}" class="pic rounded-circle">
								</div>
								<div class="pos col-1 text-center">${user.ranking}</div>
								<div class="major col-1">${user.major.major}</div>
								<div class="name col-7 col-md-8">${user.name}</div>
								<div class="count col-1">${user.minutes}</div>
							</div>
						</c:forEach>
						
					</div>
				</div>
				
				
			</div>
		</div>
		
		<!-- footer -->
	    <%@ include file="../frontend/include/footer.jspf" %>
	</body>
	
	<script>
		const ctx = document.getElementById('myPracticeChart');
		
		fetch('${pageContext.request.contextPath}/mvc/mypractice/chartdatas')
		  .then(response => response.json())
		  .then(data => {
		    new Chart(ctx, {
		      type: 'bar',
		      data: {
		        labels: data.labels,
		        datasets: [{
		          label: 'mins',
		          data: data.datas,
		          borderWidth: .8,
		          backgroundColor: '#6f42c1',
		          borderRadius: 3
		        }]
		      },
		      options: {
		        responsive: true,
		        scales: {
		          y: {
		            beginAtZero: true
		          }
		        }
		      }
		    });
		  })
		  .catch(error => console.error('Error fetching chart data:', error));
	</script>
</html>
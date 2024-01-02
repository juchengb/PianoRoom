<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
						<h5 class="fw-bold">練習履歷</h5>
						<div>
							<canvas id="myPracticeChart"></canvas>
						</div>
					</div>
				</div>

				<div class="col-md-5 mb-3">
					<div class="block h-100">
						<h5 class="fw-bold">本月累積</h5>
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
			<%@ include file="./include/ranking.jspf"%>
		</div>
	</div>
</body>
</html>
<script>
	const ctx = document.getElementById('myPracticeChart');
	
	fetch('https://localhost:8443/PianoRoom/mvc/mypractice/chartdatas')
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
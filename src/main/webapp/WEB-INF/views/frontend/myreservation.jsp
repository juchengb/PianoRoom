<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                <div class="btn-group mb-3 justify-content-between" role="group" aria-label="Basic mixed styles example">
                    <button type="button" class="btn-on me-1">預約紀錄</button>
                    <button type="button" class="btn-off">歷史紀錄</button>
                </div>
                <table class="table table-bordered caption-top text-center align-middle">
                    <!-- <caption class="text-center fw-bold mb-4 fs-4">我的預約</caption> -->
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <!-- <th scope="col">使用者</th>-->
                            <th scope="col">琴房名稱</th>
                            <th scope="col">日期</th>
                            <th scope="col">開始時間</th>
                            <th scope="col">結束時間</th>
                            <th scope="col">修改預約</th>
                            <th scope="col">取消預約</th>
                        </tr>
                    </thead>
                    <tbody >
                        <tr>
                            <th scope="row">1</th>
                            <!--<td>Mark</td>-->
                            <td>台北大學507</td>
                            <td>2023/12/14</td>
                            <td>9:00</td>
                            <td>10:00</td>
                            <td><button class="mod-btn">修改</button></td>
                            <td><button class="del-btn">取消</button></td>
                        </tr>
                        <tr>
                            <th scope="row">2</th>
                            <!--<td>Mark</td>-->
                            <td>台北大學508</td>
                            <td>2023/12/15</td>
                            <td>12:00</td>
                            <td>13:00</td>
                            <td><button class="mod-btn">修改</button></td>
                            <td><button class="del-btn">取消</button></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>		
	</body>
</html>
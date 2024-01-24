// DOM 載入後執行
$(document).ready(function () {
    // 獲取元素
    let btn = $('#check-btn');
    let contextPath = '${pageContext.request.contextPath}';
    console.log(contextPath);

    // 綁定按鈕點擊事件
    btn.on('click', function () {
        // 檢查瀏覽器是否支持GPS
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(function (position) {
                let curLat = position.coords.latitude;
                let curLng = position.coords.longitude;
                fetchLocationData(curLat, curLng)
            });
        } else {
            console.error("無法取得位置資訊，請開啟手機的網頁定位");
            alert("無法取得位置資訊，請開啟手機的網頁定位");
        }
    });

    // fetch 
    function fetchLocationData(curLat, curLng) {
        return fetch('/PianoRoom/mvc/main/location')
            .then(response => response.json())
            .then(data => {
				let id = data.reservationId;
				// 處理位置數據
                let tarLat = data.targetLat; // 目標緯度
        		let tarLng = data.targetLng; // 目標經度

                let distanceThreshold = 10; // 距離閾值（單位：度）
                let distance = calculateDistance(curLat, curLng, tarLat, tarLng);

                if (distance <= distanceThreshold) {
					if (btn.data('status')==1){
	                    // 簽到邏輯
	                    let checkInTime = new Date().toLocaleTimeString();
						alert("簽到成功！時間：" + checkInTime);
    	                window.location.href = '/PianoRoom/mvc/main/checkin/' + id;
					} else {
						// 簽退邏輯
						let checkOutTime = new Date().toLocaleTimeString();
						alert("簽退成功！時間：" + checkOutTime);
    	                window.location.href = '/PianoRoom/mvc/main/checkout/' + id;
					}
                } else {
                    alert("離目標位置太遠，無法簽到！");
                    console.log(curLat, curLng, tarLat, tarLng);
                }
			})
			.catch(error => {
           		console.error("無法取得目標位置：", error);
				alert("無法取得目標位置。");
				console.log(curLat, curLng, tarLat, tarLng);
            });
            	
    }

    // 計算兩點之間的距離（球面上的直線距離）
    function calculateDistance(curLat, curLng, tarLat, tarLng) {
        let R = 6371; // 地球半徑（單位：公里）
        let dLat = deg2rad(tarLat - curLat);
        let dLng = deg2rad(tarLng - curLng);
        let a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(curLat)) * Math.cos(deg2rad(tarLat)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
        let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        let distance = R * c;
        return distance;
    }

    function deg2rad(deg) {
        return deg * (Math.PI / 180);
    }

});
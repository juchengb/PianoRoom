// DOM 載入後執行
$(document).ready(function () {
    // 獲取元素
    let btn = $('#check-btn');
    let status = $('#status');

    // 綁定按鈕點擊事件
    btn.on('click', function () {
        // 檢查瀏覽器是否支持GPS
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition(function (position) {
                let curLat = position.coords.latitude;
                let curLng = position.coords.longitude;

            // 特定定位-台北大學(25.057989266580385,121.54228600285994)
            let targetLat = 5.057989266580385; // 目標緯度
            let targetLng = 121.54228600285994; // 目標經度
            let distanceThreshold = 0.1; // 距離閾值（單位：度）

            let distance = calculateDistance(curLat, curLng, targetLat, targetLng);

            if (distance <= distanceThreshold) {
                alert("簽到成功！");
                // 更新狀態內容
                status.textContent = "簽到成功，時間：" + formattedDate(new Date());
            } else {
                alert("離目標位置太遠，無法簽到！");
                }
            }, function (error) {
                console.error("獲取位置失敗：", error);
                alert("無法取得位置資訊，請開啟手機的網頁定位");
            });
        } else {
            console.error("無法取得位置資訊，請開啟手機的網頁定位");
            alert("無法取得位置資訊，請開啟手機的網頁定位");
        }
    });

    // 計算兩點之間的距離（球面上的直線距離）
    function calculateDistance(lat1, lon1, lat2, lon2) {
        let R = 6371; // 地球半徑（單位：公里）
        let dLat = deg2rad(lat2 - lat1);
        let dLng = deg2rad(lon2 - lon1);
        let a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
        let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        let distance = R * c;
        return distance;
    }

    function deg2rad(deg) {
        return deg * (Math.PI / 180);
    }

    function formattedDate(date){
            let month = date.getMonth() + 1; // 月份是從 0 開始的，所以要加 1
            let day = date.getDate();
            let hours = date.getHours();
            let minutes = date.getMinutes();

            // 格式化時間
            return month + "/" + day + " " + hours + ":" + minutes
    }
})
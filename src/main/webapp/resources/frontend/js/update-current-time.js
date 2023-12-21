$(document).ready(function () {
    function updateCurrentTime() {
        let element = $('#currentTime');
        let currentTime = new Date();
        let format = {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            weekday: 'narrow',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false,
            timeZone: 'Asia/Taipei'
        };

        let formattedTime = currentTime.toLocaleString('zh-TW', format);
        element.text('現在時間：' + formattedTime);
    }

    // 每秒更新一次
    setInterval(updateCurrentTime, 1000);
    updateCurrentTime();
});
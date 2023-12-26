$(document).ready(function() {
	$("#signup_form").submit(function(event) {
		event.preventDefault();

		let formData = {
			'email': $('#email').val(),
			'password': $('#password').val(),
			'majorId': $('#majorId').val()
		}

		var xhr = new XMLHttpRequest();
        xhr.open("POST", "./mvc/auth/signup", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

        xhr.onreadystatechange = function() {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    // 註冊成功
                    alert("註冊成功！");
                } else {
                    // 處理錯誤訊息
                    alert("註冊失敗：" + xhr.responseText);
                }
            }
        };

        xhr.send(formData);
    });
});
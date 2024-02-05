+Room 琴房預約系統
===

** ➤ 專題介紹 Description **
+Room 琴房預約系統旨在改善傳統繁瑣的預約流程，提供使用者更輕鬆快速的預約體驗，除了預約功能，系統也能即時查看琴房使用狀況；同時記錄所有使用者的練習時間，藉此激勵及讓使用者能更專注於練習。

** ➤ 系統架構 System Architecture **
![System Architecture](https://raw.githubusercontent.com/juchengb/PianoRoom/master/doc/SystemArchitecture.png)

** ➤ 系統特色 System features **
- **一鍵快速預約**：將琴房營業時間以一小時為單位切分為一個個時段按鈕，以利使用者快速預約。
- **地理位置打卡**：使用 HTML5 Geolocation API 取得使用者地理位置，並與琴房地理位置比對距離，實現打卡管理。
- **善用數據呈現**：善用資料庫數據比對，呈現多樣資訊給使用者，包含：目前琴房使用狀況、使用者當月練習數據及所有使用者練習排行榜。


** ➤ 系統頁面與功能 Pages and Functions **
![Pages and Functions](https://raw.githubusercontent.com/juchengb/PianoRoom/master/doc/PagesAndFunctions.png)



** ➤ 注意事項 Warning **

-  **java.base does not "opens java.time" to unnamed module**

	發生問題: Unable to make private java.time.LocalDateTime(java.time.LocalDate,java.time.LocalTime) accessible: module java.base does not "opens java.time" to unnamed module @18078bef
	
	(https://github.com/spring-projects/spring-data-mongodb/issues/3893)
	如何解決: 在 run configurations 中 VM Arguments 添加 --add-opens java.base/java.time=ALL-UNNAMED
	
-  **Javamail Could not convert socket to TLS GMail**
	
	Make sure antivirus program isn't interfering and be sure to add an exclusion to your firewall.

+Room 琴房預約系統
===
[+Room 網址連結](http://15.152.37.175:8080/PianoRoom/mvc/auth/login "佈署於 AWS EC2")

** ➤ 專題介紹 Description **
+Room 琴房預約系統旨在改善傳統繁瑣的預約流程，提供使用者更輕鬆快速的預約體驗，除了預約功能，系統也能即時查看琴房使用狀況；同時記錄所有使用者的練習時間，藉此激勵及讓使用者能更專注於練習。

** ➤ 系統架構 System Architecture **
![System Architecture](https://github.com/juchengb/PianoRoom/tree/master/doc/SystemArchitecture.png)

** ➤ 注意事項 Warning **
-  **java.base does not "opens java.time" to unnamed module**


	發生問題: Unable to make private java.time.LocalDateTime(java.time.LocalDate,java.time.LocalTime) accessible: module java.base does not "opens java.time" to unnamed module @18078bef
	
	(https://github.com/spring-projects/spring-data-mongodb/issues/3893)
	如何解決: 在 run configurations 中 VM Arguments 添加 --add-opens java.base/java.time=ALL-UNNAMED
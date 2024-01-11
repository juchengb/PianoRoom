
1. **java.base does not "opens java.time" to unnamed module**


	發生問題: Unable to make private java.time.LocalDateTime(java.time.LocalDate,java.time.LocalTime) accessible: module java.base does not "opens java.time" to unnamed module @18078bef
	
	(https://github.com/spring-projects/spring-data-mongodb/issues/3893)
	如何解決: 在 run configurations 中 VM Arguments 添加 --add-opens java.base/java.time=ALL-UNNAMED
	
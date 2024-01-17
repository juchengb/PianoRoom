package mvc.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * LoggingAspect 是一個切面（Aspect），用於記錄方法的輸入參數。
 * 主要攔截 package mvc.controller 中所有 class 的所有方法，並在方法執行前輸出方法名稱和參數。
 */
@Aspect
@Component
public class LoggingAspect {
	
	/**
	 * logMethodParams 方法
	 * 使用 @Before 通知，在攔截目標方法執行前執行，獲取方法的名稱和參數，並輸出至控制台。
	 * @param joinPoint JoinPoint 提供了有關目標方法執行的信息，如方法名、參數等。
	 */
	@Before("execution(* mvc.controller.*.*(..))")
	public void logMethodParams(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		Object[] args = joinPoint.getArgs();
		System.out.printf("調用方法：%s 參數：%s%n", methodName, Arrays.toString(args));
	}
	
}

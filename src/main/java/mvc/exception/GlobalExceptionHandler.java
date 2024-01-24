package mvc.exception;


import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Controller 全局 AOP 
/**
 * @ControllerAdvice 是 Spring 框架中非常有用的註解，允許通過集中的方式處理整個應用程序的異常、綁定數據以及在控制器（Controllers）上進行前置處理。
 * 可以被視為針對 Spring 控制器（Controllers）的一種面向切面編程（AOP）機制，提供了集中式方式來處理跨多個控制器的橫切關注點（cross-cutting concerns）。
 * 
 * 全局異常處理：通過 @ExceptionHandler 註解在 @ControllerAdvice 註解的類中處理異常。可以在一個地方定義所有控制器的異常處理邏輯，而不需要在每個控制器中個別定義。
 * */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex, Model model) {
        model.addAttribute("errorMessage", "名稱重複");
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "其他錯誤:" + ex);
        return "error";
    }
}
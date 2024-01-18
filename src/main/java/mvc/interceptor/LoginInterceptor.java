package mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mvc.model.po.User;

/**
 * LoginInterceptor 是一個實作 Spring HandlerInterceptor 的攔截器，用於執行登入驗證和路徑權限檢查。
 * 在請求被處理之前，該攔截器會檢查 Session 中是否包含用戶物件，以確保用戶已經登入。
 * 若使用者已登入，則進一步檢查路徑權限，限制後臺路徑；使用者未登入，則導向登入頁面。
 */
public class LoginInterceptor implements HandlerInterceptor{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
     * preHandle 在實際處理請求之前執行登入驗證和路徑權限檢查。
     *
     * @param request HttpServletRequest 封裝 client 端發送的請求
     * @param response HttpServletResponse 封裝 server發送的 response
     * @param handler 被處理的 Handler
     * @return 滿足登入驗證和路徑權限檢查，返回 true；反之則返回 false
     * @throws Exception
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		// 檢查 session 中是否有使用者的物件資料 (意味著使用者已經登入)
		if(session.getAttribute("user") != null) {
			User user = (User)session.getAttribute("user");
			// 路徑權限檢查
			// "/mvc/backend"路徑，使用者權限為 1 才可進入
			logger.info("RequestURI = " + request.getRequestURI());
			if(request.getRequestURI().contains("/mvc/backend")) { // 後臺
				logger.info(user.toString());
				if(user.getLevel() == 1) {
					return true; // 放行後臺路徑
				} else {
					// 權限不足，導向前臺首頁
					response.sendRedirect(request.getServletContext().getContextPath() + "/mvc/main");
					return false; // 不放行後臺路徑
				}
			} else {
				return true; // 放行前臺路徑
			}
		}
		// 未登入, 導入到登入頁面
		System.out.println(request.getRequestURI() + "被我擋掉啦！");
		response.sendRedirect(request.getServletContext().getContextPath() + "/mvc/auth/login");
		return false; // 不放行
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

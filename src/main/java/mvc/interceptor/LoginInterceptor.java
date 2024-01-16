package mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import mvc.model.po.User;

public class LoginInterceptor implements HandlerInterceptor{

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		// 檢查 session 中是否有 user 的物件資料(意味著用戶已經登入)
		if(session.getAttribute("user") != null) {
			User user = (User)session.getAttribute("user");
			// 路徑的權限檢查
			// "/mvc/backend", user level = 1 才可以進入
			logger.info("RequestURI = " + request.getRequestURI());
			if(request.getRequestURI().contains("/mvc/backend")) { // 後台
				logger.info(user.toString());
				if(user.getLevel() == 1) {
					return true; // 放行後臺路徑
				} else {
					response.sendRedirect(request.getServletContext().getContextPath() + "/mvc/auth/login");
					return false; // 不放行後臺路徑
				}
			} else {
				return true; // 放行前臺路徑
			}
		}
		// 未登入, 導入到登入頁面
		response.sendRedirect(request.getServletContext().getContextPath() + "/mvc/auth/login");
		return false; // 不放行
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		
	}

}

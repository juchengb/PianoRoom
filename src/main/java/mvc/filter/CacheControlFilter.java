package mvc.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * CacheControlFilter 是一個實作緩存控制的過濾器，繼承自 HttpFilter 並覆寫 doFilter 方法。
 * 用於每次 HTTP 請求時，向客戶端發送緩存控制相關的 HTTP Response header，確保 client 端不使用快取，並總是向 server 請求最新數據。
 */
@WebFilter("/*")
public class CacheControlFilter extends HttpFilter{
	
	/**
     * 覆寫 doFilter 方法，實現緩存控制。
     *
     * @param request HttpServletRequest 封裝 client 端發送的請求
     * @param response HttpServletResponse 封裝 server 發送的 response
     * @param chain FilterChain 提供對過濾器鏈中下一個過濾器訪問
     * @throws IOException
     * @throws ServletException Servlet 相關錯誤
     */
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 設定緩存控制相關的 HTTP Response header
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        // 繼續執行下一個過濾器
        chain.doFilter(request, response);
	}
	
}
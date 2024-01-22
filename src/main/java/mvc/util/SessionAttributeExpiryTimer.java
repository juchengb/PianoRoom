package mvc.util;

import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpSession;

/**
 * SessionAttributeExpiryTimer 是一個用於在 HttpSession 中設定屬性並定時到期的工具類別。
 */
public class SessionAttributeExpiryTimer {
	
	/**
     * 在 HttpSession 中設定具有特定到期時間的屬性。
     *
     * @param session HttpSession
     * @param attributeName 欲設定的屬性名稱
     * @param attributeValue 欲設定的屬性值
     * @param expiryTimeInMilliseconds 到期時間（以毫秒為單位）
     */
    public static void setAttributeWithExpiry(HttpSession session, String attributeName, Object attributeValue, long expiryTimeInMilliseconds) {
        // 設定屬性到 session
        session.setAttribute(attributeName, attributeValue);

        // 啟動計時器，10 分鐘後執行任務
        Timer timer = new Timer(true);
        timer.schedule(new SessionAttributeExpiryTask(session, attributeName), expiryTimeInMilliseconds);
    }
    
    /**
     * SessionAttributeExpiryTask 是 TimerTask 的子類別，用於在指定時間後從 HttpSession 中移除特定屬性。
     */
    private static class SessionAttributeExpiryTask extends TimerTask {
        private HttpSession session;
        private String attributeName;
        
        /**
         * SessionAttributeExpiryTask 的建構子，初始化 HttpSession 及屬性名稱。
         *
         * @param session HttpSession
         * @param attributeName 欲移除的屬性名稱
         */
        public SessionAttributeExpiryTask(HttpSession session, String attributeName) {
            this.session = session;
            this.attributeName = attributeName;
        }
        
        /**
         * 在執行時，移除 HttpSession 中指定屬性。
         */
        @Override
        public void run() {
            // 移除 session 中的屬性
            session.removeAttribute(attributeName);
        }
    }
}


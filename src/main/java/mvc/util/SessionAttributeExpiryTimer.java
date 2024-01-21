package mvc.util;

import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpSession;

public class SessionAttributeExpiryTimer {

    public static void setAttributeWithExpiry(HttpSession session, String attributeName, Object attributeValue, long expiryTimeInMilliseconds) {
        // 設定屬性到 session
        session.setAttribute(attributeName, attributeValue);

        // 啟動計時器，10分鐘後執行任務
        Timer timer = new Timer(true);
        timer.schedule(new SessionAttributeExpiryTask(session, attributeName), expiryTimeInMilliseconds);
    }

    private static class SessionAttributeExpiryTask extends TimerTask {
        private HttpSession session;
        private String attributeName;

        public SessionAttributeExpiryTask(HttpSession session, String attributeName) {
            this.session = session;
            this.attributeName = attributeName;
        }

        @Override
        public void run() {
            // 移除 session 中的屬性
            session.removeAttribute(attributeName);
        }
    }
}


package mvc.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class KeyUtil {
	
	/**
     * 提供從 context.xml 或 web.xml 中獲得金鑰的功能。
     * 
     * 從 context.xml 的 <Environment name="secretKey"> 或 web.xml 的 <env-entry> 中取得金鑰。
     *
     * @return 從配置中獲得金鑰對應的 String
     * @throws RuntimeException 當無法從上下文中獲取的金鑰時拋出例外
     */
    public static String getSecretKey() {
        try {
            // 1. 建立 InitialContext 物件，代表 JNDI 查詢的起始點。
            Context initialContext = new InitialContext();
            
            // 2. 對 "java:/comp/env" 進行查詢。此 JNDI 上下文用於 Java EE 中查詢程式的環境條目和資源引用。
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            
            // 3. 取得 context.xml (在 server ) 或 web.xml 中名為 "secretKey" 資源的值。
            // 對應到 web.xml：<env-entry-name>secretKey</env-entry-name>
            //       context.xml：<Environment name="secretKey" ...
            String secretKey = (String) envContext.lookup("secretKey");

            return secretKey;
        } catch (NamingException e) {
            throw new RuntimeException("從上下文中獲取金鑰時出錯", e);
        }
    }
    
    /**
     * 使用 AES 密鑰加密訊息。
     * 
     * @param aesKey AES密鑰，用於加密。
     * @param message 欲加密的訊息
     * @return 加密後的字節數據
     * @throws Exception 加密過程中發生的錯誤
     */
    public static byte[] encryptWithAESKey(SecretKey aesKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(message.getBytes());
    }
    
    /**
     * 使用 AES 密鑰解密訊息。
     * 
     * @param aesKey AES密鑰，用於解密。
     * @param encryptedData 已加密的訊息
     * @return 解密後的字串
     * @throws Exception 解密過程中發生的錯誤
     */
    public static String decryptWithAESKey(SecretKey aesKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(encryptedData));
    }
}
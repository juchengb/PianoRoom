package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class KeyUtil {
	
	/**
     * 提供從 context.xml 或 web.xml 中獲取金鑰的功能。
     * 
     * 從 context.xml 的 <Environment name="secretKey"> 中取得金鑰。
     * 或 web.xml 的 <env-entry> 中取得金鑰。
     *
     * @return 從配置中獲取的金鑰對應的 String
     * @throws RuntimeException 當無法從上下文中獲取的金鑰時拋出
     */
    public static String getSecretKey() {
        try {
            // 1. 建立InitialContext物件，它代表JNDI查找的起始點。
            Context initialContext = new InitialContext();
            
            // 2. 對"java:/comp/env"進行查找。此JNDI上下文用於Java EE中查找應用程序的環境條目和資源引用。
            Context envContext = (Context) initialContext.lookup("java:/comp/env");
            
            // 3. 取得 context.xml(在server) 或 web.xml中名為 "secretKey" 的資源的值。
            // 對應到 web.xml：<env-entry-name>secretKey</env-entry-name>
            //       context.xml：<Environment name="secretKey" ...
            String secretKey = (String) envContext.lookup("secretKey");

            return secretKey;
        } catch (NamingException e) {
            throw new RuntimeException("從上下文中獲取金鑰時出錯", e);
        }
    }
    
    /**
     * 使用AES密鑰加密訊息。
     * 
     * @param aesKey  AES密鑰，用於加密。
     * @param message 待加密的訊息。
     * @return 返回加密後的字節數據。
     * @throws Exception 若加密過程中發生錯誤時拋出。
     */
    public static byte[] encryptWithAESKey(SecretKey aesKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(message.getBytes());
    }
    
    /**
     * 使用AES密鑰解密訊息。
     * 
     * @param aesKey        AES密鑰，用於解密。
     * @param encryptedData 已加密的訊息。
     * @return 返回解密後的字符串。
     * @throws Exception 若解密過程中發生錯誤時拋出。
     */
    public static String decryptWithAESKey(SecretKey aesKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(encryptedData));
    }
}
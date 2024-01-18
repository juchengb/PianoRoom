package mvc.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * KeyUtil 是生成和處理密鑰相關功能的工具。
 */
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
    
    /**
     * 生成基於時間的一次性密碼 (TOTP)。
     *
     * @param secret Base64 編碼的秘密金鑰。
     * @param timeInterval 當前的時間間隔，用於計算 TOTP
     * @param crypto 指定的加密算法，例如 "HMAC-SHA256"
     * @return 計算出的 6 位 TOTP
     * @throws NoSuchAlgorithmException 若指定的加密算法不可用或不存在，則拋出此異常。
     * @throws InvalidKeyException 若初始化 Mac 物件時使用的密鑰是無效的，則拋出此異常。
     */
    public static String generateTOTP(String secret, long timeInterval, String crypto) 
                throws NoSuchAlgorithmException, InvalidKeyException {
            
        // 將 Base64 編碼的密鑰解碼
        byte[] decodedKey = Base64.getDecoder().decode(secret);
            
        // 創建一個加密演算法實例，例如：HMAC-SHA256 
        Mac mac = Mac.getInstance(crypto);
            
        // 用解碼後的鑰匙和原始(RAW)加密演算法初始化 Mac
        SecretKeySpec spec = new SecretKeySpec(decodedKey, "HmacSHA256");
        mac.init(spec);
            
        // 根據當前時間和給定的時間間隔計算 TOTP
        byte[] hmac = mac.doFinal(KeyUtil.longToBytes(timeInterval));
        int offset = hmac[hmac.length - 1] & 0xF;
        long otp = (hmac[offset] & 0x7F) << 24 |
                   (hmac[offset + 1] & 0xFF) << 16 |
                   (hmac[offset + 2] & 0xFF) << 8 |
                   (hmac[offset + 3] & 0xFF);
            
        // 將其縮小為 6 位數字
        otp = otp % 1000000;

        return String.format("%06d", otp);
    }
    
    /**
     * 將給定的 long 值轉換成 byte 陣列。
     * 
     * 此方法會將一個 64 位元的 long 值轉換成一個 8 位元組的 byte 陣列，其中每個 byte 代表 long 的一個字節。
     * 轉換是從最低有效位元組開始的，即 result[7] 是 l 的最低有效位元組，result[0] 是最高有效位元組。
     * 
     * @param l 需要轉換的 long 值。
     * @return 表示給定 long 值的 byte 陣列。
     */
    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }
    
}
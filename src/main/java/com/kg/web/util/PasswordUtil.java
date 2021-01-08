package com.kg.web.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * Taken from the xls-core-components for password encryption and decryption
 * 
 * @author <a href="mailto:a.bondoc@welcome-rt.com">Adrian Bondoc</a>
 */
public class PasswordUtil {
    private static final String ALGORITHM_AES = "AES";
    
    private static final int BIT_SIZE = 128;
    
    private static final String DEFAULT_KEY = "gJ6CwAYOL0XbzcSeqL/zPg==";
    
    public static String generateKey() {
        String result = "";
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
            keyGenerator.init(BIT_SIZE);
            
            // Generate the secret key pecs
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] raw = secretKey.getEncoded();
            
            // encode key to String
            Base64 base64encoder = new Base64();
            result = base64encoder.encodeToString(raw);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    
    public static String encrypt(String text) {
        return encrypt(text, DEFAULT_KEY);
    }
    
    public static String encrypt(String text, String key) {
        String result = "";
        if(text == null || text.trim().length() <= 0) {
            throw new IllegalArgumentException("decrypted string was null or empty");
        }
        
        try {
            // Decode key to byte
            Base64 base64decoder = new Base64();
            byte[] raw = base64decoder.decode(key);
            
            // secret key pecs
            SecretKeySpec fSecretKeySpec = new SecretKeySpec(raw, ALGORITHM_AES);
            
            // Instantiate the Cipher, encryption pass
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.ENCRYPT_MODE, fSecretKeySpec);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            
            // Encode text to String
            Base64 base64encoder = new Base64();
            result = base64encoder.encodeToString(encrypted);
            
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    
    public static String decrypt(String text) {
        return decrypt(text, DEFAULT_KEY);
    }
    
    public static String decrypt(String text, String key) {
        String result = "";
        if(text == null || text.trim().length() <= 0) {
            throw new IllegalArgumentException("encrypted string was null or empty");
        }
        
        try {
            // Decode key to byte
            Base64 base64decoder = new Base64();
            byte[] raw = base64decoder.decode(key);
            
            // secret key pecs
            SecretKeySpec fSecretKeySpec = new SecretKeySpec(raw, ALGORITHM_AES);
            
            // Instantiate the Cipher, decryption pass
            Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
            cipher.init(Cipher.DECRYPT_MODE, fSecretKeySpec);
            
            // Decode text to byte
            byte[] cleartext = base64decoder.decode(text);
            
            // Convert text byte to byte encrypted
            byte[] original = cipher.doFinal(cleartext);
            
            result = bytes2String(original);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }
    
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char)bytes[i]);
        }
        return stringBuffer.toString();
    }
    
    public static void main(String a[]) {
        if(a.length > 0) {
            // System.out.println("Password encrypted:" +
            // CryptoUtil.encrypt(a[0]));
        }
        
        String chp = PasswordUtil.encrypt("charles@123");
        String arp = PasswordUtil.encrypt("arun@123");
        String yop = PasswordUtil.encrypt("yoka@123");
        String admin = PasswordUtil.encrypt("admin@123");
        
        System.out.println("chls => " + chp );
        System.out.println("arun => " + arp );
        System.out.println("Yoka => " + yop );
        System.out.println("admin@123 => " + admin );
        System.out.println("chls dc => " + PasswordUtil.decrypt(chp) );
        System.out.println("arun dc => " + PasswordUtil.decrypt(arp) );
        System.out.println("yoka dc => " + PasswordUtil.decrypt(yop) );
        
        String t1 = PasswordUtil.encrypt("token001");
        String t2 = PasswordUtil.encrypt("token002");
        String t3 = PasswordUtil.encrypt("token003");
        String t4 = PasswordUtil.encrypt("token004");
        
        System.out.println("token001 => " + t1 );
        System.out.println("token002 => " + t2 );
        System.out.println("token003 => " + t3 );
        System.out.println("token004 => " + t4 );
    }
}

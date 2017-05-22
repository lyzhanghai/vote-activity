package com.lyl.outsourcing.activity.common;

import org.apache.tomcat.util.buf.HexUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * Created by liyilin on 2017/4/21.
 */
final public class CryptoUtil {

    /**
     * HmacSHA1运算
     * @param key 密钥，长度<=64字节
     * @param data 要生成mac的数据
     * @return
     */
    public static byte[] hmacSha1(byte[] key, byte[] data) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");

            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用key, 对 data 进行 hmacSha1 运算，返回值是 base64 编码的字符串，即结果是 base64(hmacSha1(key, data))
     * key, data 都是使用UTF-8编码
     * @param key 密钥，长度<=64字节
     * @param data 要生成mac的数据
     * @return
     */
    public static String hmacSha1(String key, String data) {
        try {
            byte[] buf=hmacSha1(key.getBytes("UTF-8"), data.getBytes("UTF-8"));
            return Base64.encodeBase64String(buf);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * sha1运算
     * @param buf
     * @return
     */
    public static byte[] sha1(byte []buf) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return md.digest(buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * md5运算
     * @param buf
     * @return
     */
    public static byte[] md5(byte []buf) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * md5运算
     * @param buf
     * @return
     */
    public static String md5String(byte []buf) {
        byte []tmp=md5(buf);
        return HexUtils.toHexString(tmp);
    }

    /**
     *
     * @param key
     * @param iv
     * @param buf
     * @param encrypt true表示进行加密运算，false表示进行解密
     * @return
     */
    static byte[] tripleDes(byte []key,byte []iv,byte []buf,boolean encrypt) {
        if ((key.length!=24) || (iv.length!=8))
            throw new IllegalArgumentException();

        try {
            DESedeKeySpec keySpec = new DESedeKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey secretKey = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            if (encrypt)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey,new IvParameterSpec(iv));
            else cipher.init(Cipher.DECRYPT_MODE, secretKey,new IvParameterSpec(iv));

            return cipher.doFinal(buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用3DES加密，pkcs7(5),cbc
     * @param key key是24字节的数组
     * @param iv iv必须是8字节的数组
     * @param buf
     * @return
     */
    public static byte[] tripleDesEncrypt(byte []key, byte []iv,byte []buf) {
        return tripleDes(key,iv,buf,true);
    }

    /**
     * 使用3des解密，pkcs7(5),cbc
     * @param key key长24字节
     * @param iv iv长8字节
     * @param buf
     * @return
     */
    public static byte[] tripleDesDecrypt(byte []key, byte []iv,byte []buf) {
        return tripleDes(key,iv,buf,false);
    }

    /**
     * aes加密，pkcs5, cbc。aes-128
     * @param key 16字节
     * @param iv 16字节
     * @param buf
     * @return
     */
    public static byte[] aesEncrypt(byte[] key, byte[] iv, byte[] buf) {
        return aesTransform(Cipher.ENCRYPT_MODE, key, iv, buf);
    }

    /**
     * aes解密，pkcs5, cbc。aes-128
     * @param key 16字节
     * @param iv 16字节
     * @param buf
     * @return
     */
    public static byte[] aesDecrypt(byte[] key, byte[] iv, byte[] buf) {
        return aesTransform(Cipher.DECRYPT_MODE, key, iv, buf);
    }

    static byte[] aesTransform(int mode, byte[] key, byte[] iv, byte[] buf) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(mode, keySpec, ivSpec);

            return cipher.doFinal(buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
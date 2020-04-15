package com.yorath.booksearch.common;

import com.yorath.booksearch.exception.ServiceException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class CommonUtils {
    /**
     * 난수 생성
     * @return
     */
    public static String generateSalt() {
        Random random = new Random();
        byte[] salt = new byte[10];

        random.nextBytes(salt);
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<salt.length; i++) {
            sb.append(String.format("%02x", salt[i]));
        }
        return sb.toString();
    }

    /**
     * SHA-256 단방향 알고리즘으로 비밀번호 암호화
     *
     * @param password 비밀번호 원문
     * @param salt  암호화에 적용할 난수
     * @return
     */
    public static String encryptSHA256(String password, String salt)  {

        byte[] saltBytes = salt.getBytes();
        String result = "";

        byte[] temp = password.getBytes();
        byte[] bytes = new byte[temp.length + saltBytes.length];

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(bytes);

        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();

        for(int i=0; i<b.length; i++) {
            sb.append(Integer.toString((b[i] & 0xFF) + 256, 16).substring(1));
        }
        result = sb.toString();
        return result;
    }
}

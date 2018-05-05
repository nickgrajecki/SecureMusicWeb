/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecurityClasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author jpn17fmu
 */
public class PasswordHash {
    public static String hashPass(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer sBuffer = new StringBuffer();
        for (byte b1 : b) {
            sBuffer.append(Integer.toHexString(b1 & 0xff).toString());
        }
        return sBuffer.toString();
    }
    
    public static void main(String[] args) {
        String password = "password";
        try {
            System.out.println(hashPass(password));
        } catch(NoSuchAlgorithmException e) {
            System.out.println(e);
        }
    }
}

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
        // Message Digest object accepting an SHA-256 algorithm
        MessageDigest messagedigest = MessageDigest.getInstance("SHA-256");
        // Apply algorithm to password passed as an argument in the hashPass method
        messagedigest.update(password.getBytes());
        
        // Byte array stores the value
        byte[] bArray = messagedigest.digest();
        
        // Convert the contents of the array to a string
        StringBuffer sBuffer = new StringBuffer();
        for (byte b1 : bArray) {
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

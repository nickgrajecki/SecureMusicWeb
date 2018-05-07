/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SecurityClasses;

import java.security.SecureRandom;

/**
 *
 * @author Nick
 */
public class Salt {

    public byte[] generateSalt() {
        SecureRandom secRand = new SecureRandom();
        byte bytes[] = new byte[8];
        secRand.nextBytes(bytes);
        return bytes;
    }
    
    public String saltToString(byte[] salt) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(salt);
    }
}

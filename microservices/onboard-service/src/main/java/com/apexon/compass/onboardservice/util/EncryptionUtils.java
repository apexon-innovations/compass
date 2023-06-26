package com.apexon.compass.onboardservice.util;

import com.apexon.compass.exception.custom.EncryptionException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import com.apexon.compass.onboardservice.constants.EncryptionProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptionUtils {

    public static final String ALGO = "DESede";

    public static String encryptString(String message, String aKey, String salt) throws EncryptionException {
        try {
            message = message + salt;
            byte[] encodedKey = Base64.decodeBase64(aKey);
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGO);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(1, key);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.encodeBase64String(encryptedBytes);
        }
        catch (BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | NullPointerException | IllegalBlockSizeException ex) {
            throw new EncryptionException("Cannot encrypt message" + ex.getMessage());
        }
    }

    public static String decryptString(String encryptedMessage, String aKey, String salt) throws EncryptionException {
        try {
            byte[] encodedKey = Base64.decodeBase64(aKey);
            SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGO);
            Cipher decipher = Cipher.getInstance(ALGO);
            decipher.init(2, key);
            byte[] messageToDecrypt = Base64.decodeBase64(encryptedMessage);
            byte[] decryptedBytes = decipher.doFinal(messageToDecrypt);
            String decryptedString = new String(decryptedBytes);
            return decryptedString.substring(0, decryptedString.length() - salt.length());
        }
        catch (NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                | NullPointerException | IllegalArgumentException | NoSuchAlgorithmException ex) {
            throw new EncryptionException("Cannot decrypt message" + ex.getMessage());
        }
    }

    public static String generateEncryptionKey(int length, EncryptionProperties encryptionProperties) {
        String characters = encryptionProperties.getRandom();
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, length)
            .map(digit -> random.nextInt(characters.length()))
            .mapToObj(randomIndex -> String.valueOf(characters.charAt(randomIndex)))
            .collect(Collectors.joining());
    }

}

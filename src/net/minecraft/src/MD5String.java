package net.minecraft.src;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5String {
    private String salt;

    public MD5String(String salt) {
        this.salt = salt;
    }

    public String digest(String s) {
        try {
            String string = this.salt + s;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(string.getBytes(), 0, string.length());
            return new BigInteger(1, messageDigest.digest()).toString(16);
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new RuntimeException(noSuchAlgorithmException);
        }
    }
}


package com.shitajimado.academicwritingrecommender.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordConverter {
    public static String convert(String password) {
        try {
            StringBuilder hexString = new StringBuilder();
            MessageDigest md = null;

            md = MessageDigest.getInstance("SHA-256");

            md.update(password.getBytes());

            for (byte b : md.digest()) {
                if ((0xff & b) < 0x10) {
                    hexString.append("0");
                }

                hexString.append(Integer.toHexString((0xFF & b)));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}

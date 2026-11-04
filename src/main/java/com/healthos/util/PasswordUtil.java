package com.healthos.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * HealthOS Password Hashing and Verification Utility.
 * Uses BCrypt with a secure salt work factor of 10.
 * Plain-text passwords are never stored or logged.
 */
public final class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Hashes a plain-text password using BCrypt.
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a plain-text password against a BCrypt hash.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}

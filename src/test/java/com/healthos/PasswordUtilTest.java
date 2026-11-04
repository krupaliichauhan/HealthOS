package com.healthos;

import com.healthos.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("HealthOS Password Security Test Suite")
class PasswordUtilTest {

    @Test
    @DisplayName("Should securely hash and verify passwords using BCrypt")
    void testPasswordHashingAndVerification() {
        String plain = "fairy123";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotNull(hash);
        assertNotEquals(plain, hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$"));

        assertTrue(PasswordUtil.checkPassword("fairy123", hash));
        assertFalse(PasswordUtil.checkPassword("wrongpassword", hash));
        assertFalse(PasswordUtil.checkPassword("", hash));
        assertFalse(PasswordUtil.checkPassword(null, hash));
    }
}

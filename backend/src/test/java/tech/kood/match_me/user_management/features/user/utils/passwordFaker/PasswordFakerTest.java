package tech.kood.match_me.user_management.features.user.utils.passwordFaker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordFakerTest {
    @Test
    void testPasswordLengthStrictlyBetweenMinAndMax() {
        int min = 8, max = 16;
        String pwd = PasswordFaker.generatePassword(min, max, false, false);
        assertTrue(pwd.length() > min && pwd.length() < max,
            "Password length should be strictly between min and max");
    }

    @Test
    void testPasswordContainsUpperIfRequired() {
        String pwd = PasswordFaker.generatePassword(8, 16, true, false);
        assertTrue(pwd.chars().anyMatch(Character::isUpperCase),
            "Password should contain at least one uppercase letter");
    }

    @Test
    void testPasswordContainsSpecialIfRequired() {
        String pwd = PasswordFaker.generatePassword(8, 16, false, true);
        assertTrue(pwd.chars().anyMatch(c -> PasswordFaker.SPECIAL.indexOf(c) >= 0),
            "Password should contain at least one special character");
    }

    @Test
    void testPasswordContainsOnlyAllowedChars() {
        String pwd = PasswordFaker.generatePassword(8, 16, true, true);
        String allowed = PasswordFaker.LOWER + PasswordFaker.UPPER + PasswordFaker.DIGITS + PasswordFaker.SPECIAL;
        assertTrue(pwd.chars().allMatch(c -> allowed.indexOf(c) >= 0),
            "Password should only contain allowed characters");
    }

    @Test
    void testExceptionForInvalidLengthRange() {
        assertThrows(IllegalArgumentException.class, () ->
            PasswordFaker.generatePassword(8, 9, false, false),
            "Should throw exception for invalid length range");
    }

    @Test
    void testRandomness() {
        String pwd1 = PasswordFaker.generatePassword(8, 16, true, true);
        String pwd2 = PasswordFaker.generatePassword(8, 16, true, true);
        assertNotEquals(pwd1, pwd2, "Passwords should be different on multiple calls");
    }
}


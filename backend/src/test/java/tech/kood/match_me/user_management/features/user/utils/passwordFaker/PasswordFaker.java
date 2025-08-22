package tech.kood.match_me.user_management.features.user.utils.passwordFaker;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PasswordFaker {
    public static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";
    public static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePassword(int minLength, int maxLength, boolean mustIncludeUpper, boolean mustIncludeSpecial) {
        if (minLength < 1 || maxLength <= minLength + 1) {
            throw new IllegalArgumentException("Invalid length range");
        }
        // Ensure length is strictly between minLength and maxLength
        int length = minLength + 1 + RANDOM.nextInt(maxLength - minLength - 1);
        List<Character> passwordChars = new ArrayList<>();
        StringBuilder allowed = new StringBuilder(LOWER + DIGITS);
        if (mustIncludeUpper) {
            allowed.append(UPPER);
            passwordChars.add(UPPER.charAt(RANDOM.nextInt(UPPER.length())));
        }
        if (mustIncludeSpecial) {
            allowed.append(SPECIAL);
            passwordChars.add(SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length())));
        }
        while (passwordChars.size() < length) {
            passwordChars.add(allowed.charAt(RANDOM.nextInt(allowed.length())));
        }
        Collections.shuffle(passwordChars, RANDOM);
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
    }
}

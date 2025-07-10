package tech.kood.match_me.user_management.internal.utils;

public final class EmailValidator {
    public static boolean isValidEmail(String email) {
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
}

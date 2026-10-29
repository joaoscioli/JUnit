package com.joaoscioli.testing;

public class PasswordValidator {
    public boolean isValid(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }

        // Count Unicode code points, rather than UTF-16 units (or grapheme clusters).
        return password.codePointCount(0, password.length()) >= 8
                && hasUppercase(password)
                && hasLowercase(password)
                && hasDigit(password);
    }

    public PasswordStrength strengthOf(String password) {
        if (!isValid(password)) {
            return PasswordStrength.WEAK;
        }

        if (password.codePointCount(0, password.length()) >= 12 && hasSymbol(password)) {
            return PasswordStrength.STRONG;
        }

        return PasswordStrength.ACCEPTABLE;
    }

    private boolean hasUppercase(String value) {
        return value.codePoints().anyMatch(Character::isUpperCase);
    }

    private boolean hasLowercase(String value) {
        return value.codePoints().anyMatch(Character::isLowerCase);
    }

    private boolean hasDigit(String value) {
        return value.codePoints().anyMatch(Character::isDigit);
    }

    private boolean hasSymbol(String value) {
        return value.codePoints().anyMatch(character ->
                !Character.isLetterOrDigit(character) && !Character.isWhitespace(character)
        );
    }
}

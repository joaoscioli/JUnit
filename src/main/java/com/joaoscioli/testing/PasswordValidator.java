package com.joaoscioli.testing;

public class PasswordValidator {
    public boolean isValid(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }

        return password.length() >= 8
                && hasUppercase(password)
                && hasLowercase(password)
                && hasDigit(password);
    }

    public PasswordStrength strengthOf(String password) {
        if (!isValid(password)) {
            return PasswordStrength.WEAK;
        }

        if (password.length() >= 12 && hasSymbol(password)) {
            return PasswordStrength.STRONG;
        }

        return PasswordStrength.ACCEPTABLE;
    }

    private boolean hasUppercase(String value) {
        return value.chars().anyMatch(Character::isUpperCase);
    }

    private boolean hasLowercase(String value) {
        return value.chars().anyMatch(Character::isLowerCase);
    }

    private boolean hasDigit(String value) {
        return value.chars().anyMatch(Character::isDigit);
    }

    private boolean hasSymbol(String value) {
        return value.chars().anyMatch(character -> !Character.isLetterOrDigit(character));
    }
}

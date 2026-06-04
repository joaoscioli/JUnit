package com.joaoscioli.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Nested
    @DisplayName("password validity")
    class PasswordValidity {
        @ParameterizedTest
        @ValueSource(strings = {"Secure123", "Billing2026", "Portfolio9"})
        void acceptsPasswordsWithMinimumRequiredSignals(String password) {
            assertTrue(validator.isValid(password));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"        ", "short1A", "lowercase1", "UPPERCASE1", "NoDigitsHere"})
        void rejectsPasswordsMissingRequiredSignals(String password) {
            assertFalse(validator.isValid(password));
        }
    }

    @Nested
    @DisplayName("password strength")
    class PasswordStrengthChecks {
        @ParameterizedTest
        @MethodSource("com.joaoscioli.testing.PasswordValidatorTest#passwordStrengthExamples")
        void classifiesPasswordStrength(String password, PasswordStrength expectedStrength) {
            PasswordStrength strength = validator.strengthOf(password);

            assertEquals(expectedStrength, strength);
        }

        @Test
        void keepsValidityAndStrengthRulesAligned() {
            String password = "SeniorBackend2026!";

            assertAll(
                    () -> assertTrue(validator.isValid(password)),
                    () -> assertEquals(PasswordStrength.STRONG, validator.strengthOf(password))
            );
        }
    }

    private static Stream<Arguments> passwordStrengthExamples() {
        return Stream.of(
                Arguments.of("weak", PasswordStrength.WEAK),
                Arguments.of("Secure123", PasswordStrength.ACCEPTABLE),
                Arguments.of("SeniorBackend2026!", PasswordStrength.STRONG)
        );
    }
}

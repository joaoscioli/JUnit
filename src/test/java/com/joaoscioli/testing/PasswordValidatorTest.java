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

    @ParameterizedTest
    @MethodSource("unicodePasswordExamples")
    void countsUnicodeCodePointsAndRecognizesSupplementaryLettersAndDigits(
            String password, boolean valid, PasswordStrength strength) {
        assertAll(() -> assertEquals(valid, validator.isValid(password)),
                () -> assertEquals(strength, validator.strengthOf(password)));
    }

    private static Stream<Arguments> unicodePasswordExamples() {
        var upper = new String(Character.toChars(0x10400)); // Deseret uppercase letter
        var lower = new String(Character.toChars(0x10428)); // Deseret lowercase letter
        var digit = new String(Character.toChars(0x1D7CE)); // Mathematical digit zero
        return Stream.of(
                Arguments.of("Aa1" + upper.repeat(4), false, PasswordStrength.WEAK), // 7, not 11
                Arguments.of(upper + lower + digit + "abcde", true, PasswordStrength.ACCEPTABLE),
                Arguments.of("Aa1" + upper.repeat(7) + "!", true, PasswordStrength.ACCEPTABLE), // 11
                Arguments.of(upper + lower + digit + "abcdefgh!", true, PasswordStrength.STRONG),
                Arguments.of(upper + lower + digit + "abcdefghi", true, PasswordStrength.ACCEPTABLE)
        );
    }

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

        @Test
        void doesNotTreatWhitespaceAsAStrengthSymbol() {
            assertEquals(PasswordStrength.ACCEPTABLE, validator.strengthOf("SecurePass1 "));
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

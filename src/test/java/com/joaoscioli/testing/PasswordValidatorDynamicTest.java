package com.joaoscioli.testing;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class PasswordValidatorDynamicTest {
    private final PasswordValidator validator = new PasswordValidator();

    @TestFactory
    Stream<DynamicTest> rejectsPasswordsMissingRequiredRules() {
        return Stream.of(
                "short",
                "lowercaseonly1",
                "UPPERCASEONLY1",
                "NoNumbersHere"
        ).map(password -> dynamicTest("rejects " + password, () -> assertFalse(validator.isValid(password))));
    }
}

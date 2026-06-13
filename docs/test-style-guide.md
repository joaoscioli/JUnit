# Test Style Guide

This guide defines the testing style used in this repository.

## Test Naming

Use names that describe behavior:

```java
void rejectsBlankPassword()
void chargesPaymentWhenInventoryIsAvailable()
void returnsStrongForPasswordWithAllRequiredRules()
```

Prefer behavior over implementation details.

## Test Structure

Use Arrange, Act, Assert as the mental model.

```java
// arrange
var validator = new PasswordValidator();

// act
var result = validator.validate("abc");

// assert
assertFalse(result.valid());
```

Comments are optional when the test is already obvious.

## Assertions

Use focused assertions for one behavior. Use `assertAll` when several fields
belong to the same result.

Good:

```java
assertAll(
        () -> assertTrue(result.valid()),
        () -> assertEquals(PasswordStrength.STRONG, result.strength())
);
```

## Mockito

Use mocks for external collaborators, not for every object.

Good mock targets:

- payment gateways;
- inventory gateways;
- email clients;
- HTTP clients.

Avoid mocking simple domain objects.

## Interview Talking Points

- Tests should explain business behavior.
- Parameterized tests reduce duplication for rule matrices.
- Mockito is useful for boundaries, not for replacing domain logic.
- A readable test can be more valuable than a clever test.

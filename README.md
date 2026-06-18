# JUnit Testing Lab

> Portfolio status: active testing lab.

This repository is being rebuilt as a focused Java testing lab. Its purpose is
to show practical testing discipline with JUnit Jupiter, clean test structure,
and small examples that explain testing decisions clearly.

## Why This Repository Exists

Testing is one of the strongest signals of engineering maturity in a backend
portfolio. This lab is intentionally small so each example can focus on one
testing idea instead of hiding the learning goal inside a large application.

Current focus:

- JUnit Jupiter;
- parameterized tests;
- nested test organization;
- exception assertions;
- grouped assertions;
- method sources;
- Mockito test doubles;
- readable business-rule examples;
- small commit discipline.

## Current Examples

### Discount Calculator

`DiscountCalculator` applies customer-tier and large-order discounts.

It demonstrates:

- arranging tests by behavior;
- checking multiple inputs with parameterized tests;
- validating edge cases;
- testing exceptions explicitly;
- keeping production code simple enough for the test intention to stay visible.

### Password Validator

`PasswordValidator` validates password requirements and classifies password
strength.

It demonstrates:

- `@NullAndEmptySource`;
- `@MethodSource`;
- grouped assertions with `assertAll`;
- separating validity checks from classification rules;
- documenting edge cases through readable test names.

### Order Service

`OrderService` validates an order, checks inventory, charges payment, and returns
an order receipt.

It demonstrates:

- Mockito extension setup;
- mocks for external gateways;
- stubbing with `when(...).thenReturn(...)`;
- interaction verification with `verify`;
- protecting against unwanted payment calls when validation or inventory checks
  fail.

## Run Tests

```bash
mvn test
```

## Testing Style

- [Test Style Guide](docs/test-style-guide.md)
- [Test Checklist](docs/test-checklist.md)
- [Test Data Guide](docs/test-data-guide.md)
- [Mockito Guidelines](docs/mockito-guidelines.md)
- [Coverage Strategy](docs/coverage-strategy.md)
- [Integration Testing Plan](docs/integration-testing-plan.md)

## Tech Stack

- Java 21
- Maven
- JUnit Jupiter
- Mockito

## Portfolio Role

This is a supporting repository. The main portfolio projects should still prove
end-to-end backend ownership, but this lab helps show focused testing practice
and continuous improvement.

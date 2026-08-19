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
- dynamic tests;
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
- protecting money-related calculations from numeric overflow;
- testing exceptions explicitly;
- keeping production code simple enough for the test intention to stay visible.

### Password Validator

`PasswordValidator` validates password requirements and classifies password
strength.

It demonstrates:

- `@NullAndEmptySource`;
- `@MethodSource`;
- grouped assertions with `assertAll`;
- dynamic tests with `@TestFactory`;
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

### Invoice Service

`InvoiceService` creates an invoice and sends a notification through an email
gateway.

It demonstrates:

- Mockito `ArgumentCaptor`;
- verifying generated message content;
- checking that invalid input does not trigger side effects;
- keeping external communication behind a small interface.

## Run Tests

```bash
mvn test
```

## Testing Style

- [Test Style Guide](docs/test-style-guide.md)
- [Reviewer Scorecard](docs/reviewer-scorecard.md)
- [Technical Depth Map](docs/technical-depth-map.md)
- [Portfolio Positioning](docs/portfolio-positioning.md)
- [Hiring Manager Summary](docs/hiring-manager-summary.md)
- [30-Second Pitch](docs/30-second-pitch.md)
- [Interview Route](docs/interview-route.md)
- [Key Talking Points](docs/key-talking-points.md)
- [Next Review Focus](docs/next-review-focus.md)
- [Demo Script](docs/demo-script.md)
- [Interview Questions](docs/interview-questions.md)
- [Test Checklist](docs/test-checklist.md)
- [Test Data Guide](docs/test-data-guide.md)
- [Test Naming](docs/test-naming.md)
- [Test Boundaries](docs/test-boundaries.md)
- [Assertion Strategy](docs/assertion-strategy.md)
- [Mockito Guidelines](docs/mockito-guidelines.md)
- [Coverage Strategy](docs/coverage-strategy.md)
- [Integration Testing Plan](docs/integration-testing-plan.md)
- [CI Testing](docs/ci-testing.md)
- [Changelog](CHANGELOG.md)

## Interview Checkpoint

A reviewer should inspect the discount and order tests first. They show
boundary values, validation contracts, grouped assertions, and Mockito usage
around external dependencies.

## Portfolio Proof

This repository proves testing discipline directly. It shows how readable tests
protect business behavior, edge cases, validation rules, and external
boundaries without turning the suite into noise.

## Fast Review Path

1. Read the [30-Second Pitch](docs/30-second-pitch.md) to understand the repository signal.
2. Open the [Interview Route](docs/interview-route.md) to follow the testing discussion.
3. Inspect `DiscountCalculatorTest` and `OrderServiceTest` to compare edge cases and mocked boundaries.

## Tech Stack

- Java 21
- Maven
- JUnit Jupiter
- Mockito

## Portfolio Role

This is a supporting repository. The main portfolio projects should still prove
end-to-end backend ownership, but this lab helps show focused testing practice
and continuous improvement.

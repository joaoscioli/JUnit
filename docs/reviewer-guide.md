# Reviewer Guide

This guide helps a technical reviewer understand the testing habits demonstrated
by this repository.

## What To Review First

1. `README.md` for repository purpose.
2. `docs/test-style-guide.md` for naming and structure.
3. `docs/assertion-strategy.md` for assertion quality.
4. `docs/test-boundaries.md` for unit versus integration trade-offs.
5. `src/test/java` for concrete examples.
6. `DiscountCalculatorTest` for boundary values and validation contracts.

## Strong Signals

- Tests focus on behavior rather than implementation details.
- Names describe the scenario and expected outcome.
- Fixtures are intentionally small and readable.
- Boundary and failure cases are documented as first-class testing concerns.
- Validation messages are asserted when they represent a behavior contract.

## Interview Talking Points

- Explain the difference between testing behavior and testing implementation.
- Discuss when mocks help and when they make tests brittle.
- Show how naming, fixtures, and assertions affect maintainability.
- Explain why numeric boundaries matter in money-related code.

## Next Practical Step

Add one Spring Boot-style integration example that proves validation errors
travel through the HTTP boundary, not only through isolated unit tests.

# Reviewer Scorecard

Use this scorecard to review the repository quickly during portfolio screening.

## Strong Signals

- Tests are organized around behavior, boundaries, and edge cases.
- Parameterized tests make business-rule coverage compact.
- Mockito is used around external dependencies instead of everywhere.
- Assertion strategy and naming are documented, not left implicit.

## Evidence

- `src/test/java/com/joaoscioli/testing/DiscountCalculatorTest.java`
- `src/test/java/com/joaoscioli/testing/OrderServiceTest.java`
- `src/test/java/com/joaoscioli/testing/InvoiceServiceTest.java`
- `docs/assertion-strategy.md`

## Next Senior Step

Add a small Spring Boot HTTP-boundary integration test example to connect unit
testing discipline with API-level confidence.

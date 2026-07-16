# Test Checklist

Use this checklist before considering a test example complete.

## Behavior Coverage

- The happy path is tested.
- Important edge cases are tested.
- Invalid input is tested.
- Exceptions are asserted when expected.
- Test names describe behavior.

## Structure

- Test setup is minimal.
- Assertions are focused.
- Parameterized tests are used when inputs form a matrix.
- `assertAll` is used only when grouped assertions describe one result.
- Mocks are used only for external boundaries.

## Maintainability

- Tests do not duplicate production logic.
- Tests avoid magic values when names would clarify intent.
- Tests can fail for one clear reason.
- The reader can understand the business rule from the test.

## Interview Talking Points

- Good tests document behavior.
- Edge cases show engineering maturity.
- Mocks are useful at boundaries.
- A small readable test suite can be stronger than a large noisy one.

## Questions This Lab Can Answer

- When should an exception message be asserted?
- How do parameterized tests improve coverage without copy-paste?
- What makes a mock useful instead of brittle?
- Why do money-related calculations need boundary tests?

# Technical Depth Map

This map connects the repository to the skills a technical reviewer may look for.

## Testing Fundamentals

- Parameterized tests cover business-rule variations.
- Dynamic tests document generated scenarios.
- Exception assertions make validation contracts explicit.

## Boundary Design

- Mockito is used for external dependencies.
- Tests avoid mocking simple internal behavior.
- Service tests show when side effects should not happen.

## Maintainability

- Naming and assertion strategy are documented.
- Test data guidance keeps examples readable.
- Coverage is treated as feedback, not the only goal.

## Interview Use

Start with `DiscountCalculatorTest` for edge cases and `OrderServiceTest` for
mocked boundaries.

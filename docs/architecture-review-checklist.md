# Architecture Review Checklist

Use this checklist to review the project as a test architecture exercise.

## Test Boundaries

- Unit tests protect business rules without unnecessary infrastructure.
- Integration tests are reserved for boundaries that need real wiring.
- Mocks are used around external dependencies, not internal behavior by default.

## Maintainability

- Test names explain the scenario, action, and expected result.
- Assertions prove outcomes instead of implementation details.
- Test data is readable and does not hide important conditions.

## Review Signals

- Coverage is treated as a supporting metric, not the definition of quality.
- Failure messages help identify the broken rule quickly.
- Documentation explains when to add a new test category.

## Interview Defense

Be ready to explain how this testing style helps refactor Java code without
turning tests into a fragile copy of the implementation.

# Implementation Readiness

This file defines what must be clear before adding the next executable test slice.

## Ready When

- The behavior under test is named clearly.
- Happy path, edge cases, and failure paths are identified.
- Mock boundaries are justified.
- Test data is readable and intentional.
- Assertions explain business behavior, not only object shape.

## Not Ready If

- Tests would mirror implementation details.
- Mocks hide the most important risk.
- Coverage is the only success measure.

## Review Focus

Implementation should begin with a small service example where tests communicate
behavior and make a later refactor safe.

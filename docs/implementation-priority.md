# Implementation Priority

This file defines the next implementation order for turning the repository into
a stronger testing portfolio.

## P1: Behavioral Test Examples

- Add a service with validation and edge cases.
- Write tests around observable behavior.
- Use clear failure messages and readable assertions.

## P2: Mocking Boundaries

- Add one collaborator dependency.
- Mock only the dependency boundary.
- Show success, failure, and unexpected dependency behavior.

## P3: Integration Confidence

- Add a small integration-style example.
- Document when unit tests are insufficient.
- Connect CI execution to the testing strategy.

## Why This Order

The repository should prove testing judgment before expanding into tooling. Good
tests communicate behavior, risk, and maintainability.

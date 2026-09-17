# Acceptance Criteria

This file defines what the next implementation slice must satisfy before it is
considered ready for review.

## Behavioral Test Suite

- Tests describe observable behavior in their names.
- Happy path, invalid input, boundary values, and dependency failures are covered.
- Mocks are limited to meaningful dependency boundaries.
- Assertions explain what business behavior is protected.
- Tests can be run locally with one documented command.

## Review Standard

The slice is ready when a reviewer can understand the production behavior by
reading the tests and can see how failures would guide debugging.

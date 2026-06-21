# Test Boundaries

Good tests define the boundary they are responsible for.

## Unit Test Boundary

Use unit tests for:

- business rules;
- validation logic;
- calculations;
- branching behavior;
- interactions with mocked external gateways.

Unit tests should be fast and focused.

## Integration Test Boundary

Use integration tests for:

- persistence mappings;
- database constraints;
- HTTP request and response contracts;
- Spring configuration;
- serialization and deserialization;
- security filters.

Integration tests should be fewer and more intentional.

## Mock Boundary

Mock systems you do not own:

- payment providers;
- inventory gateways;
- email gateways;
- remote APIs.

Avoid mocking the code under test so heavily that the test only proves the mock
configuration.

## Portfolio Signal

Clear test boundaries show that testing is part of design. The engineer knows
what behavior belongs in fast unit tests and what requires a wider slice.

# Integration Testing Plan

This document describes how this lab can evolve from isolated unit tests into
integration tests that verify collaboration between application layers.

## Goal

Unit tests prove business rules in isolation. Integration tests prove that
important parts of the system work together with realistic infrastructure
boundaries.

For a Java backend portfolio, integration tests are useful because they show
that the engineer understands persistence, configuration, test data, and
application startup behavior.

## What To Test

Good integration tests should focus on behavior that unit tests cannot prove
well:

- database mapping and constraints;
- repository queries;
- web request and response contracts;
- validation through the HTTP layer;
- transaction behavior;
- serialization and deserialization;
- security filters and authorization rules.

## Recommended Tools

- JUnit Jupiter for test structure;
- Spring Boot Test for application context tests;
- Testcontainers for PostgreSQL or RabbitMQ;
- AssertJ or JUnit assertions for readable verification;
- WireMock for external HTTP dependencies.

## Test Pyramid Placement

Integration tests should be fewer than unit tests because they are slower and
more expensive to maintain. They should cover critical contracts, not every
small branch.

Recommended balance for portfolio projects:

- many unit tests for business rules;
- focused integration tests for persistence and API behavior;
- a small number of end-to-end smoke tests for the main workflow.

## Example Naming

```text
SubscriptionPlanRepositoryIntegrationTest
SubscriptionPlanControllerIntegrationTest
AuthenticationApiIntegrationTest
BillingEventConsumerIntegrationTest
```

## Next Lab Increment

Add one focused Spring Boot integration test that proves validation errors flow
through the HTTP layer with the same message asserted in unit tests.

## Implementation Principles

- Prefer realistic infrastructure over mocks for database behavior.
- Keep test data explicit and small.
- Avoid sharing mutable state between tests.
- Make failures easy to diagnose.
- Do not test framework behavior directly.

## Portfolio Signal

Integration tests show that the code was built to survive outside a single
class. They are especially valuable for Spring Boot APIs because they connect
code quality with real application behavior.

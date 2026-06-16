# Mockito Guidelines

Mockito should clarify boundaries, not replace the domain model.

## When To Mock

Mock collaborators that represent external boundaries:

- payment gateway;
- inventory gateway;
- email sender;
- HTTP client;
- repository in service-level unit tests.

## When Not To Mock

Avoid mocking:

- value objects;
- simple domain entities;
- Java collections;
- methods from the class under test;
- behavior that would be clearer with a real object.

## Good Mockito Tests

Good Mockito tests show:

- what dependency behavior is expected;
- what the service does with that behavior;
- which external calls should happen;
- which external calls should not happen after validation fails.

## Verification Rule

Verify interactions when the interaction is part of the behavior.

Example:

```java
verify(paymentGateway).charge(order.total());
verifyNoInteractions(paymentGateway);
```

## Interview Talking Points

- Mock external boundaries, not everything.
- Too many mocks can make tests brittle.
- Verification is useful when an external call is the behavior.
- Domain rules should usually be tested with real objects.

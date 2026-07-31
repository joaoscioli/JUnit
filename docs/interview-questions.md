# Interview Questions

Use these questions to prepare a testing-focused technical discussion.

## What makes a test valuable?

A valuable test protects behavior that matters: business rules, edge cases,
validation contracts, and integration boundaries.

## When should Mockito be used?

Mockito should be used around external boundaries such as payment, inventory,
email, or infrastructure gateways. It should not replace clear unit design for
simple internal code.

## What is the risk of chasing coverage only?

High coverage can still miss important behavior. The better signal is whether
the tests explain the system, protect decisions, and fail for useful reasons.

## What is the senior-level signal?

The senior signal is designing tests that are readable, stable, and tied to
business outcomes instead of implementation details.

# Assertion Strategy

Assertions should make failures easy to understand.

## Principles

- Assert behavior, not implementation details.
- Prefer one clear reason for a test to fail.
- Use grouped assertions when they describe one returned result.
- Use interaction verification only when the interaction is part of the
  behavior.
- Avoid asserting the same rule in many places without purpose.

## Examples

Good:

```java
assertEquals("CREATED", invoice.status());
```

Risky:

```java
assertNotNull(invoice);
```

`assertNotNull` can be useful, but by itself it usually says little about the
business behavior.

## Mockito Assertions

Use Mockito verification for external boundaries:

- payment gateway was charged;
- email gateway received the expected message;
- dependency was not called after validation failed.

Avoid verifying internal implementation steps that could change without
changing behavior.

## Error Contracts

When an exception message is part of the public contract, assert it directly.
This is useful for validation rules returned by APIs, logs, or user-facing
diagnostics.

Avoid asserting messages for purely internal exceptions unless the message
itself carries business meaning.

## Portfolio Signal

Good assertions show that tests were written to protect behavior, not just to
increase the number of green checks.

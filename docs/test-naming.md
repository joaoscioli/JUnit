# Test Naming

Readable test names are part of the documentation of a codebase.

## Naming Goal

A test name should explain the behavior being protected, not only the method
being called.

Prefer:

```text
rejectsInvalidInvoiceBeforeSendingEmail
```

Avoid:

```text
testCreateInvoice
```

## Useful Shapes

```text
returnsExpectedResultWhenConditionIsTrue
rejectsInvalidInputBeforeCallingDependency
doesNotTriggerSideEffectWhenValidationFails
keepsExistingValueWhenOptionalInputIsMissing
```

## Why This Matters

Good names help reviewers scan test intent quickly. They also make failure
reports easier to understand in CI.

## Portfolio Signal

Clear test names show that the author cares about maintainability, not only
assertion count.

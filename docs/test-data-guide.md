# Test Data Guide

Test data should make the behavior under test easy to understand.

## Principles

- Use the smallest data set that proves the behavior.
- Name important values.
- Avoid copying large object setup into every test.
- Prefer builders or factory methods when setup repeats.
- Keep irrelevant fields out of the test.

## Named Values

Good:

```java
var strongPassword = "Str0ngPassword!";
```

Less helpful:

```java
var value = "Str0ngPassword!";
```

## Factory Methods

Use factory methods when tests need similar objects:

```java
private OrderRequest validOrder() {
    return new OrderRequest("sku-123", 2, "card-token");
}
```

## Test Data Builders

Builders help when objects have many fields and each test changes only one.

Good builder usage:

```java
var request = orderRequest()
        .quantity(0)
        .build();
```

## Interview Talking Points

- Good test data highlights the rule being tested.
- Repeated setup can hide intent.
- Builders are useful when they reduce noise.
- Too much test data can make tests fragile.

# Tough Interview Questions

Use these questions to prepare direct, technical answers.

## Why is coverage not enough?

Coverage says code executed. It does not prove that important behavior,
boundaries, edge cases, or failure paths are protected.

## When should you use mocks?

Mocks are most useful around external boundaries such as payment, email,
inventory, broker, or API clients. Simple internal logic should usually be
tested directly.

## What is a brittle test?

A brittle test fails because implementation details changed, even though the
observable behavior stayed correct.

## What would you defend in a code review?

Readable tests that explain the business rule and fail for useful reasons.

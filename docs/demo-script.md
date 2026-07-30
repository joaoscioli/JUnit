# Demo Script

Use this short script to present the repository in a technical interview.

## 3-Minute Walkthrough

1. Start with the goal: focused JUnit examples that demonstrate testing
   discipline.
2. Show the discount tests for parameterized cases, boundary values, and
   validation contracts.
3. Show the order or invoice tests for Mockito usage at external boundaries.
4. Close with the next step: an HTTP-boundary integration testing example.

## What To Emphasize

- Tests document behavior.
- Edge cases are first-class signals of engineering maturity.
- Mocks are used at boundaries, not for every internal detail.

## Before The Interview

- Open one parameterized test and one Mockito test.
- Be ready to explain a boundary-value test.
- Prepare one example of a brittle test and how to avoid it.

## Avoid Saying

- "The goal is high coverage." Instead, explain which behavior is protected.
- "Mocks make tests faster." Instead, explain why mocks belong at boundaries.

# Interview Practice Plan

Use this plan to practice explaining the repository in a technical interview.

## Goal

Explain how testing choices protect Java behavior, reduce regression risk, and
support safe refactoring.

## 2-Minute Explanation

Start with the risk: Java systems become expensive to change when behavior is
unclear. Then explain how focused tests document rules, protect edge cases, and
make refactoring safer.

## Practice Questions

- What makes a unit test valuable beyond coverage?
- When would you replace a mock with an integration test?
- How do you keep tests readable as scenarios grow?
- What kind of failure should this test produce?

## Strong Answer Criteria

- Names the behavior under protection.
- Explains the boundary being tested.
- Mentions at least one edge case or failure path.
- Connects the test to refactoring confidence.

## Before the Interview

- Read the README review path.
- Pick two examples that show different test boundaries.
- Prepare one story about a brittle test and how to improve it.
- Be ready to explain why useful tests are more important than raw coverage.

# Technical Risks

This file captures the main risks in a testing repository and how the project
plans to reduce them.

## Risks

- Tests can mirror implementation details instead of behavior.
- Overuse of mocks can hide integration problems.
- Weak test names can make failures hard to diagnose.
- Coverage metrics can create false confidence.

## Mitigations

- Name tests around observable behavior.
- Mock only at meaningful dependency boundaries.
- Add edge cases and failure paths, not only happy paths.
- Treat coverage as a signal, not the final quality target.

## Interview Angle

The strongest discussion is how tests support design feedback, safe refactoring,
and confidence in business behavior.

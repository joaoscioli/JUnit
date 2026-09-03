# Interview Red Flags

Use this file to avoid weak signals during a technical interview.

## Avoid Saying

- "High coverage means the code is well tested."
- "I mock everything to keep tests isolated."
- "Integration tests are always too slow."
- "Test names do not matter much."

## Say Instead

- Coverage supports quality review, but behavior and edge cases matter more.
- Mocks are strongest around external boundaries and unstable collaborators.
- Integration tests are useful when wiring, persistence, or contracts matter.
- Test names should explain the rule being protected.

# Decision Log

This log summarizes important technical choices in a review-friendly format.

## Decisions

| Decision | Reason | Trade-off |
| --- | --- | --- |
| Favor behavior-focused test names. | Makes failures easier to connect to business rules. | Names can be longer than terse method names. |
| Mock external boundaries, not simple internal logic. | Keeps tests meaningful and less brittle. | Some collaborators need real implementations in tests. |
| Treat coverage as a supporting metric. | Prevents false confidence from line execution alone. | Requires qualitative review of test value. |

## Interview Use

Use this file to explain how testing decisions improve refactoring confidence
and reduce regression risk in Java projects.

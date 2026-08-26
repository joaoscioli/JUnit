# Technical Debt Register

This register makes project trade-offs visible instead of hiding unfinished work.

## Current Debt

| Area | Debt | Impact | Next Action |
| --- | --- | --- | --- |
| Integration Tests | More real-boundary examples can be added. | The repo may look too unit-test focused. | Add database or API contract examples. |
| Test Data | Some examples can use richer fixtures. | Edge cases may be less visible. | Add builders for invalid and boundary scenarios. |
| CI Feedback | Pipeline documentation is stronger than automation depth. | Reviewers may expect visible quality gates. | Expand workflow checks when project code grows. |

## Review Rule

Debt is acceptable when it is explicit, bounded, and connected to a follow-up
decision. Hidden debt is what damages engineering trust.

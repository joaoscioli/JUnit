# Production Readiness Matrix

This matrix separates testing examples from production-ready test strategy.

| Capability | Current State | Production Expectation |
| --- | --- | --- |
| Unit tests | Behavior-first guidance is documented. | Broad coverage of domain rules and edge cases. |
| Integration tests | Strategy is described. | Real database, API, and external-boundary test slices. |
| CI feedback | Testing quality gates are mapped. | Required checks before merge and release. |
| Maintainability | Naming and style guides exist. | Consistent fixtures, builders, and failure diagnostics. |

## Review Note

The repo is valuable because it frames tests as design assets, not only as a
coverage target.

# Changelog

This project follows small, reviewable increments. Dates use `YYYY-MM-DD`.

Review cadence: each portfolio update should make a testing habit easier to
review, explain, or reuse in a larger Spring Boot project.

## How To Discuss This History

Use this changelog to show testing maturity over time. The strongest narrative
is moving from happy paths to boundaries, validation contracts, and external
side-effect protection.

## Review Readiness

Ready for interview review when tests explain behavior, protect edge cases, and
avoid coupling too tightly to implementation details.

Current public signal: focused Java testing examples covering boundaries,
validation contracts, mocks, and readable assertions.

## Unreleased

- Planned: focused Spring Boot-style integration testing example.
- Planned: more HTTP-boundary validation examples.

## 2026-08 Interview Readiness

- Added fast review path and stronger guidance for presenting testing examples.
- Refined GitHub hygiene with issue, pull request, CODEOWNERS, security, support,
  contributing, and Dependabot configuration updates.
- Clarified how to discuss tests through behavior, edge cases, boundaries, and
  reviewer confidence.

## 2026-07 Portfolio Hardening

- Added boundary testing for money-related calculations.
- Improved validation error assertions.
- Expanded testing guidance around assertions, review paths, and interview
  questions.

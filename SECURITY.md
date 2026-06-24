# Security Policy

This repository is a Java testing lab. Security concerns usually relate to test
data, dependency use, and examples that may normalize unsafe behavior.

## Supported Scope

Security review focuses on:

- secrets or tokens accidentally committed in fixtures;
- test data that resembles real personal data;
- dependency risk in runnable examples;
- tests that hide or normalize unsafe behavior.

## Reporting A Security Concern

Open a concise issue with the affected file and the risk. Do not include real
credentials, tokens, or private data in the report.

## Development Practices

- Use fake data in fixtures.
- Keep test credentials clearly synthetic.
- Add regression tests when fixing security-sensitive behavior.

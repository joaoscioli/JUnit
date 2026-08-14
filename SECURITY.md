# Security Policy

This repository is a Java testing lab, so security feedback should focus on
dependency safety, test data hygiene, and examples that model secure behavior.

## Supported Scope

Security feedback is welcome for:

- dependency vulnerabilities;
- accidental secret exposure in tests or fixtures;
- examples that normalize unsafe validation or boundary handling;
- misleading test guidance around security-sensitive behavior.

## Reporting

Please do not open a public issue with sensitive details.

Send a private report to `joaoscioli@outlook.com` with the affected file, the
risk, and a suggested mitigation when possible.

## Security Expectations

- Test fixtures must not contain real secrets or personal data.
- Validation examples should make failure behavior explicit.
- Security-sensitive tests should explain the risk they protect.

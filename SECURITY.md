# Security Policy

## Supported version

Security fixes target the current `main` branch.

## Reporting a vulnerability

Please do not disclose exploitable vulnerabilities in a public issue. Report them privately to the repository owner with the affected endpoint/component, reproduction steps, expected impact and any suggested mitigation.

Do not include production credentials, access tokens, personal data or other secrets in reports.

## Security expectations

Changes touching authentication, authorization, persistence, migrations or public API contracts must include appropriate tests. Credentials and environment-specific secrets must remain outside version control.

This repository is a portfolio/project environment and does not authorize destructive testing against public infrastructure.
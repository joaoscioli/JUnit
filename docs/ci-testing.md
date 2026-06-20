# CI Testing

This repository uses GitHub Actions to run the JUnit test suite automatically.

## Workflow

```text
.github/workflows/ci.yml
```

The workflow runs:

```bash
mvn -B --no-transfer-progress test
```

## Why This Matters

Automated test execution makes the lab more credible. It shows that the
examples are not only written for reading; they are expected to keep passing as
the repository evolves.

## Reviewer Signal

When a reviewer opens this repository, CI communicates that:

- test examples are executable;
- changes should keep the suite green;
- testing is part of the engineering workflow;
- the repository can be safely extended with new examples.

## Next Improvements

- Add a coverage report.
- Add mutation testing notes.
- Add a small integration testing module later, when the examples justify it.

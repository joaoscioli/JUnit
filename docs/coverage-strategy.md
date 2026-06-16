# Coverage Strategy

Coverage is useful when it helps reveal untested behavior, but high coverage
alone does not prove high quality.

## What To Cover First

Prioritize:

- business rules;
- edge cases;
- validation;
- exception paths;
- integration boundaries;
- regression-prone behavior.

## What Coverage Cannot Prove

Coverage does not prove:

- assertions are meaningful;
- edge cases are complete;
- behavior is correct;
- tests are readable;
- architecture is healthy.

## Good Coverage Questions

- Which business rule would hurt if it broke?
- Which branch represents an important decision?
- Which exception path protects users or data?
- Which external boundary needs a test double?
- Which behavior has failed before?

## Portfolio Direction

For this repository, coverage should support learning goals:

- show parameterized rule coverage;
- show exception path coverage;
- show mock boundary coverage;
- keep examples small and explainable.

## Interview Talking Points

- Coverage is a signal, not a goal by itself.
- Meaningful assertions matter more than line count.
- Critical behavior deserves explicit tests.
- Coverage gaps can help guide risk-based testing.

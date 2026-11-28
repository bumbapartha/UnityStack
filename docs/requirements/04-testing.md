# Testing phase

[← Requirements](README.md)

Test cases, test runs, release test reports and code quality.

**Tools:** [Testing](../tools-and-adapters.md#testing), [Design, documentation and developer tools](../tools-and-adapters.md#design-documentation-and-developer-tools)

**Decisions:** [TD-03](../decisions.md#td-03)

## Requirements

| ID     | Priority | Requirement                                                                                                                                                                                                                                                 |
| ------ | -------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| FR-120 | P0       | **New.** Keep test cases in Git, record every test run's results (unit, end-to-end, manual) in the test-management tool against the build or release, and link them to requirement, story and Change ID; provide a per-release test report kept in history. |
| FR-041 | P1       | Bring code-quality results (SonarQube) and IDE workflows into PR review.                                                                                                                                                                                    |

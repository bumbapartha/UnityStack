# AD-4 — Git provider behind an adapter

[← Decisions](README.md)

**Status:** Accepted · **Date:** 27 November 2026

## Context

Organisations use GitHub, Bitbucket, GitLab, Azure DevOps or self-hosted Git.

## Decision

Source control and pull requests are a capability like any other. GitHub is the first adapter; Gitea (fully open source), GitLab, Bitbucket and Azure DevOps Repos follow.

## Consequences

"GitHub owns Git state" became "the connected Git provider owns Git state". With Gitea an organisation can run the whole stack on open source.

## Related

[Development and Git](../requirements/development-and-git.md), [Tools and adapters](../tools-and-adapters.md), [Coexistence and sync](../requirements/coexistence-and-sync.md)

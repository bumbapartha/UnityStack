# Traceability

[← Documentation index](README.md)

Traceability is the core of UnityStack ([PD-3](decisions.md#pd-3)): for any line of code, test, release or bug, you can see which requirement it came from and everything that happened to it in between. This page explains how. The requirements are in [Traceability and integration](requirements/09-traceability-and-integration.md), and the decision is [AD-7](decisions.md#ad-7).

## 1. Keys: one permanent ID per item

Every requirement, story, task and bug has one **short key**: a prefix plus a number, for example `TODO-12`.

- **Handed out centrally** by UnityStack, so two people drafting at the same time never get the same key.
- **Never changed and never reused.** The number only says when an item was created, not where it sits. `TODO-31` can be a child of `TODO-4`.
- **The main ID.** Every link points to the key, and the same key is used in branch names, commits, PR titles, builds, logs and bugs. It is the Change ID.

UnityStack's own requirements already follow this: `FR-105` is a short key.

## 2. Prefixes: project first, then modules

| When                                    | What happens                                                                                                          | Example                            |
| --------------------------------------- | --------------------------------------------------------------------------------------------------------------------- | ---------------------------------- |
| Project inception                       | The AI asks for the project's prefix: 2–5 capital letters, unique in the organisation.                                | Shopping cart → `SHOP`             |
| No modules yet                          | Every item uses the project prefix.                                                                                   | `SHOP-1`, `SHOP-2`                 |
| A module is first discussed             | The AI suggests a module prefix, checks it is unique, and asks you to confirm.                                        | Payments → `PAY`, Auditing → `AUD` |
| After that                              | New items in the module use its prefix, and each prefix counts on its own.                                            | `PAY-1`, `AUD-1`                   |
| Items created before the module existed | They keep their project key and are tagged with the module. They are only re-keyed if you ask for a move (section 6). | `SHOP-7` tagged _Payments_         |

## 3. Labels: what people see

A label is the **key plus a short title**: `TODO-12 · Create task`. Outline numbers such as 1.1.1 are not used in links, because they shift whenever things move and people end up quoting the wrong one.

The hierarchy is shown around the label instead:

```text
TODO-1 · Task management
  TODO-4 · Lists
    TODO-12 · Create task
    TODO-31 · Share list
  TODO-7 · Reminders
```

- **Breadcrumb** on hover or when opened: Task management › Lists › TODO-12 · Create task.
- **Across modules:** Shopping cart › Payments › PAY-12 · Refund order.
- **Printed documents** may add outline numbers to headings, but never to links.

## 4. Links in files

| Where                                 | How                                          | Shows                                                              |
| ------------------------------------- | -------------------------------------------- | ------------------------------------------------------------------ |
| Markdown                              | `[TODO-12 · Create task](req:TODO-12)`       | the current title, as a link                                       |
| Excalidraw                            | the element's link field holds `req:TODO-12` | clickable box or text                                              |
| Plain text (branch, commit, PR title) | the bare key                                 | `feature/TODO-12-create-task`, `TODO-12: add create task endpoint` |

The workspace preview and the MkDocs build replace the stored title with the current one. A scheduled PR refreshes stored titles in Git, so the plain GitHub view stays readable.

## 5. Stories, tasks and the ticket tool

Stories and tasks get their own keys as children of the requirement, and the ticket tool keeps its own ID too:

```text
TODO-7 · Reminders                       (requirement)
  TODO-52 · Set a due-date reminder      (story, OpenProject #1234)
  TODO-53 · Email notification           (story, OpenProject #1235)
    TODO-60 · Configure mail adapter     (task,  OpenProject #1240)
```

- **On publish,** UnityStack creates the tickets through the adapter, stores the mapping (`TODO-52` ↔ `#1234`) in its index, and writes the key into the ticket's title or a custom field.
- **Developers work at story level,** so branches, commits and PRs use `TODO-52`.
- **Progress rolls up.** Opening `TODO-7` shows all its stories with their PRs, tests and releases. The requirement is done when every child is done.
- **Tickets created directly in the tool** are picked up by sync and given a key. They are linked to a parent if one is set; otherwise they are flagged as unlinked, never guessed.
- **Jira** works the same way through its own adapter, for example `TODO-52` ↔ `PROJ-88`.

## 6. Moves and retirement

Keys never change, so moving an item to another module is a **retire and replace**:

1. `PAY-12` is retired with the reason "moved to Auditing".
2. A new `AUD-13` is created, carrying over the title, description and children, and recording "moved from PAY-12".
3. Old links keep working: anything pointing to `PAY-12` shows ~~PAY-12~~ → AUD-13.
4. History treats the two as one chain, so `AUD-13` includes `PAY-12`'s earlier work.
5. In the ticket tool, the ticket is updated in place, with the new key in its custom field and the old one noted.

Deleted requirements are also retired, never removed. They show ~~TODO-31 · Share list~~ (retired, replaced by TODO-45), and the retired item records when, why and what replaced it.

## 7. The index

UnityStack keeps an index table: key, title, type, parent, module, status (active or retired), replaced-by, file path, and external IDs (ticket, PR, release).

- **Updated when changes merge,** from the Git webhook. Drafts keep provisional entries until they are approved.
- **Git is the source of truth:** the index can be rebuilt from the repository at any time.
- **An API** returns the current label and breadcrumb for a key; previews, the MkDocs plugin and the wireframes use it.

## 8. Through the loop

| Step                                   | Carries the key in                                                 |
| -------------------------------------- | ------------------------------------------------------------------ |
| Requirement, story, task               | its own key, and its parent link                                   |
| Design (HLD, LLD, ADRs, API contracts) | `req:` links to the requirements it covers                         |
| Branch, commit, PR                     | `feature/TODO-52-…`, `TODO-52: …`, PR title                        |
| Tests and test runs                    | test case links; Kiwi run records                                  |
| Build, image, release                  | build metadata, image tags, release notes                          |
| Deployment and logs                    | deployment record; OpenTelemetry attributes                        |
| Bug                                    | its own key, linked to the key, release and log lines it came from |
| User documentation                     | internal edition links each page and screenshot to its keys        |

The **timeline** for a key shows all of this in order. Missing links are shown as gaps and are never filled in by AI. Links the AI suggests are marked as inferred until a person confirms them.

## 9. Checks

A PR check fails when:

- a `req:` link points to a key that does not exist;
- a key is defined twice;
- a new reference points to a retired item (it should point to the replacement).

It also warns when an AI edit has removed an existing `req:` link.

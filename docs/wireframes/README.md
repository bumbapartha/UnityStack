# Wireframes

[← Documentation index](../README.md)

Wireframes for UnityStack's screens and the journey of the projects it manages, drawn with [Excalidraw](https://github.com/excalidraw/excalidraw) (open source). Each file is plain JSON, so changes are reviewed in pull requests like any other document.

## Opening the files locally

Nothing needs uploading to a website.

1. Install the **Excalidraw** extension for VS Code (`pomdtr.excalidraw-editor`). When you open this repository, VS Code suggests it automatically through `.vscode/extensions.json`.
   You can also run `code --install-extension pomdtr.excalidraw-editor`.
2. Open any `.excalidraw` file in this folder; it opens as a drawing board inside VS Code.
3. Edit, save, commit on a branch and open a PR as usual.

## Files

| File | Shows | Requirements |
|---|---|---|
| [00-journey-overview.excalidraw](00-journey-overview.excalidraw) | The journey of a project that UnityStack manages (example: a demo to-do app), on one page in five lanes: requirements; development and testing; deployment and release (deployment target set-up, release, image, staging and production on AWS); monitoring and feedback from staging and production, with the bug going back round the loop; and the Change ID traceability strip. Shows who decides at each step and the requirement IDs. | [All phases](../requirements/README.md) |
| [01-requirements.excalidraw](01-requirements.excalidraw) | Requirements phase for the example project TaskFlow, in six screens: New Project inception, confirm and create, connect a Git repository, the requirements workspace (chat, preview, diff), finalise → pull request with status, and planning epics and stories through to OpenProject. Each screen has a yellow note with the requirement IDs. | [01-requirements](../requirements/01-requirements.md), [10-core-platform](../requirements/10-core-platform.md) |

Planned per-phase files, numbered to match the [requirement files](../requirements/README.md): `03-development.excalidraw`, `04-testing.excalidraw`, `05-deployment.excalidraw`, `06-monitoring.excalidraw`, and a `10-core-platform.excalidraw` for the shared two-pane workspace.

## Conventions

- One file per phase, plus the overview. Small files open quickly and keep PR reviews short.
- Each box names the requirement IDs it covers. Each requirement file links back to its wireframe once one exists.
- Colours follow the legend in the overview: blue requirements, green development, yellow testing, orange deployment, red monitoring, purple traceability, grey for external tools reached through adapters.
- An orange tag marks a point where a person decides (confirm, finalise, review, accept). Dashed boxes are after the MVP.
- Wireframes show layout and flow, not final visual design. Detailed UX design belongs in Penpot later.

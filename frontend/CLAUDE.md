# Frontend guidelines

Read the root `CLAUDE.md` first. This file covers the Vue workspace.

## Stack

Vue 3 with `<script setup>` single-file components, Vite, Vue Router, plain JavaScript (ES modules). In development, Vite proxies `/api` to the backend.

## Module layout (AD-5)

```text
src/
  core/                 shared by every module; imports no feature module
    http.js             the only place that calls fetch(); returns JSON or throws an Error with the API message
    router.js           creates the router from each module's routes
    auth/               sign-in (Keycloak) and current user
    workspace/          shared two-pane components: chat, read-only preview, diff, revision history, finalise
    ui/                 shared UI components and styles
  modules/
    <module>/           requirements | design | development | testing | deployment |
                        monitoring | security | communication | traceability
      index.js          the module's public surface: routes and anything other modules may use
      routes.js         the module's routes, under /<module>/... where it makes sense
      api.js            calls to the matching backend module (/api/<module>/...) via core/http.js
      views/            pages
      components/       components used only by this module
```

## Module rules

1. **Public surface only.** Import another module only through its `index.js`. Never reach into another module's `views/`, `components/` or `api.js`. A lint rule (for example `eslint-plugin-boundaries`) will enforce this.
2. **Core is shared.** Anything two modules need goes in `core/`. Shared components stay generic and hold no module-specific logic.
3. **Each module talks to its own backend module.** Never call another module's backend endpoints directly; go through that module's `index.js`.
4. **The two-pane workspace is shared.** Every AI workspace uses `core/workspace` components: chat on the left, a read-only preview on the right, with revisions, diff and an explicit finalise action. There are no manual artifact editors in the MVP.

## Conventions

- Use the Composition API with `<script setup>`; keep state in `ref` and `computed` within the component, or in a small module-level composable when several views share it.
- Keep API calls out of components: views call functions from the module's `api.js`.
- Show API errors to the user in plain language. Never swallow them.
- Accessibility (NFR-012): use real buttons and links, labelled form controls, visible focus and keyboard access. Do not rely on colour alone.
- Never put secrets or API keys in frontend code or `VITE_` variables.

## Tests and checks

- Unit tests use Vitest (to be added) and sit next to the code as `*.spec.js`.
- End-to-end tests use Playwright against the running stack.
- Run `npm run build` before opening a PR, plus tests once they exist.

## Current state

The inception screens live in `modules/requirements/`. `main.js` passes each module's `index.js` to `core/router.js`. The app shell (`App.vue`) and global styles (`core/ui/style.css`) are in `core/`. The boundary lint rule is not added yet.

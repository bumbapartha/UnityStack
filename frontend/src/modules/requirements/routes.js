import ProjectsView from './views/ProjectsView.vue'
import InceptionView from './views/InceptionView.vue'
import ProjectView from './views/ProjectView.vue'

// Existing paths are kept as they are, so links and bookmarks keep working.
export const routes = [
  { path: '/', component: ProjectsView },
  { path: '/projects/new', component: InceptionView },
  { path: '/projects/:id', component: ProjectView, props: true },
]

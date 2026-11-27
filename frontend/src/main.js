import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import ProjectsView from './views/ProjectsView.vue'
import InceptionView from './views/InceptionView.vue'
import ProjectView from './views/ProjectView.vue'
import './style.css'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: ProjectsView },
    { path: '/projects/new', component: InceptionView },
    { path: '/projects/:id', component: ProjectView, props: true },
  ],
})

createApp(App).use(router).mount('#app')

import { createRouter as createVueRouter, createWebHistory } from 'vue-router'

// Builds the app router from each feature module's routes (AD-5).
export function createRouter(modules) {
  return createVueRouter({
    history: createWebHistory(),
    routes: modules.flatMap((m) => m.routes),
  })
}

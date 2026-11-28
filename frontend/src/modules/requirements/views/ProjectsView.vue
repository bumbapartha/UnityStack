<script setup>
import { onMounted, ref } from 'vue'
import { api } from '../api.js'
import { PRODUCT_TYPES } from '../charter.js'

const projects = ref([])
const error = ref(null)
const loading = ref(true)

onMounted(async () => {
  try {
    projects.value = await api.listProjects()
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="page">
    <div class="page-head">
      <h1>Projects</h1>
      <router-link to="/projects/new" class="btn primary" title="New Project">＋ New Project</router-link>
    </div>
    <p v-if="error" class="error">{{ error }}</p>
    <p v-else-if="loading" class="muted">Loading…</p>
    <div v-else-if="!projects.length" class="empty-state">
      <p>No projects yet.</p>
      <router-link to="/projects/new" class="btn primary">＋ Start your first project</router-link>
    </div>
    <ul v-else class="project-list">
      <li v-for="p in projects" :key="p.id">
        <router-link :to="`/projects/${p.id}`">
          <span class="key">{{ p.key }}</span>
          <span class="name">{{ p.name }}</span>
          <span class="muted">{{ PRODUCT_TYPES[p.productType] || p.productType }}</span>
          <span class="status">{{ p.status }}</span>
        </router-link>
      </li>
    </ul>
  </div>
</template>

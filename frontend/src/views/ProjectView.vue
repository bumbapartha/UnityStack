<script setup>
import { onMounted, ref } from 'vue'
import { api } from '../api.js'
import CharterPreview from '../components/CharterPreview.vue'

const props = defineProps({ id: String })
const detail = ref(null)
const error = ref(null)
const form = ref({ name: '', key: '', status: 'ACTIVE' })
const saving = ref(false)
const saveMsg = ref(null)

function load(d) {
  detail.value = d
  form.value = { name: d.project.name, key: d.project.key, status: d.project.status }
}

onMounted(async () => {
  try { load(await api.getProject(props.id)) }
  catch (e) { error.value = e.message }
})

async function save() {
  saving.value = true
  saveMsg.value = null
  try {
    load(await api.updateProject(props.id, form.value))
    saveMsg.value = 'Saved'
  } catch (e) {
    saveMsg.value = e.message
  } finally {
    saving.value = false
  }
}

function describe(a) {
  if (a.action === 'PROJECT_CREATED') return `Created project ${a.details.key}`
  if (a.action === 'PROJECT_SETTINGS_CHANGED') {
    return 'Changed ' + Object.entries(a.details).map(([k, v]) => `${k}: ${v.from} → ${v.to}`).join(', ')
  }
  return a.action
}
</script>

<template>
  <div class="page">
    <p v-if="error" class="error">{{ error }}</p>
    <template v-else-if="detail">
      <div class="page-head">
        <h1>{{ detail.project.name }} <span class="key">{{ detail.project.key }}</span></h1>
        <span class="status">{{ detail.project.status }}</span>
      </div>
      <div class="project-grid">
        <div>
          <CharterPreview :charter="detail.charter" :open-questions="detail.openQuestions" :project-key="detail.project.key" />
          <p class="muted">Charter revision {{ detail.charterRevision }} · Next steps (coming soon): connect a GitHub repository, start the requirements workspace.</p>
        </div>
        <aside>
          <form class="card settings" @submit.prevent="save">
            <h3>Settings</h3>
            <label>Name <input v-model="form.name" maxlength="200" /></label>
            <label>Key <input v-model="form.key" maxlength="10" /></label>
            <label>Status
              <select v-model="form.status">
                <option>ACTIVE</option><option>ON_HOLD</option><option>ARCHIVED</option>
              </select>
            </label>
            <button class="btn primary" :disabled="saving">Save</button>
            <span v-if="saveMsg" class="muted">{{ saveMsg }}</span>
          </form>
          <div class="card">
            <h3>Activity</h3>
            <ul class="activity">
              <li v-for="(a, i) in detail.activity" :key="i">
                <span>{{ describe(a) }}</span>
                <span class="muted">{{ a.actor }} · {{ new Date(a.createdAt).toLocaleString() }}</span>
              </li>
            </ul>
          </div>
        </aside>
      </div>
    </template>
    <p v-else class="muted">Loading…</p>
  </div>
</template>

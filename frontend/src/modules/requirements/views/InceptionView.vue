<script setup>
import { computed, nextTick, onMounted, ref } from 'vue'
import { onBeforeRouteLeave, useRouter } from 'vue-router'
import { api } from '../api.js'
import { MANDATORY } from '../charter.js'
import CharterPreview from '../components/CharterPreview.vue'

const router = useRouter()
const state = ref({ history: [], charter: {}, openQuestions: [] })
const missing = ref([...MANDATORY])
const ready = ref(false)
const input = ref('')
const busy = ref(false)
const creating = ref(false)
const created = ref(false)
const error = ref(null)
const chatEl = ref(null)

const hasUserInput = computed(() => state.value.history.some((m) => m.role === 'user'))

async function send(message) {
  busy.value = true
  error.value = null
  try {
    const r = await api.inceptionTurn(state.value, message)
    state.value = { history: r.history, charter: r.charter, openQuestions: r.openQuestions }
    missing.value = r.missing
    ready.value = r.ready
    if (message) input.value = ''
  } catch (e) {
    error.value = e.message
  } finally {
    busy.value = false
    await nextTick()
    chatEl.value?.scrollTo({ top: chatEl.value.scrollHeight, behavior: 'smooth' })
  }
}

function submit() {
  const text = input.value.trim()
  if (text && !busy.value) send(text)
}

async function create() {
  creating.value = true
  error.value = null
  try {
    const p = await api.createProject({
      charter: state.value.charter,
      openQuestions: state.value.openQuestions,
      transcript: state.value.history,
    })
    created.value = true
    router.push(`/projects/${p.project.id}`)
  } catch (e) {
    error.value = e.message
  } finally {
    creating.value = false
  }
}

function discard() {
  router.push('/')
}

// FR-105: nothing is stored until confirmation, so leaving discards the conversation.
onBeforeRouteLeave(() => {
  if (created.value || !hasUserInput.value) return true
  return window.confirm('Discard this new project? Nothing has been saved yet.')
})

onMounted(() => send(null))
</script>

<template>
  <div class="workspace">
    <section class="pane chat-pane">
      <h1>New Project</h1>
      <div ref="chatEl" class="chat">
        <div v-for="(m, i) in state.history" :key="i" :class="['msg', m.role]">{{ m.content }}</div>
        <div v-if="busy" class="msg assistant typing">…</div>
      </div>
      <p v-if="error" class="error">{{ error }}</p>
      <form class="composer" @submit.prevent="submit">
        <textarea
          v-model="input"
          rows="2"
          placeholder="Type your answer…"
          :disabled="busy || creating"
          @keydown.enter.exact.prevent="submit"
        />
        <button class="btn primary" :disabled="busy || !input.trim()">Send</button>
      </form>
    </section>

    <section class="pane preview-pane">
      <div class="readiness" :class="ready ? 'ok' : 'pending'">
        <template v-if="ready">Ready to create</template>
        <template v-else
          >{{ MANDATORY.length - missing.length }} of {{ MANDATORY.length }} required items captured</template
        >
      </div>
      <CharterPreview :charter="state.charter" :open-questions="state.openQuestions" :missing="missing" />
      <div class="actions">
        <button class="btn" :disabled="creating" @click="discard">Discard</button>
        <button class="btn primary" :disabled="!ready || busy || creating" @click="create">
          {{ creating ? 'Creating…' : 'Create project' }}
        </button>
      </div>
    </section>
  </div>
</template>

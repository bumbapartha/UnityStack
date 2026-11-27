<script setup>
import { computed } from 'vue'
import { PRODUCT_TYPES, SCALES } from '../api.js'

const props = defineProps({
  charter: { type: Object, default: () => ({}) },
  openQuestions: { type: Array, default: () => [] },
  missing: { type: Array, default: null }, // null = don't show required markers
  projectKey: { type: String, default: null },
})

const productType = computed(() => {
  const c = props.charter || {}
  if (!c.productType) return null
  const label = PRODUCT_TYPES[c.productType] || c.productType
  const details = c.productTypeDetails
  return details && details.toLowerCase() !== label.toLowerCase() ? `${label} — ${details}` : label
})

const rows = computed(() => {
  const c = props.charter || {}
  return [
    { field: 'name', label: 'Project name', value: c.name, required: true },
    { field: 'problem', label: 'Problem', value: c.problem, required: true },
    { field: 'targetUsers', label: 'Target users', value: c.targetUsers, required: true },
    { field: 'productType', label: 'Product type', value: productType.value, required: true },
    { field: 'expectedBenefit', label: 'Expected benefit', value: c.expectedBenefit, required: true },
    { field: 'scale', label: 'Scale', value: c.scale ? SCALES[c.scale] || c.scale : null, required: true },
    { field: 'motivation', label: 'Why now', value: c.motivation },
    { field: 'constraints', label: 'Constraints', value: c.constraints },
    { field: 'outOfScope', label: 'Out of scope', value: c.outOfScope },
  ]
})
</script>

<template>
  <article class="charter">
    <h2>{{ charter?.name || 'New project' }} <small>Project Charter</small></h2>
    <p v-if="projectKey" class="muted">Key: <strong>{{ projectKey }}</strong></p>
    <dl>
      <template v-for="r in rows" :key="r.field">
        <dt>
          {{ r.label }}
          <span v-if="missing && r.required" :class="missing.includes(r.field) ? 'tag tag-todo' : 'tag tag-done'">
            {{ missing.includes(r.field) ? 'required' : '✓' }}
          </span>
        </dt>
        <dd :class="{ empty: !r.value }">{{ r.value || 'Not yet captured' }}</dd>
      </template>
    </dl>
    <section v-if="openQuestions.length">
      <h3>Open questions</h3>
      <ul><li v-for="q in openQuestions" :key="q">{{ q }}</li></ul>
    </section>
  </article>
</template>

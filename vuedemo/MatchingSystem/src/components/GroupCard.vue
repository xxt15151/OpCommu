<template>
  <div class="group-card" @click="handleClick">
    <div class="group-header">
      <h4 class="group-name">{{ group.name }}</h4>
      <span class="group-count">{{ operatorCount }}个算子</span>
    </div>
    <div class="group-meta">
      <span class="date">{{ formatDate(group.createdAt) }}</span>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  group: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['click'])

const operatorCount = computed(() => {
  try {
    const raw = props.group.operatorIds
    if (!raw) return 0
    if (Array.isArray(raw)) return raw.length
    if (typeof raw === 'string') {
      return JSON.parse(raw).length
    }
    return 0
  } catch {
    return 0
  }
})

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const handleClick = () => {
  console.log('GroupCard clicked:', props.group)
  emit('click', props.group)
}
</script>

<style scoped>
/* 样式保持不变 */
.group-card {
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: var(--spacing-md);
  cursor: pointer;
  transition: all 0.2s;
}

.group-card:hover {
  border-color: var(--primary);
  transform: translateY(-2px);
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xs);
}

.group-name {
  font-size: var(--text-title-sm);
  color: var(--ink);
}

.group-count {
  font-size: var(--text-caption);
  color: var(--muted);
}

.group-meta {
  font-size: var(--text-caption);
  color: var(--muted);
}
</style>
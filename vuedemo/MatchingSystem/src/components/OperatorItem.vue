<template>
  <div class="operator-item" :class="{ selected }" @click="$emit('click')">
    <div class="operator-name">{{ operator.name }}</div>
    <div class="operator-attrs">
      <span v-for="(value, key) in operator.attributes" :key="key" class="attr-tag">
        {{ formatKey(key) }}: {{ value }}
      </span>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  operator: {
    type: Object,
    required: true
  },
  selected: {
    type: Boolean,
    default: false
  }
})

defineEmits(['click'])

// 简单将英文字段名转为中文（可选）
const formatKey = (key) => {
  const map = {
    attack: '攻击力',
    defense: '防御力',
    hp: '生命值',
    speed: '速度',
    crit_rate: '暴击率',
    crit_damage: '暴击伤害'
  }
  return map[key] || key
}
</script>

<style scoped>
.operator-item {
  padding: var(--spacing-md);
  border: 1px solid var(--hairline);
  cursor: pointer;
  transition: all 0.2s;
  background: var(--canvas);
}

.operator-item:hover {
  border-color: var(--primary);
}

.operator-item.selected {
  border: 2px solid var(--primary);
  background: var(--surface-card);
}

.operator-name {
  font-weight: var(--font-bold);
  font-size: var(--text-title-sm);
  margin-bottom: var(--spacing-xs);
  color: var(--ink);
}

.operator-attrs {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-xs);
}

.attr-tag {
  font-size: var(--text-caption);
  color: var(--muted);
  background: var(--surface-soft);
  padding: 2px 6px;
}
</style>
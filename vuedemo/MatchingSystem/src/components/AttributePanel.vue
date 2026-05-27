<template>
  <div class="attribute-panel">
    <h3 class="panel-title">属性总览</h3>
    <div v-if="Object.keys(mergedAttributes).length === 0" class="empty-state">
      暂无算子，请添加算子到组中
    </div>
    <div v-else class="attribute-list">
      <div v-for="(value, key) in mergedAttributes" :key="key" class="attribute-item">
        <span class="attr-key">{{ formatKey(key) }}</span>
        <span class="attr-value">{{ formatValue(value) }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  operators: {
    type: Array,
    default: () => []
  }
})

// 合并所有算子的属性（直接使用对象格式）
const mergedAttributes = computed(() => {
  const result = {}

  props.operators.forEach(operator => {
    const attributes = operator.attributes || {}
    Object.entries(attributes).forEach(([key, value]) => {
      if (result[key] === undefined) {
        result[key] = value
      } else {
        // 数值累加，字符串拼接
        if (typeof value === 'number' && typeof result[key] === 'number') {
          result[key] += value
        } else {
          result[key] = `${result[key]}, ${value}`
        }
      }
    })
  })

  return result
})

// 字段名中文化
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

// 格式化数值显示
const formatValue = (value) => {
  if (typeof value === 'number') {
    return value.toFixed(2)
  }
  return value
}
</script>

<style scoped>
.attribute-panel {
  background: var(--surface-card);
  border: 1px solid var(--hairline);
  padding: var(--spacing-lg);
  margin-top: var(--spacing-lg);
}

.panel-title {
  font-size: var(--text-title-md);
  margin-bottom: var(--spacing-md);
  padding-bottom: var(--spacing-sm);
  border-bottom: 1px solid var(--hairline);
}

.empty-state {
  color: var(--muted);
  text-align: center;
  padding: var(--spacing-xl);
}

.attribute-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.attribute-item {
  display: flex;
  justify-content: space-between;
  padding: var(--spacing-xs) 0;
  border-bottom: 1px solid var(--hairline);
}

.attr-key {
  font-weight: var(--font-regular);
  color: var(--muted);
  text-transform: uppercase;
  font-size: var(--text-caption);
  letter-spacing: 0.5px;
}

.attr-value {
  font-weight: var(--font-bold);
  color: var(--ink);
  font-size: var(--text-title-sm);
}
</style>
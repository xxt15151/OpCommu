<template>
  <div class="calculator">
    <div class="container calculator-container">
      <!-- 左侧：算子列表 -->
      <div class="left-panel">
        <div class="panel-header">
          <h3>算子库</h3>
          <input 
            type="text" 
            v-model="searchQuery" 
            placeholder="搜索算子..." 
            class="search-input"
          />
        </div>
        <div class="operator-list">
          <OperatorItem
            v-for="op in filteredOperators"
            :key="op.id"
            :operator="op"
            :selected="draftOperatorIds.includes(op.id)"
            @click="toggleOperator(op.id)"
          />
          <div v-if="filteredOperators.length === 0" class="empty">
            未找到算子
          </div>
        </div>
      </div>
      
      <!-- 右侧：组和属性面板 -->
      <div class="right-panel">
        <div class="groups-section">
          <div class="section-header">
            <h3>我的搭配组</h3>
            <div class="group-actions">
              <input 
                type="text" 
                v-model="newGroupName" 
                placeholder="新组名称" 
                class="group-name-input"
              />
              <Button @click="saveCurrentGroup" :disabled="!canSave">保存为新组</Button>
            </div>
          </div>
          
          <div class="group-list">
            <GroupCard
              v-for="group in userGroups"
              :key="group.id"
              :group="group"
              @click="loadGroupToDraft"
            />
            <div v-if="userGroups.length === 0" class="empty">
              暂无搭配组，创建一个吧
            </div>
          </div>
        </div>
        
        <div class="current-group-section">
          <div class="section-header">
            <h3>当前编辑组</h3>
            <span class="operator-count">{{ draftOperatorIds.length }}个算子</span>
          </div>
          
          <div class="current-operators">
            <div v-for="op in currentOperators" :key="op.id" class="current-operator">
              <span>{{ op.name }}</span>
              <button class="remove-btn" @click="removeOperator(op.id)">×</button>
            </div>
            <div v-if="currentOperators.length === 0" class="empty-tip">
              点击左侧算子添加到组
            </div>
          </div>
          
          <AttributePanel :operators="currentOperators" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useOperatorStore } from '@/stores/operatorStore'
import { useGroupStore } from '@/stores/groupStore'
import Button from '@/components/Button.vue'
import OperatorItem from '@/components/OperatorItem.vue'
import GroupCard from '@/components/GroupCard.vue'
import AttributePanel from '@/components/AttributePanel.vue'

const route = useRoute()
const userStore = useUserStore()
const operatorStore = useOperatorStore()
const groupStore = useGroupStore()

const searchQuery = ref('')
const newGroupName = ref('')
const draftOperatorIds = ref([])

const filteredOperators = computed(() => {
  if (!searchQuery.value) return operatorStore.operators
  const query = searchQuery.value.toLowerCase()
  return operatorStore.operators.filter(op => 
    op.name.toLowerCase().includes(query)
  )
})

const currentOperators = computed(() => {
  return draftOperatorIds.value
    .map(id => operatorStore.getOperatorById(id))
    .filter(Boolean)
})

const userGroups = computed(() => groupStore.groups)

const canSave = computed(() => {
  return newGroupName.value.trim() && draftOperatorIds.value.length > 0 && userStore.isAuthenticated
})

const toggleOperator = (operatorId) => {
  const index = draftOperatorIds.value.indexOf(operatorId)
  if (index > -1) {
    draftOperatorIds.value.splice(index, 1)
  } else {
    draftOperatorIds.value.push(operatorId)
  }
}

const removeOperator = (operatorId) => {
  const index = draftOperatorIds.value.indexOf(operatorId)
  if (index > -1) {
    draftOperatorIds.value.splice(index, 1)
  }
}

const saveCurrentGroup = async () => {
  if (!canSave.value) {
    alert('请填写组名并至少选择一个算子')
    return
  }
  
  try {
    await groupStore.saveGroup(newGroupName.value, draftOperatorIds.value)
    alert('保存成功')
    newGroupName.value = ''
    draftOperatorIds.value = []
    await loadUserGroups()
  } catch (error) {
    alert('保存失败：' + error.message)
  }
}

// 超强健壮的 operatorIds 解析器
const parseOperatorIds = (raw) => {
  if (!raw) return []
  if (Array.isArray(raw)) return raw
  
  if (typeof raw === 'string') {
    let trimmed = raw.trim()
    if (trimmed === '') return []
    
    // 递归去除外层多余引号
    let parsed = trimmed
    let changed = true
    while (changed && typeof parsed === 'string') {
      changed = false
      const temp = parsed.trim()
      if ((temp.startsWith('"') && temp.endsWith('"')) || 
          (temp.startsWith("'") && temp.endsWith("'"))) {
        parsed = temp.slice(1, -1)
        changed = true
      }
    }
    
    // 如果最终结果是字符串，尝试 JSON 解析
    if (typeof parsed === 'string') {
      try {
        const result = JSON.parse(parsed)
        if (Array.isArray(result)) return result
        console.warn('解析结果不是数组，尝试提取数字:', result)
        // 如果不是数组但可能是数字数组字符串如 "[1,2,3]"
        if (typeof result === 'string') {
          // 再次递归
          return parseOperatorIds(result)
        }
        return []
      } catch (e) {
        console.error('JSON.parse 失败，尝试正则提取数字:', parsed, e)
        // 最后手段：正则匹配所有数字
        const matches = parsed.match(/\d+/g)
        if (matches) return matches.map(Number)
        return []
      }
    }
    
    if (Array.isArray(parsed)) return parsed
    return []
  }
  
  console.warn('未知的 operatorIds 类型:', typeof raw, raw)
  return []
}

// 加载组到当前编辑区（不弹 alert，仅日志）
const loadGroupToDraft = (group) => {
  console.group('加载搭配组')
  console.log('组对象:', group)
  if (!group) {
    console.error('group 为空')
    console.groupEnd()
    return
  }
  
  const operatorIds = parseOperatorIds(group.operatorIds)
  console.log('解析后的 operatorIds:', operatorIds)
  
  if (operatorIds.length === 0 && group.operatorIds) {
    console.warn(`⚠️ 组 "${group.name}" 的 operatorIds 解析为空，原始值:`, group.operatorIds, typeof group.operatorIds)
    // 不清空当前编辑区，保持原有内容，但提示用户
    // 可考虑使用 toast，此处仅控制台输出
    console.warn('已保持当前编辑区内容不变')
  } else {
    // 直接替换当前编辑区的算子ID列表
    draftOperatorIds.value = [...operatorIds]
    newGroupName.value = group.name + ' (复制)'
    console.log(`✅ 成功加载 ${operatorIds.length} 个算子到编辑区`)
  }
  console.groupEnd()
}

// 根据ID加载（供URL参数使用）
const loadGroupById = (groupId) => {
  const group = groupStore.groups.find(g => g.id === parseInt(groupId))
  if (group) {
    loadGroupToDraft(group)
  } else {
    console.warn('未找到组ID:', groupId)
  }
}

const loadUserGroups = async () => {
  if (userStore.isAuthenticated) {
    await groupStore.fetchUserGroups(userStore.userId)
    console.log('用户组加载完成，数量:', groupStore.groups.length)
    // 打印每个组的 operatorIds 原始类型供调试
    groupStore.groups.forEach(g => {
      console.log(`组 "${g.name}" operatorIds 原始值:`, g.operatorIds, typeof g.operatorIds)
    })
  } else {
    console.log('用户未登录，无法加载组')
  }
}

onMounted(async () => {
  await operatorStore.fetchOperators()
  await loadUserGroups()
  if (route.query.groupId) {
    loadGroupById(route.query.groupId)
  }
})

watch(() => route.query.groupId, (newId) => {
  if (newId) {
    loadGroupById(newId)
  }
})
</script>

<style scoped>
.calculator {
  min-height: calc(100vh - 64px);
  background: var(--surface-soft);
}

.calculator-container {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: var(--spacing-xl);
  padding-top: var(--spacing-xl);
  padding-bottom: var(--spacing-xl);
}

.left-panel, .right-panel {
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: var(--spacing-lg);
}

.panel-header {
  margin-bottom: var(--spacing-lg);
}

.panel-header h3 {
  margin-bottom: var(--spacing-md);
}

.search-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--hairline);
  background: var(--canvas);
  color: var(--ink);
  font-size: var(--text-body-md);
  border-radius: var(--radius-none);
}

.operator-list {
  max-height: 600px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.groups-section {
  margin-bottom: var(--spacing-xl);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
  flex-wrap: wrap;
  gap: var(--spacing-sm);
}

.group-actions {
  display: flex;
  gap: var(--spacing-sm);
  align-items: center;
}

.group-name-input {
  padding: 8px 12px;
  border: 1px solid var(--hairline);
  background: var(--canvas);
  color: var(--ink);
  font-size: var(--text-body-sm);
  border-radius: var(--radius-none);
}

.group-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
  max-height: 300px;
  overflow-y: auto;
}

.current-group-section {
  margin-top: var(--spacing-xl);
}

.operator-count {
  font-size: var(--text-body-sm);
  color: var(--muted);
}

.current-operators {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-lg);
  padding: var(--spacing-md);
  background: var(--surface-soft);
  min-height: 100px;
}

.current-operator {
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: 6px 12px;
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-size: var(--text-body-sm);
}

.remove-btn {
  background: none;
  border: none;
  cursor: pointer;
  color: var(--error);
  font-size: 18px;
  padding: 0 4px;
}

.empty-tip {
  color: var(--muted);
  text-align: center;
  width: 100%;
}

.empty {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--muted);
}

@media (max-width: 768px) {
  .calculator-container {
    grid-template-columns: 1fr;
  }
}
</style>
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getOperators } from '@/api/operators'

export const useOperatorStore = defineStore('operator', () => {
  const operators = ref([])
  const loading = ref(false)

  // 解析后端返回的畸形 attributes 字符串
  const parseAttributes = (attrs) => {
    if (!attrs) return {}
    // 已经是对象，直接返回
    if (typeof attrs === 'object') return attrs
    if (typeof attrs !== 'string') return {}

    let clean = attrs.trim()
    // 如果整体被双引号包裹，先剥离外层引号
    if (clean.startsWith('"') && clean.endsWith('"')) {
      try {
        // 尝试作为 JSON 字符串解析（去除外层引号）
        clean = JSON.parse(clean)
      } catch {
        // 手动去除首尾双引号
        clean = clean.slice(1, -1)
      }
    }
    // 如果此时还是字符串，需要再次解析
    if (typeof clean === 'string') {
      // 移除可能存在的转义换行符
      clean = clean.replace(/\\n/g, '')
      try {
        return JSON.parse(clean)
      } catch (e) {
        console.warn('解析 attributes JSON 失败:', clean, e)
        return {}
      }
    }
    return clean
  }

  const fetchOperators = async () => {
    loading.value = true
    try {
      const response = await getOperators()
      // 预处理每个算子的 attributes 字段
      operators.value = response.data.map(op => ({
        ...op,
        attributes: parseAttributes(op.attributes)
      }))
    } catch (error) {
      console.error('获取算子失败:', error)
    } finally {
      loading.value = false
    }
  }

  const getOperatorById = (id) => {
    return operators.value.find(op => op.id === id)
  }

  return {
    operators,
    loading,
    fetchOperators,
    getOperatorById
  }
})
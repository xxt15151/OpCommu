import { defineStore } from 'pinia'
import { ref } from 'vue'
import { createGroup, getAllGroups, getUserGroups } from '@/api/groups'

export const useGroupStore = defineStore('group', () => {
  const groups = ref([])
  const loading = ref(false)

  const fetchAllGroups = async () => {
    loading.value = true
    try {
      const response = await getAllGroups()
      groups.value = response.data
    } catch (error) {
      console.error('获取所有组失败:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchUserGroups = async (userId) => {
    loading.value = true
    try {
      const response = await getUserGroups(userId)
      groups.value = response.data
      // 可选：确保每个组的 operatorIds 是原始类型（不处理）
    } catch (error) {
      console.error('获取用户组失败:', error)
    } finally {
      loading.value = false
    }
  }

  const saveGroup = async (name, operatorIds) => {
    const response = await createGroup(name, operatorIds)
    return response.data
  }

  return {
    groups,
    loading,
    fetchAllGroups,
    fetchUserGroups,
    saveGroup
  }
})
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const userId = ref(null)
  const username = ref(null)
  const email = ref(null)
  const role = ref(null)

  const isAuthenticated = computed(() => !!userId.value)

  const setUser = (user) => {
    userId.value = user.id
    username.value = user.username
    email.value = user.email
    role.value = user.role
    localStorage.setItem('user', JSON.stringify(user))
  }

  const clearUser = () => {
    userId.value = null
    username.value = null
    email.value = null
    role.value = null
    localStorage.removeItem('user')
  }

  const login = async (username, password) => {
    const response = await loginApi(username, password)
    const user = response.data
    setUser(user)
    return user
  }

  const register = async (username, password, email) => {
    const response = await registerApi(username, password, email)
    const user = response.data
    setUser(user)
    return user
  }

  const logout = () => {
    clearUser()
  }

  const loadFromStorage = () => {
    const stored = localStorage.getItem('user')
    if (stored) {
      const user = JSON.parse(stored)
      setUser(user)
    }
  }

  return {
    userId,
    username,
    email,
    role,
    isAuthenticated,
    login,
    register,
    logout,
    loadFromStorage
  }
})
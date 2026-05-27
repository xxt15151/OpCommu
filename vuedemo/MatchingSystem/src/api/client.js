import axios from 'axios'
import { useUserStore } from '@/stores/userStore'

const client = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 请求拦截器 - 添加用户ID头
client.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.userId) {
    config.headers['X-User-Id'] = userStore.userId
  }
  return config
})

// 响应拦截器 - 错误处理
client.interceptors.response.use(
  response => response,
  error => {
    const message = error.response?.data || error.message || '请求失败'
    console.error('API Error:', message)
    return Promise.reject(error)
  }
)

export default client
<template>
  <div class="auth-page">
    <div class="container">
      <div class="auth-card">
        <h2>注册</h2>
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <input type="text" v-model="username" placeholder="用户名" required class="text-input" />
          </div>
          <div class="form-group">
            <input type="email" v-model="email" placeholder="邮箱" required class="text-input" />
          </div>
          <div class="form-group">
            <input type="password" v-model="password" placeholder="密码" required class="text-input" />
          </div>
          <Button type="submit" :disabled="loading">注册</Button>
          <p class="auth-link">
            已有账号？ <router-link to="/login">立即登录</router-link>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import Button from '@/components/Button.vue'

const router = useRouter()
const userStore = useUserStore()

const username = ref('')
const email = ref('')
const password = ref('')
const loading = ref(false)

const handleRegister = async () => {
  loading.value = true
  try {
    await userStore.register(username.value, password.value, email.value)
    router.push('/')
  } catch (error) {
    alert('注册失败：' + (error.response?.data || error.message))
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--surface-soft);
}

.auth-card {
  max-width: 400px;
  margin: 0 auto;
  background: var(--canvas);
  padding: var(--spacing-xl);
  border: 1px solid var(--hairline);
}

.auth-card h2 {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.form-group {
  margin-bottom: var(--spacing-md);
}

.text-input {
  width: 100%;
  padding: 14px 16px;
  border: 1px solid var(--hairline);
  background: var(--canvas);
  color: var(--ink);
  font-size: var(--text-body-md);
  border-radius: var(--radius-none);
}

.text-input:focus {
  outline: none;
  border-color: var(--ink);
}

.auth-link {
  text-align: center;
  margin-top: var(--spacing-md);
  color: var(--muted);
}

.auth-link a {
  color: var(--primary);
}
</style>
<template>
  <nav class="navbar">
    <div class="container nav-container">
      <div class="nav-left">
        <router-link to="/" class="logo">
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2"/>
            <path d="M12 2 L12 22 M2 12 L22 12" stroke="currentColor" stroke-width="2"/>
          </svg>
          <span>算子社区</span>
        </router-link>
      </div>
      
      <div class="nav-center">
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/calculator" class="nav-link">搭配计算器</router-link>
        <router-link to="/articles" class="nav-link">文章</router-link>
        <router-link v-if="userStore.isAuthenticated" to="/articles/create" class="nav-link">发布文章</router-link>
        <router-link v-if="userStore.isAuthenticated" to="/profile" class="nav-link">我的</router-link>
      </div>
      
      <div class="nav-right">
        <button @click="toggleDarkMode" class="theme-toggle">
          {{ isDark ? '☀️' : '🌙' }}
        </button>
        
        <template v-if="userStore.isAuthenticated">
          <span class="username">{{ userStore.username }}</span>
          <Button variant="secondary" size="small" @click="handleLogout">登出</Button>
        </template>
        <template v-else>
          <Button variant="secondary" size="small" @click="$router.push('/login')">登录</Button>
          <Button variant="primary" size="small" @click="$router.push('/register')">注册</Button>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import Button from './Button.vue'

const router = useRouter()
const userStore = useUserStore()
const isDark = ref(false)

const toggleDarkMode = () => {
  isDark.value = !isDark.value
  document.documentElement.setAttribute('data-theme', isDark.value ? 'dark' : 'light')
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}

onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    isDark.value = savedTheme === 'dark'
    document.documentElement.setAttribute('data-theme', savedTheme)
  }
})
</script>

<style scoped>
.navbar {
  position: sticky;
  top: 0;
  background: var(--canvas);
  border-bottom: 1px solid var(--hairline);
  z-index: 100;
  height: 64px;
  display: flex;
  align-items: center;
}

.nav-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.nav-left, .nav-center, .nav-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  font-weight: var(--font-bold);
  font-size: 18px;
  color: var(--ink);
}

.nav-link {
  font-size: var(--text-nav-link);
  font-weight: var(--font-regular);
  letter-spacing: 0.3px;
  padding: var(--spacing-xs) 0;
  transition: color 0.2s;
}

.nav-link.router-link-active {
  color: var(--primary);
  font-weight: var(--font-bold);
  border-bottom: 2px solid var(--primary);
}

.nav-link:hover {
  color: var(--primary);
}

.username {
  color: var(--ink);
  font-size: var(--text-body-sm);
}

.theme-toggle {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  padding: var(--spacing-xs);
  border-radius: var(--radius-full);
}

@media (max-width: 768px) {
  .nav-center {
    gap: var(--spacing-md);
  }
  
  .nav-left .logo span {
    display: none;
  }
}
</style>
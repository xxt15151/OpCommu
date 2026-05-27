<template>
  <div id="app">
    <NavBar />
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import NavBar from '@/components/NavBar.vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.loadFromStorage()
})
</script>

<style>
/* 全局样式已在global.css中定义 */
</style>
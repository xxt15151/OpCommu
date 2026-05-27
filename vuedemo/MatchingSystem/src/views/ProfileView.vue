<template>
  <div class="profile">
    <div class="container">
      <div class="profile-header">
        <h1>{{ userStore.username }}</h1>
        <p class="email">{{ userStore.email }}</p>
      </div>
      
      <div class="profile-tabs">
        <button 
          class="tab" 
          :class="{ active: activeTab === 'articles' }"
          @click="activeTab = 'articles'"
        >
          我的文章
        </button>
        <button 
          class="tab" 
          :class="{ active: activeTab === 'groups' }"
          @click="activeTab = 'groups'"
        >
          我的搭配组
        </button>
      </div>
      
      <div v-show="activeTab === 'articles'">
        <div v-if="loadingArticles" class="loading">加载中...</div>
        <div v-else class="grid-4">
          <ArticleCard
            v-for="article in myArticles"
            :key="article.id"
            :article="article"
            @click="goToArticle(article.id)"
          />
        </div>
        <div v-if="myArticles.length === 0 && !loadingArticles" class="empty">
          暂无文章，快去发布吧
        </div>
      </div>
      
      <div v-show="activeTab === 'groups'">
        <div v-if="loadingGroups" class="loading">加载中...</div>
        <div v-else class="groups-grid">
          <div v-for="group in myGroups" :key="group.id" class="group-card">
            <h3>{{ group.name }}</h3>
            <p>创建于：{{ formatDate(group.createdAt) }}</p>
            <Button variant="text-link" @click="viewGroup(group)">查看搭配 →</Button>
          </div>
        </div>
        <div v-if="myGroups.length === 0 && !loadingGroups" class="empty">
          暂无搭配组，去搭配计算器创建一个吧
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useArticleStore } from '@/stores/articleStore'
import { useGroupStore } from '@/stores/groupStore'
import ArticleCard from '@/components/ArticleCard.vue'
import Button from '@/components/Button.vue'

const router = useRouter()
const userStore = useUserStore()
const articleStore = useArticleStore()
const groupStore = useGroupStore()

const activeTab = ref('articles')
const myArticles = ref([])
const myGroups = ref([])
const loadingArticles = ref(false)
const loadingGroups = ref(false)

const loadMyArticles = async () => {
  loadingArticles.value = true
  try {
    myArticles.value = await articleStore.fetchUserArticles(userStore.userId)
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loadingArticles.value = false
  }
}

const loadMyGroups = async () => {
  loadingGroups.value = true
  try {
    await groupStore.fetchUserGroups(userStore.userId)
    myGroups.value = groupStore.groups
  } catch (error) {
    console.error('加载组失败:', error)
  } finally {
    loadingGroups.value = false
  }
}

const goToArticle = (id) => {
  router.push(`/articles/${id}`)
}

// 修改此处：跳转时携带 groupId 查询参数
const viewGroup = (group) => {
  router.push({ path: '/calculator', query: { groupId: group.id } })
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadMyArticles()
  loadMyGroups()
})
</script>

<style scoped>
.profile {
  padding: var(--spacing-xl) 0;
}

.profile-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.email {
  color: var(--muted);
  margin-top: var(--spacing-xs);
}

.profile-tabs {
  display: flex;
  gap: var(--spacing-xl);
  border-bottom: 1px solid var(--hairline);
  margin-bottom: var(--spacing-xl);
}

.tab {
  background: none;
  border: none;
  padding: var(--spacing-sm) var(--spacing-md);
  font-size: var(--text-title-sm);
  font-weight: var(--font-bold);
  color: var(--muted);
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tab.active {
  color: var(--primary);
  border-bottom-color: var(--primary);
}

.groups-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: var(--spacing-lg);
}

.group-card {
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: var(--spacing-lg);
}

.group-card h3 {
  margin-bottom: var(--spacing-sm);
}

.loading, .empty {
  text-align: center;
  padding: var(--spacing-xxl);
  color: var(--muted);
}
</style>
<template>
  <div class="home">
    <div class="hero-band-dark">
      <div class="container hero-content">
        <h1>算子搭配社区</h1>
        <p class="hero-subtitle">分享你的最佳搭配，发现更多可能</p>
        <div class="hero-buttons">
          <Button @click="$router.push('/calculator')">开始搭配</Button>
          <Button variant="secondary-on-dark" @click="$router.push('/articles')">浏览文章</Button>
        </div>
      </div>
    </div>
    
    <div class="container section" ref="articlesSection">
      <div class="section-header">
        <h2>最新分享</h2>
        <Button v-if="userStore.isAuthenticated" variant="text-link" @click="$router.push('/articles/create')">
          发布搭配 →
        </Button>
      </div>
      
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else class="grid-4">
        <ArticleCard 
          v-for="article in articles" 
          :key="article.id"
          :article="article"
          @click="goToArticle(article.id)"
        />
      </div>
      <div v-if="articles.length === 0 && !loading" class="empty">
        暂无文章，快来发布第一篇吧
      </div>
      
      <div v-if="hasMore" class="load-more">
        <Button variant="secondary" @click="loadMore">加载更多</Button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useArticleStore } from '@/stores/articleStore'
import Button from '@/components/Button.vue'
import ArticleCard from '@/components/ArticleCard.vue'

const router = useRouter()
const userStore = useUserStore()
const articleStore = useArticleStore()

const articles = ref([])
const loading = ref(false)
const currentPage = ref(1)
const hasMore = ref(true)

const articlesSection = ref(null)

const loadArticles = async (reset = false) => {
  if (reset) {
    currentPage.value = 1
    articles.value = []
    hasMore.value = true
  }
  
  if (loading.value || !hasMore.value) return
  
  loading.value = true
  try {
    // 使用按时间排序的接口（首页显示最新）
    const response = await articleStore.fetchArticles(currentPage.value)
    const newArticles = articleStore.articles
    if (reset) {
      articles.value = newArticles
    } else {
      articles.value = [...articles.value, ...newArticles]
    }
    hasMore.value = newArticles.length === 10
    currentPage.value++
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

const loadMore = () => {
  loadArticles()
}

const goToArticle = (id) => {
  router.push(`/articles/${id}`)
}

onMounted(() => {
  loadArticles(true)
})
</script>

<style scoped>
/* 样式保持不变 */
.hero-band-dark {
  background: var(--surface-dark);
  padding: var(--spacing-section) 0;
}

.hero-content {
  text-align: center;
  color: var(--on-dark);
}

.hero-content h1 {
  color: var(--on-dark);
  margin-bottom: var(--spacing-md);
}

.hero-subtitle {
  font-size: 18px;
  margin-bottom: var(--spacing-xl);
  color: var(--on-dark-soft);
}

.hero-buttons {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
}

.loading, .empty {
  text-align: center;
  padding: var(--spacing-xxl);
  color: var(--muted);
}

.load-more {
  text-align: center;
  margin-top: var(--spacing-xl);
}
</style>
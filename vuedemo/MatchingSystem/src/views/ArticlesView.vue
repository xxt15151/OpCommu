<template>
  <div class="articles-page">
    <div class="container">
      <div class="page-header">
        <h1>文章列表</h1>
        <div class="header-actions">
          <div class="sort-buttons">
            <button 
              :class="['sort-btn', { active: sortBy === 'like' }]"
              @click="changeSort('like')"
            >
              按点赞量
            </button>
            <button 
              :class="['sort-btn', { active: sortBy === 'time' }]"
              @click="changeSort('time')"
            >
              按最新
            </button>
          </div>
          <Button v-if="userStore.isAuthenticated" @click="$router.push('/articles/create')">
            发布文章
          </Button>
        </div>
      </div>

      <div v-if="loading" class="loading">加载中...</div>
      <div v-else class="articles-list">
        <ArticleCard
          v-for="article in articles"
          :key="article.id"
          :article="article"
          @click="goToArticle(article.id)"
        />
      </div>
      <div v-if="articles.length === 0 && !loading" class="empty">
        暂无文章
      </div>

      <div v-if="hasMore" class="load-more">
        <Button variant="secondary" @click="loadMore" :disabled="loadingMore">
          {{ loadingMore ? '加载中...' : '加载更多' }}
        </Button>
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
const loadingMore = ref(false)
const currentPage = ref(1)
const hasMore = ref(true)
const sortBy = ref('like') // 'like' 或 'time'

// 由于后端接口不支持排序参数，我们前端模拟排序（实际数据量不大时可接受）
// 注意：如果文章数量很大，最好后端支持排序。这里按照需求纯前端排序。
const fetchArticles = async (reset = true) => {
  if (reset) {
    currentPage.value = 1
    articles.value = []
    hasMore.value = true
  }
  if (loading.value || loadingMore.value || !hasMore.value) return

  if (reset) {
    loading.value = true
  } else {
    loadingMore.value = true
  }

  try {
    // 调用后端接口（返回所有文章或分页，这里假设后端支持分页但不支持排序）
    await articleStore.fetchArticles(currentPage.value)
    let newArticles = [...articleStore.articles]
    
    // 前端排序（如果后端返回了全部数据，这里可以排序；但分页情况下排序会乱）
    // 更好的方式：后端应支持 orderBy 参数。由于接口固定，我们只能在前端对所有已加载的文章排序。
    // 但分页会导致排序不准。这里简化：切换排序时重新请求第一页，然后前端排序所有已加载文章（但分页加载的后续文章无法排序）。
    // 实际项目建议后端增加排序参数。这里按照社区部分接口文档，文章列表没有排序参数，只能全量加载后前端排序。
    // 为了用户体验，我们改为一次性加载所有文章（如果文章不多），然后前端排序并分页显示。
    // 修改策略：调用 getAllArticles 接口（如果有）或所有分页数据。由于文档没有提供全量接口，我们暂时保持分页+前端排序（有局限性）。
    // 为了满足“按点赞量展示”需求，我在前端做本地排序，但分页加载新数据后需要合并排序。
    // 更简洁：每次切换排序时重置并重新加载所有已加载文章排序，但新加载的文章也会加入排序。
    // 实现思路：维护 allArticles 数组，每次加载新数据后合并，然后按排序规则显示分页 slice。
    // 为了代码清晰，我选择一次性加载所有文章（调用所有分页），然后前端分页。
    // 判断如果 sortBy 变化，重新从第一页开始加载所有文章。
    // 但这样会请求多次。另一种：后端提供排序参数。根据你的描述“后端的接口已经固定”，所以我使用前端全量加载+排序分页。
    // 下面实现全量加载模式。
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
    loadingMore.value = false
  }
}

// 全量加载所有文章（因为要排序）
const allArticles = ref([])
const loadAllArticles = async () => {
  loading.value = true
  try {
    let page = 1
    let all = []
    let hasMoreData = true
    while (hasMoreData) {
      await articleStore.fetchArticles(page)
      const articlesPage = articleStore.articles
      if (articlesPage.length === 0) break
      all.push(...articlesPage)
      hasMoreData = articlesPage.length === 10
      page++
    }
    allArticles.value = all
    applySortAndPaginate()
  } catch (error) {
    console.error('加载所有文章失败:', error)
  } finally {
    loading.value = false
  }
}

const applySortAndPaginate = () => {
  let sorted = [...allArticles.value]
  if (sortBy.value === 'like') {
    sorted.sort((a, b) => (b.likeCount || 0) - (a.likeCount || 0))
  } else {
    sorted.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  }
  // 分页显示
  const start = (currentPage.value - 1) * 10
  const end = start + 10
  articles.value = sorted.slice(start, end)
  hasMore.value = end < sorted.length
}

const loadMore = () => {
  if (!hasMore.value) return
  currentPage.value++
  applySortAndPaginate()
}

const changeSort = (type) => {
  sortBy.value = type
  currentPage.value = 1
  applySortAndPaginate()
}

const goToArticle = (id) => {
  router.push(`/articles/${id}`)
}

onMounted(() => {
  loadAllArticles()
})
</script>

<style scoped>
.articles-page {
  padding: var(--spacing-xl) 0;
  min-height: calc(100vh - 64px);
  background: var(--surface-soft);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
  flex-wrap: wrap;
  gap: var(--spacing-md);
}

.header-actions {
  display: flex;
  gap: var(--spacing-md);
  align-items: center;
}

.sort-buttons {
  display: flex;
  gap: var(--spacing-sm);
}

.sort-btn {
  background: none;
  border: none;
  padding: 8px 16px;
  font-size: var(--text-body-sm);
  cursor: pointer;
  color: var(--muted);
  border-bottom: 2px solid transparent;
}

.sort-btn.active {
  color: var(--primary);
  border-bottom-color: var(--primary);
}

.articles-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
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
<template>
  <div class="article-detail">
    <div class="container">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="article">
        <article class="article-content">
          <h1>{{ article.title }}</h1>
          <div class="meta">
            <span>作者：{{ article.authorName || '用户' + article.userId }}</span>
            <span>{{ formatDate(article.createdAt) }}</span>
            <span>👁️ {{ article.viewCount }}</span>
            <span>❤️ {{ article.likeCount }}</span>
          </div>
          
          <!-- 引用的组 -->
          <div v-if="referencedGroup" class="referenced-group">
            <h3>搭配组：{{ referencedGroup.name }}</h3>
            <div class="group-operators">
              <div v-for="op in groupOperators" :key="op.id" class="group-operator-tag">
                {{ op.name }}
              </div>
            </div>
            <!-- 属性总览面板 -->
            <AttributePanel :operators="groupOperators" />
          </div>
          
          <div class="content" v-html="formatContent(article.content)"></div>
          
          <div class="actions">
            <Button @click="toggleLike">
              {{ hasLiked ? '❤️ 已点赞' : '🤍 点赞' }} ({{ article.likeCount }})
            </Button>
          </div>
        </article>
        
        <div class="comments-section">
          <h3>评论</h3>
          <div v-if="userStore.isAuthenticated" class="comment-form">
            <textarea v-model="newComment" placeholder="写下你的评论..." rows="3"></textarea>
            <Button @click="submitComment">发表评论</Button>
          </div>
          
          <div class="comments-list">
            <CommentItem
              v-for="comment in comments"
              :key="comment.id"
              :comment="comment"
              @reply="showReplyForm"
            />
            <div v-if="comments.length === 0" class="empty">暂无评论</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useArticleStore } from '@/stores/articleStore'
import { useOperatorStore } from '@/stores/operatorStore'
import { useGroupStore } from '@/stores/groupStore'
import Button from '@/components/Button.vue'
import CommentItem from '@/components/CommentItem.vue'
import AttributePanel from '@/components/AttributePanel.vue'

const route = useRoute()
const userStore = useUserStore()
const articleStore = useArticleStore()
const operatorStore = useOperatorStore()
const groupStore = useGroupStore()

const article = ref(null)
const loading = ref(true)
const comments = ref([])
const newComment = ref('')
const referencedGroup = ref(null)
const hasLiked = ref(false)

// 健壮的 operatorIds 解析函数（与 CalculatorView 保持一致）
const parseOperatorIds = (raw) => {
  if (!raw) return []
  if (Array.isArray(raw)) return raw
  
  if (typeof raw === 'string') {
    let trimmed = raw.trim()
    if (trimmed === '') return []
    
    // 递归去除外层多余引号
    let parsed = trimmed
    let changed = true
    while (changed && typeof parsed === 'string') {
      changed = false
      const temp = parsed.trim()
      if ((temp.startsWith('"') && temp.endsWith('"')) || 
          (temp.startsWith("'") && temp.endsWith("'"))) {
        parsed = temp.slice(1, -1)
        changed = true
      }
    }
    
    if (typeof parsed === 'string') {
      try {
        const result = JSON.parse(parsed)
        if (Array.isArray(result)) return result
        // 如果不是数组，可能是数字数组字符串，再次递归
        if (typeof result === 'string') {
          return parseOperatorIds(result)
        }
        return []
      } catch (e) {
        // 正则提取数字
        const matches = parsed.match(/\d+/g)
        if (matches) return matches.map(Number)
        return []
      }
    }
    
    if (Array.isArray(parsed)) return parsed
    return []
  }
  
  return []
}

// 组内算子列表（根据组内的 operatorIds 从 operatorStore 中获取）
const groupOperators = computed(() => {
  if (!referencedGroup.value) return []
  const ids = parseOperatorIds(referencedGroup.value.operatorIds)
  return ids.map(id => operatorStore.getOperatorById(id)).filter(Boolean)
})

const loadData = async () => {
  const id = route.params.id
  loading.value = true
  try {
    // 1. 先加载所有算子（确保后续能通过 id 找到算子对象）
    await operatorStore.fetchOperators()
    
    // 2. 加载文章详情
    const data = await articleStore.fetchArticleDetail(id)
    article.value = data
    
    // 3. 加载评论
    await articleStore.fetchComments(id)
    comments.value = articleStore.comments
    
    // 4. 加载引用组（如果存在）
    if (article.value?.groupId) {
      // 获取所有组（或只获取当前组，这里简单从 groupStore 获取）
      await groupStore.fetchAllGroups()
      referencedGroup.value = groupStore.groups.find(g => g.id === article.value.groupId)
      console.log('引用组:', referencedGroup.value)
      console.log('组内算子ID列表:', referencedGroup.value ? parseOperatorIds(referencedGroup.value.operatorIds) : [])
    }
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

const toggleLike = async () => {
  if (!userStore.isAuthenticated) {
    alert('请先登录')
    return
  }
  await articleStore.toggleLike(route.params.id)
  hasLiked.value = !hasLiked.value
  article.value.likeCount += hasLiked.value ? 1 : -1
}

const submitComment = async () => {
  if (!newComment.value.trim()) return
  if (!userStore.isAuthenticated) {
    alert('请先登录')
    return
  }
  await articleStore.postComment(route.params.id, newComment.value)
  newComment.value = ''
  await articleStore.fetchComments(route.params.id)
  comments.value = articleStore.comments
}

const showReplyForm = (parentId) => {
  newComment.value = `@回复 `
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

const formatContent = (content) => {
  return content.replace(/\n/g, '<br>')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.article-detail {
  padding: var(--spacing-xl) 0;
}

.article-content {
  background: var(--canvas);
  padding: var(--spacing-xl);
  border: 1px solid var(--hairline);
  margin-bottom: var(--spacing-xl);
}

.article-content h1 {
  margin-bottom: var(--spacing-md);
}

.meta {
  display: flex;
  gap: var(--spacing-lg);
  color: var(--muted);
  font-size: var(--text-body-sm);
  padding-bottom: var(--spacing-md);
  border-bottom: 1px solid var(--hairline);
  margin-bottom: var(--spacing-lg);
}

.referenced-group {
  background: var(--surface-soft);
  padding: var(--spacing-lg);
  margin-bottom: var(--spacing-lg);
  border: 1px solid var(--hairline);
}

.referenced-group h3 {
  margin-bottom: var(--spacing-md);
}

.group-operators {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}

.group-operator-tag {
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: 4px 12px;
  font-size: var(--text-body-sm);
}

.content {
  line-height: 1.8;
  margin-bottom: var(--spacing-lg);
}

.actions {
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--hairline);
}

.comments-section {
  background: var(--canvas);
  padding: var(--spacing-xl);
  border: 1px solid var(--hairline);
}

.comments-section h3 {
  margin-bottom: var(--spacing-lg);
}

.comment-form {
  margin-bottom: var(--spacing-xl);
}

.comment-form textarea {
  width: 100%;
  padding: var(--spacing-md);
  border: 1px solid var(--hairline);
  background: var(--canvas);
  color: var(--ink);
  font-family: inherit;
  font-size: var(--text-body-md);
  margin-bottom: var(--spacing-sm);
  border-radius: var(--radius-none);
}

.comments-list {
  margin-top: var(--spacing-lg);
}

.empty, .loading {
  text-align: center;
  padding: var(--spacing-xl);
  color: var(--muted);
}
</style>
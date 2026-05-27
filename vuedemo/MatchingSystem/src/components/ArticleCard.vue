<template>
  <div class="article-card" @click="$emit('click')">
    <div class="article-header">
      <h3 class="article-title">{{ article.title }}</h3>
      <div class="article-meta">
        <span class="author">{{ article.authorName || '用户' + article.userId }}</span>
        <span class="date">{{ formatDate(article.createdAt) }}</span>
      </div>
    </div>
    <p class="article-excerpt">{{ truncate(article.content, 150) }}</p>
    <div class="article-stats">
      <span>👁️ {{ article.viewCount || 0 }}</span>
      <span>❤️ {{ article.likeCount || 0 }}</span>
      <span>💬 {{ article.commentCount || 0 }}</span>
    </div>
    <div v-if="article.groupId" class="article-group-ref">
      <span class="text-uppercase">引用搭配组</span>
    </div>
  </div>
</template>

<script setup>
defineProps({
  article: {
    type: Object,
    required: true
  }
})

defineEmits(['click'])

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

const truncate = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.slice(0, length) + '...' : text
}
</script>

<style scoped>
.article-card {
  background: var(--canvas);
  padding: var(--spacing-lg);
  border: 1px solid var(--hairline);
  cursor: pointer;
  transition: border-color 0.2s;
}

.article-card:hover {
  border-color: var(--primary);
}

.article-header {
  margin-bottom: var(--spacing-md);
}

.article-title {
  font-size: var(--text-title-lg);
  margin-bottom: var(--spacing-xs);
}

.article-meta {
  display: flex;
  gap: var(--spacing-md);
  font-size: var(--text-body-sm);
  color: var(--muted);
}

.article-excerpt {
  margin-bottom: var(--spacing-md);
  line-height: 1.55;
}

.article-stats {
  display: flex;
  gap: var(--spacing-lg);
  font-size: var(--text-body-sm);
  color: var(--muted);
}

.article-group-ref {
  margin-top: var(--spacing-md);
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--hairline);
  font-size: var(--text-caption);
}
</style>
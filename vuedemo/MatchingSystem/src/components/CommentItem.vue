<template>
  <div class="comment-item">
    <div class="comment-header">
      <span class="author">用户{{ comment.userId }}</span>
      <span class="date">{{ formatDate(comment.createdAt) }}</span>
    </div>
    <div class="comment-content">{{ comment.content }}</div>
    <div class="comment-actions">
      <button class="reply-btn" @click="$emit('reply', comment.id)">回复</button>
    </div>
    <div v-if="comment.replies && comment.replies.length" class="replies">
      <CommentItem
        v-for="reply in comment.replies"
        :key="reply.id"
        :comment="reply"
        @reply="$emit('reply', $event)"
      />
    </div>
  </div>
</template>

<script setup>
defineProps({
  comment: {
    type: Object,
    required: true
  }
})

defineEmits(['reply'])

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}
</script>

<style scoped>
.comment-item {
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--hairline);
}

.comment-header {
  display: flex;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-xs);
}

.author {
  font-weight: var(--font-bold);
  color: var(--ink);
}

.date {
  font-size: var(--text-caption);
  color: var(--muted);
}

.comment-content {
  margin-bottom: var(--spacing-xs);
}

.reply-btn {
  background: none;
  border: none;
  color: var(--muted);
  font-size: var(--text-caption);
  cursor: pointer;
  padding: 0;
}

.reply-btn:hover {
  color: var(--primary);
}

.replies {
  margin-left: var(--spacing-xl);
  margin-top: var(--spacing-md);
}
</style>
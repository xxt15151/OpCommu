<template>
  <div class="create-article">
    <div class="container">
      <div class="form-card">
        <h1>发布文章</h1>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label>标题</label>
            <input type="text" v-model="title" required class="text-input" placeholder="请输入标题" />
          </div>
          <div class="form-group">
            <label>内容</label>
            <textarea v-model="content" required rows="8" class="text-input" placeholder="分享你的搭配心得..."></textarea>
          </div>
          <div class="form-group">
            <label>引用搭配组（可选）</label>
            <select v-model="selectedGroupId" class="text-input">
              <option :value="null">无</option>
              <option v-for="group in myGroups" :key="group.id" :value="group.id">
                {{ group.name }}
              </option>
            </select>
          </div>
          <div class="form-actions">
            <Button type="submit" :disabled="submitting">发布</Button>
            <Button variant="secondary" @click="$router.back()">取消</Button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/userStore'
import { useGroupStore } from '@/stores/groupStore'
import { useArticleStore } from '@/stores/articleStore'
import Button from '@/components/Button.vue'

const router = useRouter()
const userStore = useUserStore()
const groupStore = useGroupStore()
const articleStore = useArticleStore()

const title = ref('')
const content = ref('')
const selectedGroupId = ref(null)
const submitting = ref(false)
const myGroups = ref([])

const loadMyGroups = async () => {
  if (userStore.isAuthenticated) {
    await groupStore.fetchUserGroups(userStore.userId)
    myGroups.value = groupStore.groups
  }
}

const handleSubmit = async () => {
  if (!title.value.trim() || !content.value.trim()) {
    alert('请填写标题和内容')
    return
  }
  submitting.value = true
  try {
    await articleStore.postArticle(title.value, content.value, selectedGroupId.value)
    alert('发布成功')
    router.push('/articles')
  } catch (error) {
    alert('发布失败：' + (error.response?.data || error.message))
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadMyGroups()
})
</script>

<style scoped>
.create-article {
  padding: var(--spacing-xl) 0;
  min-height: calc(100vh - 64px);
  background: var(--surface-soft);
}

.form-card {
  max-width: 800px;
  margin: 0 auto;
  background: var(--canvas);
  border: 1px solid var(--hairline);
  padding: var(--spacing-xl);
}

.form-card h1 {
  margin-bottom: var(--spacing-xl);
}

.form-group {
  margin-bottom: var(--spacing-lg);
}

.form-group label {
  display: block;
  margin-bottom: var(--spacing-xs);
  font-weight: var(--font-bold);
  color: var(--ink);
}

.text-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--hairline);
  background: var(--canvas);
  color: var(--ink);
  font-size: var(--text-body-md);
  border-radius: var(--radius-none);
}

textarea.text-input {
  resize: vertical;
  font-family: inherit;
}

.form-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: flex-end;
}
</style>
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getArticles, getUserArticles, getArticleDetail, createArticle, likeArticle, getLikeCount } from '@/api/articles'
import { getComments, createComment } from '@/api/comments'

export const useArticleStore = defineStore('article', () => {
  const articles = ref([])
  const currentArticle = ref(null)
  const comments = ref([])
  const loading = ref(false)

  const fetchArticles = async (page = 1, size = 10) => {
    loading.value = true
    try {
      const response = await getArticles(page, size)
      articles.value = response.data
    } catch (error) {
      console.error('获取文章失败:', error)
    } finally {
      loading.value = false
    }
  }

  const fetchUserArticles = async (userId, page = 1, size = 10) => {
    loading.value = true
    try {
      const response = await getUserArticles(userId, page, size)
      return response.data
    } catch (error) {
      console.error('获取用户文章失败:', error)
      return []
    } finally {
      loading.value = false
    }
  }

  const fetchArticleDetail = async (id) => {
    loading.value = true
    try {
      const response = await getArticleDetail(id)
      currentArticle.value = response.data
      return response.data
    } catch (error) {
      console.error('获取文章详情失败:', error)
      return null
    } finally {
      loading.value = false
    }
  }

  const fetchComments = async (articleId) => {
    try {
      const response = await getComments(articleId)
      comments.value = response.data
    } catch (error) {
      console.error('获取评论失败:', error)
    }
  }

  const postArticle = async (title, content, groupId) => {
    const response = await createArticle(title, content, groupId)
    return response.data
  }

  const postComment = async (articleId, content, parentId = null) => {
    const response = await createComment(articleId, content, parentId)
    return response.data
  }

  const toggleLike = async (articleId) => {
    await likeArticle(articleId)
  }

  return {
    articles,
    currentArticle,
    comments,
    loading,
    fetchArticles,
    fetchUserArticles,
    fetchArticleDetail,
    fetchComments,
    postArticle,
    postComment,
    toggleLike
  }
})
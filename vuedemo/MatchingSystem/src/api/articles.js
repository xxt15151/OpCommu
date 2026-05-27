import client from './client'

export const getArticles = (page = 1, size = 10) => {
  return client.get('/articles', { params: { page, size } })
}

export const getUserArticles = (userId, page = 1, size = 10) => {
  return client.get(`/articles/user/${userId}`, { params: { page, size } })
}

export const getArticleDetail = (id) => {
  return client.get(`/articles/${id}`)
}

export const createArticle = (title, content, groupId = null) => {
  return client.post('/articles', { title, content, groupId })
}

export const likeArticle = (id) => {
  return client.post(`/articles/${id}/like`)
}

export const getLikeCount = (id) => {
  return client.get(`/articles/${id}/likes`)
}
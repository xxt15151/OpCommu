import client from './client'

export const getComments = (articleId) => {
  return client.get(`/articles/${articleId}/comments`)
}

export const createComment = (articleId, content, parentId = null) => {
  return client.post(`/articles/${articleId}/comments`, { content, parentId })
}
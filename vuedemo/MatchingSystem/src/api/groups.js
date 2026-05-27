import client from './client'

export const createGroup = (name, operatorIds) => {
  return client.post('/groups', { name, operatorIds })
}

export const getAllGroups = () => {
  return client.get('/groups')
}

export const getUserGroups = (userId) => {
  return client.get(`/groups?userId=${userId}`)
}
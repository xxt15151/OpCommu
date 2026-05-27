import client from './client'

export const register = (username, password, email) => {
  return client.post('/users/register', { username, password, email })
}

export const login = (username, password) => {
  return client.post('/users/login', { username, password })
}
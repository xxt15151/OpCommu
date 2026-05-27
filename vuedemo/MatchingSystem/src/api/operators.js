import client from './client'

export const getOperators = () => {
  return client.get('/operators')
}
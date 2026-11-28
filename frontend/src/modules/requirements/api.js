import { request } from '../../core/http.js'

export const api = {
  inceptionTurn: (state, message) => request('POST', '/api/inception/turn', { ...state, message }),
  listProjects: () => request('GET', '/api/projects'),
  createProject: (payload) => request('POST', '/api/projects', payload),
  getProject: (id) => request('GET', `/api/projects/${id}`),
  updateProject: (id, changes) => request('PATCH', `/api/projects/${id}`, changes),
}

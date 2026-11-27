async function request(method, url, body) {
  const res = await fetch(url, {
    method,
    headers: body ? { 'Content-Type': 'application/json' } : {},
    body: body ? JSON.stringify(body) : undefined,
  })
  const data = await res.json().catch(() => ({}))
  if (!res.ok) throw new Error(data.error || `Request failed (${res.status})`)
  return data
}

export const api = {
  inceptionTurn: (state, message) => request('POST', '/api/inception/turn', { ...state, message }),
  listProjects: () => request('GET', '/api/projects'),
  createProject: (payload) => request('POST', '/api/projects', payload),
  getProject: (id) => request('GET', `/api/projects/${id}`),
  updateProject: (id, changes) => request('PATCH', `/api/projects/${id}`, changes),
}

export const PRODUCT_TYPES = {
  website: 'Website / web app',
  mobile_app: 'Mobile app',
  api_backend: 'API / backend service',
  desktop_app: 'Desktop app',
  internal_tool: 'Internal tool',
  data_ai: 'Data / AI product',
  other: 'Other',
}

export const SCALES = {
  personal: 'Personal',
  team: 'One team',
  department: 'One department',
  organisation: 'Whole organisation',
  public: 'Public / external customers',
}

export const MANDATORY = ['name', 'problem', 'targetUsers', 'productType', 'expectedBenefit', 'scale']

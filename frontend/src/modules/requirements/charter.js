// Charter vocabulary shared by the requirements views; mirrors the backend Charter record.
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

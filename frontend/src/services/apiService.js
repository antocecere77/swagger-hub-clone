import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1'

const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor
axiosInstance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

// API methods for APIs
export const getApis = (params = {}) => {
  return axiosInstance.get('/apis', { params })
}

export const getApiById = (id) => {
  return axiosInstance.get(`/apis/${id}`)
}

export const createApi = (data) => {
  return axiosInstance.post('/apis', data)
}

export const updateApi = (id, data) => {
  return axiosInstance.put(`/apis/${id}`, data)
}

export const deleteApi = (id) => {
  return axiosInstance.delete(`/apis/${id}`)
}

export const searchApis = (query) => {
  return axiosInstance.get('/apis/search', {
    params: { q: query },
  })
}

// API methods for Versions
export const getVersions = (apiId, params = {}) => {
  return axiosInstance.get(`/apis/${apiId}/versions`, { params })
}

export const getVersionById = (apiId, versionId) => {
  return axiosInstance.get(`/apis/${apiId}/versions/${versionId}`)
}

export const createVersion = (apiId, data) => {
  return axiosInstance.post(`/apis/${apiId}/versions`, data)
}

export const updateVersion = (apiId, versionId, data) => {
  return axiosInstance.put(`/apis/${apiId}/versions/${versionId}`, data)
}

export const deleteVersion = (apiId, versionId) => {
  return axiosInstance.delete(`/apis/${apiId}/versions/${versionId}`)
}

export const publishVersion = (apiId, versionId) => {
  return axiosInstance.post(`/apis/${apiId}/versions/${versionId}/publish`)
}

// API methods for Specifications (OpenAPI/Swagger)
export const getSpecification = (apiId, versionId) => {
  return axiosInstance.get(`/apis/${apiId}/versions/${versionId}/spec`)
}

export const updateSpecification = (apiId, versionId, spec) => {
  return axiosInstance.put(`/apis/${apiId}/versions/${versionId}/spec`, {
    content: spec,
  })
}

export const validateSpecification = (spec) => {
  return axiosInstance.post('/validate-spec', {
    content: spec,
  })
}

export default axiosInstance

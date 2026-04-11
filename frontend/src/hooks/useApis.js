import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import * as apiService from '../services/apiService'

// Queries
export function useApis(params = {}) {
  return useQuery({
    queryKey: ['apis', params],
    queryFn: () => apiService.getApis(params),
    enabled: true,
  })
}

export function useApi(id) {
  return useQuery({
    queryKey: ['api', id],
    queryFn: () => apiService.getApiById(id),
    enabled: !!id,
  })
}

export function useVersions(apiId, params = {}) {
  return useQuery({
    queryKey: ['versions', apiId, params],
    queryFn: () => apiService.getVersions(apiId, params),
    enabled: !!apiId,
  })
}

export function useVersion(apiId, versionId) {
  return useQuery({
    queryKey: ['version', apiId, versionId],
    queryFn: () => apiService.getVersionById(apiId, versionId),
    enabled: !!apiId && !!versionId,
  })
}

export function useSpecification(apiId, versionId) {
  return useQuery({
    queryKey: ['spec', apiId, versionId],
    queryFn: () => apiService.getSpecification(apiId, versionId),
    enabled: !!apiId && !!versionId,
  })
}

// Mutations
export function useCreateApi() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (data) => apiService.createApi(data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['apis'] })
      queryClient.setQueryData(['api', data.id], data)
    },
  })
}

export function useUpdateApi() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ id, data }) => apiService.updateApi(id, data),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['apis'] })
      queryClient.setQueryData(['api', data.id], data)
    },
  })
}

export function useDeleteApi() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id) => apiService.deleteApi(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['apis'] })
    },
  })
}

export function useCreateVersion() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ apiId, data }) => apiService.createVersion(apiId, data),
    onSuccess: (data, { apiId }) => {
      queryClient.invalidateQueries({ queryKey: ['versions', apiId] })
      queryClient.setQueryData(['version', apiId, data.id], data)
    },
  })
}

export function useUpdateVersion() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ apiId, versionId, data }) =>
      apiService.updateVersion(apiId, versionId, data),
    onSuccess: (data, { apiId, versionId }) => {
      queryClient.invalidateQueries({ queryKey: ['versions', apiId] })
      queryClient.setQueryData(['version', apiId, versionId], data)
    },
  })
}

export function useDeleteVersion() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ apiId, versionId }) =>
      apiService.deleteVersion(apiId, versionId),
    onSuccess: (data, { apiId }) => {
      queryClient.invalidateQueries({ queryKey: ['versions', apiId] })
    },
  })
}

export function usePublishVersion() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ apiId, versionId }) =>
      apiService.publishVersion(apiId, versionId),
    onSuccess: (data, { apiId, versionId }) => {
      queryClient.invalidateQueries({ queryKey: ['versions', apiId] })
      queryClient.setQueryData(['version', apiId, versionId], data)
    },
  })
}

export function useUpdateSpecification() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ apiId, versionId, spec }) =>
      apiService.updateSpecification(apiId, versionId, spec),
    onSuccess: (data, { apiId, versionId }) => {
      queryClient.invalidateQueries({ queryKey: ['spec', apiId, versionId] })
    },
  })
}

export function useValidateSpecification() {
  return useMutation({
    mutationFn: (spec) => apiService.validateSpecification(spec),
  })
}

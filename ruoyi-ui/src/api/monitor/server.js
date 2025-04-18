import request from '@/utils/request'

// 获取服务信息
export function getServer() {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}

// 获取服务信息
export function timeSync() {
  return request({
    url: '/monitor/server/time/sync?time=' + new Date().toISOString(),
    method: 'post'
  })
}

// 获取服务信息
export function setTimezone() {
  return request({
    url: '/monitor/server/time/setTz/',
    method: 'post'
  })
}

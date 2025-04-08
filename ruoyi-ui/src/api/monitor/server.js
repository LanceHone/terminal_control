import request from '@/utils/request'

// 获取服务信息
export function getServer() {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}

// 获取服务信息
export function timeSync(open) {
  return request({
    url: '/monitor/serve/time/sync/',
    method: 'post'
  })
}

// 获取服务信息
export function setTimezone(open) {
  return request({
    url: '/monitor/serve/time/setTz/',
    method: 'post'
  })
}

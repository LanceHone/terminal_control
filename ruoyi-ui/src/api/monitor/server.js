import request from '@/utils/request'

// 获取服务信息
export function getServer() {
  return request({
    url: '/monitor/server',
    method: 'get'
  })
}

// 获取服务信息
export function timeSync(data) {
  return request({
    url: '/monitor/server/time/sync',
    method: 'post',
    data
  })
}

// 获取服务信息
export function getClockInfo() {
  return request({
    url: '/monitor/server/time',
    method: 'get'
  })
}

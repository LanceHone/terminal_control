import request from '@/utils/request'

// 查询访问控制日志列表
export function listLogs(query) {
  return request({
    url: '/access/logs/list',
    method: 'get',
    params: query
  })
}

// 查询访问控制日志详细
export function getLogs(id) {
  return request({
    url: '/access/logs/' + id,
    method: 'get'
  })
}

// 新增访问控制日志
export function addLogs(data) {
  return request({
    url: '/access/logs',
    method: 'post',
    data: data
  })
}

// 修改访问控制日志
export function updateLogs(data) {
  return request({
    url: '/access/logs',
    method: 'put',
    data: data
  })
}

// 删除访问控制日志
export function delLogs(id) {
  return request({
    url: '/access/logs/' + id,
    method: 'delete'
  })
}

// 删除modbus控制日志
export function clear() {
  return request({
    url: '/access/logs/clear',
    method: 'post'
  })
}

// 删除modbus控制日志
export function threshold(data) {
  return request({
    url: '/access/logs/threshold',
    method: 'post',
    data: data
  })
}

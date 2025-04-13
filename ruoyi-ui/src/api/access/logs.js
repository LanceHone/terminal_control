import request from '@/utils/request'

// 查询控制日志列表
export function listLogs(query) {
  return request({
    url: '/access/logs/list',
    method: 'get',
    params: query
  })
}

// 查询控制日志详细
export function getLogs(id) {
  return request({
    url: '/access/logs/' + id,
    method: 'get'
  })
}

// 新增控制日志
export function addLogs(data) {
  return request({
    url: '/access/logs',
    method: 'post',
    data: data
  })
}

// 修改控制日志
export function updateLogs(data) {
  return request({
    url: '/access/logs',
    method: 'put',
    data: data
  })
}

// 删除控制日志
export function delLogs(id) {
  return request({
    url: '/access/logs/' + id,
    method: 'delete'
  })
}

import request from '@/utils/request'

// 查询modbus控制日志列表
export function listLogs(query) {
  return request({
    url: '/access/mdb/logs/list',
    method: 'get',
    params: query
  })
}

// 查询modbus控制日志详细
export function getLogs(id) {
  return request({
    url: '/access/mdb/logs/' + id,
    method: 'get'
  })
}

// 新增modbus控制日志
export function addLogs(data) {
  return request({
    url: '/access/mdb/logs',
    method: 'post',
    data: data
  })
}

// 修改modbus控制日志
export function updateLogs(data) {
  return request({
    url: '/access/mdb/logs',
    method: 'put',
    data: data
  })
}

// 删除modbus控制日志
export function delLogs(id) {
  return request({
    url: '/access/mdb/logs/' + id,
    method: 'delete'
  })
}

// 删除modbus控制日志
export function clear() {
  return request({
    url: '/access/mdb/logs/clear',
    method: 'post'
  })
}

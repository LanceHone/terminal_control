// src/api/access/modbus.js
import request from '@/utils/request'

// 查询Modbus访问控制列表
export function listModbus(query) {
  return request({
    url: '/access/modbus/list',
    method: 'get',
    params: query
  })
}

// 查询Modbus策略详细
export function getModbus(id) {
  return request({
    url: '/access/modbus/' + id,
    method: 'get'
  })
}

// 新增Modbus策略
export function addModbus(data) {
  return request({
    url: '/access/modbus',
    method: 'post',
    data: data
  })
}

// 修改Modbus策略
export function updateModbus(data) {
  return request({
    url: '/access/modbus',
    method: 'put',
    data: data
  })
}

// 修改状态
export function updateModbusStatus(data) {
  return request({
    url: '/access/modbus/status',
    method: 'put',
    data: data
  })
}

// 删除Modbus策略
export function delModbus(id) {
  return request({
    url: '/access/modbus/' + id,
    method: 'delete'
  })
}

//測試
export function checkAccess(data) {
  return request({
    url: '/access/modbus/checkAccess',
    method: 'post',
    data: data
  })
}

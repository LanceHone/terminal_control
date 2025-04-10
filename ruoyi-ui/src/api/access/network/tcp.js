import request from '@/utils/request';

export function updateStatus(data) {
  return request({
    url: '/access/tcp/status',
    method: 'put',
    data: data
  })
}

// 查询网络层访问控制列表
export function listTcp(query) {
  return request({
    url: '/access/tcp/list',
    method: 'get',
    params: query
  });
}

// 根据ID查询网络层访问控制详细
export function getTcp(id) {
  return request({
    url: '/access/tcp/' + id,
    method: 'get'
  });
}

// 新增网络层访问控制
export function addTcp(data) {
  return request({
    url: '/access/tcp',
    method: 'post',
    data: data
  });
}

// 修改网络层访问控制
export function updateTcp(data) {
  return request({
    url: '/access/tcp',
    method: 'put',
    data: data
  });
}

export function delTcp(id) {
  return request({
    url: '/access/tcp/' + id,
    method: 'delete'
  });
}

export function http(id) {
  return request({
    url: '/access/tcp/http/' + id,
    method: 'post'
  });
}

export function telnet(id) {
  return request({
    url: '/access/tcp/telnet/' + id,
    method: 'post'
  });
}

export function ftp(id) {
  return request({
    url: '/access/tcp/ftp/' + id,
    method: 'post'
  });
}

export function status(id) {
  return request({
    url: '/access/tcp/app/status',
    method: 'get'
  });
}

export function setting_flood_status(id) {
  return request({
    url: '/access/tcp/flood/status',
    method: 'get'
  });
}

export function set_flood(key, value) {
  return request({
    url: `/access/tcp/${key}/${value}`,
    method: 'post'
  });
}
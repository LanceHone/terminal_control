import request from '@/utils/request';

// 查询HTTP访问控制列表
export function listHttp(query) {
  return request({
    url: '/access/appl/http/list',
    method: 'get',
    params: query
  });
}

// 根据ID查询HTTP访问控制详细
export function getHttp(id) {
  return request({
    url: '/access/appl/http/' + id,
    method: 'get'
  });
}

// 新增HTTP访问控制
export function addHttp(data) {
  return request({
    url: '/access/appl/http',
    method: 'post',
    data: data
  });
}

// 修改HTTP访问控制
export function updateHttp(data) {
  return request({
    url: '/access/appl/http',
    method: 'put',
    data: data
  });
}

// 删除HTTP访问控制
export function delHttp(id) {
  return request({
    url: '/access/appl/http/' + id,
    method: 'delete'
  });
}

import request from '@/utils/request';

// 查询FTP访问控制列表
export function listFtp(query) {
  return request({
    url: '/access/appl/ftp/list',
    method: 'get',
    params: query
  });
}

// 根据ID查询FTP访问控制详细
export function getFtp(id) {
  return request({
    url: '/access/appl/ftp/' + id,
    method: 'get'
  });
}

// 新增FTP访问控制
export function addFtp(data) {
  return request({
    url: '/access/appl/ftp',
    method: 'post',
    data: data
  });
}

// 修改FTP访问控制
export function updateFtp(data) {
  return request({
    url: '/access/appl/ftp',
    method: 'put',
    data: data
  });
}

// 删除FTP访问控制
export function delFtp(id) {
  return request({
    url: '/access/appl/ftp/' + id,
    method: 'delete'
  });
}

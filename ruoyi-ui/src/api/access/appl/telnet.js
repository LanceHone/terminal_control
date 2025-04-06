import request from '@/utils/request';

// 查询TELNET访问控制列表
export function listTelnet(query) {
  return request({
    url: '/access/appl/telnet/list',
    method: 'get',
    params: query
  });
}

// 根据ID查询TELNET访问控制详细
export function getTelnet(id) {
  return request({
    url: '/access/appl/telnet/' + id,
    method: 'get'
  });
}

// 新增TELNET访问控制
export function addTelnet(data) {
  return request({
    url: '/access/appl/telnet',
    method: 'post',
    data: data
  });
}

// 修改TELNET访问控制
export function updateTelnet(data) {
  return request({
    url: '/access/appl/telnet',
    method: 'put',
    data: data
  });
}

// 删除TELNET访问控制
export function delTelnet(id) {
  return request({
    url: '/access/appl/telnet/' + id,
    method: 'delete'
  });
}

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

// 删除网络层访问控制
export function delTcp(id) {
  return request({
    url: '/access/tcp/' + id,
    method: 'delete'
  });
}

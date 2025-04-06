import request from "@/utils/request";

// 获取组别列表
export function listGroup(params) {
  return request({
    url: "/system/group/list",
    method: "get",
    params,
  });
}

// 新增组别
export function addGroup(data) {
  return request({
    url: "/system/group",
    method: "post",
    data,
  });
}

// 修改组别
export function updateGroup(data) {
  return request({
    url: `/system/group/${data.id}`,
    method: "put",
    data,
  });
}

// 删除组别
export function deleteGroup(groupId) {
  return request({
    url: `/system/group/${groupId}`,
    method: "delete",
  });
}

import request from "@/utils/request";

export function directoryTree(query) {
    return request({
        url: '/directory/get',
        method: 'get',
        params: query
    })
}

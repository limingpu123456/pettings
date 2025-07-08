import request from "network/request.js"

//検索所有ペットの品種
export function requestqueryallskind(parameter) {
    return request({
      url: "/skind/queryallskind",
      params: parameter
    })
}

//検索所有ペットの品種
export function requestaddskind(parameter) {
    return request({
      url: "/skind/addskind",
      method: 'post',
      params: parameter
    })
}

//删除品種
export function requestdeleteskind(parameter) {
    return request({
      url: "/skind/deleteskind",
      params: parameter
    })
}

//删除品種
export function requestupdateskind(parameter) {
    return request({
      url: "/skind/updateskind",
      method: 'post',
      params: parameter
    })
}

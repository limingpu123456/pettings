import request from "network/request.js"

//添加订单请求
export function requestaddpetorder(parameter) {
    return request({
        url: "/petorder/addpetorder",
        method: "post",
        params: parameter
    })
}

//削除订单请求
export function requestdeletepetorder(parameter) {
    return request({
        url: "/petorder/deletepetorder",
        params: parameter
    })
}

//修正订单请求
export function requestupdatepetorder(parameter) {
    return request({
        url: "/petorder/updatepetorder",
        method: 'post',
        params: parameter
    })
}

//検索订单请求
export function requestquerypetorder(parameter) {
    return request({
        url: "/petorder/querypetorder",
        params: parameter
    })
}

//検索订单请求
export function requestquerypetorderpage(parameter) {
    return request({
        url: "/petorder/querypetorderpage",
        params: parameter
    })
}

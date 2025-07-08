import request from "network/request.js"

//增加住所
export function requestaddAddress(parameter) {
    return request({
        url: "/address/addAddress",
        method: "post",
        params: parameter
    })
}

//削除住所
export function requestdeleteAddress(parameter) {
    return request({
        url: "/address/deleteAddress",
        params: parameter
    })
}

//修改住所
export function requestupdateAddress(parameter) {
    return request({
        url: "/address/updateAddress",
        method: 'post',
        params: parameter
    })
}

//検索用户所有住所
export function requestqueryAddressbyuid(parameter) {
    return request({
        url: "/address/queryAddressbyuid",
        params: parameter
    })
}

//根据条件検索所有住所
export function requestqueryaddress(parameter) {
    return request({
        url: "/address/queryaddress",
        params: parameter
    })
}

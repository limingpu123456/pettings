import request from "network/request.js"

//検索所有宠物种类
export function requestqueryBKind() {
    return request({
        url: "/bkind/queryBKind"
    })
}

//検索所有宠物种类及对应品種
export function requestqueryBKindandSKind(parameter) {
    return request({
        url: "/bkind/queryBKindandSKind",
        params: parameter
    })
}

//增加タイプ
export function requestaddBKind(parameter) {
    return request({
        url: "/bkind/addBKind",
        method: 'post',
        params: parameter
    })
}

//删除タイプ
export function requestdeleteBKind(parameter) {
    return request({
        url: "/bkind/deleteBKind",
        params: parameter
    })
}

//修改タイプ
export function requestupdateBKind(parameter) {
    return request({
        url: "/bkind/updateBKind",
        params: parameter
    })
}

import request from "network/request.js"

//ログイン
export function requestLogin(parameter) {
    return request({
        url: "/user/login",
        params: parameter
    })
}

// サインアップ前验证账号是否已被サインアップ
export function requestcheckuser(parameter) {
    return request({
        url: "/user/checkuser",
        params: parameter
    })
}

// サインアップ
export function requestregister(parameter) {
    return request({
        url: "/user/register",
        method: "post",
        params: parameter
    })
}

import axios from 'axios'
//axios配置
export default function request(config) {
    const instance = axios.create({
        baseURL: "/api",
        timeout: 5000,
    })
    instance.interceptors.response.use((res) => {
        // console.log(res);
        return res.data;
    }, (err) => {
        return Promise.reject(err)
    })
    return instance(config)
}

import axios from 'axios'

export default function request(config) {
    const instance = axios.create({
        baseURL: "/api",
        timeout: 5000,
    })
    instance.interceptors.response.use((res) => {
        // console.log("11111"+JSON.stringify(res));
        return res.data;
    }, (err) => {
        return Promise.reject(err)
    })
    return instance(config)
}

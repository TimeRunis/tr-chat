import axios from "axios";
import {getApi} from "../../public/ApiList";

function getCookie(cname) {
    let name = cname + "=";
    let ca = document.cookie.split(";");
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i].trim();
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
}

//请求拦截器
axios.interceptors.request.use(function (config) {
    let token = getCookie("token")
    if (token) {
        // 设置请求头
        config.headers.set({
            "Content-Type":"application/json",
            "token":token
        })
    }
    return config
}, function (error) {
    //请求出错
    console.warn(error)
});


function data2url(apiId,data){
    let url=apiId+"?";
    for(let i in data){
        url+=i+"="+data[i]+"&";
    }
    return url
}


export function apiUpload(apiId,file){
    try{
        let tempAxios=axios.create();
        return tempAxios.post(getApi(apiId),file,{headers:{"Content-Type":"multipart/form-data","token":getCookie("token")}});
    }catch (e){
        console.warn("文件上传服务器错误")
    }
}

export function apiPost(apiId,data){
    try{
        return axios.post(getApi(apiId),data);
    }catch (e){
        console.warn("服务器错误")
    }
}

export function apiGet(apiId,data){
    try{
        return axios.get(data2url(getApi(apiId),data))
    }catch (e){
        console.warn("服务器错误")
    }
}

export function apiPut(apiId,data){
    try{
        return axios.put(getApi(apiId),data);
    }catch (e){
        console.warn("服务器错误")
    }
}

export function apiDelete(apiId,data){
    try{
        return axios.delete(data2url(getApi(apiId),data));
    }catch (e){
        console.warn("服务器错误")
    }
}
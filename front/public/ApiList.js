let host="192.168.22.107"
let baseUrl="http://"+host+":8080/";
let websocketUrl="ws://"+host+":8080/wsChat/";

let apiList= {
    "contact": "contact",
    "message": "message"
}

export function getApi(id){
    return baseUrl+apiList[id];
}

export function getWsUrl(){
    return websocketUrl;
}
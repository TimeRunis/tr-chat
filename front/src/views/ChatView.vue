<template>
  <div style="
  width: 100%;
  height:100%;
  display: flex;
  border-radius: 0.5em;
  box-shadow: 0 0 10px 0 #d9d9d9;
  border-style: solid;
  border-width: 1px;
  border-color: #f0f0f0;"
  >
    <lemon-imui
        ref="IMUI"
        style="width: 100%;height:100%"
        theme="blue"
        :user="user"
        @pull-messages='handlePullMessages'
        @send="handleSend"
        @change-contact="handleChangeContact"
        @menu-avatar-click="changeUser"
    />
  </div>
</template>

<script>
import {apiGet} from "@/utils/Api";
import {getWsUrl} from "../../public/ApiList";
export default {
  name: "ChatView",
  mounted() {
    const IMUI =this.$refs.IMUI;
    this.historyMsgParam.toId=this.user.id;
    this.getContactList();
    this.createWebSocket();
  },
  data(){
    return {
      websocket:null,
      historyMsgParam:{
          toId:0,
          fromId:0,
          current:1,
          size:10,
          type: 'User'
      },
      userIndex:0,
      user:{
        id: 1,
        displayName: "TimeRunis",
        avatar: "https://blog.timerunis.cn/touxiang.jpg"
      },
      userList:[{
        id: 1,
        displayName: "TimeRunis",
        avatar: "https://blog.timerunis.cn/touxiang.jpg"
      },
      {
        id: 2,
        displayName: "田所浩二",
        avatar: "https://timerunis.cn/114514/-5cf37a9c7ad56e84.gif"
      },
      {
        id: 3,
        displayName: "比利王",
        avatar: "https://img2.baidu.com/it/u=1479858207,3130601058&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400"
      }],
      contactList:[],
      // websocket相关
      socketObj: "", // websocket实例对象
      //心跳检测
      heartCheck: {
        vueThis: this, // vue实例
        timeout: 10000, // 超时时间
        timeoutObj: null, // 计时器对象——向后端发送心跳检测
        serverTimeoutObj: null, // 计时器对象——等待后端心跳检测的回复
        // 心跳检测重置
        reset: function () {
          clearTimeout(this.timeoutObj);
          clearTimeout(this.serverTimeoutObj);
          return this;
        },
        // 心跳检测启动
        start: function () {
          this.timeoutObj && clearTimeout(this.timeoutObj);
          this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
          this.timeoutObj = setTimeout(() => {
            // 这里向后端发送一个心跳检测，后端收到后，会返回一个心跳回复
            this.vueThis.socketObj.send("HeartBeat");
            // console.log("发送心跳检测");
            this.serverTimeoutObj = setTimeout(() => {
              // 如果超过一定时间还没重置计时器，说明websocket与后端断开了
              console.log("未收到心跳检测回复");
              // 关闭WebSocket
              this.vueThis.socketObj.close();
            }, this.timeout);
          }, this.timeout);
        },
      },
      socketReconnectTimer: null, // 计时器对象——重连
      socketReconnectLock: false, // WebSocket重连的锁
      socketLeaveFlag: false, // 离开标记
    }
  },
  methods:{
    changeUser(){
      this.userIndex+=1;
      if(this.userIndex>=3){
        this.userIndex=0;
      }
      this.user=this.userList[this.userIndex];
      this.socketReconnect();
      this.contactList= [];
      this.getContactList();

    },
    getContactList(){
      //获取所有contact
      apiGet("contact",{
        id:this.user.id,
        type:"Owner",
        current:1,
        size:50//最多50个contact
      }).then((resp)=>{
        if(resp.data['code']===0){
          for(let index in resp.data['data']['records']){
            if(resp.data['data']['records'][index]['user']){
              this.contactList.push({
                id: resp.data['data']['records'][index]['id'],
                userId: resp.data['data']['records'][index]['user']['id'],
                displayName: resp.data['data']['records'][index]['user']['nick'],
                avatar: resp.data['data']['records'][index]['user']['avatar'],
                index: resp.data['data']['records'][index]['user']['nickIndex'],
                unread: resp.data['data']['records'][index]['unread'],
                lastSendTime:resp.data['data']['records'][index]['lastMessage']!=null?resp.data['data']['records'][index]['lastMessage']['send_time']:null,
                lastContent:resp.data['data']['records'][index]['lastMessage']!=null?resp.data['data']['records'][index]['lastMessage']['content']:null
              });
            }else {
              this.contactList.push({
                id: resp.data['data']['records'][index]['id'],
                groupId:resp.data['data']['records'][index]['group']['id'],
                displayName: resp.data['data']['records'][index]['group']['name'],
                avatar: resp.data['data']['records'][index]['group']['avatar'],
                index: resp.data['data']['records'][index]['group']['nickIndex'],
                unread: resp.data['data']['records'][index]['unread'],
                lastSendTime:resp.data['data']['records'][index]['lastMessage']!=null?resp.data['data']['records'][index]['lastMessage']['send_time']:null,
                lastContent:resp.data['data']['records'][index]['lastMessage']!=null?resp.data['data']['records'][index]['lastMessage']['content']:null
              });
            }
          }
          this.$refs.IMUI.initContacts(this.contactList);
        }
      })
    },
    handlePullMessages(contact, next) {
      //从后端请求历史消息数据
      let messages = []
      apiGet("message",this.historyMsgParam).then((resp)=>{
        if(resp.data['code']===0){
          for(let index in resp.data['data']['records']){
            if(resp.data['data']['records'][index]['toUser']){
              messages.push({
                id: resp.data['data']['records'][index]['id'],
                status: resp.data['data']['records'][index]['status'],
                type: resp.data['data']['records'][index]['type'],
                sendTime: resp.data['data']['records'][index]['sendTime'],
                content: resp.data['data']['records'][index]['content'],
                fileSize:resp.data['data']['records'][index]['fileSize'],
                fileName:resp.data['data']['records'][index]['fileName'],
                toContactId: resp.data['data']['records'][index]['toUser'],
                fromUser: {
                  id:resp.data['data']['records'][index]['fromUser']['id'],
                  displayName:resp.data['data']['records'][index]['fromUser']['nick'],
                  avatar:resp.data['data']['records'][index]['fromUser']['avatar'],
                },
              },)
            }else {
              messages.push({
                id: resp.data['data']['records'][index]['id'],
                status: resp.data['data']['records'][index]['status'],
                type: resp.data['data']['records'][index]['type'],
                sendTime: resp.data['data']['records'][index]['sendTime'],
                content: resp.data['data']['records'][index]['content'],
                fileSize:resp.data['data']['records'][index]['fileSize'],
                fileName:resp.data['data']['records'][index]['fileName'],
                toContactId: resp.data['data']['records'][index]['toGroup'],
                fromUser: {
                  id:resp.data['data']['records'][index]['fromUser']['id'],
                  displayName:resp.data['data']['records'][index]['fromUser']['nick'],
                  avatar:resp.data['data']['records'][index]['fromUser']['avatar'],
                },
              },)
            }
          }
          //将第二个参数设为true，表示已到末尾，聊天窗口顶部会显示“暂无更多消息”，不然会一直转圈。
          messages.reverse();
          if(resp.data['data']['current']<resp.data['data']['pages']){
            next(messages);
          }else {
            next(messages,true);
          }
          this.historyMsgParam.current+=1;
        }
      })
    },
    handleSend(message, next, file) {
      let contact=this.$refs.IMUI.getCurrentContact();
      if(file){
        //上传文件
        message['fileSize']=file['size'];
        message['fileName']=file['name'];
        message['content']="";//文件地址
      }
      if(contact['userId']){
        message['toUserId']=contact['userId'];
      }else {
        message['toGroupId']=contact['groupId'];
      }
      message['fromUserId']=this.user.id;
      // console.info(message);
      // console.info(file);
      message['status']='succeed';
      this.socketObj.send(JSON.stringify(message))

      //执行到next消息会停止转圈，如果接口调用失败，可以修改消息的状态 next({status:'failed'});
      next();
    },
    handleChangeContact(contact){
      contact['unread']=0;
      this.historyMsgParam.current=1;
      this.historyMsgParam.fromId=this.user.id;
      if(contact['groupId']){
        this.historyMsgParam.type='Group';
        this.historyMsgParam.toId=contact['groupId'];
      }else {
        this.historyMsgParam.type='Private';
        this.historyMsgParam.toId=contact['userId'];
      }
    },
    // websocket启动
    createWebSocket() {
      let webSocketLink = getWsUrl() +this.user.id; // webSocket地址
      try {
        this.socketObj = new WebSocket(webSocketLink);
        // websocket事件绑定
        this.socketObj.on
        this.socketEventBind();

      } catch (e) {
        console.log("catch" + e);
        // websocket重连
        this.socketReconnect();
      }
    },
    // websocket事件绑定
    socketEventBind() {
      // 连接成功建立的回调
      this.socketObj.onopen = this.onOpenCallback;
      // 连接发生错误的回调
      this.socketObj.onerror = this.onErrorCallback;
      // 连接关闭的回调
      this.socketObj.onclose = this.onCloseCallback;
      // 向后端发送数据的回调
      this.socketObj.onsend = this.onSendCallback;
      // 接收到消息的回调
      this.socketObj.onmessage = this.getMessageCallback;

      //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
      window.onbeforeunload = () => {
        this.socketObj.close();
      };
    },
    // websocket重连
    socketReconnect() {
      if (this.socketReconnectLock) {
        return;
      }
      this.socketReconnectLock = true;
      this.socketReconnectTimer && clearTimeout(this.socketReconnectTimer);
      this.socketReconnectTimer = setTimeout(() => {
        console.log("WebSocket:重连中...");
        this.socketReconnectLock = false;
        // websocket启动
        this.createWebSocket();
      }, 4000);
    },
    // 连接成功建立的回调
    onOpenCallback: function (event) {
      console.log("WebSocket:已连接");
      // 心跳检测重置
      this.heartCheck.reset().start();
    },
    // 连接发生错误的回调
    onErrorCallback: function (event) {
      console.log("WebSocket:发生错误");
      // websocket重连
      this.socketReconnect();
    },
    // 连接关闭的回调
    onCloseCallback: function (event) {
      console.log("WebSocket:已关闭");
      // 心跳检测重置
      this.heartCheck.reset();
      if (!this.socketLeaveFlag) {
        // 没有离开——重连
        // websocket重连
        this.socketReconnect();
      }
    },
    // 向后端发送数据的回调
    onSendCallback: function () {
      console.log("WebSocket:发送信息给后端");
    },
    // 接收到消息的回调
    getMessageCallback: function (msg) {
      if (msg.data.indexOf("HeartBeat") > -1) {
        // 心跳回复——心跳检测重置
        // 收到心跳检测回复就说明连接正常
        // console.log("收到心跳检测回复");
        // 心跳检测重置
        this.heartCheck.reset().start();
      } else if(msg.data.indexOf("CONNECT_SUCCESS") > -1){

      }else {
        // 普通推送——正常处理
        try{
          let data = JSON.parse(msg.data);
          console.info(data.toGroupId!=null)
          if(data.toGroupId!=null){
            //群聊
            for (let i in this.contactList){
              if(this.contactList[i]['groupId']===data.toGroupId){
                let message = {
                  id: data['id'],
                  status: data.status,
                  type: data.type,
                  sendTime: data.sendTime,
                  content: data.content,
                  toContactId: this.contactList[i]['id'],
                  fromUser: {
                    id: data.fromUser.id,
                    displayName: data.fromUser['nick'],
                    avatar: data.fromUser.avatar,
                  }
                }
                console.info(message);
                this.$refs.IMUI.appendMessage(message, true);
              }
            }
          }else {
            //私聊
            for (let i in this.contactList){
              console.info(this.contactList[i]['userId']);
              console.info(data.toUserId);
              console.info("----");
              if(this.contactList[i]['userId']===data.fromUser.id){
                let message = {
                  id: data['id'],
                  status: data.status,
                  type: data.type,
                  sendTime: data.sendTime,
                  content: data.content,
                  toContactId: this.contactList[i]['id'],
                  fromUser: {
                    id: data.fromUser.id,
                    displayName: data.fromUser['nick'],
                    avatar: data.fromUser.avatar,
                  }
                }
                console.info(message);
                this.$refs.IMUI.appendMessage(message, true);
              }
            }
          }


        }catch (e){

        }
      }
    },
  },
}
</script>

<style scoped>

</style>
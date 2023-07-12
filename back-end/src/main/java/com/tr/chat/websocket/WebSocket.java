package com.tr.chat.websocket;

import com.alibaba.fastjson.JSON;
import com.tr.chat.entity.*;
import com.tr.chat.mapper.ContactMapper;
import com.tr.chat.mapper.GroupMapper;
import com.tr.chat.mapper.MessageMapper;
import com.tr.chat.service.MessageService;
import com.tr.chat.util.LoggerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/wsChat/{userId}")
@Component
public class WebSocket {
    private static MessageMapper messageMapper;
    private static MessageService messageService;
    private static GroupMapper groupMapper;
    private static ContactMapper contactMapper;

    private static ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();
    //实例一个session，这个session是websocket的session
    private Session session;

    //新增一个方法用于主动向客户端发送消息
    public static void sendMessage(Object message, String userId) {
        WebSocket webSocket = webSocketMap.get(userId);
        if (webSocket != null) {
            try {
                webSocket.session.getBasicRemote().sendText(JSON.toJSONString(message));
                LoggerUtil.info("[服务端]发送消息给用户{"+userId+"},消息内容{"+message.toString()+"}");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //新增一个方法用于私聊消息推送
    public static void pushPrivateMessage(Message message) {
        if(message!=null){
            WebSocket webSocket = webSocketMap.get(String.valueOf(message.getToUserId()));
            if (webSocket != null) {
                try {
                    webSocket.session.getBasicRemote().sendText(JSON.toJSONString(message));
                    LoggerUtil.info("[服务端]发送消息给用户{"+message.getToUserId()+"},消息内容{"+ message +"}");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                //离线用户增加未读消息
                Contact contact = contactMapper.getByOwnerFromUser(String.valueOf(message.getToUserId()), String.valueOf(message.getFromUser().getId()));
                contact.setUnread(contact.getUnread()+1);
                contactMapper.updateById(contact);
            }
        }
    }


    //新增一个方法用于群聊消息推送
    public static void pushGroupMessage(Message message) {
        if(message!=null){
            //查询群组信息
            Group group=  groupMapper.getGroupInfoById(String.valueOf(message.getToGroupId()));
            for(GroupUser user:group.getUserList()){
                //遍历群组所属成员来进行推送
                WebSocket webSocket = webSocketMap.get(String.valueOf(user.getUser().getId()));
                //在线用户实时推送
                if (webSocket != null) {
                    try {
                        webSocket.session.getBasicRemote().sendText(JSON.toJSONString(message));
                        LoggerUtil.info("[服务端]发送消息给用户{"+message.getToUserId()+"},消息内容{"+ message +"}");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    //离线用户增加未读消息
                    Contact contact = contactMapper.getByOwnerFromGroup(String.valueOf(user.getUser().getId()),String.valueOf(group.getId()));
                    contact.setUnread(contact.getUnread()+1);
                    contactMapper.updateById(contact);
                }
            }
        }
    }

    public static ConcurrentHashMap<String, WebSocket> getWebSocketMap() {
        return webSocketMap;
    }

    public static void setWebSocketMap(ConcurrentHashMap<String, WebSocket> webSocketMap) {
        WebSocket.webSocketMap = webSocketMap;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocket.messageMapper=applicationContext.getBean(MessageMapper.class);
        WebSocket.messageService=applicationContext.getBean(MessageService.class);
        WebSocket.groupMapper=applicationContext.getBean(GroupMapper.class);
        WebSocket.contactMapper=applicationContext.getBean(ContactMapper.class);
    }

    //前端请求时一个websocket时
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        webSocketMap.put(userId, this);
        sendMessage("CONNECT_SUCCESS", userId);
        LoggerUtil.info("[服务端]用户ID{"+userId+"}已连接");
    }

    @OnError
    public void onError(Session session,Throwable error) throws IOException {
        LoggerUtil.info("[服务端]会话{"+session.toString()+"}连接失败,错误信息:"+error.toString());
    }

    //前端关闭时一个websocket时
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        webSocketMap.remove(userId);
        LoggerUtil.info("[服务端]用户{"+userId+"}连接断开,目前用户总数:"+ webSocketMap.size());
    }

    //前端向后端发送消息
    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        if (!message.equals("HeartBeat")) {
            try{
                //消息体转java对象
                Message tMessage=JSON.parseObject(message, Message.class);
                //插入消息
                Map<Object,Object> map=new HashMap<>();
                map.put("name","InsertMsg");
                map.put("msg",tMessage);
                messageService.handle(map);
                //从数据库查询数据
                if(tMessage.getToUserId()!=null){
                    pushPrivateMessage(tMessage);
                }else {
                    pushGroupMessage(tMessage);
                }
                //告诉客户端已收到消息
                sendMessage("ok",String.valueOf(tMessage.getFromUserId()));
                LoggerUtil.info("[服务端]收到客户端发来的消息:"+tMessage);
            }catch (Exception ignored){

            }

        }else {
            session.getBasicRemote().sendText("HeartBeat");
        }
    }
}
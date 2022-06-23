package com.gs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gs.model.dto.team.MessageDto;
import com.gs.model.vo.team.MessageVo;
import com.gs.service.intf.team.MemberService;
import com.gs.service.intf.team.MessageService;
import com.gs.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ServerEndpoint 这个注解用于标识作用在类上
 * 它的主要功能是把当前类标识成一个WebSocket的服务端
 * 注解的值用户客户端连接访问的URL地址
 *
 * @author ZhangFZ
 * @date 2020/9/29 10:30
 **/
@ServerEndpoint("/game/v1.0/app/gameteam/manager/{teamId}/{userId}")
@Component
@Slf4j
public class WebSocketServer {

    private MessageService messageService;
    private MemberService memberService;

    public void initServiceImp() {
        this.messageService = SpringUtil.getBean(MessageService.class);
        this.memberService = SpringUtil.getBean(MemberService.class);
    }

    /**
     * 将每个客户端对应的MyWebSocket对象按照team进行分组
     */
    private static ConcurrentHashMap<Long, ConcurrentHashMap<Long, WebSocketServer>> webTeamSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private Long userId = null;

    /**
     * 接收teamId
     */
    private Long teamId = null;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("teamId") Long teamId, @PathParam("userId") Long userId) {
        this.session = session;
        this.userId = userId;
        this.teamId = teamId;

        if (webTeamSocketMap.containsKey(teamId)) {
            ConcurrentHashMap<Long, WebSocketServer> memberSocketMap = webTeamSocketMap.get(teamId);
            if (null == memberSocketMap) {
                memberSocketMap = new ConcurrentHashMap<>();
                memberSocketMap.put(userId, this);
                webTeamSocketMap.put(teamId, memberSocketMap);
            } else {
                if (memberSocketMap.containsKey(userId)) {
                    memberSocketMap.remove(userId);

                }
                memberSocketMap.put(userId, this);
                webTeamSocketMap.put(teamId, memberSocketMap);
            }
        } else {
            ConcurrentHashMap<Long, WebSocketServer> memberSocketMap = new ConcurrentHashMap<>();
            memberSocketMap.put(userId, this);
            webTeamSocketMap.put(teamId, memberSocketMap);
        }

        initServiceImp();

        //查看当前是否有接收到的系统消息
//        try {
//            MessageVo messageVo = new MessageVo();
//
//            String memberName = memberService.getMemberById(userId).getName();
//            String content = memberName + " 已上线";
//            messageVo.setContent(content);
//            messageVo.setType(1);
//            messageVo.setCreateTime(sdf.format(new Date()));
//            sendBroadcastMessage(messageVo);
//        } catch (IOException e) {
//            log.error("用户:" + userId + ",网络异常!!!!!!");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webTeamSocketMap.containsKey(teamId)) {
            ConcurrentHashMap<Long, WebSocketServer> memberSocketMap = webTeamSocketMap.get(teamId);
            if (memberSocketMap.containsKey(userId)) {
                memberSocketMap.remove(userId);
                if (memberSocketMap.isEmpty()) {
                    webTeamSocketMap.remove(teamId);
                }

//                try {
//                    MessageVo messageVo = new MessageVo();
//
//                    String memberName = memberService.getMemberById(userId).getName();
//                    String content = memberName + " 已下线";
//                    messageVo.setContent(content);
//                    messageVo.setType(2);
//                    messageVo.setCreateTime(sdf.format(new Date()));
//                    sendBroadcastMessage(messageVo);
//                } catch (IOException e) {
//                    log.error("用户:" + userId + ",网络异常!!!!!!");
//                }

            } else {
                log.error("team:" + teamId + "user:" + userId + ",关闭异常（team下不存在user的socket连接）!!!!!!");
            }
        } else {
            log.error("team:" + teamId + "user:" + userId + ",关闭异常（不存在team的socket连接）!!!!!!");
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("战队" + teamId + "成员" + userId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (!message.isEmpty()) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);

                Long teamId = jsonObject.getLong("teamId");

                if (teamId != null) {
                    int type = jsonObject.getInteger("type");

                    if (type != 3 && type != 4) {
                        log.error("战队" + this.teamId + "中的" + this.userId + "消息类型不正确:" + type);
                        return;
                    }

                    if (webTeamSocketMap.containsKey(teamId)) {

                        if (type == 4) {
                            //将message存在在数据库中
                            MessageDto messageDto = JSON.parseObject(message, MessageDto.class);
                            MessageVo messageVo = messageService.insertMessage(messageDto);
                            if (null == messageVo) {
                                log.error("已经发送过该消息：" + jsonObject.toJSONString());
                                return;
                            }

                            sendBroadcastMessage(messageVo);
                        } else {
                            Long toUserId = jsonObject.getLong("toId");
                            if (toUserId != null) {
                                if (toUserId.equals(userId)) {
                                    log.error("战队" + this.teamId + "中的" + this.userId + "不能给自己发送消息");
                                    return;
                                }
                            } else {
                                log.error("请求参数有问题，参数中没有填写toId参数");
                            }

                            ConcurrentHashMap<Long, WebSocketServer> memberSocketMap = webTeamSocketMap.get(teamId);

                            if (memberSocketMap.containsKey(toUserId)) {

                                WebSocketServer webSocketServer = memberSocketMap.get(toUserId);

                                //将message存在在数据库中
                                MessageDto messageDto = JSON.parseObject(message, MessageDto.class);
                                MessageVo messageVo = messageService.insertMessage(messageDto);
                                if (null == messageVo) {
                                    log.error("已经发送过该消息：" + jsonObject.toJSONString());
                                    return;
                                }

                                webSocketServer.sendMessage(messageVo);
                            } else {
                                log.error("请求的userId:" + userId + "不在该服务器上");
                            }
                        }


                    } else {
                        log.error("请求的teamId:" + teamId + "不在该服务器上");
                    }
                } else {
                    log.error("请求参数有问题，参数中没有填写teamId参数");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        log.error("战队:" + this.userId + "成员:" + this.userId + ",error原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    private void sendMessageText(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 实现服务器主动推送
     * MessageVo 自定义推送的消息实体
     */
    private void sendMessage(MessageVo message) throws IOException {
        this.session.getBasicRemote().sendText(JSONObject.toJSONString(message));
    }

    /**
     * 实现服务器主动推送
     * MessageVo 自定义推送的消息实体List
     */
    private void sendMessage(List<MessageVo> message) throws IOException {
        this.session.getBasicRemote().sendText(JSONObject.toJSONString(message));
    }

    private void sendBroadcastMessage(MessageVo message) throws IOException {
        ConcurrentHashMap<Long, WebSocketServer> memberSocketMap = webTeamSocketMap.get(teamId);
        for (WebSocketServer entry : memberSocketMap.values()) {
            if (!Objects.equals(entry.userId, this.userId)) {
                entry.sendMessage(message);
            }
        }
    }

}

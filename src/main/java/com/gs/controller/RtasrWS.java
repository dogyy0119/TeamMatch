package com.gs.controller;

/**
 * websocket服务端
 */
import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint("/ist")
@RestController
@CrossOrigin
public class RtasrWS {
    private static final AtomicInteger segId = new AtomicInteger(1);

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("I'm open.");

        // 获取请求url
        String url = session.getRequestURI().toString();
        System.out.println("request url: " + url);

        // 获取参数
        if (session.getRequestParameterMap().get("appId") != null) {
            System.out.println("appId: " + session.getRequestParameterMap().get("appId").get(0));
        }
    }

    private Object getResult() {
        Map<String, Object> result = new HashMap<>();
        result.put("seg_id", segId.getAndIncrement());
        return result;
    }

    @OnClose
    public void onClose() {
        System.out.println("I'm close.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("I'm String onMessage.");
        sendMessage(session, JSON.toJSONString(getResult()));
    }

    @OnMessage
    public void onMessage(byte[] message, Session session) {
        System.out.println("I'm byte onMessage.");
        sendMessage(session, JSON.toJSONString(getResult()));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("I'm error: " + error.getMessage());
    }

    private static void sendMessage(Session session, String message) {
        if (session == null)
            return;
        System.out.println("send msg : " + message);
        if (session.isOpen() && message.length() != 0) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                System.out.println("send message error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

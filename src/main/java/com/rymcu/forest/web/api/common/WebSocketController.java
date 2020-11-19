package com.rymcu.forest.web.api.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

/**
 * @author ronger
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/sendMessage")
    @SendTo("/public/greetings")
    public void sendMessage(JSONObject message, StompHeaderAccessor headerAccessor){
        this.template.convertAndSend("/public/greetings",message);
    }

    @MessageMapping("/message")
    @SendToUser("/message")
    public void message(JSONObject message){
        String type = message.get("type").toString();
        HashMap res = (HashMap) message.get("data");
        HashMap mine = (HashMap) res.get("mine");
        HashMap to = (HashMap) res.get("to");
        System.out.println(to.get("type"));
        boolean flag = to.get("type").equals("friend")?true:false;
        String id = to.get("id").toString();
        HashMap map = new HashMap();
        map.put("id",mine.get("id"));
        map.put("avatar",mine.get("avatar"));
        map.put("formid",mine.get("id"));
        map.put("username",mine.get("username"));
        map.put("type",to.get("type"));
        map.put("content",mine.get("content"));
        map.put("mine",false);
        map.put("cid",0);
        map.put("timestamp","");
        JSONObject json = new JSONObject();
        json.put("type",type);
        json.put("data",map);
        if(flag){
            this.template.convertAndSendToUser(id,"/message",json);
        }else{
            this.template.convertAndSendToUser(id,"/message",json);
        }
    }
}

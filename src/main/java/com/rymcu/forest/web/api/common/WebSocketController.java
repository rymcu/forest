package com.rymcu.forest.web.api.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@Controller
public class WebSocketController {

    @Resource
    private SimpMessagingTemplate template;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/greening")
    public void sendMessage(JSONObject message, StompHeaderAccessor headerAccessor) {
        this.template.convertAndSend("/topic/greening", message);
    }

    @MessageMapping("/message")
    @SendToUser("/message")
    public void message(JSONObject message, StompHeaderAccessor headerAccessor) {
        this.template.convertAndSendToUser(message.getString("to"), "/message", message);
    }
}

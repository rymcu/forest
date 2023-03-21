package com.rymcu.forest.openai;

import com.alibaba.fastjson.JSONObject;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2023/2/15 10:04.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.openai
 */
@RestController
@RequestMapping("/api/v1/openai")
public class OpenAiController {

    @Value("${openai.token}")
    private String token;

    @PostMapping("/chat")
    public GlobalResult<List<ChatCompletionChoice>> chat(@RequestBody JSONObject jsonObject) {
        String message = jsonObject.getString("message");
        if (StringUtils.isBlank(message)) {
            throw new IllegalArgumentException("参数异常！");
        }
        ChatMessage chatMessage = new ChatMessage("user", message);
        List<ChatMessage> list = new ArrayList();
        list.add(chatMessage);
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(180));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(list)
                .build();
        List<ChatCompletionChoice> choices =  service.createChatCompletion(completionRequest).getChoices();
        return GlobalResultGenerator.genSuccessResult(choices);
    }

}

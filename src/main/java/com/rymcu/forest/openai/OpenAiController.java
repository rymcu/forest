package com.rymcu.forest.openai;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.openai.entity.ChatMessageModel;
import com.rymcu.forest.openai.entity.ChatModel;
import com.rymcu.forest.openai.service.OpenAiService;
import com.rymcu.forest.openai.service.SseService;
import com.rymcu.forest.util.Html2TextUtil;
import com.rymcu.forest.util.UserUtils;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
    @Resource
    private SseService sseService;

    @Value("${openai.token}")
    private String token;

    @PostMapping("/chat")
    public GlobalResult newChat(@RequestBody ChatModel chatModel) {
        List<ChatMessageModel> messages = chatModel.getMessages();
        String model = chatModel.getModel();
        if (messages.isEmpty()) {
            throw new IllegalArgumentException("参数异常！");
        }
        User user = UserUtils.getCurrentUserByToken();
        List<ChatMessage> list = new ArrayList<>(messages.size());
        if (messages.size() > 4) {
            messages = messages.subList(messages.size() - 4, messages.size());
        }
        if (messages.size() >= 4 && messages.size() % 4 == 0) {
            ChatMessage message = new ChatMessage("system", "简单总结一下你和用户的对话, 用作后续的上下文提示 prompt, 控制在 200 字内");
            list.add(message);
        }
        messages.forEach(chatMessageModel -> {
            ChatMessage message = new ChatMessage(chatMessageModel.getRole(), Html2TextUtil.getContent(chatMessageModel.getContent()));
            list.add(message);
        });
        return sendMessage(user, list, model);
    }

    @NotNull
    private GlobalResult sendMessage(User user, List<ChatMessage> list, String model) {
        if (StringUtils.isBlank(model)) {
            model = "gpt-3.5-turbo-16k-0613";
        }
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(180));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .model(model)
                .stream(true)
                .messages(list)
                .build();
        service.streamChatCompletion(completionRequest).doOnError(Throwable::printStackTrace)
                .blockingForEach(chunk -> {
                    if (chunk.getChoices().isEmpty() || chunk.getChoices().get(0).getMessage() == null) {
                        return;
                    }
                    String text = chunk.getChoices().get(0).getMessage().getContent();
                    if (text == null) {
                        return;
                    }
                    System.out.print(text);
                    sseService.send(user.getIdUser(), text);
                });
        service.shutdownExecutor();
        return GlobalResultGenerator.genSuccessResult();
    }
}

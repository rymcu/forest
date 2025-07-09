package com.rymcu.forest.answer;

import com.alibaba.fastjson.JSONObject;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.service.log.annotation.TransactionLogger;
import com.rymcu.forest.dto.AnswerDTO;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.enumerate.TransactionEnum;
import com.rymcu.forest.util.HttpUtils;
import com.rymcu.forest.util.UserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/answer")
public class AnswerController {

    @Value("${resource.answer-api-url}")
    private String ANSWER_API_URL;

    @GetMapping("/today")
    public GlobalResult today() {
        User user = UserUtils.getCurrentUserByToken();
        String result = HttpUtils.sendGet(ANSWER_API_URL + "/record/" + user.getIdUser());
        return JSONObject.parseObject(result, GlobalResult.class);
    }

    @PostMapping("/answer")
    @TransactionLogger(transactionType = TransactionEnum.Answer)
    public GlobalResult answer(@RequestBody AnswerDTO answerDTO) {
        User user = UserUtils.getCurrentUserByToken();
        Map<String, Object> params = new HashMap<>(3);
        params.put("userId", user.getIdUser());
        params.put("answer", answerDTO.getAnswer());
        params.put("subjectQuestionId", answerDTO.getIdSubjectQuestion());
        String result = HttpUtils.sendPost(ANSWER_API_URL + "/answer/everyday", params);
        return JSONObject.parseObject(result, GlobalResult.class);
    }

    @GetMapping("/get-answer")
    public GlobalResult getAnswer(Integer idSubjectQuestion) {
        String result = HttpUtils.sendGet(ANSWER_API_URL + "/show-answer/" + idSubjectQuestion);
        return JSONObject.parseObject(result, GlobalResult.class);
    }
}

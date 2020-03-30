package com.rymcu.vertical.web.api.comment;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.entity.Comment;
import com.rymcu.vertical.service.CommentService;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/post")
    public GlobalResult postComment(@RequestBody Comment comment, HttpServletRequest request) throws BaseApiException, UnsupportedEncodingException {
        Map map = commentService.postComment(comment,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

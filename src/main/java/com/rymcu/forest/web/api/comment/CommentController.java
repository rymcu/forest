package com.rymcu.forest.web.api.comment;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.util.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/comment")
@RequiresPermissions(value = "user")
public class CommentController {

    @Resource
    private CommentService commentService;

    @PostMapping("/post")
    public GlobalResult<Comment> postComment(@RequestBody Comment comment, HttpServletRequest request) {
        comment.setCommentAuthorId(UserUtils.getCurrentUserByToken().getIdUser());
        comment = commentService.postComment(comment, request);
        return GlobalResultGenerator.genSuccessResult(comment);
    }
}

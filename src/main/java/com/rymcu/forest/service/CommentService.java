package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ronger
 */
public interface CommentService extends Service<Comment> {

    /**
     * 获取文章评论数据
     *
     * @param idArticle
     * @return
     */
    List<CommentDTO> getArticleComments(Integer idArticle);

    /**
     * 评论
     *
     * @param comment
     * @param request
     * @return
     */
    Comment postComment(Comment comment, HttpServletRequest request);

    /**
     * 获取评论列表数据
     *
     * @return
     */
    List<CommentDTO> findComments();
}

package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.CommentDTO;
import com.rymcu.vertical.entity.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface CommentService extends Service<Comment> {

    List<CommentDTO> getArticleComments(Integer idArticle);

    Map postComment(Comment comment, HttpServletRequest request);
}

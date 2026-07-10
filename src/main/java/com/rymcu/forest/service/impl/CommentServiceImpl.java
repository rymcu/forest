/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.ContentNotExistException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.handler.event.CommentEvent;
import com.rymcu.forest.mapper.CommentMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.util.XssUtils;
import org.apache.commons.lang.StringUtils;
import com.rymcu.forest.config.RabbitMQConfig;
import com.rymcu.forest.event.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author ronger
 */
@Service
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ArticleService articleService;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public List<CommentDTO> getArticleComments(Integer idArticle) {
        List<CommentDTO> commentDTOList = commentMapper.selectArticleComments(idArticle);
        return genComments(commentDTOList);
    }

    private List<CommentDTO> genComments(List<CommentDTO> commentDTOList) {
        commentDTOList.forEach(commentDTO -> {
            commentDTO.setTimeAgo(Utils.getTimeAgo(commentDTO.getCreatedTime()));
            commentDTO.setCommentContent(XssUtils.filterHtmlCode(commentDTO.getCommentContent()));
            if (commentDTO.getCommentAuthorId() != null) {
                Author author = commentMapper.selectAuthor(commentDTO.getCommentAuthorId());
                if (author != null) {
                    commentDTO.setCommenter(author);
                }
            }
            if (commentDTO.getCommentOriginalCommentId() != null && commentDTO.getCommentOriginalCommentId() > 0) {
                Author commentOriginalAuthor = commentMapper.selectCommentOriginalAuthor(commentDTO.getCommentOriginalCommentId());
                if (commentOriginalAuthor != null) {
                    commentDTO.setCommentOriginalAuthorThumbnailURL(commentOriginalAuthor.getUserAvatarURL());
                    commentDTO.setCommentOriginalAuthorNickname(commentOriginalAuthor.getUserNickname());
                }
                Comment comment = commentMapper.selectByPrimaryKey(commentDTO.getCommentOriginalCommentId());
                commentDTO.setCommentOriginalContent(comment.getCommentContent());
            }
        });
        return commentDTOList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Comment postComment(Comment comment, HttpServletRequest request) {
        if (comment.getCommentArticleId() == null) {
            throw new IllegalArgumentException("非法访问,文章主键异常！");
        }
        if (comment.getCommentAuthorId() == null) {
            throw new IllegalArgumentException("非法访问,用户未登录！");
        }
        if (StringUtils.isBlank(comment.getCommentContent())) {
            throw new IllegalArgumentException("回帖内容不能为空！");
        }
        Article article = articleService.findById(comment.getCommentArticleId().toString());
        if (article == null) {
            throw new ContentNotExistException("文章不存在！");
        }
        String ip = Utils.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        comment.setCommentIP(ip);
        comment.setCommentUA(ua);
        comment.setCreatedTime(new Date());
        comment.setCommentContent(XssUtils.filterHtmlCode(comment.getCommentContent()));
        commentMapper.insertSelective(comment);
        String commentSharpUrl = article.getArticlePermalink() + "#comment-" + comment.getIdComment();
        commentMapper.updateCommentSharpUrl(comment.getIdComment(), commentSharpUrl);

        String commentContent = comment.getCommentContent();
        if (StringUtils.isNotBlank(commentContent)) {
            eventPublisher.publish(RabbitMQConfig.RK_COMMENT_CREATE,
                    new CommentEvent(comment.getIdComment(), article.getArticleAuthorId(),
                            comment.getCommentAuthorId(), commentContent,
                            comment.getCommentOriginalCommentId()));
        }
        return comment;
    }

    @Override
    public List<CommentDTO> findComments() {
        List<CommentDTO> commentDTOList = commentMapper.selectComments();
        return genComments(commentDTOList);
    }
}

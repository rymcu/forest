package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.mapper.CommentMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.util.Html2TextUtil;
import com.rymcu.forest.util.NotificationUtils;
import com.rymcu.forest.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author ronger
 */
@Service
public class CommentServiceImpl extends AbstractService<Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private ArticleService articleService;

    private static final int MAX_PREVIEW = 200;

    @Override
    public List<CommentDTO> getArticleComments(Integer idArticle) {
        List<CommentDTO> commentDTOList = commentMapper.selectArticleComments(idArticle);
        commentDTOList.forEach(commentDTO -> {
            commentDTO.setTimeAgo(Utils.getTimeAgo(commentDTO.getCreatedTime()));
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
    public Map postComment(Comment comment, HttpServletRequest request) {
        Map map = new HashMap(1);
        if(comment.getCommentArticleId() == null){
            map.put("message","非法访问,文章主键异常！");
            return map;
        }
        if(comment.getCommentAuthorId() == null){
            map.put("message","非法访问,用户未登录！");
            return map;
        }
        if(StringUtils.isBlank(comment.getCommentContent())){
            map.put("message","回帖内容不能为空！");
            return map;
        }
        Article article = articleService.findById(comment.getCommentArticleId().toString());
        if (article == null) {
            map.put("message","文章不存在！");
            return map;
        }
        String ip = Utils.getIpAddress(request);
        String ua = request.getHeader("user-agent");
        comment.setCommentIP(ip);
        comment.setCommentUA(ua);
        comment.setCreatedTime(new Date());
        commentMapper.insertSelective(comment);
        StringBuilder commentSharpUrl = new StringBuilder(article.getArticlePermalink());
        commentSharpUrl.append("#comment-").append(comment.getIdComment());
        commentMapper.updateCommentSharpUrl(comment.getIdComment(), commentSharpUrl.toString());

        String commentContent = comment.getCommentContent();
        if(StringUtils.isNotBlank(commentContent)){
            Integer length = commentContent.length();
            if(length > MAX_PREVIEW){
                length = 200;
            }
            String commentPreviewContent = commentContent.substring(0,length);
            commentContent = Html2TextUtil.getContent(commentPreviewContent);
            // 评论者不是作者本人则进行消息通知
            if (!article.getArticleAuthorId().equals(comment.getCommentAuthorId())) {
                NotificationUtils.saveNotification(article.getArticleAuthorId(),comment.getIdComment(), NotificationConstant.Comment, commentContent);
            }
            // 判断是否是回复消息
            if (comment.getCommentOriginalCommentId() != null && comment.getCommentOriginalCommentId() != 0) {
                Comment originalComment = commentMapper.selectByPrimaryKey(comment.getCommentOriginalCommentId());
                // 回复消息时,评论者不是上级评论作者则进行消息通知
                if (!comment.getCommentAuthorId().equals(originalComment.getCommentAuthorId())) {
                    NotificationUtils.saveNotification(originalComment.getCommentAuthorId(),comment.getIdComment(), NotificationConstant.Comment, commentContent);
                }
            }
        }


        return map;
    }
}

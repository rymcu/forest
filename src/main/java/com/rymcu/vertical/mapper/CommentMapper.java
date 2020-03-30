package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.CommentDTO;
import com.rymcu.vertical.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface CommentMapper extends Mapper<Comment> {
    /**
     * 获取文章评论列表
     * @param idArticle
     * @return
     */
    List<CommentDTO> selectArticleComments(@Param("idArticle") Integer idArticle);

    /**
     * 查询评论作者
     * @param commentAuthorId
     * @return
     */
    Author selectAuthor(@Param("commentAuthorId") Integer commentAuthorId);

    /**
     * 查询父评论作者
     * @param commentOriginalCommentId
     * @return
     */
    Author selectCommentOriginalAuthor(@Param("commentOriginalCommentId") Integer commentOriginalCommentId);

    /**
     * 更新文章评论分享链接
     * @param idComment
     * @param commentSharpUrl
     * @return
     */
    Integer updateCommentSharpUrl(@Param("idComment") Integer idComment, @Param("commentSharpUrl") String commentSharpUrl);
}

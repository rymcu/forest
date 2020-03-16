package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.CommentDTO;
import com.rymcu.vertical.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper extends Mapper<Comment> {
    /**
     * @param idArticle
     * @return
     */
    List<CommentDTO> selectArticleComments(@Param("idArticle") Integer idArticle);

    /**
     * @param commentAuthorId
     * @return
     */
    Author selectAuthor(@Param("commentAuthorId") Integer commentAuthorId);

    /**
     * @param commentOriginalCommentId
     * @return
     */
    Author selectCommentOriginalAuthor(@Param("commentOriginalCommentId") Integer commentOriginalCommentId);

    /**
     * @param idComment
     * @param toString
     * @return
     */
    Integer updateCommentSharpUrl(@Param("idComment") Integer idComment, @Param("commentSharpUrl") String commentSharpUrl);
}

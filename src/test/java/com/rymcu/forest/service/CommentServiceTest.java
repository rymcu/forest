package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.exception.ContentNotExistException;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentServiceTest extends BaseServiceTest {


    /**
     * 模拟request请求参数
     */
    private MockHttpServletRequest request;
    /**
     * 测试用的Article数据，用于该单元测试的一系列操作
     */
    private final ArticleDTO testArticle;
    /**
     * 与Article相关联的测试User数据(建表时提前放入)
     */
    private final User testUser = new User();
    @Autowired
    private CommentService commentService;
    @Autowired
    private ArticleService articleService;

    {
        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        // 构建数据之间的关联结构
        Author testAuthor = Author.builder()
                .idUser(2L)
                .userArticleCount("0")
                .userAccount("testUser")
                .userNickname("testUser")
                .userAvatarURL(null)
                .build();
        BeanUtils.copyProperties(testAuthor, testUser);

        ArticleTagDTO tagDTO = ArticleTagDTO.builder()
                .tagTitle("Test")
                .tagDescription("Test")
                .idTag(111)
                .tagAuthorId(testUser.getIdUser())
                .build();

        List<ArticleTagDTO> tags = new ArrayList<>();
        tags.add(tagDTO);

        testArticle = ArticleDTO.builder()
                .articleAuthor(testAuthor)
                .articleAuthorId(testAuthor.getIdUser())
                .articleContent("Test")
                .articleLink("Test")
                .articlePerfect("0")
                .articlePermalink("Test")
                .articleAuthorName(testAuthor.getUserNickname())
                .articleCommentCount(0)
                .articleStatus("0")
                .articleTags("Test")
                .articleContentHtml("<h1>Test</h1>")
                .articleTitle("Test")
                .articleType("0")
                .articlePreviewContent("Test")
                .articleSponsorCount(12)
                .articlePermalink("Test")
                .articleViewCount(0)
                .tags(tags)
                .build();
    }

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        Long articleId = articleService.postArticle(testArticle, testUser);
        assertNotNull(articleId);
        testArticle.setIdArticle(articleId);
    }


    @Test
    @DisplayName("获取评论")
    void getArticleComments() {
        Integer idArticle = testArticle.getIdArticle().intValue();
        List<CommentDTO> articleComments = commentService.getArticleComments(idArticle);
        assertEquals(0, articleComments.size());
    }

    @Test
    @DisplayName("评论")
    void postComment() {
        Comment comment = new Comment();

        // 文章id为null
        assertThrows(IllegalArgumentException.class,()->{
            commentService.postComment(comment,request);
        });

        comment.setCommentArticleId(-2L);
        // 用户为空
        assertThrows(IllegalArgumentException.class,()->{
            commentService.postComment(comment,request);
        });

        comment.setCommentAuthorId(2L);
        // 回帖内容为空
        assertThrows(IllegalArgumentException.class,()->{
            commentService.postComment(comment,request);
        });

        comment.setCommentContent("评论内容");
        // 文章不存在
        assertThrows(ContentNotExistException.class,()->{
            commentService.postComment(comment,request);
        });

        comment.setCommentArticleId(testArticle.getIdArticle());
        Comment comment1 = commentService.postComment(comment, request);
        // 评论成功
        assertEquals(comment.getCommentContent(), comment1.getCommentContent());

        //测试评论数量
        List<CommentDTO> articleComments = commentService.getArticleComments(testArticle.getIdArticle().intValue());
        assertEquals(1, articleComments.size());

        List<CommentDTO> findComments = commentService.findComments();
        assertEquals(1, findComments.size());
    }

    @Test
    @DisplayName("获取评论列表数据")
    void findComments() {
        Integer idArticle = testArticle.getIdArticle().intValue();
        List<CommentDTO> articleComments = commentService.findComments();
        assertEquals(0, articleComments.size());
    }
}
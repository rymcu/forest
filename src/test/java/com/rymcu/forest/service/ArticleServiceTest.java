package com.rymcu.forest.service;

import cn.hutool.core.collection.CollectionUtil;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleSearchDTO;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
// 顺序执行单元测试
@TestMethodOrder(MethodOrderer.Random.class)
class ArticleServiceTest {

    /**
     * 测试用的Article数据，用于该单元测试的一系列操作
     */
    private final ArticleDTO testArticle;

    /**
     * 与Article相关联的测试User数据(建表时提前放入)
     */
    private final User testUser = new User();

    {
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

    @Autowired
    private ArticleService articleService;

    /**
     * 将测试用的Article数据插入数据库中（会回滚的:)）
     *
     * 测试数据是否会返回Article的Id，并且Id会填充到测试数据中
     * @throws UnsupportedEncodingException
     */
    @Test
    @BeforeEach
    public void postArticle() throws UnsupportedEncodingException {
        Long articleId = articleService.postArticle(testArticle, testUser);
        assertNotNull(articleId);
        testArticle.setIdArticle(articleId);
    }

    /**
     * 测试条件查询
     * <p>
     * 无参数时返回所有Article
     */
    @Test
    void findArticles() {
        // 无参数时返回参数不应为EmptyList
        List<ArticleDTO> articlesAll = articleService.findArticles(new ArticleSearchDTO());
        assertTrue(CollectionUtil.isNotEmpty(articlesAll));

        // 测试条件查询是否含有目标数据
        ArticleSearchDTO articleSearchDTO = new ArticleSearchDTO();
        articleSearchDTO.setSearchText(testArticle.getArticleContent());
        Map<Long, ArticleDTO> idArticleMap = articleService.findArticles(articleSearchDTO)
                .stream()
                .collect(Collectors.toMap(ArticleDTO::getIdArticle, item -> item));
        assertNotNull(idArticleMap.get(testArticle.getIdArticle()));
    }

    /**
     * 测试通过Id获取Article
     */
    @Test
    void findArticleDTOById() {
        // 测试参数为
        ArticleDTO articleDTOByIdNull = articleService.findArticleDTOById(null, 0);
        assertNull(articleDTOByIdNull);
        ArticleDTO articleDTOById = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        assertNotNull(articleDTOById);
    }

//    @Test
//    void findArticlesByTopicUri() {
//        List<ArticleDTO> articlesByTopicUri = articleService.findArticlesByTopicUri();
//    }

//    @Test
//    void findArticlesByTagName() {
//        List<ArticleDTO> articlesByTagName = articleService.findArticlesByTagName();
//    }

    /**
     * 通过UserId获取对应的Articles
     */
    @Test
    void findUserArticlesByIdUser() {
        List<ArticleDTO> userArticlesByIdUser = articleService.findUserArticlesByIdUser(testArticle.getArticleAuthorId());
        assertTrue(CollectionUtil.isNotEmpty(userArticlesByIdUser));
    }

    /**
     * 测试文章浏览增量后是否成功
     */
    @Test
    void incrementArticleViewCount() {
        ArticleDTO articleDTOByIdBefore = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        articleService.incrementArticleViewCount(testArticle.getIdArticle());
        ArticleDTO articleDTOByIdAfter = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        assertEquals(articleDTOByIdBefore.getArticleViewCount() + 1, articleDTOByIdAfter.getArticleViewCount());
    }

//    @Test
//    void share() {
//        String share = articleService.share();
//    }

//    @Test
//    void findDrafts() {
//        List<ArticleDTO> drafts = articleService.findDrafts(testArticle.getArticleAuthorId());
//    }

//    @Test
//    void findArticlesByIdPortfolio() {
//        List<ArticleDTO> articlesByIdPortfolio = articleService.findArticlesByIdPortfolio();
//    }

//    @Test
//    void selectUnbindArticles() {
//        List<ArticleDTO> articleDTOS = articleService.selectUnbindArticles();
//    }

//    @Test
//    void findAnnouncements() {
//        List<ArticleDTO> announcements = articleService.findAnnouncements();
//    }

//    @Test
//    void updateTags() {
//        Boolean aBoolean = articleService.updateTags();
//    }

    /**
     * 测试更新文章为优选\非优选
     */
    @Test
    void updatePerfect() {
        articleService.updatePerfect(testArticle.getIdArticle(), "1");
        ArticleDTO articleDTOByIdAfter1 = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        assertEquals("1", articleDTOByIdAfter1.getArticlePerfect());

        articleService.updatePerfect(testArticle.getIdArticle(), "0");
        ArticleDTO articleDTOByIdAfter2 = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        assertEquals("0", articleDTOByIdAfter2.getArticlePerfect());
    }

    /**
     * 测试数据删除是否成功
     * <p>
     * 运行该测试时，可能会产生以下错误：
     * cn.hutool.core.io.IORuntimeException: Path [xxxxxxx] is not directory!
     * 这是由于Lucene的路径通配符默认为linux的，解决方式：
     * 将ArticleIndexUtil.deleteIndex()方法中的PATH改为WINDOW_PATH即可 :)
     */
    @Test
    void delete() {
        articleService.delete(testArticle.getIdArticle());
        ArticleDTO articleDTOByIdAfter = articleService.findArticleDTOById(testArticle.getIdArticle(), 0);
        assertNull(articleDTOByIdAfter);
    }
}
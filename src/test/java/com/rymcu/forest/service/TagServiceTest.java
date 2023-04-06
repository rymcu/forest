package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 标签test
 */
class TagServiceTest extends BaseServiceTest {

    /**
     * 测试用的Article数据，用于该单元测试的一系列操作
     */
    private final ArticleDTO testArticle;
    /**
     * 与Article相关联的测试User数据(建表时提前放入)
     */
    private final User testUser = new User();
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleService articleService;

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

    @BeforeEach
    void setUp() throws UnsupportedEncodingException {
        Long articleId = articleService.postArticle(testArticle, testUser);
        assertNotNull(articleId);
        testArticle.setIdArticle(articleId);
    }

    @Test
    @DisplayName("保存文章标签")
    void saveTagArticle() throws UnsupportedEncodingException {
        Article article = new Article();
        BeanUtils.copyProperties(testArticle, article);
        Integer integer = tagService.saveTagArticle(article, "article", testUser.getIdUser());
        assertEquals(1, integer);
    }

    @Test
    @DisplayName("清除未使用标签")
    void cleanUnusedTag() throws Exception {
        boolean b = tagService.cleanUnusedTag();
        assertFalse(b);

        Tag tag = new Tag();
        tag.setTagDescription("test");
        tag.setTagTitle("test");
        Tag tag1 = tagService.saveTag(tag);
        assertNotNull(tag1.getIdTag());

        b = tagService.cleanUnusedTag();
        assertTrue(b);
    }

    @Test
    @DisplayName("添加/更新标签")
    void saveTag() throws Exception {
        List<LabelModel> tagLabels = tagService.findTagLabels();
        assertTrue(tagLabels.isEmpty());

        Tag tag = new Tag();
        tag.setTagDescription("test");

        assertThrows(IllegalArgumentException.class, () -> tagService.saveTag(tag));

        tag.setTagTitle("test");
        Tag tag1 = tagService.saveTag(tag);
        assertNotNull(tag1.getIdTag());

        tagLabels = tagService.findTagLabels();
        assertTrue(tagLabels.isEmpty());

        tag.setIdTag(null);
        assertThrows(BusinessException.class, () -> tagService.saveTag(tag));
    }

    @Test
    @DisplayName("获取标签列表")
    void findTagLabels() {
        List<LabelModel> tagLabels = tagService.findTagLabels();
        assertTrue(tagLabels.isEmpty());
    }
}
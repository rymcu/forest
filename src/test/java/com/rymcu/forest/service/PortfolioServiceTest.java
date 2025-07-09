package com.rymcu.forest.service;

import com.github.pagehelper.PageInfo;
import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.exception.UltraViresException;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Portfolio;
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
 * 作品集测试
 */
class PortfolioServiceTest extends BaseServiceTest {

    /**
     * 测试用的Article数据，用于该单元测试的一系列操作
     */
    private final ArticleDTO testArticle;
    private final UserDTO userDTO = new UserDTO();
    private final Portfolio portfolio = new Portfolio();
    /**
     * 与Article相关联的测试User数据(建表时提前放入)
     */
    private final User testUser = new User();
    PortfolioArticleDTO portfolioArticleDTO = new PortfolioArticleDTO();
    ;
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private ArticleService articleService;

    {
        userDTO.setIdUser(2L);

        portfolio.setPortfolioDescriptionHtml("test");
        portfolio.setPortfolioDescription("test");
        portfolio.setPortfolioAuthorId(userDTO.getIdUser());
        portfolio.setPortfolioTitle("test");

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
    @DisplayName("查询用户作品集")
    void findUserPortfoliosByUser() {
        List<PortfolioDTO> userPortfoliosByUser = portfolioService.findUserPortfoliosByUser(userDTO);
        assertTrue(userPortfoliosByUser.isEmpty());
    }

    @Test
    @DisplayName("查询作品集")
    void findPortfolioDTOById() {
        PortfolioDTO portfolioDTO = portfolioService.findPortfolioDTOById(1L, null);
        assertNull(portfolioDTO.getIdPortfolio());
    }

    @Test
    @DisplayName("保持/更新作品集")
    void postPortfolio() {
        List<PortfolioDTO> userPortfoliosByUser = portfolioService.findUserPortfoliosByUser(userDTO);
        assertTrue(userPortfoliosByUser.isEmpty());
        portfolioService.postPortfolio(portfolio);

        Portfolio portfolio1 = portfolioService.postPortfolio(portfolio);
        assertNotNull(portfolio1.getIdPortfolio());
        assertEquals(portfolio, portfolio1);

        userPortfoliosByUser = portfolioService.findUserPortfoliosByUser(userDTO);
        assertEquals(1, userPortfoliosByUser.size());

        PortfolioDTO portfolioDTO = portfolioService.findPortfolioDTOById(portfolio1.getIdPortfolio(), null);
        assertEquals(portfolio1.getPortfolioDescription(), portfolioDTO.getPortfolioDescription());


    }

    @Test
    @DisplayName("查询作品集下未绑定文章")
    void findUnbindArticles() {
        assertThrows(
                BusinessException.class, () -> portfolioService.findUnbindArticles(1, 10, "test", portfolio.getIdPortfolio(), userDTO.getIdUser())
        );

        Portfolio portfolio1 = portfolioService.postPortfolio(portfolio);

        assertThrows(
                UltraViresException.class, () -> portfolioService.findUnbindArticles(1, 10, "test", portfolio.getIdPortfolio(), 1L)
        );

        PageInfo<ArticleDTO> articles = portfolioService.findUnbindArticles(1, 10, "test", portfolio.getIdPortfolio(), userDTO.getIdUser());
        assertEquals(1L, articles.getTotal());
    }

    @Test
    @DisplayName("绑定文章")
    void bindArticle() {
        portfolioService.postPortfolio(portfolio);

        portfolioArticleDTO.setIdArticle(testArticle.getIdArticle());
        portfolioArticleDTO.setIdPortfolio(portfolio.getIdPortfolio());
        boolean b = portfolioService.bindArticle(portfolioArticleDTO);

        assertTrue(b);

        assertThrows(BusinessException.class, () -> portfolioService.bindArticle(portfolioArticleDTO));

    }

    @Test
    @DisplayName("更新文章排序号")
    void updateArticleSortNo() {
        portfolioService.postPortfolio(portfolio);

        assertThrows(ServiceException.class, () -> portfolioService.updateArticleSortNo(portfolioArticleDTO));
        portfolioArticleDTO.setIdArticle(testArticle.getIdArticle());
        portfolioArticleDTO.setIdPortfolio(portfolio.getIdPortfolio());
        portfolioArticleDTO.setSortNo(10);

        assertThrows(ServiceException.class, () -> portfolioService.updateArticleSortNo(portfolioArticleDTO));

        portfolioService.bindArticle(portfolioArticleDTO);

        boolean b = portfolioService.updateArticleSortNo(portfolioArticleDTO);
        assertTrue(b);

        portfolioService.updateArticleSortNo(portfolioArticleDTO);
        assertTrue(b);
    }

    @Test
    @DisplayName("取消绑定文章")
    void unbindArticle() {
        assertThrows(ServiceException.class, () -> portfolioService.unbindArticle(null, null));

        portfolioService.postPortfolio(portfolio);

        portfolioArticleDTO.setIdArticle(testArticle.getIdArticle());
        portfolioArticleDTO.setIdPortfolio(portfolio.getIdPortfolio());
        portfolioArticleDTO.setSortNo(10);
        portfolioService.bindArticle(portfolioArticleDTO);

        boolean b = portfolioService.unbindArticle(portfolio.getIdPortfolio(), portfolioArticleDTO.getIdArticle());
        assertTrue(b);
    }


    @Test
    @DisplayName("删除作品集")
    void deletePortfolio() {
        portfolioService.postPortfolio(portfolio);
        assertThrows(IllegalArgumentException.class, () -> portfolioService.deletePortfolio(null, null, null));

        assertThrows(NullPointerException.class, () -> portfolioService.deletePortfolio(portfolio.getIdPortfolio(), null, 3));


        assertThrows(UltraViresException.class, () -> portfolioService.deletePortfolio(portfolio.getIdPortfolio(), 1L, 3));

        boolean b = portfolioService.deletePortfolio(portfolio.getIdPortfolio(), userDTO.getIdUser(), 3);
        assertTrue(b);

        portfolio.setIdPortfolio(null);
        portfolioService.postPortfolio(portfolio);
        b = portfolioService.deletePortfolio(portfolio.getIdPortfolio(), userDTO.getIdUser(), 1);
        assertTrue(b);

        assertThrows(BusinessException.class, () -> portfolioService.deletePortfolio(portfolio.getIdPortfolio(), userDTO.getIdUser(), 1));

        portfolio.setIdPortfolio(null);
        portfolioService.postPortfolio(portfolio);

        portfolioArticleDTO.setIdArticle(testArticle.getIdArticle());
        portfolioArticleDTO.setIdPortfolio(portfolio.getIdPortfolio());
        portfolioArticleDTO.setSortNo(10);
        portfolioService.bindArticle(portfolioArticleDTO);

        assertThrows(BusinessException.class, () -> portfolioService.deletePortfolio(portfolio.getIdPortfolio(), userDTO.getIdUser(), 1));

        portfolioService.unbindArticle(portfolio.getIdPortfolio(), portfolioArticleDTO.getIdArticle());

        portfolioService.deletePortfolio(portfolio.getIdPortfolio(), userDTO.getIdUser(), 1);
        assertTrue(b);

    }

    @Test
    @DisplayName("获取作品集列表数据")
    void findPortfolios() {
        List<PortfolioDTO> portfolios = portfolioService.findPortfolios();
        assertTrue(portfolios.isEmpty());

        portfolioService.postPortfolio(portfolio);
        portfolios = portfolioService.findPortfolios();
        assertFalse(portfolios.isEmpty());
    }
}
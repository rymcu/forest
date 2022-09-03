package com.rymcu.forest.web.api.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.AccountExistsException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.result.GlobalResultMessage;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.*;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

    @Resource
    private JavaMailService javaMailService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private PortfolioService portfolioService;
    @Resource
    private ProductService productService;

    @GetMapping("/get-email-code")
    public GlobalResult<Map<String, String>> getEmailCode(@RequestParam("email") String email) throws MessagingException {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            throw new AccountExistsException("该邮箱已被注册!");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if (result == 0) {
                map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/get-forget-password-email")
    public GlobalResult getForgetPasswordEmail(@RequestParam("email") String email) throws MessagingException, ServiceException {
        User user = userService.findByAccount(email);
        if (user != null) {
            Integer result = javaMailService.sendForgetPasswordEmail(email);
            if (result == 0) {
                throw new ServiceException(GlobalResultMessage.SEND_FAIL.getMessage());
            }
        } else {
            throw new UnknownAccountException("该邮箱未注册！");
        }
        return GlobalResultGenerator.genSuccessResult(GlobalResultMessage.SEND_SUCCESS.getMessage());
    }

    @PostMapping("/register")
    public GlobalResult<Boolean> register(@RequestBody UserRegisterInfoDTO registerInfo) {
        boolean flag = userService.register(registerInfo.getEmail(), registerInfo.getPassword(), registerInfo.getCode());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/login")
    public GlobalResult<TokenUser> login(@RequestBody User user) throws ServiceException {
        TokenUser tokenUser = userService.login(user.getAccount(), user.getPassword());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @GetMapping("/heartbeat")
    public GlobalResult heartbeat() {
        return GlobalResultGenerator.genSuccessResult("heartbeat");
    }

    @GetMapping("/articles")
    @VisitLogger
    public GlobalResult<PageInfo<ArticleDTO>> articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, ArticleSearchDTO searchDTO) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticles(searchDTO);
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/announcements")
    public GlobalResult<PageInfo<ArticleDTO>> announcements(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "5") Integer rows) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findAnnouncements();
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/article/{id}")
    @VisitLogger
    public GlobalResult<ArticleDTO> article(@PathVariable Long id) {
        ArticleDTO articleDTO = articleService.findArticleDTOById(id, 1);
        return GlobalResultGenerator.genSuccessResult(articleDTO);
    }

    @PatchMapping("/forget-password")
    public GlobalResult<Boolean> forgetPassword(@RequestBody ForgetPasswordDTO forgetPassword) throws ServiceException {
        boolean flag = userService.forgetPassword(forgetPassword.getCode(), forgetPassword.getPassword());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @GetMapping("/portfolio/{id}")
    @VisitLogger
    public GlobalResult<PortfolioDTO> portfolio(@PathVariable Long id) {
        PortfolioDTO portfolioDTO = portfolioService.findPortfolioDTOById(id, 1);
        return GlobalResultGenerator.genSuccessResult(portfolioDTO);
    }

    @GetMapping("/portfolio/{id}/articles")
    public GlobalResult<PageInfo<ArticleDTO>> articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @PathVariable Long id) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticlesByIdPortfolio(id);
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/portfolios")
    public GlobalResult<PageInfo<PortfolioDTO>> portfolios(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows) {
        PageHelper.startPage(page, rows);
        List<PortfolioDTO> list = portfolioService.findPortfolios();
        PageInfo<PortfolioDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/products")
    public GlobalResult<PageInfo<ProductDTO>> products(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows) {
        PageHelper.startPage(page, rows);
        List<ProductDTO> list = productService.findProducts();
        PageInfo<ProductDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/product/{id}")
    @VisitLogger
    public GlobalResult<ProductDTO> product(@PathVariable Integer id) {
        ProductDTO productDTO = productService.findProductDTOById(id, 1);
        return GlobalResultGenerator.genSuccessResult(productDTO);
    }
}

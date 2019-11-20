package com.rymcu.vertical.web.api.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.exception.ServiceException;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.core.result.GlobalResultMessage;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.JavaMailService;
import com.rymcu.vertical.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

    @Resource
    private JavaMailService javaMailService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;

    @ApiOperation(value = "获取邮件验证码")
    @PostMapping("/get-email-code")
    public GlobalResult getEmailCode(@RequestParam("email") String email) throws ServiceException {
        Map map = new HashMap();
        map.put("message",GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            map.put("message","该邮箱已被注册！");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if(result == 0){
                map.put("message",GlobalResultMessage.SEND_FAIL.getMessage());
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/register")
    public GlobalResult register(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("code") String code){
        Map map = userService.register(email,password,code);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/login")
    public GlobalResult login(@RequestParam("account") String account, @RequestParam("password") String password){
        Map map = userService.login(account,password);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/heartbeat")
    public GlobalResult heartbeat(){
        return GlobalResultGenerator.genSuccessResult("heartbeat");
    }

    @GetMapping("/articles")
    public GlobalResult articles(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "10") Integer rows,@RequestParam(defaultValue = "") String searchText,@RequestParam(defaultValue = "") String tag){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.articles(searchText,tag);
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap();
        map.put("articles", pageInfo.getList());
        Map pagination = new HashMap();
        pagination.put("paginationPageCount",pageInfo.getPages());
        pagination.put("paginationPageNums",pageInfo.getNavigatepageNums());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }



    @GetMapping("/article/{id}")
    public GlobalResult detail(@PathVariable Integer id){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id,1);
        Map map = new HashMap<>();
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/update/{id}")
    public GlobalResult update(@PathVariable Integer id){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id,2);
        Map map = new HashMap<>();
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

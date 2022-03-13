package com.rymcu.forest.web.api.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.dto.admin.Dashboard;
import com.rymcu.forest.service.DashboardService;
import com.rymcu.forest.util.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping
    public GlobalResult dashboard() {
        Dashboard dashboard = dashboardService.dashboard();
        return GlobalResultGenerator.genSuccessResult(dashboard);
    }

    @GetMapping("/last-thirty-days")
    public GlobalResult LastThirtyDaysData() {
        Map map = dashboardService.lastThirtyDaysData();
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/history")
    public GlobalResult history() {
        Map map = dashboardService.history();
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/new-users")
    public GlobalResult newUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<UserInfoDTO> list = dashboardService.newUsers();
        PageInfo<UserInfoDTO> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("users", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/new-bank-accounts")
    public GlobalResult newBankAccounts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<BankAccountDTO> list = dashboardService.newBankAccounts();
        PageInfo<BankAccountDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("bankAccounts", pageInfo.getList());
        Map pagination = new HashMap(4);
        pagination.put("pageSize", pageInfo.getPageSize());
        pagination.put("total", pageInfo.getTotal());
        pagination.put("currentPage", pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/new-articles")
    public GlobalResult newArticles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = dashboardService.newArticles();
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        Map<String, Object> map = new HashMap<>(2);
        map.put("articles", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

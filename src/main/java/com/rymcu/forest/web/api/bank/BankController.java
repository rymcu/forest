package com.rymcu.forest.web.api.bank;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;
import com.rymcu.forest.service.BankService;
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
@RequestMapping("/api/v1/admin/bank")
public class BankController {

    @Resource
    private BankService bankService;

    @GetMapping("/list")
    public GlobalResult banks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<BankDTO> list = bankService.findBanks();
        PageInfo<BankDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("banks", pageInfo.getList());
        Map pagination = new HashMap(4);
        pagination.put("pageSize", pageInfo.getPageSize());
        pagination.put("total", pageInfo.getTotal());
        pagination.put("currentPage", pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}

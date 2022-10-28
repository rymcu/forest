package com.rymcu.forest.web.api.bank;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.service.BankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin/bank")
public class BankController {

    @Resource
    private BankService bankService;

    @GetMapping("/list")
    public GlobalResult<PageInfo<BankDTO>> banks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<BankDTO> list = bankService.findBanks();
        PageInfo<BankDTO> pageInfo = new PageInfo(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

}

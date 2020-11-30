package com.rymcu.forest.web.api.bank;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.entity.Bank;
import com.rymcu.forest.entity.BankAccount;
import com.rymcu.forest.service.BankAccountService;
import com.rymcu.forest.service.BankService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin/bank-account")
public class BankAccountController {

    @Resource
    private BankAccountService bankAccountService;

    @GetMapping("/list")
    public GlobalResult banks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, BankAccountSearchDTO bankAccountSearchDTO) {
        PageHelper.startPage(page, rows);
        List<BankAccountDTO> list = bankAccountService.findBankAccounts(bankAccountSearchDTO);
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

    @GetMapping("/{idUser}")
    public GlobalResult detail(@PathVariable Integer idUser) {
        BankAccountDTO bankAccount = bankAccountService.findBankAccountByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(bankAccount);
    }

}

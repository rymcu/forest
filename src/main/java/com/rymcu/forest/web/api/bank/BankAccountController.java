package com.rymcu.forest.web.api.bank;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.service.BankAccountService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/admin/bank-account")
@RequiresRoles(value = {"blog_admin", "admin"}, logical = Logical.OR)
public class BankAccountController {

    @Resource
    private BankAccountService bankAccountService;

    @GetMapping("/list")
    public GlobalResult<PageInfo<BankAccountDTO>> banks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, BankAccountSearchDTO bankAccountSearchDTO) {
        PageHelper.startPage(page, rows);
        List<BankAccountDTO> list = bankAccountService.findBankAccounts(bankAccountSearchDTO);
        PageInfo<BankAccountDTO> pageInfo = new PageInfo(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/{idUser}")
    public GlobalResult<BankAccountDTO> detail(@PathVariable Long idUser) {
        BankAccountDTO bankAccount = bankAccountService.findBankAccountByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(bankAccount);
    }

    @GetMapping("/transaction-records")
    public GlobalResult<PageInfo<TransactionRecordDTO>> transactionRecords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer rows, HttpServletRequest request) {
        String bankAccount = request.getParameter("bankAccount");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        PageHelper.startPage(page, rows);
        List<TransactionRecordDTO> list = bankAccountService.findUserTransactionRecords(bankAccount, startDate, endDate);
        PageInfo<TransactionRecordDTO> pageInfo = new PageInfo(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

}

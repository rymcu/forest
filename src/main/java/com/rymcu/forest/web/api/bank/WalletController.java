package com.rymcu.forest.web.api.bank;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.security.annotation.SecurityInterceptor;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.service.BankAccountService;
import com.rymcu.forest.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2021/12/10 19:25.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    @Resource
    private BankAccountService bankAccountService;


    @GetMapping("/{idUser}")
    @SecurityInterceptor
    public GlobalResult detail(@PathVariable Integer idUser) {
        BankAccountDTO bankAccount = bankAccountService.findBankAccountByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(bankAccount);
    }


    @GetMapping("/transaction-records")
    @SecurityInterceptor
    public GlobalResult transactionRecords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer rows, HttpServletRequest request) {
        String idUser = request.getParameter("idUser");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        BankAccountDTO bankAccount = bankAccountService.findBankAccountByIdUser(Integer.valueOf(idUser));
        PageHelper.startPage(page, rows);
        List<TransactionRecordDTO> list = bankAccountService.findUserTransactionRecords(bankAccount.getBankAccount(), startDate, endDate);
        PageInfo<TransactionRecordDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("records", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}

package com.rymcu.forest.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.BankAccountSearchDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.BankAccount;
import com.rymcu.forest.mapper.BankAccountMapper;
import com.rymcu.forest.service.BankAccountService;
import com.rymcu.forest.service.TransactionRecordService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class BankAccountServiceImpl extends AbstractService<BankAccount> implements BankAccountService {

    @Resource
    BankAccountMapper bankAccountMapper;
    @Resource
    TransactionRecordService transactionRecordService;

    @Override
    public List<BankAccountDTO> findBankAccounts(BankAccountSearchDTO bankAccountSearchDTO) {
        return bankAccountMapper.selectBankAccounts(bankAccountSearchDTO.getBankName(), bankAccountSearchDTO.getAccountOwnerName(), bankAccountSearchDTO.getBankAccount());
    }

    @Override
    public BankAccountDTO findBankAccountByIdUser(Long idUser) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountOwner(idUser);
        String defaultAccountType = "0";
        bankAccount.setAccountType(defaultAccountType);
        List<BankAccount> bankAccounts = bankAccountMapper.select(bankAccount);
        if (Objects.nonNull(bankAccounts) && bankAccounts.size() > 0) {
            return bankAccountMapper.selectBankAccount(bankAccounts.get(0).getIdBankAccount());
        }
        return null;
    }

    @Override
    public List<TransactionRecordDTO> findUserTransactionRecords(String bankAccount, String startDate, String endDate) {
        if (StringUtils.isBlank(startDate)) {
            LocalDateTime now = LocalDateTime.now();
            endDate = LocalDateTimeUtil.format(now, "yyyy-MM-dd");
            startDate = LocalDateTimeUtil.format(now.minus(30, ChronoUnit.DAYS), "yyyy-MM-dd");
        }
        // 查询交易记录
        return transactionRecordService.findTransactionRecords(bankAccount, startDate, endDate);
    }

    @Override
    public BankAccount createBankAccount(Long idUser) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setAccountBalance(new BigDecimal("0"));
        // 默认为社区发展与改革银行
        bankAccount.setIdBank(2L);
        bankAccount.setAccountOwner(idUser);
        bankAccount.setBankAccount(nextBankAccount());
        bankAccount.setCreatedTime(new Date());
        bankAccountMapper.insertSelective(bankAccount);
        return bankAccount;
    }

    @Override
    public BankAccountDTO findByBankAccount(String bankAccount) {
        return bankAccountMapper.selectByBankAccount(bankAccount);
    }

    @Override
    public BankAccount findSystemBankAccount() {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setIdBank(1L);
        bankAccount.setAccountType("1");
        bankAccount.setAccountOwner(2L);
        return bankAccountMapper.selectOne(bankAccount);
    }

    @Override
    public BankAccount findInfoByBankAccount(String bankAccount) {
        BankAccount searchBankAccount = new BankAccount();
        searchBankAccount.setBankAccount(bankAccount);
        return bankAccountMapper.selectOne(searchBankAccount);
    }

    private String nextBankAccount() {
        String bankAccount = "600000001";
        String maxBankAccount = bankAccountMapper.selectMaxBankAccount();
        if (StringUtils.isNotBlank(maxBankAccount)) {
            BigDecimal bigDecimal = new BigDecimal(maxBankAccount).add(new BigDecimal("1"));
            return bigDecimal.toString();
        }
        return bankAccount;
    }
}

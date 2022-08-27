package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.TransactionException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.BankAccountDTO;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.BankAccount;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.enumerate.TransactionCode;
import com.rymcu.forest.enumerate.TransactionEnum;
import com.rymcu.forest.mapper.TransactionRecordMapper;
import com.rymcu.forest.service.BankAccountService;
import com.rymcu.forest.service.TransactionRecordService;
import com.rymcu.forest.util.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class TransactionRecordServiceImpl extends AbstractService<TransactionRecord> implements TransactionRecordService {

    @Resource
    private TransactionRecordMapper transactionRecordMapper;
    @Resource
    private BankAccountService bankAccountService;
    @Resource
    private RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransactionRecord transfer(TransactionRecord transactionRecord) {
        // 判断发起者账户状态
        boolean formAccountStatus = checkFormAccountStatus(transactionRecord.getFormBankAccount(), transactionRecord.getMoney());
        if (formAccountStatus) {
            Integer result = transactionRecordMapper.transfer(transactionRecord.getFormBankAccount(), transactionRecord.getToBankAccount(), transactionRecord.getMoney());
            if (result > 0) {
                transactionRecord.setTransactionNo(nextTransactionNo());
                transactionRecord.setTransactionTime(new Date());
                transactionRecordMapper.insertSelective(transactionRecord);
            }
        } else {
            throw new TransactionException(TransactionCode.InsufficientBalance);
        }
        return transactionRecord;
    }

    @Override
    public List<TransactionRecordDTO> findTransactionRecords(String bankAccount, String startDate, String endDate) {
        List<TransactionRecordDTO> list = transactionRecordMapper.selectTransactionRecords(bankAccount, startDate, endDate);
        list.forEach(this::genTransactionRecord);
        return list;
    }

    private TransactionRecordDTO genTransactionRecord(TransactionRecordDTO transactionRecordDTO) {
        BankAccountDTO toBankAccount = bankAccountService.findByBankAccount(transactionRecordDTO.getToBankAccount());
        BankAccountDTO formBankAccount = bankAccountService.findByBankAccount(transactionRecordDTO.getFormBankAccount());
        transactionRecordDTO.setFormBankAccountInfo(formBankAccount);
        transactionRecordDTO.setToBankAccountInfo(toBankAccount);
        return transactionRecordDTO;
    }

    @Override
    public TransactionRecord userTransfer(Long toUserId, Long formUserId, TransactionEnum transactionType) {
        BankAccountDTO toBankAccount = bankAccountService.findBankAccountByIdUser(toUserId);
        BankAccountDTO formBankAccount = bankAccountService.findBankAccountByIdUser(formUserId);
        TransactionRecord transactionRecord = new TransactionRecord();
        transactionRecord.setToBankAccount(toBankAccount.getBankAccount());
        transactionRecord.setFormBankAccount(formBankAccount.getBankAccount());
        transactionRecord.setMoney(new BigDecimal(transactionType.getMoney()));
        transactionRecord.setFunds(transactionType.getDescription());
        return transfer(transactionRecord);
    }

    @Override
    public TransactionRecord bankTransfer(Long idUser, TransactionEnum transactionType) {
        BankAccountDTO toBankAccount = bankAccountService.findBankAccountByIdUser(idUser);
        Boolean isTrue;
        // 校验货币规则
        switch (transactionType) {
            case Answer:
            case CorrectAnswer:
                isTrue = transactionRecordMapper.existsWithBankAccountAndFunds(toBankAccount.getBankAccount(), transactionType.getDescription());
                break;
            default:
                isTrue = true;
        }
        if (isTrue) {
            BankAccount formBankAccount = bankAccountService.findSystemBankAccount();
            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setToBankAccount(toBankAccount.getBankAccount());
            transactionRecord.setFormBankAccount(formBankAccount.getBankAccount());
            transactionRecord.setMoney(new BigDecimal(transactionType.getMoney()));
            transactionRecord.setFunds(transactionType.getDescription());
            return transfer(transactionRecord);
        }
        return null;
    }

    @Override
    public TransactionRecord newbieRewards(TransactionRecord transactionRecord) {
        // 判断是否重复发放
        Boolean result = transactionRecordMapper.existsWithNewbieRewards(transactionRecord.getToBankAccount());
        if (result) {
            return transactionRecord;
        }
        BankAccount formBankAccount = bankAccountService.findSystemBankAccount();
        transactionRecord.setFormBankAccount(formBankAccount.getBankAccount());
        transactionRecord.setMoney(new BigDecimal(TransactionEnum.NewbieRewards.getMoney()));
        transactionRecord.setFunds(TransactionEnum.NewbieRewards.getDescription());
        return transfer(transactionRecord);
    }

    private String nextTransactionNo() {
        String orderNo = "E";
        String key = "orderId";
        int timeout = 60;
        //根据时间获取前缀
        String prefix = getPrefix(new Date());
        //使用redis获取自增ID
        long id = redisService.incrBy(key, timeout);
        return orderNo + prefix + DateUtil.getNowDateNum() + String.format("%1$05d", id);
    }

    private static String getPrefix(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        //天数转为3位字符串,不满3位用0补齐
        String dayFmt = String.format("%1$03d", day);
        //小时转为2位字符串,不满2位用0补齐
        String hourFmt = String.format("%1$02d", hour);
        //2位年份+3位天数+2位小时
        return (year - 2000) + dayFmt + hourFmt;
    }

    private boolean checkFormAccountStatus(String formBankAccount, BigDecimal money) {
        BankAccount bankAccount = bankAccountService.findInfoByBankAccount(formBankAccount);
        if (Objects.nonNull(bankAccount)) {
            if (bankAccount.getAccountBalance().compareTo(money) > 0) {
                return true;
            }
        }
        return false;
    }
}

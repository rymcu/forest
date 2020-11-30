package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.TransactionRecordDTO;
import com.rymcu.forest.entity.BankAccount;
import com.rymcu.forest.entity.TransactionRecord;
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
        }
        return transactionRecord;
    }

    @Override
    public List<TransactionRecordDTO> findTransactionRecords(String bankAccount) {
        return transactionRecordMapper.selectTransactionRecords(bankAccount);
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
        BankAccount bankAccount = bankAccountService.findByBankAccount(formBankAccount);
        if (Objects.nonNull(bankAccount)) {
            if (bankAccount.getAccountBalance().compareTo(money) > 0) {
                return true;
            }
        }
        return false;
    }
}

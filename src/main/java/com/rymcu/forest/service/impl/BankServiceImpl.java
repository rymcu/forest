package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;
import com.rymcu.forest.mapper.BankMapper;
import com.rymcu.forest.service.BankService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 银行
 *
 * @author ronger
 */
@Service
public class BankServiceImpl extends AbstractService<Bank> implements BankService {

    @Resource
    private BankMapper bankMapper;

    @Override
    public List<BankDTO> findBanks() {
        List<BankDTO> banks = bankMapper.selectBanks();
        return banks;
    }
}

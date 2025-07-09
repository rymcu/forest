package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;

import java.util.List;

/**
 * 银行
 *
 * @author ronger
 */
public interface BankService extends Service<Bank> {
    List<BankDTO> findBanks();
}

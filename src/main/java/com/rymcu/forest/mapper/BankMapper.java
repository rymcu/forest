package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.BankDTO;
import com.rymcu.forest.entity.Bank;

import java.util.List;

/**
 * @author ronger
 */
public interface BankMapper extends Mapper<Bank> {
    /**
     * 查询银行列表数据
     *
     * @return
     */
    List<BankDTO> selectBanks();
}

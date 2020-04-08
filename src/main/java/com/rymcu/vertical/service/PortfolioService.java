package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.PortfolioDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.Portfolio;
import com.rymcu.vertical.web.api.exception.BaseApiException;

import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
public interface PortfolioService extends Service<Portfolio> {

    /**
     * 查询用户作品集
     * @param userDTO
     * @return
     */
    List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO);

    /** 查询作品集
     * @param id
     * @return
     */
    PortfolioDTO findPortfolioDTOById(Integer id);

    /**
     * 保持/更新作品集
     * @param portfolio
     * @return
     */
    Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException;
}

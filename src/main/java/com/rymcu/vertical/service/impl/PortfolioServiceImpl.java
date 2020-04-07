package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.Portfolio;
import com.rymcu.vertical.mapper.PortfolioMapper;
import com.rymcu.vertical.service.PortfolioService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@Service
public class PortfolioServiceImpl extends AbstractService<Portfolio> implements PortfolioService {

    @Resource
    private PortfolioMapper portfolioMapper;

}

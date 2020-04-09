package com.rymcu.vertical.web.api.portfolio;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.PortfolioDTO;
import com.rymcu.vertical.entity.Portfolio;
import com.rymcu.vertical.service.PortfolioService;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Resource
    private PortfolioService portfolioService;

    @GetMapping("/detail/{id}")
    public GlobalResult detail(@PathVariable Integer id) {
        PortfolioDTO portfolio = portfolioService.findPortfolioDTOById(id);
        Map map = new HashMap<>(1);
        map.put("portfolio", portfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult add(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @PutMapping("/post")
    public GlobalResult update(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

}
package com.rymcu.forest.web.api.rule;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.CurrencyRule;
import com.rymcu.forest.service.CurrencyRuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/rule/currency")
public class CurrencyRuleController {

    @Resource
    private CurrencyRuleService currencyRuleService;

    @GetMapping("/list")
    public GlobalResult list() {
        List<CurrencyRule> list = currencyRuleService.findAll();
        return GlobalResultGenerator.genSuccessResult(list);
    }

}

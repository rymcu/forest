package com.rymcu.forest.web.api.bank;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.service.TransactionRecordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/transaction")
public class TransactionRecordController {

    @Resource
    private TransactionRecordService transactionRecordService;

    @PostMapping("/transfer")
    public GlobalResult transfer(@RequestBody TransactionRecord transactionRecord) throws Exception {
        transactionRecord = transactionRecordService.transfer(transactionRecord);
        return GlobalResultGenerator.genSuccessResult(transactionRecord);
    }

}

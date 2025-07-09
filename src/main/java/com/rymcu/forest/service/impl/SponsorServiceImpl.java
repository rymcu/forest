package com.rymcu.forest.service.impl;


import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.exception.TransactionException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.entity.Sponsor;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.enumerate.TransactionCode;
import com.rymcu.forest.enumerate.TransactionEnum;
import com.rymcu.forest.mapper.SponsorMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.service.TransactionRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class SponsorServiceImpl extends AbstractService<Sponsor> implements SponsorService {

    @Resource
    private SponsorMapper sponsorMapper;
    @Resource
    private ArticleService articleService;
    @Resource
    private TransactionRecordService transactionRecordService;

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean sponsorship(Sponsor sponsor) throws ServiceException {
        TransactionEnum transactionEnum = TransactionEnum.findTransactionEnum(sponsor.getDataType());
        BigDecimal money = BigDecimal.valueOf(transactionEnum.getMoney());
        sponsor.setSponsorshipMoney(money);
        sponsor.setSponsorshipTime(new Date());
        sponsorMapper.insertSelective(sponsor);
        // 赞赏金额划转
        if (transactionEnum.isArticleSponsor()) {
            ArticleDTO articleDTO = articleService.findArticleDTOById(sponsor.getDataId(), 1);
            TransactionRecord transactionRecord = transactionRecordService.userTransfer(articleDTO.getArticleAuthorId(), sponsor.getSponsor(), transactionEnum);
            if (Objects.isNull(transactionRecord.getIdTransactionRecord())) {
                throw new TransactionException(TransactionCode.INSUFFICIENT_BALANCE);
            }
            // 更新文章赞赏数
            int result = sponsorMapper.updateArticleSponsorCount(articleDTO.getIdArticle());
            if (result == 0) {
                throw new ServiceException("操作失败!");
            }
        }
        return true;
    }
}

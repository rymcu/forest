package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.exception.TransactionException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.entity.Sponsor;
import com.rymcu.forest.entity.TransactionRecord;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.enumerate.TransactionCode;
import com.rymcu.forest.enumerate.TransactionEnum;
import com.rymcu.forest.mapper.SponsorMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.service.TransactionRecordService;
import com.rymcu.forest.util.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
    @Transactional(rollbackFor = Exception.class)
    public Map sponsorship(Sponsor sponsor) throws Exception {
        Map map = new HashMap(2);
        if (Objects.isNull(sponsor) || Objects.isNull(sponsor.getDataId()) || Objects.isNull(sponsor.getDataType())) {
            map.put("success", false);
            map.put("message", "数据异常");
        } else {
            TransactionEnum result = TransactionEnum.findTransactionEnum(sponsor.getDataType());
            BigDecimal money = BigDecimal.valueOf(result.getMoney());
            sponsor.setSponsorshipMoney(money);
            User user = UserUtils.getCurrentUserByToken();
            sponsor.setSponsor(user.getIdUser());
            sponsor.setSponsorshipTime(new Date());
            sponsorMapper.insertSelective(sponsor);
            // 赞赏金额划转
            if (result.isArticleSponsor()) {
                ArticleDTO articleDTO = articleService.findArticleDTOById(sponsor.getDataId(), 1);
                TransactionRecord transactionRecord = transactionRecordService.transferByUserId(articleDTO.getArticleAuthorId(), user.getIdUser(), money);
                if (Objects.isNull(transactionRecord.getIdTransactionRecord())) {
                    throw new TransactionException(TransactionCode.InsufficientBalance);
                }
                // 更新文章赞赏数
                sponsorMapper.updateArticleSponsorCount(articleDTO.getIdArticle());
            }
            map.put("success", true);
            map.put("message", "赞赏成功");
        }
        return map;
    }
}

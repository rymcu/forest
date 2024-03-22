package com.rymcu.forest.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.enumerate.FileDataType;
import com.rymcu.forest.enumerate.FilePath;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.mapper.TagMapper;
import com.rymcu.forest.service.TagService;
import com.rymcu.forest.util.XssUtils;
import com.rymcu.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author ronger
 */
@Service
public class TagServiceImpl extends AbstractService<Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private RedisService redisService;

    @Override
    @Transactional(rollbackFor = {UnsupportedEncodingException.class})
    public Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException {
        String articleTags = article.getArticleTags();
        if (StringUtils.isNotBlank(articleTags)) {
            String[] tags = articleTags.split(",");
            List<ArticleTagDTO> articleTagDTOList = articleMapper.selectTags(article.getIdArticle());
            for (int i = 0; i < tags.length; i++) {
                boolean addTagArticle = false;
                boolean addUserTag = false;
                Tag tag = new Tag();
                tag.setTagTitle(tags[i]);
                tag = tagMapper.selectOne(tag);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTagTitle(tags[i]);
                    tag.setTagUri(URLEncoder.encode(tag.getTagTitle(), "UTF-8"));
                    tag.setCreatedTime(new Date());
                    tag.setUpdatedTime(tag.getCreatedTime());
                    tag.setTagArticleCount(1);
                    tagMapper.insertSelective(tag);
                    addTagArticle = true;
                    addUserTag = true;
                } else {
                    int n = articleTagDTOList.size();
                    for (int m = 0; m < n; m++) {
                        ArticleTagDTO articleTag = articleTagDTOList.get(m);
                        if (articleTag.getIdTag().toString().equals(tag.getIdTag().toString())) {
                            articleTagDTOList.remove(articleTag);
                            m--;
                            n--;
                        }
                    }
                    Integer count = tagMapper.selectCountTagArticleById(tag.getIdTag(), article.getIdArticle());
                    if (count == 0) {
                        tag.setTagArticleCount(tag.getTagArticleCount() + 1);
                        tagMapper.updateByPrimaryKeySelective(tag);
                        addTagArticle = true;
                    }
                    Integer countUserTag = tagMapper.selectCountUserTagById(userId, tag.getIdTag());
                    if (countUserTag == 0) {
                        addUserTag = true;
                    }
                }
                articleTagDTOList.forEach(articleTagDTO -> {
                    articleMapper.deleteUnusedArticleTag(articleTagDTO.getIdArticleTag());
                });
                if (addTagArticle) {
                    tagMapper.insertTagArticle(tag.getIdTag(), article.getIdArticle());
                }
                if (addUserTag) {
                    tagMapper.insertUserTag(tag.getIdTag(), userId, 1);
                }
            }
            return 1;
        } else {
            if (StringUtils.isNotBlank(articleContentHtml)) {
                article.setArticleTags("待分类");
                saveTagArticle(article, articleContentHtml, userId);
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanUnusedTag() {
        return tagMapper.deleteUnusedTag() > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag saveTag(Tag tag) {
        Integer result;
        tag.setTagDescription(XssUtils.filterHtmlCode(tag.getTagDescription()));
        if (tag.getIdTag() == null) {
            if (StringUtils.isBlank(tag.getTagTitle())) {
                throw new IllegalArgumentException("标签名不能为空!");
            } else {
                Condition tagCondition = new Condition(Tag.class);
                tagCondition.createCriteria().andCondition("tag_title =", tag.getTagTitle());
                List<Tag> tags = tagMapper.selectByCondition(tagCondition);
                if (!tags.isEmpty()) {
                    throw new BusinessException("标签 '" + tag.getTagTitle() + "' 已存在!");
                }
            }
            if (FileDataType.BASE64.equals(tag.getTagImageType())) {
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), FilePath.TAG);
                tag.setTagIconPath(tagIconPath);
            } else {
                tag.setTagIconPath(tag.getTagIconPath());
            }
            tag.setCreatedTime(new Date());
            tag.setUpdatedTime(tag.getCreatedTime());
            result = tagMapper.insertSelective(tag);
        } else {
            tag.setUpdatedTime(new Date());
            if (FileDataType.BASE64.equals(tag.getTagImageType())) {
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), FilePath.TAG);
                tag.setTagIconPath(tagIconPath);
            }
            result = tagMapper.update(tag.getIdTag(), tag.getTagUri(), tag.getTagIconPath(), tag.getTagStatus(), tag.getTagDescription(), tag.getTagReservation());
        }
        if (result == 0) {
            throw new ServiceException("操作失败!");
        }
        return tag;
    }

    @Override
    public List<LabelModel> findTagLabels() {
        List<LabelModel> list = JSONObject.parseArray(redisService.get("tags"), LabelModel.class);
        if (list == null) {
            list = tagMapper.selectTagLabels();
            redisService.set("tags", JSON.toJSONString(list), 600);
        }
        return list;
    }
}

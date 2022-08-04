package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.ArticleTagDTO;
import com.rymcu.forest.dto.LabelModel;
import com.rymcu.forest.dto.baidu.TagNlpDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.mapper.ArticleMapper;
import com.rymcu.forest.mapper.TagMapper;
import com.rymcu.forest.service.TagService;
import com.rymcu.forest.util.BaiDuAipUtils;
import com.rymcu.forest.util.CacheUtils;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.util.XssUtils;
import com.rymcu.forest.web.api.common.UploadController;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@Service
public class TagServiceImpl extends AbstractService<Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleMapper articleMapper;

    @Override
    @Transactional(rollbackFor = {UnsupportedEncodingException.class, BaseApiException.class})
    public Integer saveTagArticle(Article article, String articleContentHtml, Long userId) throws UnsupportedEncodingException, BaseApiException {
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
                        if (articleTag.getIdTag().equals(tag.getIdTag())) {
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
                List<TagNlpDTO> list = BaiDuAipUtils.getKeywords(article.getArticleTitle(), articleContentHtml);
                if (list.size() > 0) {
                    StringBuffer tags = new StringBuffer();
                    for (TagNlpDTO tagNlpDTO : list) {
                        tags.append(tagNlpDTO.getTag()).append(",");
                    }
                    article.setArticleTags(tags.toString());
                } else {
                    article.setArticleTags("待分类");
                }
                saveTagArticle(article, articleContentHtml, userId);
            }
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map cleanUnusedTag() {
        Map map = new HashMap(1);
        tagMapper.deleteUnusedTag();
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map saveTag(Tag tag) {
        Integer result;

        Map map = new HashMap(1);
        tag.setTagDescription(XssUtils.filterHtmlCode(tag.getTagDescription()));
        if (tag.getIdTag() == null) {
            if (StringUtils.isBlank(tag.getTagTitle())) {
                map.put("message", "标签名不能为空!");
                return map;
            } else {
                Condition tagCondition = new Condition(Tag.class);
                tagCondition.createCriteria().andCondition("tag_title =", tag.getTagTitle());
                List<Tag> tags = tagMapper.selectByCondition(tagCondition);
                if (!tags.isEmpty()) {
                    map.put("message", "标签 '" + tag.getTagTitle() + "' 已存在!");
                    return map;
                }
            }
            Tag newTag = new Tag();
            newTag.setTagTitle(tag.getTagTitle());
            newTag.setTagUri(tag.getTagUri());
            if (StringUtils.isNotBlank(tag.getTagIconPath()) && tag.getTagIconPath().contains("base64")) {
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), 2);
                newTag.setTagIconPath(tagIconPath);
            } else {
                newTag.setTagIconPath(tag.getTagIconPath());
            }
            newTag.setTagStatus(tag.getTagStatus());
            newTag.setTagDescription(tag.getTagDescription());
            newTag.setTagReservation(tag.getTagReservation());
            newTag.setCreatedTime(new Date());
            newTag.setUpdatedTime(tag.getCreatedTime());
            result = tagMapper.insertSelective(newTag);
        } else {
            tag.setUpdatedTime(new Date());
            if (StringUtils.isNotBlank(tag.getTagIconPath()) && tag.getTagIconPath().contains("base64")) {
                String tagIconPath = UploadController.uploadBase64File(tag.getTagIconPath(), 2);
                tag.setTagIconPath(tagIconPath);
            }
            result = tagMapper.update(tag.getIdTag(), tag.getTagUri(), tag.getTagIconPath(), tag.getTagStatus(), tag.getTagDescription(), tag.getTagReservation());
        }
        if (result == 0) {
            map.put("message", "操作失败!");
        } else {
            map.put("tag", tag);
        }
        return map;
    }

    @Override
    public List<LabelModel> findTagLabels() {
        List<LabelModel> list = (List<LabelModel>) CacheUtils.get("tags");
        if (list == null) {
            list = tagMapper.selectTagLabels();
            CacheUtils.put("tags", list);
        }
        return list;
    }
}

package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleTagDTO;
import com.rymcu.vertical.dto.LabelModel;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.ArticleMapper;
import com.rymcu.vertical.mapper.TagMapper;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.util.CacheUtils;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.web.api.exception.BaseApiException;
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
    @Transactional(rollbackFor = { UnsupportedEncodingException.class,BaseApiException.class })
    public Integer saveTagArticle(Article article) throws UnsupportedEncodingException, BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        String articleTags = article.getArticleTags();
        if(StringUtils.isNotBlank(articleTags)){
            String[] tags = articleTags.split(",");
            List<ArticleTagDTO> articleTagDTOList = articleMapper.selectTags(article.getIdArticle());
            for (int i = 0; i < tags.length; i++) {
                boolean addTagArticle = false;
                boolean addUserTag = false;
                Tag tag = new Tag();
                tag.setTagTitle(tags[i]);
                tag = tagMapper.selectOne(tag);
                if(tag == null){
                    tag = new Tag();
                    tag.setTagTitle(tags[i]);
                    tag.setTagUri(URLEncoder.encode(tag.getTagTitle(),"UTF-8"));
                    tag.setCreatedTime(new Date());
                    tag.setUpdatedTime(tag.getCreatedTime());
                    tag.setTagArticleCount(1);
                    tagMapper.insertSelective(tag);
                    addTagArticle = true;
                    addUserTag = true;
                } else {
                    for(int m=0,n=articleTagDTOList.size()-1;m<n; m++) {
                        ArticleTagDTO articleTag = articleTagDTOList.get(m);
                        if (articleTag.getIdTag().equals(tag.getIdTag())) {
                            articleTagDTOList.remove(articleTag);
                        }
                    }
                    Integer count = tagMapper.selectCountTagArticleById(tag.getIdTag(),article.getIdArticle());
                    if(count == 0){
                        tag.setTagArticleCount(tag.getTagArticleCount() + 1);
                        tagMapper.updateByPrimaryKeySelective(tag);
                        addTagArticle = true;
                    }
                    Integer countUserTag = tagMapper.selectCountUserTagById(user.getIdUser(),tag.getIdTag());
                    if(countUserTag == 0){
                        addUserTag = true;
                    }
                }
                articleTagDTOList.forEach(articleTagDTO -> {
                    articleMapper.deleteUnusedArticleTag(articleTagDTO.getIdArticleTag());
                });
                if(addTagArticle){
                    tagMapper.insertTagArticle(tag.getIdTag(),article.getIdArticle());
                }
                if(addUserTag){
                    tagMapper.insertUserTag(tag.getIdTag(),user.getIdUser(),1);
                }
            }
            return 1;
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
        if (tag.getIdTag() == null) {
            if (StringUtils.isBlank(tag.getTagTitle())) {
                map.put("message","标签名不能为空!");
                return map;
            } else {
                Condition tagCondition = new Condition(Tag.class);
                tagCondition.createCriteria().andCondition("tag_title =", tag.getTagTitle());
                List<Tag> tags = tagMapper.selectByCondition(tagCondition);
                if (!tags.isEmpty()) {
                    map.put("message","标签 '" + tag.getTagTitle() + "' 已存在!");
                    return map;
                }
            }
            Tag newTag = new Tag();
            newTag.setTagTitle(tag.getTagTitle());
            newTag.setTagUri(tag.getTagUri());
            newTag.setTagIconPath(tag.getTagIconPath());
            newTag.setTagStatus(tag.getTagStatus());
            newTag.setTagDescription(tag.getTagDescription());
            newTag.setTagReservation(tag.getTagReservation());
            newTag.setCreatedTime(new Date());
            newTag.setUpdatedTime(tag.getCreatedTime());
            result = tagMapper.insertSelective(newTag);
        } else {
            tag.setUpdatedTime(new Date());
            result = tagMapper.update(tag.getIdTag(),tag.getTagUri(),tag.getTagIconPath(),tag.getTagStatus(),tag.getTagDescription(),tag.getTagReservation());
        }
        if (result == 0) {
            map.put("message","操作失败!");
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

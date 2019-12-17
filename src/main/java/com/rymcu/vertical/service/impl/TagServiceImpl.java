package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.TagMapper;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@Service
public class TagServiceImpl extends AbstractService<Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    @Transactional(rollbackFor = { UnsupportedEncodingException.class,BaseApiException.class })
    public Integer saveTagArticle(Article article) throws UnsupportedEncodingException, BaseApiException {
        User user = UserUtils.getWxCurrentUser();
        String articleTags = article.getArticleTags();
        if(StringUtils.isNotBlank(articleTags)){
            String[] tags = articleTags.split(",");
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
        Integer result = 0;
        if (tag.getIdTag() == null) {
            tag.setCreatedTime(new Date());
            tag.setUpdatedTime(tag.getCreatedTime());
            result = tagMapper.insertSelective(tag);
        } else {
            tag.setUpdatedTime(new Date());
            result = tagMapper.update(tag.getIdTag(),tag.getTagUri(),tag.getTagIconPath(),tag.getTagStatus(),tag.getTagDescription());
        }
        Map map = new HashMap(1);
        if (result == 0) {
            map.put("message","操作失败!");
        } else {
            map.put("tag", tag);
        }
        return map;
    }
}

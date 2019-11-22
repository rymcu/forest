package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.TagMapper;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

@Service
public class TagServiceImpl extends AbstractService<Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    @Transactional
    public void saveTagArticle(Article article) throws UnsupportedEncodingException, MallApiException {
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
                    tagMapper.insertSelective(tag);
                    addTagArticle = true;
                    addUserTag = true;
                } else {
                    Integer count = tagMapper.selectCountTagArticleById(tag.getIdTag(),article.getIdArticle());
                    if(count == 0){
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
        }
    }
}

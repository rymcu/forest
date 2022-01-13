package com.rymcu.forest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.admin.TagDTO;
import com.rymcu.forest.dto.admin.TopicDTO;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.Topic;
import com.rymcu.forest.mapper.TopicMapper;
import com.rymcu.forest.service.TopicService;
import com.rymcu.forest.web.api.common.UploadController;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@Service
public class TopicServiceImpl extends AbstractService<Topic> implements TopicService {

    @Resource
    private TopicMapper topicMapper;

    @Override
    public List<Topic> findTopicNav() {
        List<Topic> topics = topicMapper.selectTopicNav();
        return topics;
    }

    @Override
    public Topic findTopicByTopicUri(String topicUri) {
        Topic searchTopic = new Topic();
        searchTopic.setTopicUri(topicUri);
        return topicMapper.selectOne(searchTopic);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map saveTopic(Topic topic) {
        Integer result = 0;
        Map map = new HashMap(1);
        if (topic.getIdTopic() == null) {
            if (StringUtils.isBlank(topic.getTopicTitle())) {
                map.put("message","标签名不能为空!");
                return map;
            } else {
                Condition topicCondition = new Condition(Topic.class);
                topicCondition.createCriteria().andCondition("topic_title =", topic.getTopicTitle());
                List<Topic> topics = topicMapper.selectByCondition(topicCondition);
                if (!topics.isEmpty()) {
                    map.put("message","专题 '" + topic.getTopicTitle() + "' 已存在!");
                    return map;
                }
            }
            Topic newTopic = new Topic();
            newTopic.setTopicTitle(topic.getTopicTitle());
            newTopic.setTopicUri(topic.getTopicUri());
            if (StringUtils.isNotBlank(topic.getTopicIconPath()) && topic.getTopicIconPath().contains("base64")) {
                String topicIconPath = UploadController.uploadBase64File(topic.getTopicIconPath(), 3);
                newTopic.setTopicIconPath(topicIconPath);
            } else {
                newTopic.setTopicIconPath(topic.getTopicIconPath());
            }
            newTopic.setTopicNva(topic.getTopicNva());
            newTopic.setTopicStatus(topic.getTopicStatus());
            newTopic.setTopicSort(topic.getTopicSort());
            newTopic.setTopicDescription(topic.getTopicDescription());
            newTopic.setTopicDescriptionHtml(topic.getTopicDescriptionHtml());
            newTopic.setCreatedTime(new Date());
            newTopic.setUpdatedTime(topic.getCreatedTime());
            result = topicMapper.insertSelective(newTopic);
        } else {
            if (StringUtils.isNotBlank(topic.getTopicIconPath()) && topic.getTopicIconPath().contains("base64")) {
                String topicIconPath = UploadController.uploadBase64File(topic.getTopicIconPath(), 3);
                topic.setTopicIconPath(topicIconPath);
            }
            topic.setUpdatedTime(new Date());
            result = topicMapper.update(topic.getIdTopic(),topic.getTopicTitle(),topic.getTopicUri()
                    ,topic.getTopicIconPath(),topic.getTopicNva(),topic.getTopicStatus()
                    ,topic.getTopicSort(),topic.getTopicDescription(),topic.getTopicDescriptionHtml());
        }
        if (result == 0) {
            map.put("message","操作失败!");
        } else {
            map.put("topic", topic);
        }
        return map;
    }

    @Override
    public List<Tag> findUnbindTagsById(Integer idTopic, String tagTitle) {
        if (StringUtils.isBlank(tagTitle)) {
            tagTitle = "";
        }
        return topicMapper.selectUnbindTagsById(idTopic,tagTitle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map bindTopicTag(TopicTagDTO topicTag) {
        Integer result = topicMapper.insertTopicTag(topicTag.getIdTopic(), topicTag.getIdTag());
        Map map = new HashMap(1);
        if (result == 0) {
            map.put("message", "操作失败!");
        } else {
            map.put("topicTag", topicTag);
        }
        return map;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map unbindTopicTag(TopicTagDTO topicTag) {
        Integer result = topicMapper.deleteTopicTag(topicTag.getIdTopic(), topicTag.getIdTag());
        Map map = new HashMap(1);
        if (result == 0) {
            map.put("message", "操作失败!");
        } else {
            map.put("topicTag", topicTag);
        }
        return map;
    }

    @Override
    public Map findTagsByTopicUri(String topicUri, Integer page, Integer rows) {
        Map map = new HashMap(2);
        TopicDTO topic = topicMapper.selectTopicByTopicUri(topicUri);
        if (topic == null) {
            return map;
        }
        PageHelper.startPage(page, rows);
        List<TagDTO> list = topicMapper.selectTopicTag(topic.getIdTopic());
        PageInfo pageInfo = new PageInfo(list);
        map.put("tags", pageInfo.getList());
        Map pagination = new HashMap(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return map;
    }
}

package com.rymcu.vertical.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.admin.TagDTO;
import com.rymcu.vertical.dto.admin.TopicDTO;
import com.rymcu.vertical.entity.Topic;
import com.rymcu.vertical.mapper.TopicMapper;
import com.rymcu.vertical.service.TopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public Map findTopicByTopicUri(String topicUri, Integer page, Integer rows) {
        Map map = new HashMap(2);
        TopicDTO topic = topicMapper.selectTopicByTopicUri(topicUri);
        if (topic == null) {
            return map;
        }
        PageHelper.startPage(page, rows);
        List<TagDTO> list = topicMapper.selectTopicTag(topic.getIdTopic());
        PageInfo pageInfo = new PageInfo(list);
        topic.setTags(pageInfo.getList());
        map.put("topic", topic);
        Map pagination = new HashMap(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return map;
    }
}

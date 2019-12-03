package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.admin.TagDTO;
import com.rymcu.vertical.dto.admin.TopicDTO;
import com.rymcu.vertical.entity.Topic;
import com.rymcu.vertical.mapper.TopicMapper;
import com.rymcu.vertical.service.TopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public TopicDTO findTopicByTopicUri(String topicUri, Integer page, Integer rows) {
        TopicDTO topic = topicMapper.selectTopicByTopicUri(topicUri);
        if (topic == null) {
            return new TopicDTO();
        }
        List<TagDTO> list = topicMapper.selectTopicTag(topic.getIdTopic());
        return topic;
    }
}

package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.admin.TopicDTO;
import com.rymcu.vertical.entity.Topic;

import java.util.List;

/**
 * @author ronger
 */
public interface TopicService extends Service<Topic> {

    /**
     * 获取导航主题数据
     * @return
     * */
    List<Topic> findTopicNav();

    /**
     * 根据 topicUri 获取主题信息及旗下标签数据
     * @param topicUri 主题 URI
     * @param page
     * @param rows
     * @return
     * */
    TopicDTO findTopicByTopicUri(String topicUri, Integer page, Integer rows);
}

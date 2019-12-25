package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.admin.TopicTagDTO;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.Topic;

import java.util.List;
import java.util.Map;

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
    Map findTopicByTopicUri(String topicUri, Integer page, Integer rows);

    /**
     * 新增/更新主题信息
     * @param topic 主题信息
     * @return
     * */
    Map saveTopic(Topic topic);

    /**
     * 查询未绑定标签
     * @param idTopic
     * @param tagTitle
     * @return
     */
    List<Tag> findUnbindTagsById(Integer idTopic, String tagTitle);

    /**
     * 绑定标签
     * @param topicTag
     * @return
     */
    Map bindTopicTag(TopicTagDTO topicTag);

    /**
     * 取消绑定标签
     * @param topicTag
     * @return
     */
    Map unbindTopicTag(TopicTagDTO topicTag);
}

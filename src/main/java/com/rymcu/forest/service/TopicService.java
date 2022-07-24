package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.Topic;

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
     * @return
     * */
    Topic findTopicByTopicUri(String topicUri);

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
    List<Tag> findUnbindTagsById(Long idTopic, String tagTitle);

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

    /**
     * 获取主题下标签列表
     * @param topicUri
     * @param page
     * @param rows
     * @return
     */
    Map findTagsByTopicUri(String topicUri, Integer page, Integer rows);
}

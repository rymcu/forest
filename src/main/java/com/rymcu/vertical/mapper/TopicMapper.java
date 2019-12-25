package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.admin.TagDTO;
import com.rymcu.vertical.dto.admin.TopicDTO;
import com.rymcu.vertical.entity.Tag;
import com.rymcu.vertical.entity.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ronger
 */
public interface TopicMapper extends Mapper<Topic> {
    /**
     * 获取导航主题
     * @return
     */
    List<Topic> selectTopicNav();

    /**
     * @param topicUri
     * @return
     */
    TopicDTO selectTopicByTopicUri(@Param("topicUri") String topicUri);

    /**
     * @param idTopic
     * @return
     */
    List<TagDTO> selectTopicTag(@Param("idTopic") Integer idTopic);

    /**
     * @param idTopic
     * @param topicTitle
     * @param topicUri
     * @param topicIconPath
     * @param topicNva
     * @param topicStatus
     * @param topicSort
     * @param topicDescription
     * @return
     */
    Integer update(@Param("idTopic") Integer idTopic, @Param("topicTitle") String topicTitle, @Param("topicUri") String topicUri, @Param("topicIconPath") String topicIconPath, @Param("topicNva") String topicNva, @Param("topicStatus") String topicStatus, @Param("topicSort") Integer topicSort, @Param("topicDescription") String topicDescription);

    /**
     * @param idTopic
     * @param tagTitle
     * @return
     */
    List<Tag> selectUnbindTagsById(@Param("idTopic") Integer idTopic, @Param("tagTitle") String tagTitle);

    Integer insertTopicTag(@Param("idTopic") Integer idTopic, @Param("idTag") Integer idTag);

    /**
     * @param idTopic
     * @param idTag
     * @return
     */
    Integer deleteTopicTag(@Param("idTopic") Integer idTopic, @Param("idTag") Integer idTag);
}

package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.admin.TagDTO;
import com.rymcu.vertical.dto.admin.TopicDTO;
import com.rymcu.vertical.entity.Topic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicMapper extends Mapper<Topic> {
    List<Topic> selectTopicNav();

    TopicDTO selectTopicByTopicUri(@Param("topicUri") String topicUri);

    List<TagDTO> selectTopicTag(@Param("idTopic") Integer idTopic);

    Integer update(@Param("idTopic") Integer idTopic, @Param("topicTitle") String topicTitle, @Param("topicUri") String topicUri, @Param("topicIconPath") String topicIconPath, @Param("topicNva") String topicNva, @Param("topicStatus") String topicStatus, @Param("topicSort") Integer topicSort, @Param("topicDescription") String topicDescription);
}

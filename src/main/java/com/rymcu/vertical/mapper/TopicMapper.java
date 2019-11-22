package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Topic;

import java.util.List;

public interface TopicMapper extends Mapper<Topic> {
    List<Topic> selectTopicNav();
}

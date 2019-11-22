package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.entity.Topic;

import java.util.List;

public interface TopicService extends Service<Topic> {
    List<Topic> findTopicNav();
}

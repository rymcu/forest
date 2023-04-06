package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.dto.admin.TagDTO;
import com.rymcu.forest.dto.admin.TopicTagDTO;
import com.rymcu.forest.entity.Tag;
import com.rymcu.forest.entity.Topic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 主题test
 */
class TopicServiceTest extends BaseServiceTest {

    @Autowired
    private TopicService topicService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("获取导航主题数据")
    void findTopicNav() {
        List<Topic> topicNav = topicService.findTopicNav();
        assertTrue(topicNav.isEmpty());
    }

    @Test
    @DisplayName("根据 topicUri 获取主题信息及旗下标签数据")
    void findTopicByTopicUri() {
        Topic topicNav = topicService.findTopicByTopicUri("rymcu.com/topic/123792");
        assertNull(topicNav);
    }

    @Test
    @DisplayName("新增/更新主题信息")
    void saveTopic() {
        Topic topic = new Topic();
        assertThrows(IllegalArgumentException.class, () -> topicService.saveTopic(topic));

        topic.setTopicTitle("test");
        topicService.saveTopic(topic);
        assertNotNull(topic.getIdTopic());

        topic.setIdTopic(null);
        assertThrows(BusinessException.class, () -> topicService.saveTopic(topic));

    }

    @Test
    @DisplayName("查询未绑定标签")
    void findUnbindTagsById() {
        List<Tag> tags = topicService.findUnbindTagsById(1L, "test");
        assertTrue(tags.isEmpty());
    }

    @Test
    @DisplayName("绑定标签")
    void bindTopicTag() {
        TopicTagDTO topicTagDTO = new TopicTagDTO();
        topicTagDTO.setIdTag(1L);
        topicTagDTO.setIdTopic(1L);
        topicService.bindTopicTag(topicTagDTO);
        assertNotNull(topicTagDTO.getIdTag());
    }

    @Test
    @DisplayName("取消绑定标签")
    void unbindTopicTag() {
        TopicTagDTO topicTagDTO = new TopicTagDTO();
        topicTagDTO.setIdTag(1L);
        topicTagDTO.setIdTopic(1L);
        assertThrows(ServiceException.class, () -> topicService.unbindTopicTag(topicTagDTO));

        topicService.bindTopicTag(topicTagDTO);

        assertDoesNotThrow(() -> topicService.unbindTopicTag(topicTagDTO));
    }

    @Test
    @DisplayName("获取主题下标签列表")
    void findTagsByTopicUri() {
        List<TagDTO> tags = topicService.findTagsByTopicUri("test.com/topic/123792");
        assertNull(tags);
    }
}
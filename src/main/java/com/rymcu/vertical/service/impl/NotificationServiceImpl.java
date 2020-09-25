package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.NotificationDTO;
import com.rymcu.vertical.entity.Comment;
import com.rymcu.vertical.entity.Follow;
import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.NotificationMapper;
import com.rymcu.vertical.service.*;
import com.rymcu.vertical.util.BeanCopierUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class NotificationServiceImpl extends AbstractService<Notification> implements NotificationService {

    @Resource
    private NotificationMapper notificationMapper;
    @Resource
    private ArticleService articleService;
    @Resource
    private CommentService commentService;
    @Resource
    private UserService userService;
    @Resource
    private FollowService followService;
    @Value("${resource.domain}")
    private String domain;

    @Override
    public List<Notification> findUnreadNotifications(Integer idUser) {
        List<Notification> list = notificationMapper.selectUnreadNotifications(idUser);
        return list;
    }

    @Override
    public List<NotificationDTO> findNotifications(Integer idUser) {
        List<Notification> list = notificationMapper.selectNotifications(idUser);
        List<NotificationDTO> notifications = new ArrayList<>();
        list.forEach(notification -> {
            NotificationDTO notificationDTO = genNotification(notification);
            notifications.add(notificationDTO);
        });
        return notifications;
    }

    private NotificationDTO genNotification(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanCopierUtil.copy(notification, notificationDTO);
        ArticleDTO article;
        Comment comment;
        User user;
        Follow follow;
        switch (notification.getDataType()) {
            case "0":
                // 系统公告/帖子
                article = articleService.findArticleDTOById(notification.getDataId(), 0);
                notificationDTO.setDataTitle("系统公告");
                notificationDTO.setDataUrl(article.getArticlePermalink());
                user = userService.findById(article.getArticleAuthorId().toString());
                notificationDTO.setAuthor(genAuthor(user));
                break;
            case "1":
                // 关注
                follow = followService.findById(notification.getDataId().toString());
                notificationDTO.setDataTitle("关注提醒");
                user = userService.findById(follow.getFollowerId().toString());
                notificationDTO.setDataUrl(getFollowLink(follow.getFollowingType(), user.getNickname()));
                notificationDTO.setAuthor(genAuthor(user));
                break;
            case "2":
                // 回帖
                comment = commentService.findById(notification.getDataId().toString());
                article = articleService.findArticleDTOById(comment.getCommentArticleId(), 0);
                notificationDTO.setDataTitle(article.getArticleTitle());
                notificationDTO.setDataUrl(comment.getCommentSharpUrl());
                user = userService.findById(comment.getCommentAuthorId().toString());
                notificationDTO.setAuthor(genAuthor(user));
                break;
            default:
                break;
        }
        return notificationDTO;
    }

    private String getFollowLink(String followingType, String id) {
        StringBuilder url = new StringBuilder();
        url.append(domain);
        switch (followingType) {
            case "0":
                url = url.append("/user/").append(id);
                break;
            default:
                url.append("/notification");
        }
        return url.toString();
    }

    private Author genAuthor(User user) {
        Author author = new Author();
        author.setUserNickname(user.getNickname());
        author.setUserAvatarURL(user.getAvatarUrl());
        author.setIdUser(user.getIdUser());
        return author;
    }

    @Override
    public Notification findNotification(Integer idUser, Integer dataId, String dataType) {
        return notificationMapper.selectNotification(idUser, dataId, dataType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(Integer idUser, Integer dataId, String dataType, String dataSummary) {
        return notificationMapper.insertNotification(idUser, dataId, dataType, dataSummary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer readNotification(Integer id) {
        return notificationMapper.readNotification(id);
    }
}

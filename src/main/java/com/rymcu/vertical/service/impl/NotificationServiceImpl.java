package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.NotificationDTO;
import com.rymcu.vertical.entity.Comment;
import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.NotificationMapper;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.CommentService;
import com.rymcu.vertical.service.NotificationService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.BeanCopierUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        }
        return notificationDTO;
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

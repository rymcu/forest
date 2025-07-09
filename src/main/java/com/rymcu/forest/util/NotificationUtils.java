package com.rymcu.forest.util;

import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.Author;
import com.rymcu.forest.dto.NotificationDTO;
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.*;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Objects;

/**
 * 消息通知工具类
 *
 * @author ronger
 */
@Slf4j
public class NotificationUtils {

    private static final NotificationService notificationService = SpringContextHolder.getBean(NotificationService.class);
    private static final UserService userService = SpringContextHolder.getBean(UserService.class);
    private static final FollowService followService = SpringContextHolder.getBean(FollowService.class);
    private static final JavaMailService mailService = SpringContextHolder.getBean(JavaMailService.class);
    private static final ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static final CommentService commentService = SpringContextHolder.getBean(CommentService.class);

    public static void sendAnnouncement(Long dataId, String dataType, String dataSummary) {
        List<User> users = userService.findAll();
        users.forEach(user -> {
            try {
                saveNotification(user.getIdUser(), dataId, dataType, dataSummary);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void saveNotification(Long idUser, Long dataId, String dataType, String dataSummary) throws MessagingException {
        Notification notification = notificationService.findNotification(idUser, dataId, dataType);
        if (notification == null || NotificationConstant.UpdateArticle.equals(dataType) || NotificationConstant.UpdateArticleStatus.equals(dataType)) {
            log.info("------------------- 开始执行消息通知 ------------------");
            Integer result = notificationService.save(idUser, dataId, dataType, dataSummary);
            if (result == 0) {
                // TODO 记录操作失败数据
            }
        }
        if (NotificationConstant.Comment.equals(dataType)) {
            notification = notificationService.findNotification(idUser, dataId, dataType);
            NotificationDTO notificationDTO = genNotification(notification);
            mailService.sendNotification(notificationDTO);
        }
    }

    public static void sendArticlePush(Long dataId, String dataType, String dataSummary, Long articleAuthorId) {
        List<Follow> follows;
        if (NotificationConstant.PostArticle.equals(dataType)) {
            // 关注用户通知
            follows = followService.findByFollowingId("0", articleAuthorId);
        } else {
            // 关注文章通知
            follows = followService.findByFollowingId("3", articleAuthorId);
        }
        follows.forEach(follow -> {
            try {
                saveNotification(follow.getFollowerId(), dataId, dataType, dataSummary);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static NotificationDTO genNotification(Notification notification) {
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
                if (Objects.nonNull(article)) {
                    notificationDTO.setDataTitle("系统公告");
                    notificationDTO.setDataUrl(article.getArticlePermalink());
                    user = userService.findById(article.getArticleAuthorId().toString());
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            case "1":
                // 关注
                follow = followService.findById(notification.getDataId().toString());
                notificationDTO.setDataTitle("关注提醒");
                if (Objects.nonNull(follow)) {
                    user = userService.findById(follow.getFollowerId().toString());
                    notificationDTO.setDataUrl(getFollowLink(follow.getFollowingType(), user.getAccount()));
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            case "2":
                // 回帖
                comment = commentService.findById(notification.getDataId().toString());
                if (Objects.nonNull(comment)) {
                    article = articleService.findArticleDTOById(comment.getCommentArticleId(), 0);
                    if (Objects.nonNull(article)) {
                        notificationDTO.setDataTitle(article.getArticleTitle());
                        notificationDTO.setDataUrl(comment.getCommentSharpUrl());
                        user = userService.findById(comment.getCommentAuthorId().toString());
                        notificationDTO.setAuthor(genAuthor(user));
                    }
                }
                break;
            case "3":
                // 关注用户发布文章
            case "4":
                // 关注文章更新
                article = articleService.findArticleDTOById(notification.getDataId(), 0);
                if (Objects.nonNull(article)) {
                    notificationDTO.setDataTitle("关注通知");
                    notificationDTO.setDataUrl(article.getArticlePermalink());
                    user = userService.findById(article.getArticleAuthorId().toString());
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            case "5":
                // 文章状态变更
                article = articleService.findArticleDTOById(notification.getDataId(), 0);
                if (Objects.nonNull(article)) {
                    notificationDTO.setDataTitle("系统通知");
                    notificationDTO.setDataUrl(article.getArticlePermalink());
                    user = userService.findById(article.getArticleAuthorId().toString());
                    notificationDTO.setAuthor(genAuthor(user));
                }
                break;
            default:
                break;
        }
        return notificationDTO;
    }

    private static String getFollowLink(String followingType, String id) {
        StringBuilder url = new StringBuilder();
        url.append(Utils.getProperty("resource.domain"));
        if ("0".equals(followingType)) {
            url.append("/user/").append(id);
        } else {
            url.append("/notification");
        }
        return url.toString();
    }

    private static Author genAuthor(User user) {
        Author author = new Author();
        author.setUserNickname(user.getNickname());
        author.setUserAvatarURL(user.getAvatarUrl());
        author.setIdUser(user.getIdUser());
        return author;
    }
}

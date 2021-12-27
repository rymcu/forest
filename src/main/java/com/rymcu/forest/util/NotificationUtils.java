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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 消息通知工具类
 *
 * @author ronger
 */
public class NotificationUtils {

    private static NotificationService notificationService = SpringContextHolder.getBean(NotificationService.class);
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    private static FollowService followService = SpringContextHolder.getBean(FollowService.class);
    private static JavaMailService mailService = SpringContextHolder.getBean(JavaMailService.class);

    private static ArticleService articleService = SpringContextHolder.getBean(ArticleService.class);
    private static CommentService commentService = SpringContextHolder.getBean(CommentService.class);

    public static void sendAnnouncement(Integer dataId, String dataType, String dataSummary) {
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(() -> {
            try {
                List<User> users = userService.findAll();
                users.forEach(user -> {
                    saveNotification(user.getIdUser(), dataId, dataType, dataSummary);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        }, executor);
    }

    public static void saveNotification(Integer idUser, Integer dataId, String dataType, String dataSummary) {
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(() -> {
            try {
                Notification notification = notificationService.findNotification(idUser, dataId, dataType);
                if (notification == null || NotificationConstant.UpdateArticle.equals(dataType)) {
                    System.out.println("------------------- 开始执行消息通知 ------------------");
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
            } catch (Exception ex) {
                // TODO 记录操作失败数据
                ex.printStackTrace();
            }
            return 0;
        }, executor);

    }

    public static void sendArticlePush(Integer dataId, String dataType, String dataSummary, Integer articleAuthorId) {
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(() -> {
            try {
                List<Follow> follows;
                if (NotificationConstant.PostArticle.equals(dataType)) {
                    // 关注用户通知
                    follows = followService.findByFollowingId("0", articleAuthorId);
                } else {
                    // 关注文章通知
                    follows = followService.findByFollowingId("3", articleAuthorId);
                }
                follows.forEach(follow -> {
                    saveNotification(follow.getFollowerId(), dataId, dataType, dataSummary);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        }, executor);
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
            default:
                break;
        }
        return notificationDTO;
    }

    private static String getFollowLink(String followingType, String id) {
        StringBuilder url = new StringBuilder();
        url.append(Utils.getProperty("resource.domain"));
        switch (followingType) {
            case "0":
                url = url.append("/user/").append(id);
                break;
            default:
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

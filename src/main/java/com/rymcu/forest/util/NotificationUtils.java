package com.rymcu.forest.util;

import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.entity.Notification;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.service.NotificationService;
import com.rymcu.forest.service.UserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

/**
 * 消息通知工具类
 *
 * @author ronger
 */
public class NotificationUtils {

    @Resource
    private static NotificationService notificationService = SpringContextHolder.getBean(NotificationService.class);
    @Resource
    private static UserService userService = SpringContextHolder.getBean(UserService.class);
    @Resource
    private static FollowService followService = SpringContextHolder.getBean(FollowService.class);

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
}

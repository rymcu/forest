package com.rymcu.vertical.util;

import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.NotificationService;
import com.rymcu.vertical.service.UserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.*;

/**
 * 消息通知工具类
 * @author ronger
 */
public class NotificationUtils {

    @Resource
    private static NotificationService notificationService = SpringContextHolder.getBean(NotificationService.class);
    @Resource
    private static UserService userService = SpringContextHolder.getBean(UserService.class);

    public static void sendAnnouncement(Integer dataId, String dataType, String dataSummary) {
        ExecutorService executor= new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(()-> {
            System.out.println("------------------- 开始执行消息通知 ------------------");
            try {
                List<User> users = userService.findAll();
                users.forEach(user -> {
                    Notification notification = notificationService.findNotification(user.getIdUser(),dataId,dataType);
                    if (notification == null) {
                        Integer result = notificationService.save(user.getIdUser(),dataId,dataType,dataSummary);
                        if (result == 0) {
                            saveNotification(user.getIdUser(),dataId,dataType,dataSummary);
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        },executor);
    }

    private static void saveNotification(Integer idUser, Integer dataId, String dataType, String dataSummary) {
        ExecutorService executor= new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(()-> {
            try {
                Notification notification = notificationService.findNotification(idUser,dataId,dataType);
                if (notification == null) {
                    Integer result = notificationService.save(idUser,dataId,dataType,dataSummary);
                    if (result == 0) {
                        // TODO 记录操作失败数据
                    }
                }
            } catch (Exception ex) {
                // TODO 记录操作失败数据
                ex.printStackTrace();
            }
            return 0;
        },executor);

    }
}

package com.rymcu.vertical.util;

import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.entity.Notification;
import com.rymcu.vertical.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.core.env.Environment;

import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
public class Utils {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    private static Environment env = SpringContextHolder.getBean(Environment.class);

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword) {
        String plain = Encodes.unescapeHtml(plainPassword);
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        byte[] hashPassword = Digests.sha1(plain.getBytes(), salt, HASH_INTERATIONS);
        return Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword);
    }

    /**
     *一般检查工具密码比对  add by xlf 2018-11-8
     * @param pwd
     * @param enpwd    加密的密码
     * @return
     */
    public static boolean comparePwd(String pwd,String enpwd){
        byte[] salt = Encodes.decodeHex(enpwd.substring(0,16));
        byte[] hashPassword = Digests.sha1(pwd.getBytes(), salt, HASH_INTERATIONS);
        return enpwd.equals(Encodes.encodeHex(salt)+Encodes.encodeHex(hashPassword));
    }

    public static User getCurrentUser() {
        return null;
    }

    public static Session getSession() {
        try{
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null){
                session = subject.getSession();
            }
            if (session != null){
                return session;
            }
            subject.logout();
        }catch (InvalidSessionException e){

        }
        return null;
    }

    public static Integer genCode() {
        Integer code = (int)((Math.random()*9+1)*100000);
        return code;
    }

    /**
     * 获取配置文件内属性
     * @param key 键值
     * @return 属性值
     * */
    public static String getProperty(String key){
        return env.getProperty(key);
    }

    public static String getTimeAgo(Date date) {

        String timeAgo;

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate oldLocalDate = localDateTime.toLocalDate();

        LocalDate today = LocalDate.now();

        Period p = Period.between(oldLocalDate, today);
        if(p.getYears() > 0){
            timeAgo = p.getYears()+" 年前 ";
        }else if(p.getMonths() > 0){
            timeAgo = p.getMonths()+" 月前 ";
        }else if(p.getDays() > 0){
            timeAgo = p.getDays()+" 天前 ";
        }else {
            long to = System.currentTimeMillis();
            long from = date.getTime();
            int hours = (int) ((to - from)/(1000 * 60 * 60));
            if(hours > 0){
                timeAgo = hours+" 小时前 ";
            }else {
                int minutes = (int) ((to - from)/(1000 * 60));
                if(minutes == 0){
                    timeAgo = " 刚刚 ";
                }else {
                    timeAgo = minutes+" 分钟前 ";
                }
            }
        }
        return timeAgo;
    }

    public static Map getPagination(PageInfo pageInfo) {
        Map pagination = new HashMap(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        return pagination;
    }

    public static void main(String[] args){
        LocalDate localDate = LocalDate.parse("2019-11-15");
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        String s = getTimeAgo(Date.from(instant));
        System.out.println(s);
    }

    public static Map getArticlesGlobalResult(PageInfo<ArticleDTO> pageInfo) {
        Map map = new HashMap(2);
        map.put("articles", pageInfo.getList());
        Map pagination = new HashMap(4);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return map;
    }

    public static Map getNotificationsGlobalResult(PageInfo<Notification> pageInfo) {
        Map map = new HashMap(2);
        map.put("notifications", pageInfo.getList());
        Map pagination = new HashMap(4);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return map;
    }
}

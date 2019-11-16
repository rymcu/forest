package com.rymcu.vertical.util;

import com.rymcu.vertical.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

public class Utils {
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_SIZE = 8;
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
//			subject.logout();
        }catch (InvalidSessionException e){

        }
        return null;
    }
}

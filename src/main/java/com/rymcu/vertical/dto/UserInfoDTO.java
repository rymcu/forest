package com.rymcu.vertical.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserInfoDTO implements Serializable {

    private String idUser;

    private String account;

    private String avatarType;

    private String avatarUrl;

    private String nickname;

    private String email;

    private String phone;

    private String status;

    private String roleIds;

    private String sex;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date lastLoginTime;

}

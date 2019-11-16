package com.rymcu.vertical.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {

    private String id;

    private String office;

    private String officeId;

    private String loginName;

    private String password;

    private String no;

    private String name;

    private String email;

    private String phone;

    private String mobile;

    private String status;

    private String remarks;

    private String roleIds;

    private String sex = "男";

    private String inputCode;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date lastLoginTime;

}

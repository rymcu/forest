package com.rymcu.vertical.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ronger
 */
@Data
@Table(name = "vertical_user_extend")
public class UserExtend {

    @Id
    private Integer idUser;

    private String github;

    private String weibo;

    private String weixin;

    private String qq;

    private String blog;

}

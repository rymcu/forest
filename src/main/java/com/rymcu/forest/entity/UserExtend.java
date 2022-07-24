package com.rymcu.forest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ronger
 */
@Data
@Table(name = "forest_user_extend")
public class UserExtend {

    @Id
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long idUser;

    private String github;

    private String weibo;

    private String weixin;

    private String qq;

    private String blog;

}

package com.rymcu.forest.dto;


import lombok.Data;

/**
 * @author ronger
 */
@Data
public class UserDTO {

    private Long idUser;

    private String account;

    private String avatarType;

    private String avatarUrl;

    private String nickname;

    private String signature;

    private String bgImgUrl;
}

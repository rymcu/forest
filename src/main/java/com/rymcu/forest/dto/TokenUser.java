package com.rymcu.forest.dto;


import lombok.Data;

import java.util.Set;

/**
 * @author ronger
 */
@Data
public class TokenUser {

    private Long idUser;

    private String account;

    private String nickname;

    private String token;

    private String avatarUrl;

    private String refreshToken;

    private Set<String> scope;

    private String bankAccount;

}

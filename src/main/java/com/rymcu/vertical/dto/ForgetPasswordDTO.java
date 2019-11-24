package com.rymcu.vertical.dto;

import lombok.Data;

@Data
public class ForgetPasswordDTO {
    private String code;

    private String password;
}

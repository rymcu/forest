package com.rymcu.forest.dto;

import lombok.Data;

@Data
public class RoleDTO {

    // id
    private String id;

    // 名称
    private String name;

    // 英文名称
    private String inputCode;

    // 角色授权菜单ids
    private String menuIds;
}

package com.rymcu.vertical.dto;

import lombok.Data;

@Data
public class RoleDTO {

    // id
    private String id;

    // 名称
    private String name;

    // 英文名称
    private String enname;

    // 用户类别
    private String roleType;

    // 角色授权菜单ids
    private String menuIds;

    // 备注
    private String remarks;
}

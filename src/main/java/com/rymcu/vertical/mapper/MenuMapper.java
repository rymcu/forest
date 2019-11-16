package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.MenuDTO;
import com.rymcu.vertical.entity.Menu;
import com.rymcu.vertical.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends Mapper<Menu> {

    List<Menu> selectMenuByRole(@Param("role") Role role);

    List<MenuDTO> findByParentId(@Param("parentId") String parentId);

    MenuDTO findMenuDTOById(@Param("id") String id);

    void deleteRoleMenu(@Param("menuId") String id);

    List<MenuDTO> findByParentIdAndUserId(@Param("parentId") String parentId, @Param("userId") String userId);

    void deleteMenu(@Param("id") String id);
}
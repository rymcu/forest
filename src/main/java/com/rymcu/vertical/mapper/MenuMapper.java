package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.dto.MenuDTO;
import com.rymcu.vertical.entity.Menu;
import com.rymcu.vertical.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends Mapper<Menu> {

    List<Menu> selectMenuByIdRole(@Param("role") Integer role);
}
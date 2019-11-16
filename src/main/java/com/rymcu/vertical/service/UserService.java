package com.rymcu.vertical.service;

import com.rymcu.vertical.core.exception.ServiceException;
import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserExportDTO;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.exceptions.TooManyResultsException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface UserService extends Service<User> {

    List<UserDTO> findAllDTO(HttpServletRequest request);

    UserDTO findUserDTOById(Integer id);

    void saveUser(UserDTO userDTO);

    void updateUser(UserDTO userDTO);

    void deleteUserById(Integer id);

    Boolean checkLoginName(String loginName);

    void updateUserStatus(Integer id, Integer flag);

    void resetPassword(Integer userId, String password);

    User findByLoginName(String loginName) throws TooManyResultsException, ServiceException;

    List<UserDTO> queryUserByInputCode(String eName);

    void updateLastLoginTime(Integer id);

    List<UserExportDTO> exportExcel(HttpServletRequest request);

    List<RoleDTO> findRoleOfUser(HttpServletRequest request);
}

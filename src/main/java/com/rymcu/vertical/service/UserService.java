package com.rymcu.vertical.service;

import com.rymcu.vertical.core.service.Service;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.User;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.util.Map;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
public interface UserService extends Service<User> {


    User findByAccount(String account) throws TooManyResultsException;

    Map register(String email, String password, String code);

    Map login(String account, String password);

    UserDTO findUserDTOByNickname(String nickname);
}

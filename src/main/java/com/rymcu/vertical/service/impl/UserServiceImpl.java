package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.exception.ServiceException;
import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.RoleDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.dto.UserExportDTO;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.UserMapper;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.BeanCopierUtil;
import com.rymcu.vertical.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/05/29.
 */
@Service
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<UserDTO> findAllDTO(HttpServletRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName(request.getParameter("loginName"));
        userDTO.setInputCode(request.getParameter("inputCode"));
        userDTO.setName(request.getParameter("name"));
        userDTO.setStatus(request.getParameter("status"));
        userDTO.setOfficeId(request.getParameter("officeId"));// 为不创建多余字段，此处使用 Remark 字段存放 nodeId 数据
        return userMapper.findAllDTO(userDTO);
    }

    @Override
    public UserDTO findUserDTOById(Integer id) {
        return userMapper.findUserDTOById(id);
    }

    @Override
    @Transactional
    public void saveUser(UserDTO userDTO) {
        User user = new User();
        BeanCopierUtil.copy(userDTO, user);
        //user.setPassword(Utils.entryptPassword("123456"));
        //Utils.createBase(user);
        //所有字符转为小写
        user.setAccount(user.getAccount().toLowerCase());
        userMapper.insertSelective(user);

        String roleIds = userDTO.getRoleIds();
        if(StringUtils.isNotBlank(roleIds)){
            String[] r = roleIds.split(",");
            for(int i=0,len=r.length;i<len;i++){
                userMapper.insertUserRole(user.getIdUser(), Integer.parseInt(r[i]));
            }
        }

    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = userMapper.selectByPrimaryKey(userDTO.getId());
        String password = user.getPassword();
        userDTO.setLastLoginTime(user.getLastLoginTime());
        BeanCopierUtil.copy(userDTO,user);
        // 如果密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(Utils.entryptPassword(user.getPassword()));
        }else {
            user.setPassword(password);
        }
        if(user.getCreatedTime() == null){
            user.setCreatedTime(new Date());
        }
        userMapper.updateByPrimaryKeySelective(user);

        String roleIds = userDTO.getRoleIds();

        userMapper.deleteUserRoleByUserId(user.getIdUser());
        if(!"0".equals(roleIds) && StringUtils.isNotBlank(roleIds)){
            String[] r = roleIds.split(",");
            for(int i=0,len=r.length;i<len;i++){
                userMapper.insertUserRole(user.getIdUser(), Integer.parseInt(r[i]));
            }
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Integer idUser) {
        if(idUser.equals(Utils.getCurrentUser().getIdUser())){

        }else{
            userMapper.deleteUserById(idUser);
        }
    }

    @Override
    public Boolean checkLoginName(String loginName){
        Condition userCondition = new Condition(User.class);
        userCondition.createCriteria().andCondition("LOGIN_NAME =",loginName.toLowerCase());
        List<User> user = userMapper.selectByCondition(userCondition);
        Boolean b = false;
        if (user.size() == 0){
            b = true;
        }
        return b;
    }

    @Override
    @Transactional
    public void updateUserStatus(Integer id, Integer flag) {
        User user = userMapper.selectByPrimaryKey(id);
        user.setStatus(flag);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    @Transactional
    public void resetPassword(Integer userId, String password) {
        userMapper.resetPassword(userId,password);
    }

    @Override
    public User findByLoginName(String loginName) throws TooManyResultsException, ServiceException{
        return userMapper.findByLoginName(loginName);
    }

    @Override
    public List<UserDTO> queryUserByInputCode(String eName) {
        return userMapper.queryUserByInputCode(eName);
    }

    @Override
    @Transactional
    public void updateLastLoginTime(Integer id) {
        userMapper.updateLastLoginTime(id,new Date());
    }

    @Override
    public List<UserExportDTO> exportExcel(HttpServletRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLoginName(request.getParameter("loginName"));
        userDTO.setInputCode(request.getParameter("inputCode"));
        userDTO.setName(request.getParameter("name"));
        userDTO.setStatus(request.getParameter("status"));
        userDTO.setOfficeId(request.getParameter("officeId"));
        return userMapper.findUserExportData(userDTO);
    }

    @Override
    public List<RoleDTO> findRoleOfUser(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        return userMapper.findRoleOfUser(Integer.parseInt(userId));
    }
}

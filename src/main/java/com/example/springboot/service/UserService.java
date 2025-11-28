package com.example.springboot.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public List<User> selectAll() {
        return userMapper.selectAll(null);
    }

    public void update(User user) {
        User dbUser = userMapper.selectUserByUsername(user.getUsername());
        if (ObjectUtil.isNotEmpty(dbUser)&&dbUser.getId()!=user.getId()){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        userMapper.update(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectAll(user);
        return PageInfo.of(userList);
    }

    public void add(User user) {
        User dbUser = userMapper.selectUserByUsername(user.getUsername());
        if (ObjectUtil.isNotEmpty(dbUser)){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        else {
            user.setRole("user");
        if (ObjectUtil.isEmpty(user.getAvatar())) {
                user.setAvatar("http://localhost:9090/files/default.jpg");
            }
       
            userMapper.insert(user);
        }
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void register(User user) {
        //查看该用户名是否已经存在
        User dbUser = userMapper.selectUserByUsername(user.getUsername());
        //如果数据库无该用户，可以添加
        if (ObjectUtil.isNotEmpty(dbUser)){
            throw new ServiceException(ResultCode.USERNAME_EXIST_ERROR);
        }
        //存在则抛出异常
        else {
            user.setRole("user");
            user.setAvatar("http://localhost:9090/files/default.jpg");
            userMapper.insert(user);
        }
    }

    public User login(User user) {
        //查看该用户名是否已经存在
        User dbUser = userMapper.selectUserByUsername(user.getUsername());
        //如果数据库无该用户，抛出异常
        if ( dbUser==null){
            throw new ServiceException(ResultCode.USER_NOT_EXIST_ERROR);
        }
         //如果密码不一致 抛出异常
        if (!dbUser.getPassword().equals(user.getPassword())){
            throw new ServiceException(ResultCode.USER_ACCOUNT_ERROR);
        }
        return dbUser;
         }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }
    public void updatePassword(Account account) {
        User dbUser = userMapper.selectUserByUsername(account.getUsername());
        if (!account.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException(ResultCode.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.update(dbUser);
    }

}
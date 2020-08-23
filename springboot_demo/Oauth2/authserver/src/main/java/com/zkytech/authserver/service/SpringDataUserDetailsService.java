package com.zkytech.authserver.service;

import com.alibaba.fastjson.JSON;
import com.zkytech.authserver.entity.UserInfo;
import com.zkytech.authserver.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDataUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoRepository userInfoRepository;

    //根据 账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //将来连接数据库根据账号查询用户信息
        UserInfo userInfo = userInfoRepository.findUserInfoByUsername(username);
        if(userInfo == null){
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }
//        //根据用户的id查询用户的权限
//        List<String> permissions = userDao.findPermissionsByUserId(userInfo.getId());
//        //将permissions转成数组
//        String[] permissionArray = new String[permissions.size()];
//        permissions.toArray(permissionArray);
//        //将userDto转成json
//        String principal = JSON.toJSONString(userInfo);
//        UserDetails userDetails = User.withUsername(principal).password(userInfo.getPassword()).authorities(permissionArray).build();
//        return userDetails;
        //将userDto转成json
        String principal = JSON.toJSONString(userInfo);
        UserDetails userDetails = User.withUsername(principal).password(userInfo.getPassword()).authorities("p1","p3").build();
        return userDetails;
    }
}
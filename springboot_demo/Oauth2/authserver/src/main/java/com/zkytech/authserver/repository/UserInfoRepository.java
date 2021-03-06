package com.zkytech.authserver.repository;

import com.zkytech.authserver.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,String> {
    UserInfo findUserInfoByUsername(String username);
}

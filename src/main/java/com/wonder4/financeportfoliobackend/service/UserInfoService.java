package com.wonder4.financeportfoliobackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.entity.UserInfo;

import java.util.List;

public interface UserInfoService {
    UserInfo create(UserInfo userInfo);

    UserInfo getById(Long id);

    void delete(Long id);

    UserInfo update(Long id, UserInfo userInfo);

    List<UserInfo> list();

    IPage<UserInfo> page(long current, long size);
}

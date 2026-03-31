package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;
import com.wonder4.financeportfoliobackend.service.UserInfoService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfo create(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getById(Long id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        if (userInfo == null) {
            throw new BusinessException(404, "User not found with ID: " + id);
        }
        return userInfo;
    }

    @Override
    public void delete(Long id) {
        int rows = userInfoMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(404, "Cannot delete. User not found with ID: " + id);
        }
    }

    @Override
    public UserInfo update(Long id, UserInfo userInfo) {
        UserInfo existing = userInfoMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "Cannot update. User not found with ID: " + id);
        }
        userInfo.setId(id);
        userInfoMapper.updateUserById(userInfo);
        return userInfo;
    }

    @Override
    public List<UserInfo> list() {
        return userInfoMapper.selectAllUsers();
    }

    @Override
    public IPage<UserInfo> page(long current, long size) {
        return userInfoMapper.selectUserPage(new Page<>(current, size));
    }
}

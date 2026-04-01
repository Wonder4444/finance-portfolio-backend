package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;
import com.wonder4.financeportfoliobackend.service.UserInfoService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    public UserInfoServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfo create(UserInfo userInfo) {
        log.info("Creating UserInfo: {}", userInfo);

        userInfoMapper.insert(userInfo);
        return userInfo;
    }

    @Override
    public UserInfo getById(Long id) {
        log.info("Getting UserInfo by ID: {}", id);

        UserInfo userInfo = userInfoMapper.selectById(id);
        if (userInfo == null) {
            throw new BusinessException(404, "User not found with ID: " + id);
        }
        return userInfo;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting UserInfo by ID: {}", id);

        int rows = userInfoMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(404, "Cannot delete. User not found with ID: " + id);
        }
    }

    @Override
    public UserInfo update(Long id, UserInfo userInfo) {
        log.info("Updating UserInfo with ID: {}, Data: {}", id, userInfo);

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
        log.info("Listing all UserInfo records");

        return userInfoMapper.selectAllUsers();
    }

    @Override
    public IPage<UserInfo> page(long current, long size) {
        log.info("Paginating UserInfo records: page {}, size {}", current, size);

        return userInfoMapper.selectUserPage(new Page<>(current, size));
    }
}

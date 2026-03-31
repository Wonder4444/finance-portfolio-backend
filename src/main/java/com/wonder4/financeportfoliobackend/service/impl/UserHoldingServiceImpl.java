package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.mapper.UserHoldingMapper;
import com.wonder4.financeportfoliobackend.service.UserHoldingService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserHoldingServiceImpl implements UserHoldingService {

    private final UserHoldingMapper userHoldingMapper;

    public UserHoldingServiceImpl(UserHoldingMapper userHoldingMapper) {
        this.userHoldingMapper = userHoldingMapper;
    }

    @Override
    public UserHolding create(UserHolding userHolding) {
        userHoldingMapper.insert(userHolding);
        return userHolding;
    }

    @Override
    public UserHolding getById(Long id) {
        UserHolding holding = userHoldingMapper.selectById(id);
        if (holding == null) {
            throw new BusinessException(404, "Holding not found with ID: " + id);
        }
        return holding;
    }

    @Override
    public void delete(Long id) {
        int rows = userHoldingMapper.deleteHoldingById(id);
        if (rows == 0) {
            throw new BusinessException(404, "Cannot delete. Holding not found with ID: " + id);
        }
    }

    @Override
    public UserHolding update(Long id, UserHolding userHolding) {
        UserHolding existing = userHoldingMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "Cannot update. Holding not found with ID: " + id);
        }
        userHolding.setId(id);
        userHoldingMapper.updateHoldingById(userHolding);
        return userHolding;
    }

    @Override
    public List<UserHolding> list() {
        return userHoldingMapper.selectAllHoldings();
    }

    @Override
    public List<UserHolding> listByUserId(Long userId) {
        return userHoldingMapper.selectByUserId(userId);
    }

    @Override
    public IPage<UserHolding> page(long current, long size) {
        return userHoldingMapper.selectPage(new Page<>(current, size), null);
    }
}

package com.wonder4.financeportfoliobackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.entity.UserHolding;

import java.util.List;

public interface UserHoldingService {
    UserHolding create(UserHolding userHolding);

    UserHolding getById(Long id);

    void delete(Long id);

    UserHolding update(Long id, UserHolding userHolding);

    List<UserHolding> list();

    List<UserHolding> listByUserId(Long userId);

    IPage<UserHolding> page(long current, long size);
}

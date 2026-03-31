package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.mapper.UserHoldingMapper;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户持仓 Controller — 直接使用 Mapper 层
 *
 * <p>使用 BaseMapper 方法: insert, selectById, selectPage 使用 XML 自定义 SQL: deleteHoldingById,
 * updateHoldingById, selectByUserId, selectAllHoldings
 */
@RestController
@RequestMapping("/api/holdings")
public class UserHoldingController {

    private final UserHoldingMapper userHoldingMapper;

    public UserHoldingController(UserHoldingMapper userHoldingMapper) {
        this.userHoldingMapper = userHoldingMapper;
    }

    /** 新增持仓 —— BaseMapper.insert() */
    @PostMapping
    public ApiResult<UserHolding> create(@RequestBody UserHolding userHolding) {
        userHoldingMapper.insert(userHolding);
        return ApiResult.success(userHolding);
    }

    /** 根据 id 查询持仓 —— BaseMapper.selectById() */
    @GetMapping("/{id}")
    public ApiResult<UserHolding> getById(@PathVariable Long id) {
        return ApiResult.success(userHoldingMapper.selectById(id));
    }

    /** 逻辑删除持仓 —— XML: deleteHoldingById (手动 UPDATE is_deleted=1) */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userHoldingMapper.deleteHoldingById(id);
        return ApiResult.success();
    }

    /** 更新持仓信息 —— XML: updateHoldingById (动态 SQL) */
    @PutMapping("/{id}")
    public ApiResult<UserHolding> update(
            @PathVariable Long id, @RequestBody UserHolding userHolding) {
        userHolding.setId(id);
        userHoldingMapper.updateHoldingById(userHolding);
        return ApiResult.success(userHolding);
    }

    /** 查询全部持仓 —— XML: selectAllHoldings */
    @GetMapping
    public ApiResult<List<UserHolding>> list() {
        return ApiResult.success(userHoldingMapper.selectAllHoldings());
    }

    /** 按用户 id 查询持仓列表 —— XML: selectByUserId */
    @GetMapping("/user/{userId}")
    public ApiResult<List<UserHolding>> listByUserId(@PathVariable Long userId) {
        return ApiResult.success(userHoldingMapper.selectByUserId(userId));
    }

    /** 分页查询持仓 —— BaseMapper.selectPage() */
    @GetMapping("/page")
    public ApiResult<IPage<UserHolding>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        IPage<UserHolding> page = userHoldingMapper.selectPage(new Page<>(current, size), null);
        return ApiResult.success(page);
    }
}

package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.mapper.UserHoldingMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
@Tag(name = "UserHoldingController", description = "User holdings related api")
public class UserHoldingController {

    private final UserHoldingMapper userHoldingMapper;

    public UserHoldingController(UserHoldingMapper userHoldingMapper) {
        this.userHoldingMapper = userHoldingMapper;
    }

    /** 新增持仓 —— BaseMapper.insert() */
    @PostMapping
    @Operation(
            summary = "Create a new user holding",
            description =
                    "Creates a new user holding and returns the created holding with its generated ID.")
    public ApiResult<UserHolding> create(@RequestBody UserHolding userHolding) {
        userHoldingMapper.insert(userHolding);
        return ApiResult.success(userHolding);
    }

    /** 根据 id 查询持仓 —— BaseMapper.selectById() */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get user holding by ID",
            description = "Retrieves the user holding information for the given holding ID.")
    public ApiResult<UserHolding> getById(@PathVariable Long id) {
        return ApiResult.success(userHoldingMapper.selectById(id));
    }

    /** 逻辑删除持仓 —— XML: deleteHoldingById (手动 UPDATE is_deleted=1) */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user holding by ID",
            description =
                    "Deletes the user holding with the given ID. This is a logical delete, so the record will not be physically removed from the database.")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userHoldingMapper.deleteHoldingById(id);
        return ApiResult.success();
    }

    /** 更新持仓信息 —— XML: updateHoldingById (动态 SQL) */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user holding by ID",
            description =
                    "Updates the user holding information for the given holding ID. Only non-null fields will be updated.")
    public ApiResult<UserHolding> update(
            @PathVariable Long id, @RequestBody UserHolding userHolding) {
        userHolding.setId(id);
        userHoldingMapper.updateHoldingById(userHolding);
        return ApiResult.success(userHolding);
    }

    /** 查询全部持仓 —— XML: selectAllHoldings */
    @GetMapping
    @Operation(
            summary = "List all user holdings",
            description = "Retrieves a list of all user holdings in the system.")
    public ApiResult<List<UserHolding>> list() {
        return ApiResult.success(userHoldingMapper.selectAllHoldings());
    }

    /** 按用户 id 查询持仓列表 —— XML: selectByUserId */
    @GetMapping("/user/{userId}")
    @Operation(
            summary = "List user holdings by user ID",
            description = "Retrieves a list of user holdings for the given user ID.")
    public ApiResult<List<UserHolding>> listByUserId(@PathVariable Long userId) {
        return ApiResult.success(userHoldingMapper.selectByUserId(userId));
    }

    /** 分页查询持仓 —— BaseMapper.selectPage() */
    @GetMapping("/page")
    @Operation(
            summary = "Paginate user holdings",
            description =
                    "Retrieves a paginated list of user holdings. You can specify the page number and page size as query parameters.")
    public ApiResult<IPage<UserHolding>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        IPage<UserHolding> page = userHoldingMapper.selectPage(new Page<>(current, size), null);
        return ApiResult.success(page);
    }
}

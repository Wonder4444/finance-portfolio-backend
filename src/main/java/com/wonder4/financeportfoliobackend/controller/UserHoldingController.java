package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.service.UserHoldingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 用户持仓 Controller — 标准三层架构 Controller */
@RestController
@RequestMapping("/api/holdings")
@Tag(name = "UserHoldingController", description = "User holdings related api")
public class UserHoldingController {

    private final UserHoldingService userHoldingService;

    public UserHoldingController(UserHoldingService userHoldingService) {
        this.userHoldingService = userHoldingService;
    }

    @PostMapping
    @Operation(summary = "Create a new user holding")
    public ApiResult<UserHolding> create(@RequestBody UserHolding userHolding) {
        return ApiResult.success(userHoldingService.create(userHolding));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user holding by ID")
    public ApiResult<UserHolding> getById(@PathVariable Long id) {
        return ApiResult.success(userHoldingService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user holding by ID")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userHoldingService.delete(id);
        return ApiResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user holding by ID")
    public ApiResult<UserHolding> update(
            @PathVariable Long id, @RequestBody UserHolding userHolding) {
        return ApiResult.success(userHoldingService.update(id, userHolding));
    }

    @GetMapping
    @Operation(summary = "List all user holdings")
    public ApiResult<List<UserHolding>> list() {
        return ApiResult.success(userHoldingService.list());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List user holdings by user ID")
    public ApiResult<List<UserHolding>> listByUserId(@PathVariable Long userId) {
        return ApiResult.success(userHoldingService.listByUserId(userId));
    }

    @GetMapping("/page")
    @Operation(summary = "Paginate user holdings")
    public ApiResult<IPage<UserHolding>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return ApiResult.success(userHoldingService.page(current, size));
    }
}

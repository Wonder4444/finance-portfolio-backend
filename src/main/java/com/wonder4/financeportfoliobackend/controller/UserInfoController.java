package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.service.UserInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 用户信息 Controller — 标准三层架构 Controller */
@RestController
@RequestMapping("/api/users")
@Tag(name = "UserInfoController", description = "User information related api")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ApiResult<UserInfo> create(@RequestBody UserInfo userInfo) {
        return ApiResult.success(userInfoService.create(userInfo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ApiResult<UserInfo> getById(@PathVariable Long id) {
        return ApiResult.success(userInfoService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userInfoService.delete(id);
        return ApiResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by ID")
    public ApiResult<UserInfo> update(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        return ApiResult.success(userInfoService.update(id, userInfo));
    }

    @GetMapping
    @Operation(summary = "List all users")
    public ApiResult<List<UserInfo>> list() {
        return ApiResult.success(userInfoService.list());
    }

    @GetMapping("/page")
    @Operation(summary = "Paginate users")
    public ApiResult<IPage<UserInfo>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return ApiResult.success(userInfoService.page(current, size));
    }
}

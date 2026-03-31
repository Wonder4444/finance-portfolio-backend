package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户信息 Controller — 直接使用 Mapper 层
 *
 * <p>使用 BaseMapper 方法: insert, selectById, deleteById 使用 XML 自定义 SQL: updateUserById,
 * selectAllUsers, selectUserPage
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "UserInfoController", description = "User information related api")
public class UserInfoController {

    private final UserInfoMapper userInfoMapper;

    public UserInfoController(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /** 新增用户 —— BaseMapper.insert() */
    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the created user with its generated ID.")
    public ApiResult<UserInfo> create(@RequestBody UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
        return ApiResult.success(userInfo);
    }

    /** 根据 id 查询用户 —— BaseMapper.selectById() */
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves the user information for the given user ID.")
    @GetMapping("/{id}")
    public ApiResult<UserInfo> getById(@PathVariable Long id) {
        return ApiResult.success(userInfoMapper.selectById(id));
    }

    /** 逻辑删除用户 —— BaseMapper.deleteById() (配合 @TableLogic 自动逻辑删除) */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user by ID",
            description =
                    "Deletes the user with the given ID. This is a logical delete, so the record will not be physically removed from the database.")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userInfoMapper.deleteById(id);
        return ApiResult.success();
    }

    /** 更新用户信息 —— XML: updateUserById (动态 SQL, 只更新非空字段) */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update user by ID",
            description =
                    "Updates the user information for the given user ID. Only non-null fields in the request body will be updated.")
    public ApiResult<UserInfo> update(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        userInfo.setId(id);
        userInfoMapper.updateUserById(userInfo);
        return ApiResult.success(userInfo);
    }

    /** 查询全部用户 —— XML: selectAllUsers */
    @GetMapping
    @Operation(
            summary = "List all users",
            description = "Retrieves a list of all users in the system.")
    public ApiResult<List<UserInfo>> list() {
        return ApiResult.success(userInfoMapper.selectAllUsers());
    }

    /** 分页查询用户 —— XML: selectUserPage (MyBatis Plus 拦截器自动分页) */
    @GetMapping("/page")
    @Operation(
            summary = "Paginate users",
            description =
                    "Retrieves a paginated list of users. The 'current' query parameter specifies the page number (default is 1), and the 'size' query parameter specifies the number of records per page (default is 10).")
    public ApiResult<IPage<UserInfo>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        IPage<UserInfo> page = userInfoMapper.selectUserPage(new Page<>(current, size));
        return ApiResult.success(page);
    }
}

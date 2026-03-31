package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;

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
public class UserInfoController {

    private final UserInfoMapper userInfoMapper;

    public UserInfoController(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    /** 新增用户 —— BaseMapper.insert() */
    @PostMapping
    public ApiResult<UserInfo> create(@RequestBody UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
        return ApiResult.success(userInfo);
    }

    /** 根据 id 查询用户 —— BaseMapper.selectById() */
    @GetMapping("/{id}")
    public ApiResult<UserInfo> getById(@PathVariable Long id) {
        return ApiResult.success(userInfoMapper.selectById(id));
    }

    /** 逻辑删除用户 —— BaseMapper.deleteById() (配合 @TableLogic 自动逻辑删除) */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        userInfoMapper.deleteById(id);
        return ApiResult.success();
    }

    /** 更新用户信息 —— XML: updateUserById (动态 SQL, 只更新非空字段) */
    @PutMapping("/{id}")
    public ApiResult<UserInfo> update(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        userInfo.setId(id);
        userInfoMapper.updateUserById(userInfo);
        return ApiResult.success(userInfo);
    }

    /** 查询全部用户 —— XML: selectAllUsers */
    @GetMapping
    public ApiResult<List<UserInfo>> list() {
        return ApiResult.success(userInfoMapper.selectAllUsers());
    }

    /** 分页查询用户 —— XML: selectUserPage (MyBatis Plus 拦截器自动分页) */
    @GetMapping("/page")
    public ApiResult<IPage<UserInfo>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        IPage<UserInfo> page = userInfoMapper.selectUserPage(new Page<>(current, size));
        return ApiResult.success(page);
    }
}

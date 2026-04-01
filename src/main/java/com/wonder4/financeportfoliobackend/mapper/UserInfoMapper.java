package com.wonder4.financeportfoliobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.entity.UserInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserInfo Mapper
 *
 * <p>BaseMapper 方法: insert(), selectById(), deleteById() XML 自定义 SQL: updateUserById(),
 * selectAllUsers(), selectUserPage()
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /** XML: 根据 id 更新用户信息 */
    int updateUserById(@Param("entity") UserInfo userInfo);

    /** XML: 查询全部用户 */
    List<UserInfo> selectAllUsers();

    /** XML: 分页查询用户 */
    IPage<UserInfo> selectUserPage(IPage<UserInfo> page);
}

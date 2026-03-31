package com.wonder4.financeportfoliobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.entity.dto.PortfolioOverviewDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserHolding Mapper
 *
 * <p>BaseMapper 方法: insert(), selectById(), selectPage() XML 自定义 SQL: deleteHoldingById(),
 * updateHoldingById(), selectByUserId(), selectAllHoldings()
 */
@Mapper
public interface UserHoldingMapper extends BaseMapper<UserHolding> {

    /** XML: 逻辑删除持仓 (UPDATE is_deleted = 1) */
    int deleteHoldingById(@Param("id") Long id);

    /** XML: 根据 id 更新持仓信息 */
    int updateHoldingById(@Param("entity") UserHolding userHolding);

    /** XML: 根据 userId 查询持仓列表 */
    List<UserHolding> selectByUserId(@Param("userId") Long userId);

    /** XML: 查询全部持仓 */
    List<UserHolding> selectAllHoldings();

    /** XML: 获取总资产和总成本聚合 */
    PortfolioOverviewDTO getPortfolioOverviewStats(@Param("userId") Long userId);
}

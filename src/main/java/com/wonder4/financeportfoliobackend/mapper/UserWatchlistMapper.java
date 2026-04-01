package com.wonder4.financeportfoliobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wonder4.financeportfoliobackend.entity.UserWatchlist;
import com.wonder4.financeportfoliobackend.entity.vo.UserWatchlistVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserWatchlistMapper extends BaseMapper<UserWatchlist> {
    
    /**
     * Join user_watchlist and asset_info to get the full watchlist details.
     * Supports optional fuzzy keyword matching against symbol or full base name.
     */
    List<UserWatchlistVO> selectWatchlistWithAssetInfo(
            @Param("userId") Long userId, 
            @Param("keyword") String keyword);
}

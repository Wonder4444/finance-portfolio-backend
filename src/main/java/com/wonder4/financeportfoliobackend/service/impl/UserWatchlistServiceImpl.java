package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.entity.UserWatchlist;
import com.wonder4.financeportfoliobackend.entity.vo.UserWatchlistVO;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;
import com.wonder4.financeportfoliobackend.mapper.UserWatchlistMapper;
import com.wonder4.financeportfoliobackend.service.UserWatchlistService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserWatchlistServiceImpl implements UserWatchlistService {

    private final UserWatchlistMapper userWatchlistMapper;
    private final UserInfoMapper userInfoMapper;
    private final AssetInfoMapper assetInfoMapper;

    @Override
    public UserWatchlist addToWatchlist(Long userId, Long assetId) {
        // Validation: user must exist
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "Target user not found");
        }

        // Validation: asset must exist
        AssetInfo asset = assetInfoMapper.selectById(assetId);
        if (asset == null) {
            throw new BusinessException(404, "Target asset not found");
        }

        // Check for existing watch record to prevent duplicates
        LambdaQueryWrapper<UserWatchlist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserWatchlist::getUserId, userId).eq(UserWatchlist::getAssetId, assetId);

        UserWatchlist existing = userWatchlistMapper.selectOne(queryWrapper);
        if (existing != null) {
            // Already watchlisted, safe return
            return existing;
        }

        UserWatchlist newWatch = new UserWatchlist();
        newWatch.setUserId(userId);
        newWatch.setAssetId(assetId);

        userWatchlistMapper.insert(newWatch);
        return newWatch;
    }

    @Override
    public void removeFromWatchlist(Long id) {
        UserWatchlist existing = userWatchlistMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(404, "Watchlist record not found");
        }
        // Custom update to set is_deleted = id for soft delete to free up unique constraint (user_id, asset_id, 0)
        existing.setIsDeleted(id);
        userWatchlistMapper.updateById(existing);
    }

    @Override
    public List<UserWatchlistVO> getUserWatchlist(Long userId, String keyword) {
        // Validate user existence early
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "Target user not found");
        }

        // Let the XML join query handle fuzzy param seamlessly
        return userWatchlistMapper.selectWatchlistWithAssetInfo(userId, keyword);
    }
}

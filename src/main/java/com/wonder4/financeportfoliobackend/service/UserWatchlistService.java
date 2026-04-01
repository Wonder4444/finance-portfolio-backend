package com.wonder4.financeportfoliobackend.service;

import com.wonder4.financeportfoliobackend.entity.UserWatchlist;
import com.wonder4.financeportfoliobackend.entity.vo.UserWatchlistVO;

import java.util.List;

public interface UserWatchlistService {

    /** Add asset to watchlist */
    UserWatchlist addToWatchlist(Long userId, Long assetId);

    /** Remove asset from watchlist by watchlist record ID */
    void removeFromWatchlist(Long id);

    /** Get user's full watchlist, with optional keyword for fuzzy searching */
    List<UserWatchlistVO> getUserWatchlist(Long userId, String keyword);
}

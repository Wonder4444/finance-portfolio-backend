package com.wonder4.financeportfoliobackend.entity.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserWatchlistVO {

    private Long id;          // The user_watchlist record ID
    private Long userId;      // The user who favorited it
    private Long assetId;     // The asset_info ID
    
    // Joined fields from asset_info
    private String symbol;
    private String fullName;
    private String assetType;
    private BigDecimal currentPrice;
    
    private LocalDateTime createTime; // Time it was watchlisted
}

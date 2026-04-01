package com.wonder4.financeportfoliobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHoldingWithInfoDTO {
    private Long id;
    private Long userId;
    private Long assetId;
    private BigDecimal quantity;
    private BigDecimal avgCost;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // fields from AssetInfo
    private String symbol;
    private String fullName;
    private String assetType;
    private BigDecimal currentPrice;
}

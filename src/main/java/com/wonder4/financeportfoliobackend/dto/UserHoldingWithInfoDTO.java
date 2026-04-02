package com.wonder4.financeportfoliobackend.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserHoldingWithInfoDTO {

    private BigDecimal quantity;
    private BigDecimal avgCost;

    // fields from AssetInfo
    private String symbol;
    private String fullName;
    private String assetType;
}

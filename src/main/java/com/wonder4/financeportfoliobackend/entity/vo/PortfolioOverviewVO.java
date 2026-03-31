package com.wonder4.financeportfoliobackend.entity.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Portfolio Overview Metrics")
public class PortfolioOverviewVO {

    @Schema(description = "Total Portfolio Balance", example = "4892.96")
    private BigDecimal totalBalance;

    @Schema(description = "Total Profit/Loss", example = "601.65")
    private BigDecimal totalProfitLoss;

    @Schema(description = "Return Rate percentage (e.g. 14.02 for 14.02%)", example = "14.02")
    private BigDecimal returnRate;
}

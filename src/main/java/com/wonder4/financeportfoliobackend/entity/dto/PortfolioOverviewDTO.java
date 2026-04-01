package com.wonder4.financeportfoliobackend.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PortfolioOverviewDTO {
    // Aggregated balance = sum(quantity * current_price)
    private BigDecimal totalBalance;
    // Aggregated cost = sum(quantity * avg_cost)
    private BigDecimal totalCost;
}

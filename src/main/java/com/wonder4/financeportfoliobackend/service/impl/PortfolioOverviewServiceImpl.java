package com.wonder4.financeportfoliobackend.service.impl;

import com.wonder4.financeportfoliobackend.entity.dto.PortfolioOverviewDTO;
import com.wonder4.financeportfoliobackend.entity.vo.PortfolioOverviewVO;
import com.wonder4.financeportfoliobackend.mapper.UserHoldingMapper;
import com.wonder4.financeportfoliobackend.service.PortfolioOverviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioOverviewServiceImpl implements PortfolioOverviewService {

    private final UserHoldingMapper userHoldingMapper;

    @Override
    public PortfolioOverviewVO getOverview(Long userId) {
        log.info("Getting portfolio overview for user ID: {}", userId);

        PortfolioOverviewDTO stats = userHoldingMapper.getPortfolioOverviewStats(userId);

        BigDecimal totalBalance = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        if (stats != null) {
            totalBalance =
                    stats.getTotalBalance() != null ? stats.getTotalBalance() : BigDecimal.ZERO;
            totalCost = stats.getTotalCost() != null ? stats.getTotalCost() : BigDecimal.ZERO;
        }

        BigDecimal profitLoss = totalBalance.subtract(totalCost);
        BigDecimal returnRate = BigDecimal.ZERO;
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            // (profit / total_cost) * 100
            returnRate =
                    profitLoss
                            .divide(totalCost, 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal("100"));
        }

        log.info("For user ID:{}, calculated total balance: {}, total cost: {}, calculated profit/loss: {}, return rate: {}%",
                userId, totalBalance, totalCost, profitLoss, returnRate);

        return PortfolioOverviewVO.builder()
                .totalBalance(totalBalance.setScale(2, RoundingMode.HALF_UP))
                .totalProfitLoss(profitLoss.setScale(2, RoundingMode.HALF_UP))
                .returnRate(returnRate.setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}

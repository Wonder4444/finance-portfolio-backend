package com.wonder4.financeportfoliobackend.service;

import com.wonder4.financeportfoliobackend.entity.vo.PortfolioOverviewVO;

public interface PortfolioOverviewService {
    PortfolioOverviewVO getOverview(Long userId);
}

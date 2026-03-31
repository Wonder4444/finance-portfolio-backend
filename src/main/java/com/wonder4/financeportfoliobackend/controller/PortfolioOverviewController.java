package com.wonder4.financeportfoliobackend.controller;

import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.vo.PortfolioOverviewVO;
import com.wonder4.financeportfoliobackend.service.PortfolioOverviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/overview")
@RequiredArgsConstructor
@Tag(
        name = "Portfolio Overview APIs",
        description = "Endpoints for portfolio overview metrics (Balance, Profit, Return Rate)")
public class PortfolioOverviewController {

    private final PortfolioOverviewService portfolioOverviewService;

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get User Portfolio Overview",
            description =
                    "Calculates and returns total balance, total profit/loss, and return rate based on the user's holdings.")
    public ApiResult<PortfolioOverviewVO> getOverview(
            @Parameter(description = "User ID", required = true) @PathVariable("userId")
                    Long userId) {
        PortfolioOverviewVO vo = portfolioOverviewService.getOverview(userId);
        return ApiResult.success(vo);
    }
}

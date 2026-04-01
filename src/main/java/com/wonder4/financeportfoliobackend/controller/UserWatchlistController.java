package com.wonder4.financeportfoliobackend.controller;

import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.UserWatchlist;
import com.wonder4.financeportfoliobackend.entity.vo.UserWatchlistVO;
import com.wonder4.financeportfoliobackend.service.UserWatchlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(
        name = "UserWatchlistController",
        description = "Watchlist / Favorites api for frontend dashboard")
public class UserWatchlistController {

    private final UserWatchlistService userWatchlistService;

    @PostMapping
    @Operation(summary = "Add an asset to user's watchlist")
    public ApiResult<UserWatchlist> add(@RequestParam Long userId, @RequestParam Long assetId) {
        return ApiResult.success(userWatchlistService.addToWatchlist(userId, assetId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove an asset from watchlist by record ID")
    public ApiResult<Void> remove(@PathVariable Long id) {
        userWatchlistService.removeFromWatchlist(id);
        return ApiResult.success();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user's watchlist with optional fuzzy search by symbol or name")
    public ApiResult<List<UserWatchlistVO>> getUserWatchlist(
            @PathVariable Long userId, @RequestParam(required = false) String keyword) {
        return ApiResult.success(userWatchlistService.getUserWatchlist(userId, keyword));
    }
}

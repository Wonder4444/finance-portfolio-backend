package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.service.AssetInfoService;
import com.wonder4.financeportfoliobackend.task.AssetSyncTask;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 资产信息 Controller — 标准三层架构 Controller */
@RestController
@RequestMapping("/api/assets")
@Tag(name = "AssetInfoController", description = "Assets related api")
public class AssetInfoController {

    private final AssetInfoService assetInfoService;
    private final AssetSyncTask assetSyncTask;

    public AssetInfoController(AssetInfoService assetInfoService, AssetSyncTask assetSyncTask) {
        this.assetInfoService = assetInfoService;
        this.assetSyncTask = assetSyncTask;
    }

    @PostMapping("/sync")
    @Operation(summary = "Manually trigger the asset sync task")
    public ApiResult<Void> triggerManualSync() {
        assetSyncTask.dailySync();
        return ApiResult.success();
    }

    @PostMapping
    @Operation(summary = "Create a new asset")
    public ApiResult<AssetInfo> create(@RequestBody AssetInfo assetInfo) {
        return ApiResult.success(assetInfoService.create(assetInfo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public ApiResult<AssetInfo> getById(@PathVariable Long id) {
        return ApiResult.success(assetInfoService.getById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset by ID")
    public ApiResult<Void> delete(@PathVariable Long id) {
        assetInfoService.delete(id);
        return ApiResult.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset by ID")
    public ApiResult<AssetInfo> update(@PathVariable Long id, @RequestBody AssetInfo assetInfo) {
        return ApiResult.success(assetInfoService.update(id, assetInfo));
    }

    @GetMapping
    @Operation(summary = "List all assets")
    public ApiResult<List<AssetInfo>> list() {
        return ApiResult.success(assetInfoService.list());
    }

    @GetMapping("/page")
    @Operation(summary = "Paginate assets")
    public ApiResult<IPage<AssetInfo>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        return ApiResult.success(assetInfoService.page(current, size));
    }
}

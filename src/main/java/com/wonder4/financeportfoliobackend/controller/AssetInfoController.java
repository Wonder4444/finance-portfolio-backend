package com.wonder4.financeportfoliobackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.ApiResult;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产信息 Controller — 直接使用 Mapper 层
 *
 * <p>使用 BaseMapper 方法: deleteById, updateById, selectList 使用 XML 自定义 SQL: insertAsset,
 * selectAssetById, selectAssetPage
 */
@RestController
@RequestMapping("/api/assets")
public class AssetInfoController {

    private final AssetInfoMapper assetInfoMapper;

    public AssetInfoController(AssetInfoMapper assetInfoMapper) {
        this.assetInfoMapper = assetInfoMapper;
    }

    /** 新增资产 —— XML: insertAsset (演示 XML insert + 主键回填) */
    @PostMapping
    public ApiResult<AssetInfo> create(@RequestBody AssetInfo assetInfo) {
        assetInfoMapper.insertAsset(assetInfo);
        return ApiResult.success(assetInfo);
    }

    /** 根据 id 查询资产 —— XML: selectAssetById */
    @GetMapping("/{id}")
    public ApiResult<AssetInfo> getById(@PathVariable Long id) {
        return ApiResult.success(assetInfoMapper.selectAssetById(id));
    }

    /** 逻辑删除资产 —— BaseMapper.deleteById() (配合 @TableLogic) */
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable Long id) {
        assetInfoMapper.deleteById(id);
        return ApiResult.success();
    }

    /** 更新资产信息 —— BaseMapper.updateById() */
    @PutMapping("/{id}")
    public ApiResult<AssetInfo> update(@PathVariable Long id, @RequestBody AssetInfo assetInfo) {
        assetInfo.setId(id);
        assetInfoMapper.updateById(assetInfo);
        return ApiResult.success(assetInfo);
    }

    /** 查询全部资产 —— BaseMapper.selectList() + QueryWrapper */
    @GetMapping
    public ApiResult<List<AssetInfo>> list() {
        return ApiResult.success(assetInfoMapper.selectList(new QueryWrapper<>()));
    }

    /** 分页查询资产 —— XML: selectAssetPage */
    @GetMapping("/page")
    public ApiResult<IPage<AssetInfo>> page(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        IPage<AssetInfo> page = assetInfoMapper.selectAssetPage(new Page<>(current, size));
        return ApiResult.success(page);
    }
}

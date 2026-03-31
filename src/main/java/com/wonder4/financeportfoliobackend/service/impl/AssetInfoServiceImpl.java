package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;
import com.wonder4.financeportfoliobackend.service.AssetInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssetInfoServiceImpl implements AssetInfoService {

    private final AssetInfoMapper assetInfoMapper;

    public AssetInfoServiceImpl(AssetInfoMapper assetInfoMapper) {
        this.assetInfoMapper = assetInfoMapper;
    }

    @Override
    public AssetInfo create(AssetInfo assetInfo) {
        log.info("Creating AssetInfo: {}", assetInfo);

        assetInfoMapper.insertAsset(assetInfo);
        return assetInfo;
    }

    @Override
    public AssetInfo getById(Long id) {
        log.info("Getting AssetInfo by ID: {}", id);

        AssetInfo assetInfo = assetInfoMapper.selectAssetById(id);
        if (assetInfo == null) {
            throw new BusinessException(404, "Asset not found with ID: " + id);
        }
        return assetInfo;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting AssetInfo by ID: {}", id);

        int rows = assetInfoMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(404, "Cannot delete. Asset not found with ID: " + id);
        }
    }

    @Override
    public AssetInfo update(Long id, AssetInfo assetInfo) {
        log.info("Updating AssetInfo with ID: {}, Data: {}", id, assetInfo);

        AssetInfo existing = assetInfoMapper.selectAssetById(id);
        if (existing == null) {
            throw new BusinessException(404, "Cannot update. Asset not found with ID: " + id);
        }
        assetInfo.setId(id);
        assetInfoMapper.updateById(assetInfo);
        return assetInfo;
    }

    @Override
    public List<AssetInfo> list() {
        log.info("Listing all AssetInfo records");

        return assetInfoMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public IPage<AssetInfo> page(long current, long size) {
        log.info("Paginating AssetInfo records: current={}, size={}", current, size);

        return assetInfoMapper.selectAssetPage(new Page<>(current, size));
    }
}

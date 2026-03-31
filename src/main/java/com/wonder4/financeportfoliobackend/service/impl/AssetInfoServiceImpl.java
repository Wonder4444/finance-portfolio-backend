package com.wonder4.financeportfoliobackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wonder4.financeportfoliobackend.common.BusinessException;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;
import com.wonder4.financeportfoliobackend.service.AssetInfoService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetInfoServiceImpl implements AssetInfoService {

    private final AssetInfoMapper assetInfoMapper;

    public AssetInfoServiceImpl(AssetInfoMapper assetInfoMapper) {
        this.assetInfoMapper = assetInfoMapper;
    }

    @Override
    public AssetInfo create(AssetInfo assetInfo) {
        assetInfoMapper.insertAsset(assetInfo);
        return assetInfo;
    }

    @Override
    public AssetInfo getById(Long id) {
        AssetInfo assetInfo = assetInfoMapper.selectAssetById(id);
        if (assetInfo == null) {
            throw new BusinessException(404, "Asset not found with ID: " + id);
        }
        return assetInfo;
    }

    @Override
    public void delete(Long id) {
        int rows = assetInfoMapper.deleteById(id);
        if (rows == 0) {
            throw new BusinessException(404, "Cannot delete. Asset not found with ID: " + id);
        }
    }

    @Override
    public AssetInfo update(Long id, AssetInfo assetInfo) {
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
        return assetInfoMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public IPage<AssetInfo> page(long current, long size) {
        return assetInfoMapper.selectAssetPage(new Page<>(current, size));
    }
}

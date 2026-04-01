package com.wonder4.financeportfoliobackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;

import java.util.List;

public interface AssetInfoService {

    AssetInfo create(AssetInfo assetInfo);

    AssetInfo getById(Long id);

    void delete(Long id);

    AssetInfo update(Long id, AssetInfo assetInfo);

    List<AssetInfo> list();

    IPage<AssetInfo> page(long current, long size);

    List<AssetInfo> searchList(String keyword);
}

package com.wonder4.financeportfoliobackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wonder4.financeportfoliobackend.entity.AssetInfo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * AssetInfo Mapper
 *
 * <p>BaseMapper 方法: deleteById(), updateById(), selectList() XML 自定义 SQL: insertAsset(),
 * selectAssetById(), selectAssetPage()
 */
@Mapper
public interface AssetInfoMapper extends BaseMapper<AssetInfo> {

    /** XML: 新增资产 (演示 XML insert 写法) */
    int insertAsset(@Param("entity") AssetInfo assetInfo);

    /** XML: 根据 id 查询资产 (演示 XML select 写法) */
    AssetInfo selectAssetById(@Param("id") Long id);

    /** XML: 分页查询资产 */
    IPage<AssetInfo> selectAssetPage(IPage<AssetInfo> page);

    /** XML: 批量新增或更新资产 (以 symbol 为唯一键) */
    int insertOrUpdateBatch(@Param("list") java.util.List<AssetInfo> assetInfoList);

    /** XML: 全局模糊搜索资产 (限制 50 条) */
    java.util.List<AssetInfo> searchAssets(@Param("keyword") String keyword);
}

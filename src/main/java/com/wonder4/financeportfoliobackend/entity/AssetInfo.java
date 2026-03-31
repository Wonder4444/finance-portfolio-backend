package com.wonder4.financeportfoliobackend.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("asset_info")
public class AssetInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String symbol;

    private String fullName;

    private String assetType;

    private BigDecimal currentPrice;

    @TableLogic private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

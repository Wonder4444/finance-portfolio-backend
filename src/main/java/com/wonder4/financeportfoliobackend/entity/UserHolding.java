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
@TableName("user_holding")
public class UserHolding {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long assetId;

    private BigDecimal quantity;

    private BigDecimal avgCost;

    @TableLogic private Long isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

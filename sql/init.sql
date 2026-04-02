-- Disable foreign key checks for clean execution
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Table: user_info
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary Key: Unique internal ID',
                             `user_name` VARCHAR(64) NOT NULL COMMENT 'User display name',
                             `email` VARCHAR(128) NOT NULL COMMENT 'User email for login',
                             `base_currency` VARCHAR(10) DEFAULT 'USD' COMMENT 'Display currency (e.g., USD, EUR)',
                             `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete: 0=active, 1=deleted',
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
                             `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update time',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User account information';

-- 2. Table: asset_info
DROP TABLE IF EXISTS `asset_info`;
CREATE TABLE `asset_info` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary Key: Internal asset ID',
                              `symbol` VARCHAR(32) NOT NULL COMMENT 'Ticker symbol (e.g., AAPL, BTC, NVDA)',
                              `full_name` VARCHAR(128) NOT NULL COMMENT 'Full name of the asset',
                              `asset_type` VARCHAR(32) NOT NULL COMMENT 'Category (e.g., STOCK, CRYPTO)',
                              `current_price` DECIMAL(24, 10) NOT NULL DEFAULT '0.0000000000' COMMENT 'Latest market price',
                              `market_cap` DECIMAL(24, 2) DEFAULT NULL COMMENT '市值',
                              `change_percent` DECIMAL(10, 4) DEFAULT NULL COMMENT '日内涨跌幅(%)',
                              `pe_ratio` DECIMAL(10, 4) DEFAULT NULL COMMENT '市盈率(PE)',
                              `ps_ratio` DECIMAL(10, 4) DEFAULT NULL COMMENT '市销率(PS)',
                              `pb_ratio` DECIMAL(10, 4) DEFAULT NULL COMMENT '市净率(PB)',
                              `industry` VARCHAR(128) DEFAULT NULL COMMENT '行业',
                              `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete: 0=active, 1=deleted',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation time',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Last price update time',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_symbol` (`symbol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Asset master data and pricing';

-- 3. Table: user_holding
DROP TABLE IF EXISTS `user_holding`;
CREATE TABLE `user_holding` (
                                `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary Key: Holding record ID',
                                `user_id` BIGINT NOT NULL COMMENT 'Logical FK: Reference to user_info.id',
                                `asset_id` BIGINT NOT NULL COMMENT 'Logical FK: Reference to asset_info.id',
                                `quantity` DECIMAL(24, 10) NOT NULL DEFAULT '0.0000000000' COMMENT 'Total amount held (maps to AMOUNT in UI)',
                                `avg_cost` DECIMAL(24, 10) NOT NULL DEFAULT '0.0000000000' COMMENT 'Average purchase price',
                                `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete: 0=active, 1=deleted',
                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
                                PRIMARY KEY (`id`),
                                INDEX `idx_user_id` (`user_id`) COMMENT 'Performance index for user portfolio lookup',
                                UNIQUE KEY `uk_user_asset` (`user_id`, `asset_id`) COMMENT 'Ensures unique entry per asset per user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User asset holdings and cost basis';

-- 4. Table: user_watchlist
DROP TABLE IF EXISTS `user_watchlist`;
CREATE TABLE `user_watchlist` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Primary Key: Watchlist record ID',
                                  `user_id` BIGINT NOT NULL COMMENT 'Logical FK: Reference to user_info.id',
                                  `asset_id` BIGINT NOT NULL COMMENT 'Logical FK: Reference to asset_info.id',
                                  `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete: 0=active, 1=deleted',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
                                  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
                                  PRIMARY KEY (`id`),
                                  INDEX `idx_user_id` (`user_id`) COMMENT 'Performance index for user watchlist lookup',
                                  UNIQUE KEY `uk_user_asset_watch` (`user_id`, `asset_id`) COMMENT 'Ensures unique entry per asset per user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User asset watchlist';


SET FOREIGN_KEY_CHECKS = 1;
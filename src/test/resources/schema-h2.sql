-- H2 compatible schema for testing (MySQL MODE)
DROP TABLE IF EXISTS user_holding;
DROP TABLE IF EXISTS asset_info;
DROP TABLE IF EXISTS user_info;

CREATE TABLE user_info
(
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    user_name     VARCHAR(64)  NOT NULL,
    email         VARCHAR(128) NOT NULL,
    base_currency VARCHAR(10)           DEFAULT 'USD',
    is_deleted    TINYINT      NOT NULL DEFAULT 0,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (email)
);

CREATE TABLE asset_info
(
    id            BIGINT          NOT NULL AUTO_INCREMENT,
    symbol        VARCHAR(32)     NOT NULL,
    full_name     VARCHAR(128)    NOT NULL,
    asset_type    VARCHAR(32)     NOT NULL,
    current_price DECIMAL(24, 10) NOT NULL DEFAULT 0,
    is_deleted    TINYINT         NOT NULL DEFAULT 0,
    create_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (symbol)
);

CREATE TABLE user_holding
(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     BIGINT          NOT NULL,
    asset_id    BIGINT          NOT NULL,
    quantity    DECIMAL(24, 10) NOT NULL DEFAULT 0,
    avg_cost    DECIMAL(24, 10) NOT NULL DEFAULT 0,
    is_deleted  TINYINT         NOT NULL DEFAULT 0,
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE (user_id, asset_id)
);

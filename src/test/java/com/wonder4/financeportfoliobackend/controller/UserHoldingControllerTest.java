package com.wonder4.financeportfoliobackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.entity.UserHolding;
import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;
import com.wonder4.financeportfoliobackend.mapper.UserHoldingMapper;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserHoldingControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserHoldingMapper userHoldingMapper;

    private static Long holdingId;
    private static Long userId;
    private static Long assetId;

    @BeforeAll
    static void setUp(
            @Autowired UserInfoMapper userInfoMapper, @Autowired AssetInfoMapper assetInfoMapper) {
        // 创建前置数据：用户和资产
        UserInfo user = new UserInfo();
        user.setUserName("Bob");
        user.setEmail("bob-holding-test@example.com");
        userInfoMapper.insert(user);
        userId = user.getId();

        AssetInfo asset = new AssetInfo();
        asset.setSymbol("BTC-HOLD-TEST");
        asset.setFullName("Bitcoin");
        asset.setAssetType("CRYPTO");
        asset.setCurrentPrice(new BigDecimal("60000"));
        assetInfoMapper.insertAsset(asset);
        assetId = asset.getId();
    }

    @Test
    @Order(1)
    void testCreate() throws Exception {
        UserHolding holding = new UserHolding();
        holding.setUserId(userId);
        holding.setAssetId(assetId);
        holding.setQuantity(new BigDecimal("2.5"));
        holding.setAvgCost(new BigDecimal("55000"));

        mockMvc.perform(
                        post("/api/holdings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(holding)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        holdingId = userHoldingMapper.selectList(null).get(0).getId();
    }

    @Test
    @Order(2)
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/holdings/" + holdingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.assetId").value(assetId));
    }

    @Test
    @Order(3)
    void testListByUserId() throws Exception {
        mockMvc.perform(get("/api/holdings/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @Order(4)
    void testList() throws Exception {
        mockMvc.perform(get("/api/holdings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @Order(5)
    void testUpdate() throws Exception {
        UserHolding holding = new UserHolding();
        holding.setQuantity(new BigDecimal("5.0"));
        holding.setAvgCost(new BigDecimal("57000"));

        mockMvc.perform(
                        put("/api/holdings/" + holdingId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(holding)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/holdings/" + holdingId))
                .andExpect(jsonPath("$.data.quantity").value(5.0));
    }

    @Test
    @Order(6)
    void testPage() throws Exception {
        mockMvc.perform(get("/api/holdings/page").param("current", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    @Order(7)
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/holdings/" + holdingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // XML deleteHoldingById 手动 UPDATE is_deleted=1,
        // 之后 BaseMapper.selectById 因 @TableLogic 自动过滤已删除记录
        mockMvc.perform(get("/api/holdings/" + holdingId)).andExpect(jsonPath("$.data").isEmpty());
    }
}

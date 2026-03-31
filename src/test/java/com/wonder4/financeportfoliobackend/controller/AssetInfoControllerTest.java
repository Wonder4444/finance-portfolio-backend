package com.wonder4.financeportfoliobackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.wonder4.financeportfoliobackend.entity.AssetInfo;
import com.wonder4.financeportfoliobackend.mapper.AssetInfoMapper;

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
class AssetInfoControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private AssetInfoMapper assetInfoMapper;

    private static Long createdId;

    @Test
    @Order(1)
    void testCreate() throws Exception {
        AssetInfo asset = new AssetInfo();
        asset.setSymbol("AAPL");
        asset.setFullName("Apple Inc.");
        asset.setAssetType("STOCK");
        asset.setCurrentPrice(new BigDecimal("150.50"));

        mockMvc.perform(
                        post("/api/assets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.symbol").value("AAPL"))
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        createdId = assetInfoMapper.selectList(null).get(0).getId();
    }

    @Test
    @Order(2)
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/assets/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.symbol").value("AAPL"))
                .andExpect(jsonPath("$.data.fullName").value("Apple Inc."));
    }

    @Test
    @Order(3)
    void testList() throws Exception {
        mockMvc.perform(get("/api/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @Order(4)
    void testUpdate() throws Exception {
        AssetInfo asset = new AssetInfo();
        asset.setCurrentPrice(new BigDecimal("175.00"));

        mockMvc.perform(
                        put("/api/assets/" + createdId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(asset)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/assets/" + createdId))
                .andExpect(jsonPath("$.data.currentPrice").value(175.0));
    }

    @Test
    @Order(5)
    void testPage() throws Exception {
        mockMvc.perform(get("/api/assets/page").param("current", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    @Order(6)
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/assets/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/assets/" + createdId)).andExpect(jsonPath("$.data").isEmpty());
    }
}

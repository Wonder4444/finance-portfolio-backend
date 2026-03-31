package com.wonder4.financeportfoliobackend.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.wonder4.financeportfoliobackend.entity.UserInfo;
import com.wonder4.financeportfoliobackend.mapper.UserInfoMapper;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserInfoControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserInfoMapper userInfoMapper;

    private static Long createdId;

    @Test
    @Order(1)
    void testCreate() throws Exception {
        UserInfo user = new UserInfo();
        user.setUserName("Alice");
        user.setEmail("alice@example.com");
        user.setBaseCurrency("USD");

        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userName").value("Alice"))
                .andExpect(jsonPath("$.data.id").isNotEmpty());

        createdId = userInfoMapper.selectAllUsers().get(0).getId();
    }

    @Test
    @Order(2)
    void testGetById() throws Exception {
        mockMvc.perform(get("/api/users/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userName").value("Alice"));
    }

    @Test
    @Order(3)
    void testList() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @Order(4)
    void testUpdate() throws Exception {
        UserInfo user = new UserInfo();
        user.setUserName("Alice Updated");
        user.setBaseCurrency("EUR");

        mockMvc.perform(
                        put("/api/users/" + createdId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/users/" + createdId))
                .andExpect(jsonPath("$.data.userName").value("Alice Updated"))
                .andExpect(jsonPath("$.data.baseCurrency").value("EUR"));
    }

    @Test
    @Order(5)
    void testPage() throws Exception {
        mockMvc.perform(get("/api/users/page").param("current", "1").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    @Order(6)
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        mockMvc.perform(get("/api/users/" + createdId)).andExpect(jsonPath("$.data").isEmpty());
    }
}

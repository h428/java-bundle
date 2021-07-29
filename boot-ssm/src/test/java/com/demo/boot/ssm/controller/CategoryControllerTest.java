package com.demo.boot.ssm.controller;

import com.demo.base.component.util.JwtUtil;
import com.demo.boot.ssm.BaseWebTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;

public class CategoryControllerTest extends BaseWebTest {

    String token = JwtUtil.generateToken("1");

    @Test
    public void add() {



    }

    @Test
    public void importData() {
    }

    @Test
    public void add1() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void subCategoryNum() {
    }

    @Test
    public void checkDelete() {
    }

    @Test
    public void update() {
    }

    @Test
    public void get() throws Exception{
        String url = "/category/{id}";

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get(url, 101)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("草系"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pid").value("1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void list() {
    }
}
package com.hao.bundle.demo.web.controller;

import static org.junit.Assert.*;
import com.hao.bundle.demo.BaseWebTest;
import com.hao.bundle.demo.pojo.dto.CategoryDto;
import org.junit.Test;

public class CategoryControllerTest extends BaseWebTest {

    private final String prefix = "/category/";

    @Test
    public void getTest() {
        final String url = prefix + "1";
//        Response<CategoryDto> categoryDto = get(url).token(catToken).execute()
//            .validateResponse()
//            .print()
//            .getBean(Response.class);
//        assertEquals("经典系", categoryDto.getName());
    }
}
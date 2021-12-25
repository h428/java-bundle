package com.hao.bundle.demo.common.util;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.constant.EntityAdd;
import com.hao.bundle.demo.extra.entity.User;
import org.junit.Test;

public class ValidationUtilTest extends BaseTest {

    @Test
    public void test() {
        User user = new User();
        user.setId(1L);
        user.setEmail("da");
        ValidationUtil.validate(user, EntityAdd.class);
    }

}
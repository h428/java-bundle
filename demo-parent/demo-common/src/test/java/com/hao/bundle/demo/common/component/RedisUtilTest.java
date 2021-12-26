package com.hao.bundle.demo.common.component;

import com.hao.bundle.demo.BaseTest;
import com.hao.bundle.demo.pojo.dto.Foo;
import com.hao.bundle.demo.common.util.EntityUtil;
import com.hao.bundle.demo.common.util.JacksonUtil;
import org.junit.Test;

public class RedisUtilTest extends BaseTest {

    @Test
    public void test() {
        Foo foo = EntityUtil.generateRandomOne(Foo.class);
        RedisUtil.put("foo", foo);
        Foo read = RedisUtil.get("foo");
        System.out.println(JacksonUtil.toJson(read));
    }

    @Test
    public void t2() {
        RedisUtil.put("str", "nice try");
        String str = RedisUtil.get("str");
        System.out.println(str);
    }


}
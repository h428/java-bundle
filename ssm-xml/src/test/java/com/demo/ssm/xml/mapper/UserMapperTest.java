package com.demo.ssm.xml.mapper;


import com.demo.base.component.mapper2.AddUserMapper;
import com.demo.ssm.xml.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class UserMapperTest extends BaseTest {


    @Autowired
    private AddUserMapper addUserMapper;

    @Test
    public void test() {
        System.out.println(this.addUserMapper.queryPermListByRoleId(2L));
    }


}

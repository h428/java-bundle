package com.demo.ssm.xml.service;

import com.demo.base.component.base.BaseService;
import com.demo.base.component.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserService extends BaseService<User> {

    @Override
    public User getById(Object id) {
        return super.getById(id);
    }

}

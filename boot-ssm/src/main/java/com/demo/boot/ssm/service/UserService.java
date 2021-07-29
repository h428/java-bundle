package com.demo.boot.ssm.service;

import com.demo.base.component.base.BaseService;
import com.demo.base.component.entity.User;
import com.demo.base.component.mapper2.AddUserMapper;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> {

    @Autowired
    private AddUserMapper addUserMapper;

    public boolean queryExistUserName(String userName) {
        // 有一条记录说明已存在
        return this.mapper.selectCount(User.builder().userName(userName).build()) == 1;
    }

    public boolean queryExistEmail(String email) {
        // 有一条记录说明已存在
        return this.mapper.selectCount(User.builder().email(email).build()) == 1;
    }

    @Override
    public String queryUniqueBeforeSave(User entity) {

        String email = entity.getEmail();

        if (StringUtils.isNotBlank(email) && this.queryExistEmail(email)) {
            return "邮箱 " + email + " 已被使用";
        }

        String userName = entity.getUserName();
        if (StringUtils.isNoneBlank(userName) && this.queryExistUserName(userName)) {
            return "用户名 " + userName + " 已被占用";
        }

        return null;
    }

    @Override
    public String queryUniqueBeforeUpdate(User entity) {
        // 和保存时保持一致，
        return this.queryUniqueBeforeSave(entity);
    }

    @Override
    public User getById(Object id) {
        return super.getById(id);
    }

    public Set<String> queryStringPermission(Long userId) {
        return this.addUserMapper.queryStringPermSetByUserId(userId, true);
    }
}

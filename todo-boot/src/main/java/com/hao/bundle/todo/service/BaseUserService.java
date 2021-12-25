package com.hao.bundle.todo.service;

import cn.hutool.core.bean.BeanUtil;
import com.hao.bundle.todo.bean.dto.BaseUserDTO;
import com.hao.bundle.todo.bean.dto.BaseUserRegisterDTO;
import com.hao.bundle.todo.bean.dto.BaseUserUpdateDTO;
import com.hao.bundle.todo.bean.entity.BaseUserEntity;
import com.hao.bundle.todo.repository.BaseUserDao;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BaseUserService {

    private static final Class<BaseUserEntity> ENTITY_CLASS = BaseUserEntity.class;

    @Autowired
    private BaseUserDao baseUserDao;

    /**
     * 保存用户，用于用户注册
     *
     * @param baseUserRegisterDTO 用户相关信息
     * @return 用户信息
     */
    public BaseUserDTO register(BaseUserRegisterDTO baseUserRegisterDTO) {
        BaseUserEntity baseUserEntity = BeanUtil.copyProperties(baseUserRegisterDTO, ENTITY_CLASS);
        Date now = new Date(System.currentTimeMillis());
        baseUserEntity.setLastUpdateTime(now);
        baseUserEntity.setRegisterTime(now);
        return BeanUtil.copyProperties(baseUserDao.save(baseUserEntity), BaseUserDTO.class);
    }

    /**
     * 更新用户信息
     *
     * @param baseUserUpdateDTO 用户
     * @return 新用户信息
     */
    public BaseUserDTO update(BaseUserUpdateDTO baseUserUpdateDTO) {
        BaseUserEntity baseUserEntity = BeanUtil.copyProperties(baseUserUpdateDTO, ENTITY_CLASS);
        Date now = new Date(System.currentTimeMillis());
        baseUserEntity.setLastUpdateTime(now);
        return BeanUtil.copyProperties(baseUserDao.save(baseUserEntity), BaseUserDTO.class);
    }

    /**
     * 查询用户信息
     * @param id 用户 id
     * @return 用户信息
     */
    public BaseUserDTO get(Long id) {
        return BeanUtil.copyProperties(baseUserDao.getOne(id), BaseUserDTO.class);
    }

    public BaseUserDTO loginByEmail(String email, String userPass) {

        BaseUserEntity baseUser = BaseUserEntity.builder()
            .email(email)
//            .userPass(userPass)
            .build();

        Example<BaseUserEntity> example = Example.of(baseUser);

        return this.baseUserDao.findOne(example)
            .map(baseUserEntity -> BeanUtil.copyProperties(baseUserEntity, BaseUserDTO.class))
            .orElse(null);
    }

}

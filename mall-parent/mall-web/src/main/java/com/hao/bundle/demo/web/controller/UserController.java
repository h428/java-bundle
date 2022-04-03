package com.hao.bundle.demo.web.controller;

import com.hao.bundle.demo.common.aop.anno.Perm;
import com.hao.bundle.demo.common.threadlocal.UserIdThreadLocal;
import com.hao.bundle.demo.pojo.constant.PermTag;
import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;

    @GetMapping("current")
    public UserDto current() {
        Long id = UserIdThreadLocal.get();
        return userService.get(id);
    }

    @GetMapping("{id}")
    @Perm(PermTag.USER)
    public UserDto get(@PathVariable Long id) {
        return userService.get(id);
    }

}

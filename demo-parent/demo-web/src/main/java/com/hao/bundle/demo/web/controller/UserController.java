package com.hao.bundle.demo.web.controller;

import com.hao.bundle.demo.pojo.dto.UserDto;
import com.hao.bundle.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{id}")
    public UserDto get(@PathVariable Long id) {
        return userService.get(id);
    }

}

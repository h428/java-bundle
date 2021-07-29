package com.demo.common.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

// 登录成功返回的数据结果
@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessVO {

    // 状态码
    private int status;

    // 登录用的 token
    private String loginToken;

    // 登录 token 过期时用到的 token
    @NotEmpty(message = "refreshToken 不能为空") // 刷新 loginToken 时的参数校验
    private String refreshToken;

    // 此次登录的用户 id，刷新 token 时也要用到
    @NotEmpty(message = "userId 不能为空") // 刷新 loginToken 时的参数校验
    private String userId;

}

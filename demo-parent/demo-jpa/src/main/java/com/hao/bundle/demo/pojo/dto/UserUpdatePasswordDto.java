package com.hao.bundle.demo.pojo.dto;

import cn.hutool.core.util.StrUtil;
import com.hao.bundle.demo.common.exception.ParamErrorException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用于更新密码
 *
 * @author hao
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class UserUpdatePasswordDto {

    private Long id;

    private String oldPass;

    private String userPass;

    private String confirmPass;

    public void checkPasswordBusinessValid() {
        if (StrUtil.equals(oldPass, userPass)) {
            throw new ParamErrorException("旧密码不能和新密码一致");
        }

        if (!StrUtil.equals(userPass, confirmPass)) {
            throw new ParamErrorException("两次新密码输入不一致");
        }
    }



}

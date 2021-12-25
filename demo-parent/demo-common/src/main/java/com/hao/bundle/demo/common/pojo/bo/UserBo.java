package com.hao.bundle.demo.common.pojo.bo;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
public class UserBo {

    private String userPass;
    private String salt;


    /**
     * 生成 salt ，以及根据用户输入的原有密码生成加盐加密后的密码； 前置条件：password 已经设置且不为空
     */
    public void generateSaltAndPassword() {
        Assert.notEmpty(this.userPass, "生成加密密码前必须设置初始密码");

        // 生成并设置盐值
        String salt = SecureUtil.sha256(IdUtil.fastSimpleUUID());
        this.setSalt(salt);
        // 对原密码加盐 hash 后的到新密码
        String originPassword = this.userPass; // 未加密的密码
        // 新密码加密为采用：sha2(原始密码 + salt)
        this.setUserPass(SecureUtil.sha256(originPassword + salt));
    }

    /**
     * 用已有的 salt 和 password 校验输入的密码是否正确
     *
     * @param inputPassword 用户输入密码
     * @return 密码正确返回 true；否则返回 false
     */
    public boolean passwordValid(String inputPassword) {

        if (this.salt == null || this.userPass == null) {
            throw new IllegalStateException("salt 和 password 为空，无法校验输入密码的正确性");
        }

        // sha2(输入密码, salt)
        String hashPass = SecureUtil.sha256(inputPassword + this.salt);

        // 加密后的密码和原来保存的加密结果不相同，则 false
        if (!hashPass.equals(this.userPass)) {
            return false;
        }

        return true;
    }

}

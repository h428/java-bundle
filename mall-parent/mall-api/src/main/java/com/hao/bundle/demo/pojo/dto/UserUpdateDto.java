package com.hao.bundle.demo.pojo.dto;

import com.hao.bundle.demo.pojo.constant.UserConstant;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 用于更新 BaseUser
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
public class UserUpdateDto {

    private Long id;

    /**
     * 用户名
     */
    @Pattern(regexp = UserConstant.USER_NAME_PATTERN_REGEXP, message = UserConstant.USER_NAME_PATTERN_MESSAGE)
    private String userName;

    private String avatar;

}

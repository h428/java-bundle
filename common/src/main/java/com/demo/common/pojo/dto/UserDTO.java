package com.demo.common.pojo.dto;

import com.demo.common.entity.User;
import com.demo.common.pojo.converter.PojoConverter;
import com.demo.common.util.EntityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends PojoConverter<UserDTO, User> {

    private Long id;

    @Email(message = "电子邮箱格式不正确")
    private String email; // email 允许为空，因为更新时不能更新 email

    @NotBlank(message = "用户名不能为空")
    private String userName;

    private String userPass;

    private Integer age;

    private Double height;

    private String profile;

    private Long roleId;

    public static void main(String[] args) {
        testConvertFrom();
        testConvertTo();

        UserDTO userDto = UserDTO.builder().build()
                .setAge(1)
                .setEmail("");

    }
    
    private static void testConvertTo() {
        UserDTO userDTO = EntityUtil.generateRandomOne(UserDTO.class);
        EntityUtil.printString(userDTO);

        User user = userDTO.convertToEntity();
        EntityUtil.printString(user);
    }

    private static void testConvertFrom() {
        User user = EntityUtil.generateRandomOne(User.class);
        EntityUtil.printString(user);

        UserDTO userDTO = new UserDTO().convertFromEntity(user);
        EntityUtil.printString(userDTO);
    }

}

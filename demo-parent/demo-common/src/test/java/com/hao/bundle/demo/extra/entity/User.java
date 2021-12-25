package com.hao.bundle.demo.extra.entity;


import com.hao.bundle.demo.constant.EntityAdd;
import com.hao.bundle.demo.constant.EntityUpdate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class User {

    @NotNull(message = "id 不能为空", groups = EntityUpdate.class)
    private Long id;

    @NotEmpty(message = "名称不能为空", groups = EntityAdd.class)
    private String name;

    @Email(message = "邮箱不能为空")
    private String email;

}

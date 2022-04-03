package com.hao.bundle.demo.entity;

import com.hao.bundle.demo.entity.RolePerm.RolePermId;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString
@Entity
@Table(name = "role_perm")
@DynamicInsert
@DynamicUpdate
@IdClass(RolePermId.class)
public class RolePerm {

    @Id
    private Long roleId;

    @Id
    private Long permId;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Accessors(chain = true)
    @Builder
    @EqualsAndHashCode
    public static class RolePermId implements Serializable {
        @Id
        private Long roleId;

        @Id
        private Long permId;
    }

}

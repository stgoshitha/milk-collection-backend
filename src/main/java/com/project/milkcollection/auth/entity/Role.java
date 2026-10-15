package com.project.milkcollection.auth.entity;

import com.project.milkcollection.common.entity.BaseEntity;
import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id", nullable = false, updatable = false)
    private UUID roleId;

    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;

    @Column(name = "role_name", nullable = false, length = 50, unique = true)
    private String roleName;

    @Column(name = "description", length = 225)
    private String description;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "common_status")
    private CommonStatus status;

    @OneToMany(
            mappedBy = "role",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<RolePermission> rolePermissions = new ArrayList<>();

}

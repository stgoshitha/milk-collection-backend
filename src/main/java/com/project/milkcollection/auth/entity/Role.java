package com.project.milkcollection.auth.entity;

import com.project.milkcollection.common.entity.BaseEntity;
import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "role_name", nullable = false, length = 50, unique = true)
    private String roleName;

    @Column(name = "description", length = 225)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommonStatus status;

}

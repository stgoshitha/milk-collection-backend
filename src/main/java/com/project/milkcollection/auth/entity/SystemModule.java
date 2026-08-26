package com.project.milkcollection.auth.entity;

import com.project.milkcollection.common.entity.BaseEntity;
import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemModule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "module_id", nullable = false, updatable = false)
    private UUID systemModuleId;

    @Column(name = "module_name", nullable = false, length = 100, unique = true)
    private String systemModuleName;

    @Column(name = "description")
    private String description;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "icon", length = 100)
    private String icon;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "common_status")
    private CommonStatus status;

}

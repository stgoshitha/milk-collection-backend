package com.project.milkcollection.auth.entity;

import com.project.milkcollection.common.entity.BaseEntity;
import com.project.milkcollection.common.enums.CommonStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "module_id", nullable = false, updatable = false)
    private UUID moduleId;

    @Column(name = "module_name", nullable = false, length = 100, unique = true)
    private String moduleName;

    @Column(name = "description", length = 225)
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "icon", length = 100)
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommonStatus status;

}

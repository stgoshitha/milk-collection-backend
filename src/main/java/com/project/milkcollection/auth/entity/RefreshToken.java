package com.project.milkcollection.auth.entity;

import com.project.milkcollection.common.entity.CreatedOnlyEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken extends CreatedOnlyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refresh_token_id", nullable = false, unique = true)
    private UUID refreshTokenId;

    @Column(name = "token", nullable = false, length = 500)
    private String tokenHash;

    @Column(name = "device_info", length = 225)
    private String deviceInfo;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_refresh_token_user")
    )
    private User user;

}

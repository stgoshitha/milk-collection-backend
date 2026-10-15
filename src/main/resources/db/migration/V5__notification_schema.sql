-- =====================================================
-- Migration : V5__notification_schema.sql
-- =====================================================

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE notification_type AS ENUM(
    'SYSTEM',
    'ACCOUNT',
    'SECURITY',
    'FARMER',
    'COLLECTOR',
    'MILK_COLLECTION',
    'MILK_PRICE',
    'PAYMENT',
    'REPORT',
    'ANNOUNCEMENT'
);

CREATE TYPE notification_priority AS ENUM(
    'LOW',
    'MEDIUM',
    'HIGH'
);

-- =====================================================
-- NOTIFICATIONS
-- =====================================================
CREATE TABLE notifications(
    notification_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type notification_type NOT NULL DEFAULT 'SYSTEM',
    priority notification_priority NOT NULL DEFAULT 'MEDIUM',
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    reference_id UUID,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_notification_user
ON notifications(user_id);

CREATE INDEX idx_notification_user_unread
ON notifications(user_id, is_read);

CREATE INDEX idx_notification_type
ON notifications(type);

CREATE INDEX idx_notification_created_at
ON notifications(created_at);
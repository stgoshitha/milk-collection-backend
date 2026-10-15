-- =====================================================
-- Migration : V6__audit_schema.sql
-- =====================================================

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE audit_action AS ENUM(
    'CREATE',
    'UPDATE',
    'DELETE',
    'LOGIN',
    'LOGOUT',
    'VIEW',
    'EXPORT'
);

CREATE TYPE audit_status AS ENUM(
    'SUCCESS',
    'FAILED'
);

-- =====================================================
-- AUDIT LOGS
-- =====================================================
CREATE TABLE audit_logs(
    audit_log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    module_name VARCHAR(100) NOT NULL,
    action audit_action NOT NULL,
    status audit_status NOT NULL DEFAULT 'SUCCESS',
    record_id UUID,
    description TEXT,
    old_value JSONB,
    new_value JSONB,
    ip_address VARCHAR(45),
    user_agent VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_audit_user
ON audit_logs(user_id);

CREATE INDEX idx_audit_module
ON audit_logs(module_name);

CREATE INDEX idx_audit_action
ON audit_logs(action);

CREATE INDEX idx_audit_status
ON audit_logs(status);

CREATE INDEX idx_audit_created_at
ON audit_logs(created_at);

CREATE INDEX idx_audit_record
ON audit_logs(record_id);
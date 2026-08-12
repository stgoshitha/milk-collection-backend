-- =====================================================
-- Migration : V1__authentication_schema.sql
-- =====================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE user_status AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'SUSPENDED'
);

CREATE TYPE common_status AS ENUM (
    'ACTIVE',
    'INACTIVE'
);

-- =====================================================
-- ROLES
-- =====================================================
CREATE TABLE roles(
    role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_code VARCHAR(50) NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- MODULES
-- =====================================================
CREATE TABLE modules(
    module_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    module_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    display_order INTEGER NOT NULL,
    icon VARCHAR(100),
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- PERMISSIONS
-- =====================================================
CREATE TABLE permissions(
    permission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    module_id UUID NOT NULL,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_permission_module
        FOREIGN KEY (module_id)
            REFERENCES modules(module_id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);

-- =====================================================
-- ROLE PERMISSIONS
-- =====================================================
CREATE TABLE role_permissions(
    role_permission_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id UUID NOT NULL,
    permission_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_role_permission_role
        FOREIGN KEY (role_id)
            REFERENCES roles(role_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT fk_role_permission_permission
        FOREIGN KEY (permission_id)
            REFERENCES permissions(permission_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE,

    CONSTRAINT uq_role_permission
        UNIQUE(role_id, permission_id)
);

-- =====================================================
-- USERS
-- =====================================================
CREATE TABLE users(
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role_id UUID NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(500),
    status user_status NOT NULL DEFAULT 'ACTIVE',
    last_login TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
            REFERENCES roles(role_id)
            ON UPDATE CASCADE
            ON DELETE RESTRICT
);

-- =====================================================
-- REFRESH TOKEN
-- =====================================================
CREATE TABLE refresh_tokens(
    refresh_token_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token VARCHAR(500) NOT NULL UNIQUE,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    expiry_date TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY(user_id)
            REFERENCES users(user_id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_user_role
ON users(role_id);

CREATE INDEX idx_permission_module
ON permissions(module_id);

CREATE INDEX idx_role_permission_role
ON role_permissions(role_id);

CREATE INDEX idx_role_permission_permission
ON role_permissions(permission_id);

CREATE INDEX idx_refresh_token_user
ON refresh_tokens(user_id);

CREATE INDEX idx_refresh_token_token
ON refresh_tokens(token);

CREATE INDEX idx_refresh_token_expiry
ON refresh_tokens(expiry_date);

-- =====================================================
-- DEFAULT ROLES
-- =====================================================
INSERT INTO roles
(role_code, role_name,description)
VALUES
('ROLE_ADMIN','ADMIN','System Administrator'),
('ROLE_MANAGER','MANAGER','Collection Center Manager'),
('ROLE_COLLECTOR','COLLECTOR','Milk Collection Officer'),
('ROLE_ACCOUNTANT', 'ACCOUNTANT','Finance Officer');


-- =====================================================
-- DEFAULT MODULES
-- =====================================================
INSERT INTO modules
(module_name, description, display_order, icon)
VALUES
('Dashboard','System Dashboard',1,'LayoutDashboard'),
('Users','User Management',2,'Users'),
('Collectors','Collector Management',3,'UserRound'),
('Farmers','Farmer Management',4,'UsersRound'),
('Collection Centers','Collection Center Management',5,'Building2'),
('Milk Collections','Milk Collection Management',6,'Milk'),
('Milk Prices','Milk Price Management',7,'BadgeDollarSign'),
('Payments','Payment Management',8,'Wallet'),
('Banks','Bank Management',9,'Landmark'),
('Reports','System Reports',10,'ChartColumn'),
('Settings','Application Settings',11,'Settings');

-- =====================================================
-- DEFAULT PERMISSIONS
-- =====================================================
INSERT INTO permissions
(module_id, permission_name, description)

SELECT module_id,'DASHBOARD_VIEW','View Dashboard'
FROM modules
WHERE module_name='Dashboard';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'USER_VIEW','View Users'
FROM modules
WHERE module_name='Users';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'USER_CREATE','Create User'
FROM modules
WHERE module_name='Users';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'USER_UPDATE','Update User'
FROM modules
WHERE module_name='Users';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'USER_DELETE','Delete User'
FROM modules
WHERE module_name='Users';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'COLLECTOR_VIEW','View Collectors'
FROM modules
WHERE module_name='Collectors';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'COLLECTOR_CREATE','Create Collector'
FROM modules
WHERE module_name='Collectors';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'COLLECTOR_UPDATE','Update Collector'
FROM modules
WHERE module_name='Collectors';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'COLLECTOR_DELETE','Delete Collector'
FROM modules
WHERE module_name='Collectors';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'FARMER_VIEW','View Farmers'
FROM modules
WHERE module_name='Farmers';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'FARMER_CREATE','Create Farmer'
FROM modules
WHERE module_name='Farmers';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'FARMER_UPDATE','Update Farmer'
FROM modules
WHERE module_name='Farmers';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'FARMER_DELETE','Delete Farmer'
FROM modules
WHERE module_name='Farmers';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'MILK_COLLECTION_VIEW','View Milk Collections'
FROM modules
WHERE module_name='Milk Collections';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'MILK_COLLECTION_CREATE','Create Milk Collection'
FROM modules
WHERE module_name='Milk Collections';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'MILK_COLLECTION_UPDATE','Update Milk Collection'
FROM modules
WHERE module_name='Milk Collections';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'PAYMENT_VIEW','View Payments'
FROM modules
WHERE module_name='Payments';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'PAYMENT_CREATE','Create Payment'
FROM modules
WHERE module_name='Payments';

INSERT INTO permissions
(module_id,permission_name,description)

SELECT module_id,'REPORT_VIEW','View Reports'
FROM modules
WHERE module_name='Reports';

-- =====================================================
-- ADMIN GETS ALL PERMISSIONS
-- =====================================================
INSERT INTO role_permissions(role_id, permission_id)
SELECT
    r.role_id,
    p.permission_id
FROM roles r
CROSS JOIN permissions p
WHERE r.role_name='ADMIN';
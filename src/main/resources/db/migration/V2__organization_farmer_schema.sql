-- =====================================================
-- Migration : V2__organization_farmer_schema.sql
-- =====================================================

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE employment_status AS ENUM(
    'ACTIVE',
    'ON_LEAVE',
    'RESIGNED',
    'TERMINATED'
);

CREATE TYPE farmer_status AS ENUM(
    'ACTIVE',
    'INACTIVE',
    'BLACKLISTED'
);

-- =====================================================
-- COLLECTION CENTERS
-- =====================================================
CREATE TABLE collection_centers(
    collection_center_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    center_code VARCHAR(20) NOT NULL UNIQUE,
    center_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    whatsapp_number VARCHAR(15),
    email VARCHAR(100),
    address TEXT NOT NULL,
    village VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- COLLECTORS
-- =====================================================
CREATE TABLE collectors(
    collector_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE,
    collection_center_id UUID NOT NULL,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    nic VARCHAR(12) NOT NULL UNIQUE,
    personal_contact_number VARCHAR(15),
    address TEXT NOT NULL,
    joining_date DATE NOT NULL,
    remarks TEXT,
    employment_status employment_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_collector_user
        FOREIGN KEY(user_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_collector_center
        FOREIGN KEY(collection_center_id)
        REFERENCES collection_centers(collection_center_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- =====================================================
-- FARMERS
-- =====================================================
CREATE TABLE farmers(
    farmer_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_code VARCHAR(20) NOT NULL UNIQUE,
    collection_center_id UUID NOT NULL,
    nic VARCHAR(12) UNIQUE,
    phone_number VARCHAR(15) NOT NULL,
    whatsapp_number VARCHAR(15),
    address TEXT NOT NULL,
    village VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    joined_date DATE NOT NULL,
    status farmer_status NOT NULL DEFAULT 'ACTIVE',
    qr_code VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_farmer_center
        FOREIGN KEY(collection_center_id)
        REFERENCES collection_centers(collection_center_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- =====================================================
-- BANKS
-- =====================================================
CREATE TABLE banks(
    bank_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    bank_name VARCHAR(100) NOT NULL UNIQUE,
    bank_code VARCHAR(20),
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- FARMER BANK ACCOUNTS
-- =====================================================
CREATE TABLE farmer_bank_accounts(
    bank_account_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    bank_id UUID NOT NULL,
    branch_name VARCHAR(100) NOT NULL,
    account_number VARCHAR(30) NOT NULL,
    account_holder_name VARCHAR(150) NOT NULL,
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_farmer_bank_farmer
        FOREIGN KEY(farmer_id)
        REFERENCES farmers(farmer_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_farmer_bank
        FOREIGN KEY(bank_id)
        REFERENCES banks(bank_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_collectors_user
ON collectors(user_id);

CREATE INDEX idx_collectors_center
ON collectors(collection_center_id);

CREATE INDEX idx_farmers_center
ON farmers(collection_center_id);

CREATE INDEX idx_farmer_bank_farmer
ON farmer_bank_accounts(farmer_id);

CREATE INDEX idx_farmer_bank_bank
ON farmer_bank_accounts(bank_id);

CREATE INDEX idx_farmer_phone
ON farmers(phone_number);

CREATE INDEX idx_collector_phone
ON collectors(phone_number);

-- =====================================================
-- DEFAULT BANKS
-- =====================================================
INSERT INTO banks(bank_name, bank_code)
VALUES
('Bank of Ceylon','BOC'),
('People''s Bank','PB'),
('Commercial Bank','COM'),
('Hatton National Bank','HNB'),
('Sampath Bank','SB'),
('National Development Bank','NDB'),
('Nations Trust Bank','NTB'),
('DFCC Bank','DFCC'),
('Pan Asia Bank','PABC'),
('Seylan Bank','SEY'),
('Union Bank','UB'),
('Amana Bank','AB'),
('Cargills Bank','CB');
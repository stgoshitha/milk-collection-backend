-- =====================================================
-- Migration : V4__finance_system_schema.sql
-- =====================================================

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE payment_method AS ENUM(
    'BANK_TRANSFER',
    'CASH',
    'CHEQUE'
);

CREATE TYPE payment_status AS ENUM(
    'PENDING',
    'PAID',
    'FAILED',
    'CANCELLED'
);

-- =====================================================
-- PAYMENTS
-- =====================================================
CREATE TABLE payments(
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    payment_period_from DATE NOT NULL,
    payment_period_to DATE NOT NULL,
    total_kg DECIMAL(10,2) NOT NULL DEFAULT 0,
    gross_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    deduction_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    bonus_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    net_amount DECIMAL(12,2) NOT NULL DEFAULT 0,
    payment_method payment_method NOT NULL,
    payment_date DATE NOT NULL,
    status payment_status NOT NULL DEFAULT 'PENDING',
    remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(farmer_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_total_kg
        CHECK(total_kg >= 0),

    CONSTRAINT chk_gross_amount
        CHECK(gross_amount >= 0),

    CONSTRAINT chk_deduction
        CHECK(deduction_amount >= 0),

    CONSTRAINT chk_bonus
        CHECK(bonus_amount >= 0),

    CONSTRAINT chk_net_amount
        CHECK(net_amount >= 0)

);

-- =====================================================
-- PAYMENT DETAILS
-- =====================================================
CREATE TABLE payment_details(
    payment_detail_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID NOT NULL,
    milk_collection_id UUID NOT NULL,
    quantity_kg DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_detail_payment
        FOREIGN KEY(payment_id)
        REFERENCES payments(payment_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_payment_detail_collection
        FOREIGN KEY(milk_collection_id)
        REFERENCES milk_collections(milk_collection_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_payment_detail_quantity
        CHECK(quantity_kg > 0),

    CONSTRAINT chk_payment_detail_price
        CHECK(unit_price >= 0),

    CONSTRAINT chk_payment_detail_amount
        CHECK(amount >= 0)

);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_payment_farmer
ON payments(farmer_id);

CREATE INDEX idx_payment_date
ON payments(payment_date);

CREATE INDEX idx_payment_status
ON payments(status);

CREATE INDEX idx_payment_period
ON payments(payment_period_from, payment_period_to);

CREATE INDEX idx_payment_details_payment
ON payment_details(payment_id);

CREATE INDEX idx_payment_details_collection
ON payment_details(milk_collection_id);
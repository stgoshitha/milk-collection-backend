-- =====================================================
-- Migration : V3__milk_schema.sql
-- =====================================================

-- =====================================================
-- ENUMS
-- =====================================================
CREATE TYPE collection_session AS ENUM(
    'MORNING',
    'EVENING'
);

-- =====================================================
-- MILK PRICES
-- =====================================================
CREATE TABLE milk_prices(
    milk_price_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    collection_center_id UUID,
    effective_from DATE NOT NULL,
    effective_to DATE,
    unit_price DECIMAL(10,2) NOT NULL,
    status common_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_milk_price_center
        FOREIGN KEY (collection_center_id)
        REFERENCES collection_centers(collection_center_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- =====================================================
-- MILK COLLECTIONS
-- =====================================================
CREATE TABLE milk_collections(
    milk_collection_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    farmer_id UUID NOT NULL,
    collector_id UUID NOT NULL,
    collection_center_id UUID NOT NULL,
    milk_price_id UUID NOT NULL,
    collection_date DATE NOT NULL,
    collection_session collection_session NOT NULL,
    quantity_kg DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_quantity
        CHECK (quantity_kg > 0),

    CONSTRAINT chk_unit_price
        CHECK (unit_price >= 0),

    CONSTRAINT chk_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT fk_collection_farmer
        FOREIGN KEY (farmer_id)
        REFERENCES farmers(farmer_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_collection_collector
        FOREIGN KEY (collector_id)
        REFERENCES collectors(collector_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_collection_center
        FOREIGN KEY (collection_center_id)
        REFERENCES collection_centers(collection_center_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_collection_price
        FOREIGN KEY (milk_price_id)
        REFERENCES milk_prices(milk_price_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- =====================================================
-- UNIQUE CONSTRAINT
-- One farmer can have only one collection
-- per session per day.
-- =====================================================
ALTER TABLE milk_collections
ADD CONSTRAINT uq_farmer_collection
UNIQUE
(
    farmer_id,
    collection_date,
    collection_session,
    collection_center_id
);

-- =====================================================
-- INDEXES
-- =====================================================
CREATE INDEX idx_milk_collection_farmer
ON milk_collections(farmer_id);

CREATE INDEX idx_milk_collection_collector
ON milk_collections(collector_id);

CREATE INDEX idx_milk_collection_center
ON milk_collections(collection_center_id);

CREATE INDEX idx_milk_collection_date
ON milk_collections(collection_date);

CREATE INDEX idx_milk_collection_price
ON milk_collections(milk_price_id);

CREATE INDEX idx_milk_price_center
ON milk_prices(collection_center_id);

CREATE INDEX idx_milk_price_effective_from
ON milk_prices(effective_from);

-- =====================================================
-- DEFAULT MILK PRICE
-- =====================================================
INSERT INTO milk_prices
(
    collection_center_id,
    effective_from,
    effective_to,
    unit_price
)
VALUES
(
    NULL,
    CURRENT_DATE,
    NULL,
    160.00
);
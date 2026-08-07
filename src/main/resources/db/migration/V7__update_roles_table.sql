-- =====================================================
-- Migration : V7__update_roles_table.sql
-- Purpose   : Add role_code column to roles table
-- =====================================================

-- Add role_code column
ALTER TABLE roles
ADD COLUMN role_code VARCHAR(50);


-- Populate role_code for existing default roles
UPDATE roles
SET role_code = CASE role_name
    WHEN 'ADMIN' THEN 'ROLE_ADMIN'
    WHEN 'MANAGER' THEN 'ROLE_MANAGER'
    WHEN 'COLLECTOR' THEN 'ROLE_COLLECTOR'
    WHEN 'ACCOUNTANT' THEN 'ROLE_ACCOUNTANT'
END
WHERE role_name IN (
    'ADMIN',
    'MANAGER',
    'COLLECTOR',
    'ACCOUNTANT'
);


-- Ensure no null role_code exists before adding constraint
DELETE FROM roles
WHERE role_code IS NULL;


-- Make role_code mandatory
ALTER TABLE roles
ALTER COLUMN role_code SET NOT NULL;


-- Add unique constraint
ALTER TABLE roles
ADD CONSTRAINT uq_roles_role_code UNIQUE (role_code);
-- Update existing internships with application fee columns

-- First, add the columns if they don't exist
ALTER TABLE internships 
ADD COLUMN IF NOT EXISTS application_fee DOUBLE DEFAULT 0,
ADD COLUMN IF NOT EXISTS requires_payment BOOLEAN DEFAULT FALSE;

-- Update all existing internships to have default values
UPDATE internships 
SET application_fee = 0, 
    requires_payment = FALSE 
WHERE application_fee IS NULL OR requires_payment IS NULL;

-- Optional: Set a default fee for all paid internships
-- UPDATE internships 
-- SET application_fee = 100, 
--     requires_payment = TRUE 
-- WHERE is_paid = TRUE AND application_fee = 0;

-- Verify the update
SELECT id, title, company, is_paid, application_fee, requires_payment 
FROM internships 
LIMIT 10;

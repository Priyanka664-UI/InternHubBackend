-- Add application fee and requires payment columns to internships table

ALTER TABLE internships 
ADD COLUMN application_fee DOUBLE DEFAULT 0,
ADD COLUMN requires_payment BOOLEAN DEFAULT FALSE;

-- Update existing records to have default values
UPDATE internships 
SET application_fee = 0, 
    requires_payment = FALSE 
WHERE application_fee IS NULL;

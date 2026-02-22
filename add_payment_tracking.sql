-- Add payment tracking columns to internship_applications table

ALTER TABLE internship_applications 
ADD COLUMN payment_status VARCHAR(50) DEFAULT 'NOT_REQUIRED',
ADD COLUMN payment_amount DOUBLE DEFAULT 0.0,
ADD COLUMN payment_id VARCHAR(255);

-- Update existing records
UPDATE internship_applications 
SET payment_status = 'NOT_REQUIRED', 
    payment_amount = 0.0 
WHERE payment_status IS NULL;

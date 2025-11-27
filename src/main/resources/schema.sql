-- Create internship_applications table
CREATE TABLE IF NOT EXISTS internship_applications (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    internship_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    applied_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cover_letter TEXT,
    resume_url VARCHAR(500),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE
);

-- Create certificates table
CREATE TABLE IF NOT EXISTS certificates (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    internship_id BIGINT NOT NULL,
    certificate_number VARCHAR(50) UNIQUE NOT NULL,
    issue_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completion_date TIMESTAMP NOT NULL,
    certificate_url VARCHAR(500),
    performance_rating INTEGER CHECK (performance_rating >= 1 AND performance_rating <= 5),
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE
);

-- Create attendance table
CREATE TABLE IF NOT EXISTS attendance (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    internship_id BIGINT NOT NULL,
    date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    status VARCHAR(20) NOT NULL DEFAULT 'ABSENT',
    remarks VARCHAR(500),
    hours_worked DECIMAL(4,2),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE,
    UNIQUE(student_id, internship_id, date)
);

-- Add category column to companies table if it doesn't exist
ALTER TABLE companies ADD COLUMN IF NOT EXISTS category VARCHAR(50);

-- Add state_id and city_id columns to companies table
ALTER TABLE companies ADD COLUMN IF NOT EXISTS state_id BIGINT;
ALTER TABLE companies ADD COLUMN IF NOT EXISTS city_id BIGINT;
ALTER TABLE companies ADD CONSTRAINT IF NOT EXISTS fk_company_state FOREIGN KEY (state_id) REFERENCES states(id);
ALTER TABLE companies ADD CONSTRAINT IF NOT EXISTS fk_company_city FOREIGN KEY (city_id) REFERENCES cities(id);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_applications_student ON internship_applications(student_id);
CREATE INDEX IF NOT EXISTS idx_applications_internship ON internship_applications(internship_id);
CREATE INDEX IF NOT EXISTS idx_applications_status ON internship_applications(status);
CREATE INDEX IF NOT EXISTS idx_certificates_student ON certificates(student_id);
CREATE INDEX IF NOT EXISTS idx_certificates_internship ON certificates(internship_id);
CREATE INDEX IF NOT EXISTS idx_attendance_student ON attendance(student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance(date);
CREATE INDEX IF NOT EXISTS idx_companies_category ON companies(category);
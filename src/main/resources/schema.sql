-- Create groups table
CREATE TABLE IF NOT EXISTS groups (
    id BIGSERIAL PRIMARY KEY,
    group_name VARCHAR(255),
    college_name VARCHAR(255),
    department VARCHAR(255),
    academic_year VARCHAR(255),
    semester VARCHAR(255),
    total_students INTEGER,
    internship_type VARCHAR(50),
    preferred_mode VARCHAR(50),
    duration_months INTEGER,
    start_date DATE,
    end_date DATE,
    faculty_name VARCHAR(255),
    faculty_email VARCHAR(255),
    faculty_phone VARCHAR(255),
    leader_id BIGINT,
    status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (leader_id) REFERENCES students(id)
);

-- Create group_members table
CREATE TABLE IF NOT EXISTS group_members (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT,
    student_id BIGINT,
    student_name VARCHAR(255),
    github_link VARCHAR(500),
    status VARCHAR(50) DEFAULT 'PENDING',
    FOREIGN KEY (group_id) REFERENCES groups(id),
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- Create group_invitations table
CREATE TABLE IF NOT EXISTS group_invitations (
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT,
    invited_email VARCHAR(255),
    invitation_token VARCHAR(500),
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES groups(id)
);
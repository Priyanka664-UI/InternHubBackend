-- Create payment_receipts table
CREATE TABLE payment_receipts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) UNIQUE NOT NULL,
    payment_id VARCHAR(255) NOT NULL,
    order_id VARCHAR(255),
    application_id BIGINT,
    student_id BIGINT NOT NULL,
    amount DOUBLE NOT NULL,
    payment_date DATETIME NOT NULL,
    payment_method VARCHAR(50),
    company_name VARCHAR(255),
    internship_title VARCHAR(255),
    application_type VARCHAR(20),
    group_id BIGINT,
    FOREIGN KEY (application_id) REFERENCES internship_applications(id) ON DELETE SET NULL,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE SET NULL,
    INDEX idx_payment_id (payment_id),
    INDEX idx_student_id (student_id),
    INDEX idx_application_id (application_id),
    INDEX idx_group_id (group_id)
);

-- Drop existing table if exists
DROP TABLE IF EXISTS classes CASCADE;

-- Create classes table
CREATE TABLE classes (
    id BIGSERIAL PRIMARY KEY,
    date VARCHAR(255) NOT NULL,
    time VARCHAR(255) NOT NULL,
    student_id BIGINT REFERENCES students(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT REFERENCES users(id),
    last_modified_by BIGINT REFERENCES users(id),
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_classes_student_id ON classes(student_id);
CREATE INDEX idx_classes_created_by ON classes(created_by);
CREATE INDEX idx_classes_last_modified_by ON classes(last_modified_by);

-- Add trigger to automatically update updated_at timestamp
CREATE TRIGGER update_classes_updated_at
    BEFORE UPDATE ON classes
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column(); 
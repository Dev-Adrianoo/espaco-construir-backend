 -- Drop existing tables if they exist
DROP TABLE IF EXISTS schedules CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    cnpj VARCHAR(14),
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create students table
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER,
    grade VARCHAR(50),
    difficulties TEXT,
    condition TEXT,
    guardian_id BIGINT REFERENCES users(id),
    registered_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create schedules table
CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES students(id),
    teacher_id BIGINT REFERENCES users(id),
    start_time TIMESTAMP WITH TIME ZONE NOT NULL,
    end_time TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(50) NOT NULL,
    modality VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT REFERENCES users(id),
    last_modified_by BIGINT REFERENCES users(id),
    last_modified_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_time_range CHECK (end_time > start_time)
);

-- Create indexes for better performance
CREATE INDEX idx_students_guardian_id ON students(guardian_id);
CREATE INDEX idx_students_registered_by ON students(registered_by);
CREATE INDEX idx_schedules_student_id ON schedules(student_id);
CREATE INDEX idx_schedules_teacher_id ON schedules(teacher_id);
CREATE INDEX idx_schedules_created_by ON schedules(created_by);
CREATE INDEX idx_schedules_last_modified_by ON schedules(last_modified_by);

-- Add triggers to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_students_updated_at
    BEFORE UPDATE ON students
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- Add some common status values as an enum
DROP TYPE IF EXISTS schedule_status;
DROP TYPE IF EXISTS schedule_modality;
DROP TYPE IF EXISTS user_role;

CREATE TYPE schedule_status AS ENUM ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING');
CREATE TYPE schedule_modality AS ENUM ('ONLINE', 'IN_PERSON', 'HYBRID');
CREATE TYPE user_role AS ENUM ('ADMIN', 'TEACHER', 'GUARDIAN', 'STUDENT');
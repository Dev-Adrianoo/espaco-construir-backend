-- Add triggers to automatically update updated_at timestamp if they don't exist
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

DO $$ 
BEGIN
    -- Create triggers only if they don't exist
    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_users_updated_at') THEN
        CREATE TRIGGER update_users_updated_at
            BEFORE UPDATE ON users
            FOR EACH ROW
            EXECUTE FUNCTION update_updated_at_column();
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'update_students_updated_at') THEN
        CREATE TRIGGER update_students_updated_at
            BEFORE UPDATE ON students
            FOR EACH ROW
            EXECUTE FUNCTION update_updated_at_column();
    END IF;
END $$;

-- Add indexes if they don't exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_students_guardian_id') THEN
        CREATE INDEX idx_students_guardian_id ON students(guardian_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_students_registered_by') THEN
        CREATE INDEX idx_students_registered_by ON students(registered_by);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_schedules_student_id') THEN
        CREATE INDEX idx_schedules_student_id ON schedules(student_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_schedules_teacher_id') THEN
        CREATE INDEX idx_schedules_teacher_id ON schedules(teacher_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_schedules_created_by') THEN
        CREATE INDEX idx_schedules_created_by ON schedules(created_by);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE indexname = 'idx_schedules_last_modified_by') THEN
        CREATE INDEX idx_schedules_last_modified_by ON schedules(last_modified_by);
    END IF;
END $$;

-- Add ENUMs if they don't exist
DO $$ 
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'schedule_status') THEN
        CREATE TYPE schedule_status AS ENUM ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'PENDING');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'schedule_modality') THEN
        CREATE TYPE schedule_modality AS ENUM ('ONLINE', 'IN_PERSON', 'HYBRID');
    END IF;

    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role') THEN
        CREATE TYPE user_role AS ENUM ('ADMIN', 'TEACHER', 'GUARDIAN', 'STUDENT');
    END IF;
END $$; 
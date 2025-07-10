ALTER TABLE students ADD COLUMN registered_by_user_id BIGINT;
ALTER TABLE students ADD CONSTRAINT fk_students_registered_by FOREIGN KEY (registered_by_user_id) REFERENCES users(id);
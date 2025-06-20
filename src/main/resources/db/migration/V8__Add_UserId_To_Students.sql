ALTER TABLE students
ADD COLUMN user_id BIGINT NOT NULL,
ADD CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users(id); 
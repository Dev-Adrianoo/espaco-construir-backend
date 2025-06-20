CREATE TABLE  IF NOT EXISTS  class_history (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    class_id BIGINT,
    comment TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_classhistory_student FOREIGN KEY (student_id) REFERENCES users(id),
    CONSTRAINT fk_classhistory_teacher FOREIGN KEY (teacher_id) REFERENCES users(id),
    CONSTRAINT fk_classhistory_class FOREIGN KEY (class_id) REFERENCES classes(id)
); 
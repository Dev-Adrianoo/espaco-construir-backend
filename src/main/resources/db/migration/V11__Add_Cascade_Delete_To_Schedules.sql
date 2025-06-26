-- Primeiro remove as constraints existentes
ALTER TABLE schedules DROP CONSTRAINT schedules_student_id_fkey;
ALTER TABLE classes DROP CONSTRAINT classes_student_id_fkey;
ALTER TABLE class_history DROP CONSTRAINT fk_classhistory_student;

-- Adiciona as constraints novamente com ON DELETE CASCADE
ALTER TABLE schedules
ADD CONSTRAINT schedules_student_id_fkey
FOREIGN KEY (student_id)
REFERENCES students(id)
ON DELETE CASCADE;

ALTER TABLE classes
ADD CONSTRAINT classes_student_id_fkey
FOREIGN KEY (student_id)
REFERENCES students(id)
ON DELETE CASCADE;

ALTER TABLE class_history
ADD CONSTRAINT fk_classhistory_student
FOREIGN KEY (student_id)
REFERENCES users(id)
ON DELETE CASCADE; 
-- Remove a foreign key antiga (ajuste o nome se necessário)
ALTER TABLE schedules DROP CONSTRAINT IF EXISTS fk2tkh9o1h0gfyfb8ar82sv94lw;

-- Crie a nova foreign key correta
ALTER TABLE schedules
ADD CONSTRAINT fk_schedules_student FOREIGN KEY (student_id) REFERENCES students(id); 
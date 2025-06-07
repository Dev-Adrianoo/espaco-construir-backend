# Espaço Construir Backend

## Visão Geral

Este projeto é o backend de um sistema de tutoria, desenvolvido em Java com Spring Boot. Ele gerencia responsáveis, alunos, professores e agendamentos de aulas.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4
- PostgreSQL 15
- Docker
- Flyway (Migrations)
- Maven

---

## Estrutura do Projeto

- **controller/**: Endpoints REST (GuardianController, ProfessorController)
- **service/**: Lógica de negócio (UserService, StudentService, ScheduleService)
- **model/**: Entidades JPA (User, Student, Schedule)
- **dto/**: Objetos de transferência de dados (GuardianDTO, TeacherDTO)
- **repository/**: Interfaces para acesso ao banco de dados
- **config/**: Configurações do Spring Boot
- **resources/db/migration/**: Arquivos de migração do Flyway

---

## Como rodar o projeto

### Usando Docker (Recomendado)

1. Certifique-se de ter o Docker instalado
2. Execute o script de inicialização:
   ```powershell
   .\start.ps1
   ```
   Este script vai:
   - Iniciar o container do PostgreSQL
   - Aguardar o banco ficar disponível
   - Iniciar a aplicação Spring Boot

### Manualmente

1. Instale o Java 21+ e o PostgreSQL 15
2. Configure o banco no arquivo `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/tutoring_db
       username: postgres
       password: postgres
   ```
3. Rode o projeto com `./mvnw spring-boot:run`

---

## Banco de Dados

### Estrutura

O projeto usa PostgreSQL com as seguintes tabelas:

- `users`: Usuários do sistema (responsáveis e professores)

  - Campos: id, name, email, password, phone, cnpj, role, created_at, updated_at

- `students`: Alunos

  - Campos: id, name, age, grade, difficulties, condition, guardian_id, registered_by, created_at, updated_at

- `schedules`: Agendamentos
  - Campos: id, student_id, teacher_id, start_time, end_time, status, modality, created_at, created_by, last_modified_by, last_modified_at

### Migrações (Flyway)

O projeto usa Flyway para controle de versão do banco de dados. As migrações estão em:

```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql
├── V2__Create_Core_Tables.sql
├── V3__Add_Triggers_And_Indexes.sql
└── V4__Create_Classes_Table.sql
```

---

## Endpoints Principais

### Autenticação

- `POST /api/auth/login`: Login de usuários
- `POST /api/auth/register`: Registro de novos usuários

### Responsáveis (Guardians)

- `POST /api/guardians/register`: Cadastrar responsável
- `GET /api/guardians/{id}`: Buscar responsável
- `PUT /api/guardians/{id}`: Atualizar responsável
- `DELETE /api/guardians/{id}`: Deletar responsável
- `GET /api/guardians`: Listar todos responsáveis

### Professores

- `POST /api/professors/register`: Cadastrar professor
- `GET /api/professors/{id}`: Buscar professor
- `PUT /api/professors/{id}`: Atualizar professor
- `DELETE /api/professors/{id}`: Deletar professor
- `GET /api/professors`: Listar todos professores

### Alunos

- `GET /api/students`: Listar todos alunos
- `POST /api/students`: Cadastrar novo aluno
- `GET /api/students/{id}`: Buscar aluno por ID
- `PUT /api/students/{id}`: Atualizar aluno
- `DELETE /api/students/{id}`: Deletar aluno

### Agendamentos

- `POST /api/schedules`: Criar novo agendamento
- `GET /api/schedules`: Listar agendamentos
- `GET /api/schedules/{id}`: Buscar agendamento por ID
- `PUT /api/schedules/{id}`: Atualizar agendamento
- `DELETE /api/schedules/{id}`: Cancelar agendamento

---

## Desenvolvimento

### Portas

- Frontend: `http://localhost:5173`
- Backend: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

### Ferramentas Recomendadas

- IntelliJ IDEA ou VS Code
- Postman ou Insomnia para testes de API
- DBeaver ou pgAdmin para gerenciamento do banco

### Dicas

- Use o Postman/Insomnia para testar os endpoints
- Todos os endpoints estão sob `/api/*`
- As respostas são em formato JSON
- Consulte os logs do Spring Boot para debugging

---

## Contato e Suporte

Se precisar de ajuda:

1. Consulte este README
2. Verifique os logs da aplicação
3. Pergunte para o time
4. Verifique o histórico de commits

Você está aprendendo, não desista! 🚀

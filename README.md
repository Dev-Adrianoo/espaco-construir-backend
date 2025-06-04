# Espaço Construir Backend

## Visão Geral

Este projeto é o backend de um sistema de tutoria, desenvolvido em Java com Spring Boot. Ele gerencia responsáveis, alunos, aulas e histórico de aulas.

---

## Estrutura do Projeto

- **controller/**: Endpoints REST (GuardianController, ProfessorController)
- **service/**: Lógica de negócio (UserService, StudentService, ClassService)
- **model/**: Entidades JPA (User, Student, Class)
- **dto/**: Objetos de transferência de dados (GuardianDTO, TeacherDTO)
- **repository/**: Interfaces para acesso ao banco de dados
- **config/**: Configurações do Spring Boot

---

## Endpoints Principais

### Responsáveis (Guardians)

- **Cadastrar responsável**

  - `POST /api/guardians/register`
  - Body: `{ "name": "...", "email": "...", "password": "...", "phone": "...", "role": "RESPONSAVEL" }`

- **Buscar responsável por ID**

  - `GET /api/guardians/{id}`

- **Atualizar responsável**

  - `PUT /api/guardians/{id}`
  - Body: `{ "name": "...", "email": "...", "phone": "..." }`

- **Deletar responsável**

  - `DELETE /api/guardians/{id}`

- **Listar todos responsáveis**
  - `GET /api/guardians`
  - Resposta: Lista de responsáveis (id, nome, email, telefone)

### Professores

- **Cadastrar professor**

  - `POST /api/professors/register`
  - Body: `{ "name": "...", "email": "...", "password": "...", "phone": "...", "cnpj": "...", "role": "PROFESSORA" }`

- **Buscar professor por ID**

  - `GET /api/professors/{id}`

- **Atualizar professor**

  - `PUT /api/professors/{id}`
  - Body: `{ "name": "...", "email": "...", "phone": "...", "cnpj": "..." }`

- **Deletar professor**

  - `DELETE /api/professors/{id}`

- **Listar todos professores**
  - `GET /api/professors`
  - Resposta: Lista de professores (id, nome, email, telefone, cnpj, role)

### Alunos e Aulas

- **Listar alunos de um responsável**

  - `GET /api/guardians/children?responsavelId=ID_DO_RESPONSAVEL`
  - Resposta: Lista de alunos do responsável

- **Histórico de aulas de um aluno**
  - `GET /api/guardians/history?alunoId=ID_DO_ALUNO`
  - Resposta: Lista de aulas do aluno

---

## Como rodar o projeto

1. Instale o Java 17+ e o PostgreSQL
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

## Dicas para Desenvolvimento

- Use o Postman/Insomnia para testar os endpoints
- O frontend está configurado em `http://localhost:5173`
- O backend roda em `http://localhost:8080`
- Todos os endpoints estão sob `/api/*`
- As respostas são em formato JSON

---

## Banco de Dados

O projeto usa PostgreSQL com as seguintes tabelas:

- `teachers`: Professores (id, name, email, password, phone, cnpj, role)
- `users`: Responsáveis (id, name, email, password, phone, role)
- `students`: Alunos (id, name, age, grade, difficulties, condition, guardian_id)
- `classes`: Aulas (id, date, time, student_id)

---

## Contato e Suporte

Se ficar perdido, procure este README ou pergunte para o time! Você está aprendendo, não desista 🚀

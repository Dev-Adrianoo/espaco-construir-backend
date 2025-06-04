# Espaço Construir Backend

## Visão Geral
Este projeto é o backend de um sistema de tutoria, desenvolvido em Java com Spring Boot. Ele gerencia responsáveis, alunos, aulas e histórico de aulas.

----

## Estrutura do Projeto

- **controller/**: Endpoints REST (ex: GuardianController, ChildController)
- **service/**: Lógica de negócio (ex: UserService, StudentService, ClassService)
- **model/**: Entidades JPA (ex: User, Student, Class)
- **dto/**: Objetos de transferência de dados (ex: GuardianDTO, GuardianResponseDTO)
- **repository/**: Interfaces para acesso ao banco de dados

----

## Endpoints Principais

### Responsáveis (Guardians)
- **Cadastrar responsável**
  - `POST /api/guardians`
  - Body: `{ "name": "...", "email": "...", "password": "...", "phone": "..." }`
- **Listar responsáveis**
  - `GET /api/guardians`
  - Resposta: Lista de responsáveis (id, nome, email, telefone)

### Alunos (Children)
- **Cadastrar aluno**
  - `POST /api/children`
  - Body: `{ "name": "...", "age": 10, "grade": "...", "difficulties": "...", "condition": "...", "classType": "Online|Presencial", "parent": idDoResponsavel }`
- **Listar alunos de um responsável**
  - `GET /api/children?responsavelId=ID_DO_RESPONSAVEL`
  - Resposta: Lista de alunos

### Aulas (Classes)
- **Agendar aula**
  - `POST /api/classes`
  - Body: `{ "date": "2025-06-01", "time": "10:00", "studentId": 1 }`
- **Histórico de aulas de um aluno**
  - `GET /api/classes/history?alunoId=ID_DO_ALUNO`
  - Resposta: Lista de aulas daquele aluno

---

## Como rodar o projeto
1. Instale o Java 17+ e o PostgreSQL
2. Configure o banco no arquivo `application.yml`
3. Rode o projeto com `./mvnw spring-boot:run` ou via sua IDE

---

## Dicas para Desenvolvimento
- Use o Postman/Insomnia para testar os endpoints
- O frontend espera respostas em JSON
- Métodos principais: `GET` para buscar, `POST` para criar, `PUT` para atualizar, `DELETE` para remover

---

## Contato e Suporte
Se ficar perdido, procure este README ou pergunte para o time! Você está aprendendo, não desista 🚀 
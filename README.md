# Espaço Construir Backend

## Visão Geral

Este projeto é o backend de um sistema de tutoria, desenvolvido em Java com Spring Boot. Ele gerencia responsáveis, alunos, professores, agendamentos de aulas e histórico de aulas.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.5**
- **PostgreSQL 15**
- **Docker & Docker Compose**
- **Flyway (Migrations)**
- **Maven**
- **JWT (JSON Web Tokens)**
- **Spring Security**
- **Lombok**

---

## Estrutura do Projeto

```
src/main/java/br/com/espacoconstruir/tutoring_backend/
├── config/           # Configurações (CORS, Security, Password)
├── controller/       # Endpoints REST
├── dto/             # Objetos de transferência de dados
├── exception/       # Tratamento de exceções
├── model/           # Entidades JPA
├── repository/      # Interfaces para acesso ao banco
├── security/        # Filtros JWT
└── service/         # Lógica de negócio
```

---

## Como rodar o projeto

### Usando Docker (Recomendado)

1. **Certifique-se de ter o Docker e Docker Compose instalados**

2. **Execute o script de inicialização:**

   ```powershell
   .\start.ps1
   ```

   Este script vai:

   - Iniciar o container do PostgreSQL
   - Aguardar o banco ficar disponível
   - Iniciar a aplicação Spring Boot

3. **Ou execute manualmente:**
   ```bash
   docker-compose up -d
   ```

### Configuração Manual

1. **Instale o Java 21+ e o PostgreSQL 15**

2. **Configure as variáveis de ambiente no arquivo `application.yml`:**

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/tutoring_db
       username: postgres
       password: postgres
   ```

3. **Rode o projeto:**
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Configurações de Ambiente

O projeto suporta configuração via variáveis de ambiente:

| Variável          | Padrão                                                             | Descrição                 |
| ----------------- | ------------------------------------------------------------------ | ------------------------- |
| `APP_NAME`        | `espaco-construir-backend`                                         | Nome da aplicação         |
| `DB_URL`          | `jdbc:postgresql://localhost:5432/tutoring_db`                     | URL do banco de dados     |
| `DB_USERNAME`     | `postgres`                                                         | Usuário do banco          |
| `DB_PASSWORD`     | `postgres`                                                         | Senha do banco            |
| `DB_DRIVER`       | `org.postgresql.Driver`                                            | Driver do banco           |
| `JPA_DDL_AUTO`    | `validate`                                                         | Modo do Hibernate         |
| `JPA_SHOW_SQL`    | `true`                                                             | Mostrar SQL no console    |
| `JPA_FORMAT_SQL`  | `true`                                                             | Formatar SQL no console   |
| `FLYWAY_ENABLED`  | `true`                                                             | Habilitar Flyway          |
| `SERVER_PORT`     | `8080`                                                             | Porta da aplicação        |
| `JWT_SECRET`      | `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970` | Chave secreta JWT         |
| `JWT_EXPIRATION`  | `86400000`                                                         | Expiração do token (24h)  |
| `ALLOWED_ORIGINS` | `http://localhost:5173`                                            | CORS - origens permitidas |

---

## Banco de Dados

### Estrutura

O projeto usa PostgreSQL com as seguintes tabelas principais:

#### `users`

- **Campos:** id, name, email, password, phone, cnpj, role, created_at, updated_at
- **Roles:** RESPONSAVEL, PROFESSOR, ALUNO

#### `students`

- **Campos:** id, name, age, grade, difficulties, condition, guardian_id, user_id, registered_by, created_at, updated_at
- **Relacionamentos:**
  - `guardian_id` → `users.id` (responsável)
  - `user_id` → `users.id` (usuário do aluno)

#### `schedules`

- **Campos:**
  - id: Identificador único
  - student_id: ID do aluno (ref: students)
  - teacher_id: ID do professor (ref: users)
  - start_time: Data/hora início
  - end_time: Data/hora fim
  - status: Status (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED)
  - modality: Modalidade (ONLINE, IN_PERSON, HYBRID)
  - description: Descrição da aula
  - subject: Matéria/disciplina (nullable)
  - meeting_link: Link para aula online
  - difficulties: Dificuldades do aluno
  - condition: Condição do aluno
  - created_at, created_by, last_modified_by, last_modified_at

#### `classes`

- **Campos:** id, date, time, student_id, created_at, updated_at, created_by, last_modified_by, last_modified_at
- **Relacionamentos:** `student_id` → `students.id`

#### `class_history`

- **Campos:** id, student_id, teacher_id, class_id, comment, created_at
- **Relacionamentos:**
  - `student_id` → `users.id`
  - `teacher_id` → `users.id`
  - `class_id` → `classes.id` (nullable)

### Migrações (Flyway)

O projeto usa Flyway para controle de versão do banco. Migrações disponíveis:

```
src/main/resources/db/migration/
├── V1__Initial_Schema.sql
├── V2__Create_Core_Tables.sql
├── V3__Add_Triggers_And_Indexes.sql
├── V4__Create_Classes_Table.sql
├── V5__Add_Missing_Columns_To_Schedules.sql
├── V6__Add_Difficulties_Condition_To_Schedules.sql
├── V7__Create_Class_History_Table.sql
├── V8__Add_UserId_To_Students.sql
├── V9__Fix_Schedule_Student_FK.sql
└── V10__Make_Schedules_Subject_Nullable.sql
```

---

## Endpoints da API

### Autenticação

- `POST /api/auth/login` - Login de usuários
- `POST /api/auth/register` - Registro de novos usuários
- `POST /api/auth/refresh` - Renovar token JWT

### Responsáveis (Guardians)

- `POST /api/guardians/register` - Cadastrar responsável
- `GET /api/guardians/{id}` - Buscar responsável
- `PUT /api/guardians/{id}` - Atualizar responsável
- `DELETE /api/guardians/{id}` - Deletar responsável
- `GET /api/guardians` - Listar todos responsáveis
- `GET /api/guardians/me` - Buscar responsável autenticado
- `GET /api/guardians/children` - Listar filhos do responsável
- `GET /api/guardians/history` - Histórico de aulas do aluno

### Professores

- `POST /api/professors/register` - Cadastrar professor
- `GET /api/professors/{id}` - Buscar professor
- `PUT /api/professors/{id}` - Atualizar professor
- `DELETE /api/professors/{id}` - Deletar professor
- `GET /api/professors` - Listar todos professores
- `GET /api/professors/me` - Buscar professor autenticado

### Alunos

- `GET /api/students` - Listar todos alunos
- `POST /api/students/register` - Cadastrar novo aluno
- `GET /api/students/{id}` - Buscar aluno por ID
- `PUT /api/students/{id}` - Atualizar aluno
- `DELETE /api/students/{id}` - Deletar aluno
- `GET /api/students/teacher/{teacherId}` - Listar alunos por professor

### Agendamentos

- `POST /api/schedules` - Criar novo agendamento
- `GET /api/schedules` - Listar agendamentos
- `GET /api/schedules/{id}` - Buscar agendamento por ID
- `PUT /api/schedules/{id}` - Atualizar agendamento
- `DELETE /api/schedules/{id}` - Cancelar agendamento
- `POST /api/schedules/book` - Agendar aula
- `GET /api/schedules/teacher/{id}` - Listar agendamentos do professor
- `GET /api/schedules/student/{id}` - Listar agendamentos do aluno
- `GET /api/schedules/with-students` - Listar horários com alunos
- `PUT /api/schedules/{scheduleId}/status` - Atualizar status do agendamento

### Histórico de Aulas

- `POST /api/history` - Salvar histórico de aula
- `GET /api/history` - Buscar histórico por aluno (parâmetro: studentId)

---

## Funcionalidades Especiais

### Cadastro de Alunos

- **E-mail e senha não são obrigatórios para alunos**
- Se não for informado um e-mail no cadastro, o backend irá gerar automaticamente um e-mail único no formato:
  ```
  aluno<ID>@aluno.espacoconstruir.com.br
  ```
- Esse e-mail é apenas para identificação interna e não deve ser usado para login

### Agendamento de Aulas Coletivas

- O sistema suporta agendamento de aulas coletivas
- Múltiplos alunos podem ser agendados para o mesmo horário
- Endpoint `/api/schedules/book` permite agendar vários alunos de uma vez

### Histórico de Aulas

- Sistema de histórico para registrar comentários sobre aulas
- Professores podem adicionar observações sobre o progresso dos alunos
- Histórico vinculado a alunos, professores e aulas específicas

---

## Desenvolvimento

### Portas

- **Frontend:** `http://localhost:5173`
- **Backend:** `http://localhost:8080`
- **PostgreSQL:** `localhost:5432`

### Variáveis de Ambiente para Desenvolvimento

Crie um arquivo `.env` na raiz do projeto:

```env
DB_URL=jdbc:postgresql://localhost:5432/tutoring_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=sua_chave_secreta_aqui
ALLOWED_ORIGINS=http://localhost:5173
```

### Ferramentas Recomendadas

- **IDE:** IntelliJ IDEA ou VS Code
- **API Testing:** Postman ou Insomnia
- **Database:** DBeaver ou pgAdmin
- **Docker:** Docker Desktop

### Dicas de Desenvolvimento

- Use o Postman/Insomnia para testar os endpoints
- Todos os endpoints estão sob `/api/*`
- As respostas são em formato JSON
- Consulte os logs do Spring Boot para debugging
- Use o token JWT no header `Authorization: Bearer <token>`
- O sistema tem logs detalhados para debugging de autenticação JWT

### Comandos Úteis

```bash
# Build do projeto
./mvnw clean package

# Executar testes
./mvnw test

# Executar migrações Flyway
./mvnw flyway:migrate

# Limpar banco (cuidado!)
./mvnw flyway:clean
```

---

## Docker

### Build da Imagem

```bash
docker build -t espaco-construir-backend .
```

### Executar com Docker Compose

```bash
# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Parar serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

---

## Segurança

- **JWT Authentication:** Todos os endpoints (exceto login/registro) requerem autenticação
- **CORS:** Configurado para permitir apenas origens específicas
- **Password Encoding:** Senhas são criptografadas com BCrypt
- **Role-based Access:** Sistema de roles (RESPONSAVEL, PROFESSOR, ALUNO)

---

## Contato e Suporte

Se precisar de ajuda:

1. Consulte este README
2. Verifique os logs da aplicação
3. Pergunte para o time
4. Verifique o histórico de commits

Você está aprendendo, não desista! 🚀

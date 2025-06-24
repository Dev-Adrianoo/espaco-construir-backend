# 🔄 BACKUP COMPLETO - ESPAÇO CONSTRUIR BACKEND

## 📋 Informações do Backup

**Data do Backup:** $(Get-Date -Format "dd/MM/yyyy HH:mm:ss")
**Versão:** v1.0.0-stable
**Status:** ✅ FUNCIONANDO PERFEITAMENTE

---

## 🎯 Repositório GitHub

**URL:** https://github.com/Dev-Adrianoo/espaco-construir-backend.git
**Branch Principal:** main
**Tag de Backup:** v1.0.0-stable

---

## 🚀 Como Clonar e Executar

### 1. Clonar o Repositório

```bash
git clone https://github.com/Dev-Adrianoo/espaco-construir-backend.git
cd espaco-construir-backend
```

### 2. Verificar a Versão Estável

```bash
git checkout v1.0.0-stable
```

### 3. Executar com Docker (Recomendado)

```bash
# Windows PowerShell
.\start.ps1

# Linux/Mac
docker-compose up -d
```

### 4. Executar Manualmente

```bash
# Instalar dependências
./mvnw clean install

# Executar aplicação
./mvnw spring-boot:run
```

---

## 📁 Estrutura do Projeto

```
espaco-construir-backend/
├── src/main/java/br/com/espacoconstruir/tutoring_backend/
│   ├── config/           # Configurações (CORS, Security, Password)
│   ├── controller/       # Endpoints REST
│   ├── dto/             # Objetos de transferência de dados
│   ├── exception/       # Tratamento de exceções
│   ├── model/           # Entidades JPA
│   ├── repository/      # Interfaces para acesso ao banco
│   ├── security/        # Filtros JWT
│   └── service/         # Lógica de negócio
├── src/main/resources/
│   ├── application.yml  # Configurações da aplicação
│   └── db/migration/    # Migrações Flyway (V1-V10)
├── docker-compose.yml   # Configuração Docker
├── Dockerfile          # Build da aplicação
├── pom.xml             # Dependências Maven
└── README.md           # Documentação completa
```

---

## 🔧 Configurações Importantes

### Variáveis de Ambiente (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tutoring_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    baseline-on-migrate: true

server:
  port: 8080

jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
  expiration: 86400000

cors:
  allowed-origins: http://localhost:5173
```

### Portas Utilizadas

- **Backend:** 8080
- **PostgreSQL:** 5432
- **Frontend:** 5173

---

## 📊 Funcionalidades Implementadas

### ✅ Autenticação e Segurança

- JWT Authentication
- Spring Security
- CORS configurado
- Password encoding com BCrypt

### ✅ Usuários e Roles

- RESPONSAVEL (Responsáveis)
- PROFESSOR (Professores)
- ALUNO (Alunos)

### ✅ Endpoints Principais

- **Autenticação:** `/api/auth/*`
- **Responsáveis:** `/api/guardians/*`
- **Professores:** `/api/professors/*`
- **Alunos:** `/api/students/*`
- **Agendamentos:** `/api/schedules/*`
- **Histórico:** `/api/history/*`

### ✅ Banco de Dados

- PostgreSQL 15
- 10 migrações Flyway
- Tabelas: users, students, schedules, classes, class_history

### ✅ Funcionalidades Especiais

- Agendamento de aulas coletivas
- Histórico de aulas com comentários
- E-mail automático para alunos
- Sistema de status de agendamentos

---

## 🐳 Docker

### Comandos Docker

```bash
# Build da imagem
docker build -t espaco-construir-backend .

# Executar com Docker Compose
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Parar serviços
docker-compose down
```

---

## 🔍 Troubleshooting

### Problemas Comuns

1. **Porta 8080 em uso:**

   ```bash
   # Windows
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F

   # Linux/Mac
   lsof -i :8080
   kill -9 <PID>
   ```

2. **Banco não conecta:**

   ```bash
   # Verificar se PostgreSQL está rodando
   docker-compose ps

   # Reiniciar serviços
   docker-compose restart
   ```

3. **Migrações com erro:**
   ```bash
   # Limpar e recriar banco
   ./mvnw flyway:clean
   ./mvnw flyway:migrate
   ```

---

## 📝 Logs Importantes

### Verificar se está funcionando

```bash
# Logs da aplicação
docker-compose logs -f backend

# Ou se executando localmente
./mvnw spring-boot:run
```

### Logs esperados no startup:

```
Hibernate: create table users...
Flyway: Successfully applied 10 migrations
Started EspacoConstruirBackendApplication
```

---

## 🎯 Status Atual

- ✅ **Backend funcionando perfeitamente**
- ✅ **Docker configurado e testado**
- ✅ **Banco de dados com todas as migrações**
- ✅ **Autenticação JWT funcionando**
- ✅ **Todos os endpoints testados**
- ✅ **README atualizado e completo**

---

## 📞 Suporte

Se precisar de ajuda:

1. Verifique este arquivo BACKUP_INFO.md
2. Consulte o README.md principal
3. Verifique os logs da aplicação
4. Acesse o repositório: https://github.com/Dev-Adrianoo/espaco-construir-backend

---

**🎉 BACKUP CRIADO COM SUCESSO!**

Agora você pode clonar este repositório em qualquer máquina e terá exatamente a mesma versão funcionando!

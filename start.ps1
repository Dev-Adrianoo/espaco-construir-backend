function Test-PostgresConnection {
  try {
    $result = docker exec tutoring_db pg_isready
    return $result -like "*accepting connections*"
  }
  catch {
    return $false
  }
}

Write-Host ">>> Iniciando container Docker do PostgreSQL..." -ForegroundColor Cyan

$containerExists = docker ps -a --filter "name=tutoring_db" --format "{{.Names}}"

if ($containerExists) {
  Write-Host ">>> Container tutoring_db ja existe, iniciando..." -ForegroundColor Yellow
  docker start tutoring_db
}
else {
  Write-Host ">>> Criando novo container tutoring_db..." -ForegroundColor Green
  docker run --name tutoring_db -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=tutoring_db -p 5432:5432 -d postgres:15
}

Write-Host ">>> Aguardando PostgreSQL ficar pronto..." -ForegroundColor Cyan
$attempts = 0
$maxAttempts = 30

while (-not (Test-PostgresConnection)) {
  $attempts++
  if ($attempts -ge $maxAttempts) {
    Write-Host ">>> ERRO: Tempo limite excedido aguardando PostgreSQL" -ForegroundColor Red
    exit 1
  }
  Write-Host "." -NoNewline
  Start-Sleep -Seconds 1
}

Write-Host "`n>>> PostgreSQL esta pronto!" -ForegroundColor Green
Write-Host ">>> Iniciando aplicacao Spring Boot..." -ForegroundColor Cyan

# Inicia a aplicação Spring Boot
./mvnw spring-boot:run 
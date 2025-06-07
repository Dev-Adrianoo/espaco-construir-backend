chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host ""
$line = "====================================================="
Write-Host $line -ForegroundColor DarkCyan
Write-Host "        ESPACO CONSTRUIR BACKEND - DEV ENV           " -ForegroundColor Cyan
Write-Host $line -ForegroundColor DarkCyan
Write-Host ""

function Test-PostgresConnection {
  try {
    $result = docker exec tutoring_db pg_isready
    return $result -like "*accepting connections*"
  }
  catch {
    return $false
  }
}

Write-Host ""
Write-Host "Iniciando servicos Docker com Docker Compose..." -ForegroundColor Cyan
docker-compose up -d > $null 2>&1

Write-Host ""
Write-Host "Aguardando PostgreSQL ficar pronto..." -ForegroundColor Cyan
Start-Sleep -Seconds 5
$attempts = 0
$maxAttempts = 60

while (-not (Test-PostgresConnection)) {
  $attempts++
  if ($attempts -ge $maxAttempts) {
    Write-Host "ERRO: Tempo limite excedido aguardando PostgreSQL" -ForegroundColor Red
    exit 1
  }
  Write-Host "." -NoNewline
  Start-Sleep -Seconds 1
}

Write-Host ""
Write-Host "PostgreSQL esta pronto!" -ForegroundColor Green
Write-Host ""

Write-Host "Garantindo que o banco tutoring_db existe..." -ForegroundColor Cyan
$checkDb = docker exec tutoring_db psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='tutoring_db'"
if ($checkDb -ne "1") {
  docker exec tutoring_db psql -U postgres -c "CREATE DATABASE tutoring_db"
  Write-Host "Banco tutoring_db criado." -ForegroundColor Green
} else {
  Write-Host "Banco tutoring_db ja existe." -ForegroundColor Yellow
}
Write-Host ""

Write-Host "Iniciando aplicacao Spring Boot..." -ForegroundColor Cyan
Write-Host $line -ForegroundColor DarkCyan
Write-Host ""

Start-Sleep -Seconds 5

./mvnw spring-boot:run
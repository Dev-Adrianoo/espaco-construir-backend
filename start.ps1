chcp 65001 > $null
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host ""
$line = "====================================================="
Write-Host $line -ForegroundColor DarkCyan
Write-Host "        ESPACO CONSTRUIR BACKEND - DEV ENV           " -ForegroundColor Cyan
Write-Host $line -ForegroundColor DarkCyan
Write-Host ""

Write-Host ""
Write-Host "Iniciando servicos Docker com Docker Compose..." -ForegroundColor Cyan
docker-compose up -d --build > $null 2>&1

Write-Host ""
Write-Host "Aguardando o banco de dados estar pronto..."
Start-Sleep -Seconds 10

$attempts = 0
$maxAttempts = 60
while ($true) {
  $result = docker exec tutoring_db psql -U postgres -d tutoring_db -c "SELECT 1;" 2>&1
  if ($result -match "1 row") {
    Write-Host "Banco tutoring_db está pronto para uso!"
    break
  }
  $attempts++
  if ($attempts -ge $maxAttempts) {
    Write-Host "ERRO: Tempo limite excedido aguardando o banco aceitar conexões" -ForegroundColor Red
    exit 1
  }
  Write-Host "Aguardando banco aceitar conexões... Tentativa $attempts de $maxAttempts"
  Start-Sleep -Seconds 2
}

Write-Host "Verificando se o banco tutoring_db existe..." -ForegroundColor Cyan
$checkDb = docker exec tutoring_db psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='tutoring_db'"
if ($checkDb -ne "1") {
  Write-Host "Banco tutoring_db nao existe. Criando..." -ForegroundColor Yellow
  docker exec tutoring_db psql -U postgres -c "CREATE DATABASE tutoring_db"
  Write-Host "Banco tutoring_db criado com sucesso!" -ForegroundColor Green
}
else {
  Write-Host "Banco tutoring_db ja existe." -ForegroundColor Green
}
Write-Host ""

Write-Host "Backend e banco estão rodando via Docker Compose!" -ForegroundColor Cyan
Write-Host $line -ForegroundColor DarkCyan
Write-Host ""
Write-Host "Acesse o backend em http://localhost:8080" -ForegroundColor Green
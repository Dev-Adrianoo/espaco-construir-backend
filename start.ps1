$ErrorActionPreference = "Stop"


$startDir = Get-Location


$evoProjectDirName = "evolution-api"


function Cleanup-Container {
    param (
        [string]$containerName
    )

    if (docker ps -a -q -f "name=^/$($containerName)$") {
        Write-Host "🧹 Removendo contêiner antigo '$containerName'..."
        docker rm -f $containerName
    }
}


Write-Host "🧹 Iniciando limpeza geral de contêineres..."
Cleanup-Container -containerName "evolution_api"
Cleanup-Container -containerName "postgres"
Cleanup-Container -containerName "redis"
Cleanup-Container -containerName "espaco_construir_backend"
Cleanup-Container -containerName "tutoring_db"
Write-Host "✅ Limpeza geral concluída."


Write-Host ""
Write-Host "====================================================="
Write-Host "   INICIANDO ETAPA 1: EVOLUTION API & REDE COMPARTILHADA "
Write-Host "====================================================="
Write-Host ""


Set-Location ..


if (-not (Test-Path -Path $evoProjectDirName)) {
    git clone "https://github.com/EvolutionAPI/evolution-api.git" "$evoProjectDirName"
} 


Set-Location "$evoProjectDirName"


docker compose down --remove-orphans *> $null


Write-Host "🚀 Iniciando contêineres da Evolution API e criando a rede 'evolution-net'..."
docker compose up --build -d

Write-Host "⏳ Aguardando o n8n estar pronto..."


Set-Location "$startDir"


Write-Host ""
Write-Host "====================================================="
Write-Host "        INICIANDO ETAPA 2: SEU BACKEND               "
Write-Host "====================================================="
Write-Host ""

docker compose down --remove-orphans *> $null


Write-Host "🚀 Iniciando contêineres do seu backend e conectando à 'evolution-net'..."
docker compose up --build -d

Write-Host "⏳ Aguardando o banco do backend estar pronto..."

Write-Host ""
Write-Host "====================================================="
Write-Host "🎉 AMBIENTE COMPLETO NO AR! 🎉"
Write-Host "===> ABRA UM TERMINAL E RODE O COMANDO NPX NGROK HTTP 8080"
Write-Host "====================================================="
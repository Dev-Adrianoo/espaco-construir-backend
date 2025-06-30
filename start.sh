#!/bin/bash
# ==============================================================================
#      SCRIPT MESTRE DEFINITIVO - ESPAÇO CONSTRUIR DEV ENV
# ==============================================================================

# --- SEÇÃO DE CONFIGURAÇÃO E FUNÇÕES ---

# Garante que o script pare se algum comando crítico falhar (re-ativado)
set -e

# Carrega variáveis de um arquivo .env LOCAL, que NÃO vai para o Git.
if [ -f ".env" ]; then
    echo "🔑 Carregando variáveis de ambiente do arquivo .env..."
    export $(grep -v '^#' .env | xargs)
fi

# Variáveis globais
START_DIR=$(pwd)
SHARED_NETWORK_NAME="espacoconstruir_net"
EVO_PROJECT_DIR_NAME="evolution-api"

# Função de Limpeza de Container
cleanup_container() {
    CONTAINER_NAME=$1
    # Verifica se o container existe (rodando ou parado)
    if [ $(docker ps -a -q -f name=^/${CONTAINER_NAME}$) ]; then
        echo "🧹 Encontrado container antigo '$CONTAINER_NAME'. Forçando remoção..."
        docker rm -f $CONTAINER_NAME
    fi
}

# ==============================================================================
#       ETAPA 0: PREPARAÇÃO DO AMBIENTE
# ==============================================================================

echo "🌐 Verificando a rede compartilhada '$SHARED_NETWORK_NAME'..."
if [ -z "$(docker network ls --filter name=^${SHARED_NETWORK_NAME}$ -q)" ]; then
    echo "⚠️  Rede '$SHARED_NETWORK_NAME' não encontrada. Criando..."
    docker network create $SHARED_NETWORK_NAME
    echo "✅ Rede '$SHARED_NETWORK_NAME' criada com sucesso."
else
    echo "✅ Rede '$SHARED_NETWORK_NAME' já existe."
fi

echo "🧹 Iniciando limpeza geral de containers..."
cleanup_container evolution_api
cleanup_container postgres
cleanup_container redis
cleanup_container espaco_construir_backend
cleanup_container tutoring_db
echo "✅ Limpeza geral concluída."


# ==============================================================================
#       ETAPA 1: EVOLUTION API
# ==============================================================================
echo ""
echo "====================================================="
echo "        INICIANDO ETAPA 1: EVOLUTION API             "
echo "====================================================="
echo ""

# Navega para o diretório pai
cd ..

if [ ! -d "$EVO_PROJECT_DIR_NAME" ]; then
    echo "⬇️  Clonando o projeto Evolution API..."
    git clone "https://github.com/EvolutionAPI/evolution-api.git" "$EVO_PROJECT_DIR_NAME"
fi

cd "$EVO_PROJECT_DIR_NAME"
echo "📍 Acessando o diretório: $(pwd)"

# Limpa a composição antiga da evolution, se houver
docker compose down --remove-orphans > /dev/null 2>&1 || true

# Cria o .env da evolution se não existir
if [ ! -f ".env" ]; then
    cp .env.example .env
    sed -i "s#^CACHE_REDIS_URI=.*#CACHE_REDIS_URI=redis://redis:6379/6#" .env
fi

echo "🚀 Iniciando containers da Evolution API..."
docker compose up --build -d


# ==============================================================================
#       ETAPA 2: BACKEND ESPAÇO CONSTRUIR
# ==============================================================================
cd "$START_DIR"

echo ""
echo "====================================================="
echo "        INICIANDO ETAPA 2: SEU BACKEND               "
echo "====================================================="
echo ""

# Limpa a composição antiga do backend, se houver
docker compose down --remove-orphans > /dev/null 2>&1 || true

echo "🚀 Iniciando containers do seu backend..."
docker compose up --build -d

echo "⏳ Aguardando o banco de dados estar pronto..."
sleep 10
attempts=0
maxAttempts=60
while true; do
  if docker exec tutoring_db psql -U postgres -d tutoring_db -c "SELECT 1;" > /dev/null 2>&1; then
    echo "✅ Banco tutoring_db está pronto para uso!"
    break
  fi
  attempts=$((attempts + 1))
  if [ $attempts -ge $maxAttempts ]; then
    echo "❌ ERRO: Tempo limite excedido aguardando o banco."
    exit 1
  fi
  echo "Aguardando... Tentativa $attempts"
  sleep 2
done


# ==============================================================================
#       FINALIZAÇÃO
# ==============================================================================
echo ""
echo "====================================================="
echo "🎉 AMBIENTE COMPLETO NO AR! 🎉"
echo ""
echo "  - Seu Backend está rodando em http://localhost:8081"
echo "  - A Evolution API está rodando na porta 8080"
echo ""
echo "  --> AGORA ABRA UM NOVO TERMINAL E RODE O COMANDO: npx ngrok http 8080"
echo ""
echo "====================================================="
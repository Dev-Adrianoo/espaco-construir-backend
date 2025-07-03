#!/bin/bash

set -e
START_DIR=$(pwd)
EVO_PROJECT_DIR_NAME="evolution-api"


cleanup_container() {
    CONTAINER_NAME=$1
    if [ $(docker ps -a -q -f name=^/${CONTAINER_NAME}$) ]; then
        echo "🧹 Removendo container antigo '$CONTAINER_NAME'..."
        docker rm -f $CONTAINER_NAME
    fi
}

echo "🧹 Iniciando limpeza geral de containers..."
cleanup_container evolution_api; cleanup_container postgres; cleanup_container redis; cleanup_container espaco_construir_backend; cleanup_container tutoring_db
echo "✅ Limpeza geral concluída."


echo ""
echo "====================================================="
echo "   INICIANDO ETAPA 1: EVOLUTION API & REDE COMPARTILHADA "
echo "====================================================="
echo ""
cd ..
if [ ! -d "$EVO_PROJECT_DIR_NAME" ]; then
    git clone "https://github.com/EvolutionAPI/evolution-api.git" "$EVO_PROJECT_DIR_NAME"
fi
cd "$EVO_PROJECT_DIR_NAME"
docker compose down --remove-orphans > /dev/null 2>&1 || true
echo "🚀 Iniciando containers da Evolution API e criando a rede 'evolution-net'..."
docker compose up --build -d

echo "⏳ Aguardando o n8n estar pronto..."

cd "$START_DIR"
echo ""
echo "====================================================="
echo "        INICIANDO ETAPA 2: SEU BACKEND               "
echo "====================================================="
echo ""
docker compose down --remove-orphans > /dev/null 2>&1 || true
echo "🚀 Iniciando containers do seu backend e conectando à 'evolution-net'..."
docker compose up --build -d

#
echo "⏳ Aguardando o banco do backend estar pronto..."



echo ""
echo "====================================================="
echo "🎉 AMBIENTE COMPLETO NO AR! 🎉"
echo "===> ABRA UM TERMINAL E RODE O COMANDO NPX NGROK HTTP 8080"
echo "====================================================="
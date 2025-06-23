#!/bin/bash

# Configurar encoding UTF-8
export LANG=C.UTF-8
export LC_ALL=C.UTF-8

echo ""
echo "====================================================="
echo "        ESPACO CONSTRUIR BACKEND - DEV ENV           "
echo "====================================================="
echo ""

echo ""
echo "Iniciando servicos Docker com Docker Compose..."
docker compose up -d --build > /dev/null 2>&1

echo ""
echo "Aguardando o banco de dados estar pronto..."
sleep 10

attempts=0
maxAttempts=60
while true; do
  result=$(docker exec tutoring_db psql -U postgres -d tutoring_db -c "SELECT 1;" 2>&1)
  if echo "$result" | grep -q "1 row"; then
    echo "Banco tutoring_db está pronto para uso!"
    break
  fi
  attempts=$((attempts + 1))
  if [ $attempts -ge $maxAttempts ]; then
    echo "ERRO: Tempo limite excedido aguardando o banco aceitar conexões"
    exit 1
  fi
  echo "Aguardando banco aceitar conexões... Tentativa $attempts de $maxAttempts"
  sleep 2
done

echo "Verificando se o banco tutoring_db existe..."
checkDb=$(docker exec tutoring_db psql -U postgres -tAc "SELECT 1 FROM pg_database WHERE datname='tutoring_db'")
if [ "$checkDb" != "1" ]; then
  echo "Banco tutoring_db nao existe. Criando..."
  docker exec tutoring_db psql -U postgres -c "CREATE DATABASE tutoring_db"
  echo "Banco tutoring_db criado com sucesso!"
else
  echo "Banco tutoring_db ja existe."
fi
echo ""

echo "Backend e banco estão rodando via Docker Compose!"
echo "====================================================="
echo ""
echo "Acesse o backend em http://localhost:8080" 
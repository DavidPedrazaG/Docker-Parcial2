#!/bin/bash

# Colores y emojis
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color
CHECK="✅"
CROSS="❌"
DOCKER="🐳"
ROCKET="🚀"
WRENCH="🔧"
WAIT="⏳"

echo -e "${DOCKER} ${WAIT} Levantando base de datos..."
docker compose up -d db

echo -e "${DOCKER} ${WAIT} Esperando que la base de datos esté saludable..."
while [ "$(docker inspect -f '{{.State.Health.Status}}' database)" != "healthy" ]; do
    sleep 2
    echo -n "."
done
echo -e "\n${CHECK} Base de datos lista."

echo -e "${WRENCH} Ejecutando tests..."
docker compose run --rm app-tests
TEST_EXIT_CODE=$?

if [ $TEST_EXIT_CODE -ne 0 ]; then
  echo -e "${CROSS} ${RED}Los tests fallaron. La aplicación no será desplegada.${NC}"
  exit 1
fi

echo -e "${CHECK} ${GREEN}Tests pasaron correctamente.${NC}"

echo -e "${ROCKET} Lanzando la aplicación..."
docker compose up -d app

echo -e "${CHECK} ${GREEN}Aplicación desplegada exitosamente en http://localhost:8080${NC}"
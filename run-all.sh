#!/usr/bin/env bash
set -euo pipefail

# HTTP ports and corresponding modules
PORTS=(8080 8081 8082 8084 8085)
MODULES=(
  catalogo-service
  pedido-service
  pagamento-service
  fiscal-service
  estoque-service
)

# Cleanup function: kill processes by port on Ctrl+C
cleanup() {
  echo -e "\n🛑 Parando serviços nas portas: ${PORTS[*]}..."
  for port in "${PORTS[@]}"; do
    pids=$(lsof -ti TCP:"$port" 2>/dev/null || true)
    if [[ -n "$pids" ]]; then
      echo "🔪 Matando processo(s) na porta $port: $pids"
      kill $pids
    fi
  done
  exit 0
}

trap cleanup SIGINT SIGTERM

# 1) Build all modules
echo "🔨 Buildando todos os micro-serviços..."
mvn clean install -q

echo "🚀 Iniciando micro-serviços com Maven..."

# 2) Start each module with spring-boot:run
for i in "${!MODULES[@]}"; do
  svc=${MODULES[$i]}
  port=${PORTS[$i]}
  echo "📦 Subindo ${svc} na porta HTTP ${port}..."
  mvn -pl "${svc}" spring-boot:run -Dspring-boot.run.arguments="--server.port=${port}" &
done

# 3) Wait for all to finish (allows cleanup trap)
wait

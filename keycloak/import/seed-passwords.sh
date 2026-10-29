#!/bin/sh
# Keycloak 26 no hashea las credenciales en formato "value" plano durante --import-realm
# al boot (se importan realm/roles/clientes/usuarios, pero el usuario queda SIN credencial
# real: "disableableCredentialTypes" vacio y login falla con "Account is not fully set up").
# Este script corre despues de que Keycloak esta arriba y fija las contrasenas via Admin REST API.
#
# Uso: ./seed-passwords.sh  (requiere curl y python3 en el PATH)
#
# Correr una sola vez despues de `docker compose -f compose-dev.yml up -d` (o cada vez que se
# recree el volumen de Postgres de Keycloak desde cero).

set -e

KC_URL="${KC_URL:-http://localhost:18180}"
ADMIN_USER="${KC_ADMIN_USER:-admin}"
ADMIN_PASS="${KC_ADMIN_PASS:-admin123}"
REALM="lamb"

echo "Esperando a que Keycloak responda en $KC_URL ..."
for i in $(seq 1 30); do
  if curl -s -o /dev/null -w "%{http_code}" "$KC_URL/realms/$REALM" 2>/dev/null | grep -q "200\|403"; then
    break
  fi
  sleep 3
done

ADMIN_TOKEN=$(curl -s -X POST "$KC_URL/realms/master/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=admin-cli" -d "username=$ADMIN_USER" -d "password=$ADMIN_PASS" -d "grant_type=password" \
  | python3 -c "import json,sys;print(json.load(sys.stdin)['access_token'])")

set_password() {
  username="$1"
  password="$2"
  uid=$(curl -s -H "Authorization: Bearer $ADMIN_TOKEN" "$KC_URL/admin/realms/$REALM/users?username=$username" \
    | python3 -c "import json,sys;print(json.load(sys.stdin)[0]['id'])")
  status=$(curl -s -o /dev/null -w "%{http_code}" -X PUT \
    -H "Authorization: Bearer $ADMIN_TOKEN" -H "Content-Type: application/json" \
    -d "{\"type\":\"password\",\"value\":\"$password\",\"temporary\":false}" \
    "$KC_URL/admin/realms/$REALM/users/$uid/reset-password")
  echo "$username: $status"
}

set_password admin admin123
set_password docente1 docente123
set_password estudiante1 estudiante123

echo "Listo. Los 3 usuarios semilla ya pueden loguearse."

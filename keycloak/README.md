# Keycloak - Lamb

Reemplaza a `lamb-auth-ms` (JWT propio) como emisor de tokens. Basado en la guia S07b, adaptado al dominio Lamb.

## Levantar

```bash
cd keycloak
docker compose -f compose-dev.yml up -d
```

El realm `lamb` se importa solo al primer arranque (`--import-realm` + `./import/lamb-realm.json`), no hace falta crearlo a mano en la consola.

Consola admin: http://localhost:18180 (`admin` / `admin123`).

## Que trae el import

- Realm `lamb` con roles: `ADMIN`, `COORDINADOR`, `DOCENTE`, `ESTUDIANTE` (los mismos que ya usaba el Gateway en `hasAnyRole(...)`).
- Client `lamb-frontend`: publico, PKCE S256 obligatorio, Standard flow, redirect `http://localhost:4200/*` (puerto default de `ng serve`, para S11).
- Protocol mapper `idPersona`: mapea el *user attribute* `idPersona` a un claim `idPersona` (tipo `long`) en el token. **A proposito no se usa `sub`** para esto (divergencia intencional respecto a la Parte C de la guia S07b): `idPersona` es la PK real de `Persona` en `lamb-personas-ms` y la usan 6 microservicios mas; reemplazarla por el `sub` (UUID de Keycloak) hubiera obligado a migrar esa PK en cascada por todo el sistema. Se queda como atributo separado, igual que el enfoque original de S07.
- 3 usuarios semilla: `admin` (rol ADMIN, idPersona=1), `docente1` (rol DOCENTE, idPersona=2), `estudiante1` (rol ESTUDIANTE, idPersona=3).

**Importante:** los valores de `idPersona` (1, 2, 3) son placeholders. Antes de probar flujos reales, actualiza el atributo del usuario en Keycloak (Users -> usuario -> Attributes) para que coincida con un `id` real de la tabla `personas` en `lamb-personas-ms`.

## Gateway y servicios

Ya migrados a `issuer-uri` (ver `infra/lamb-config/config-repo/*-dev.yml` / `*-prod.yml`): `lamb-gateway`, `lamb-admision-ms`, `lamb-curriculo-ms`, `lamb-evaluacion-ms`, `lamb-planificacion-ms`, `lamb-trayectoria-ms`. El `JwtAuthenticationConverter` del Gateway no se toco (ya leia `realm_access.roles`, formato Keycloak).

`lamb-auth-ms` no se porto a este repo: queda retirado, reemplazado directo por Keycloak.

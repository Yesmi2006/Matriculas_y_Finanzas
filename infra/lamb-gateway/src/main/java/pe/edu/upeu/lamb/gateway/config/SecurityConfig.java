package pe.edu.upeu.lamb.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
public class SecurityConfig {

    // Rutas de gestion academica (coordinacion/admision/curriculo/evaluacion/trayectoria)
    private static final String[] ACADEMICO = {
            "/api/v1/planes-estudio/**", "/api/v1/cursos/**", "/api/v1/postulaciones/**"
    };

    // Matricula: acciones del propio estudiante (matricularse, reservar cupo, pedir excepcion)
    private static final String[] MATRICULA = {
            "/api/v1/matriculas/**", "/api/v1/reservas-vacante/**", "/api/v1/solicitudes-excepcion/**"
    };

    // Matricula: catalogos de configuracion (periodos, tipos, cronogramas, secciones, motivos, limites)
    private static final String[] MATRICULA_CATALOGOS = {
            "/api/v1/periodos-academicos/**", "/api/v1/tipos-matricula/**", "/api/v1/cronogramas-matricula/**",
            "/api/v1/secciones/**", "/api/v1/motivos-anulacion/**", "/api/v1/limites-creditos/**"
    };

    // Rutas institucionales y de personas: lectura abierta a cualquier autenticado, escritura restringida
    private static final String[] INSTITUCIONAL_PERSONAS = {
            "/api/v1/facultades/**", "/api/v1/programas/**", "/api/v1/personas/**"
    };

    // Finanzas: pagos del estudiante y recursos operativos derivados (cuotas, refinanciamiento,
    // descuentos, documentos, transacciones de pasarela)
    private static final String[] FINANZAS = {
            "/api/v1/pagos/**", "/api/v1/planes-refinanciamiento/**", "/api/v1/descuentos/**",
            "/api/v1/documentos-pago/**", "/api/v1/transacciones-pasarela/**"
    };

    // Finanzas: catalogos de configuracion (conceptos, metodos de pago, mora, motivos)
    private static final String[] FINANZAS_CATALOGOS = {
            "/api/v1/conceptos-pago/**", "/api/v1/metodos-pago/**", "/api/v1/configuraciones-mora/**",
            "/api/v1/motivos-pago/**"
    };

    // realm_access.roles (formato Keycloak) -> ROLE_<rol> para hasRole(...)
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            Object roles = realmAccess == null ? null : realmAccess.get("roles");
            if (!(roles instanceof Collection<?> lista)) {
                return List.<GrantedAuthority>of();
            }
            return lista.stream()
                    .map(rol -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + rol))
                    .toList();
        });
        return converter;
    }

    // Primera linea: reglas por ruta, metodo y rol. Spring aplica la primera regla que coincide.
    // Segunda linea (JwtAuthenticationConverter + oauth2ResourceServer): la valida cada microservicio por su cuenta.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, ACADEMICO).authenticated()
                        .requestMatchers(ACADEMICO).hasAnyRole("ADMIN", "COORDINADOR")
                        .requestMatchers("/api/v1/actas/**").hasAnyRole("ADMIN", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/trayectorias/me").authenticated()
                        .requestMatchers("/api/v1/trayectorias/**").hasAnyRole("ADMIN", "COORDINADOR", "DOCENTE")
                        .requestMatchers(HttpMethod.GET, "/api/v1/notas/**").authenticated()
                        .requestMatchers("/api/v1/notas/**").hasAnyRole("ADMIN", "DOCENTE")
                        .requestMatchers(HttpMethod.GET, INSTITUCIONAL_PERSONAS).authenticated()
                        .requestMatchers(INSTITUCIONAL_PERSONAS).hasAnyRole("ADMIN", "COORDINADOR")
                        .requestMatchers(HttpMethod.GET, MATRICULA).authenticated()
                        .requestMatchers(MATRICULA).hasAnyRole("ADMIN", "COORDINADOR", "ESTUDIANTE")
                        .requestMatchers(HttpMethod.GET, MATRICULA_CATALOGOS).authenticated()
                        .requestMatchers(MATRICULA_CATALOGOS).hasAnyRole("ADMIN", "COORDINADOR")
                        .requestMatchers(HttpMethod.GET, FINANZAS).authenticated()
                        .requestMatchers(FINANZAS).hasAnyRole("ADMIN", "COORDINADOR")
                        .requestMatchers(HttpMethod.GET, FINANZAS_CATALOGOS).authenticated()
                        .requestMatchers(FINANZAS_CATALOGOS).hasAnyRole("ADMIN", "COORDINADOR")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );
        return http.build();
    }
}

# Pruebas guiadas S6-S9 del Modulo 02. Cargar en la consola:   . .\scripts\demo-pruebas.ps1
# Luego:  Demo-Ayuda

$script:G = 'http://localhost:18080'
$script:Raiz = Split-Path -Parent $PSScriptRoot

function Titulo([string]$texto) { Write-Host "`n=== $texto ===" -ForegroundColor Cyan }

function Codigo([scriptblock]$bloque) {
    try { & $bloque | Out-Null; return '2xx' } catch { return [string]$_.Exception.Response.StatusCode.value__ }
}

# Mensaje de error del backend; funciona en Windows PowerShell 5.1 y en PowerShell 7
function Error-Api($err) {
    $codigo = [string]$err.Exception.Response.StatusCode.value__
    $cuerpo = $err.ErrorDetails.Message
    if (-not $cuerpo) {
        try {
            $lector = New-Object System.IO.StreamReader($err.Exception.Response.GetResponseStream())
            $cuerpo = $lector.ReadToEnd()
        } catch { $cuerpo = '' }
    }
    $mensaje = $cuerpo
    try { $mensaje = ($cuerpo | ConvertFrom-Json).message } catch { }
    return "$codigo $mensaje"
}

function Get-Token([string]$email, [string]$password) {
    $body = @{ email = $email; password = $password } | ConvertTo-Json
    (Invoke-RestMethod -Method Post -Uri "$G/api/v1/auth/login" -ContentType 'application/json' -Body $body).access_token
}

function Demo-Login {
    Titulo 'S7 - Login por el Gateway (lamb-auth-ms emite JWT RS256)'
    $script:TokenAdmin = Get-Token 'admin@lamb.edu.pe' 'admin123'
    $script:TokenCoord = Get-Token 'coordinador@lamb.edu.pe' 'coordinador123'
    $script:TokenDocente = Get-Token 'docente@lamb.edu.pe' 'docente123'
    $script:TokenEst = Get-Token 'estudiante@lamb.edu.pe' 'estudiante123'
    $payload = $TokenCoord.Split('.')[1].Replace('-', '+').Replace('_', '/')
    while ($payload.Length % 4) { $payload += '=' }
    Write-Host 'Claims del token del COORDINADOR (pegalo en jwt.io para ver la firma):'
    $claims = [Text.Encoding]::UTF8.GetString([Convert]::FromBase64String($payload)) | ConvertFrom-Json
    [pscustomobject]@{ iss = $claims.iss; sub = $claims.sub; email = $claims.email; roles = ($claims.realm_access.roles -join ','); idPersona = $claims.idPersona
                       expira = [DateTimeOffset]::FromUnixTimeSeconds($claims.exp).LocalDateTime } | Format-List | Out-Host
    Write-Host "TokenCoord = $($TokenCoord.Substring(0, 30))..."
}

function Auth([string]$token) { @{ Authorization = "Bearer $token" } }

# Invoke-RestMethod devuelve un arreglo JSON como un solo objeto: se desenrolla aqui
function Lista([string]$url, [string]$token = $TokenCoord) {
    $r = Invoke-RestMethod $url -Headers (Auth $token)
    foreach ($x in $r) { $x }
}

function Demo-Seguridad {
    if (-not $TokenAdmin) { Demo-Login }
    Titulo 'S7 - Matriz de accesos (401 / 403 / 2xx)'
    $p = $TokenAdmin.Split('.')
    $primera = if ($p[2][0] -eq 'X') { 'Y' } else { 'X' }
    $alterado = "$($p[0]).$($p[1]).$primera$($p[2].Substring(1))"
    $curso = '{"codigo":"TMP01","nombre":"Curso de prueba","creditos":3}'
    $filas = @(
        @{ Caso = 'Sin token, GET /secciones (gateway)';            R = (Codigo { Invoke-RestMethod "$G/api/v1/secciones" }) ; E = '401' },
        @{ Caso = 'Firma alterada, GET /secciones (gateway)';       R = (Codigo { Invoke-RestMethod "$G/api/v1/secciones" -Headers (Auth $alterado) }) ; E = '401' },
        @{ Caso = 'ESTUDIANTE, POST /cursos (rol incorrecto)';      R = (Codigo { Invoke-RestMethod -Method Post "$G/api/v1/cursos" -Headers (Auth $TokenEst) -ContentType 'application/json' -Body $curso }) ; E = '403' },
        @{ Caso = 'ESTUDIANTE, GET /secciones';                     R = (Codigo { Invoke-RestMethod "$G/api/v1/secciones" -Headers (Auth $TokenEst) }) ; E = '2xx' },
        @{ Caso = 'ESTUDIANTE, GET /actas';                         R = (Codigo { Invoke-RestMethod "$G/api/v1/actas" -Headers (Auth $TokenEst) }) ; E = '403' },
        @{ Caso = 'DOCENTE, GET /actas';                            R = (Codigo { Invoke-RestMethod "$G/api/v1/actas" -Headers (Auth $TokenDocente) }) ; E = '2xx' },
        @{ Caso = 'Directo a planificacion :8083 sin token';        R = (Codigo { Invoke-RestMethod 'http://localhost:8083/api/v1/secciones' }) ; E = '401' },
        @{ Caso = 'Directo a planificacion :8083 token manipulado'; R = (Codigo { Invoke-RestMethod 'http://localhost:8083/api/v1/secciones' -Headers (Auth 'token.invalido.manipulado') }) ; E = '401' },
        @{ Caso = 'Directo a curriculo :8088 sin token';            R = (Codigo { Invoke-RestMethod 'http://localhost:8088/api/v1/planes-estudio' }) ; E = '401' },
        @{ Caso = 'Directo a evaluacion :8086 sin token';           R = (Codigo { Invoke-RestMethod 'http://localhost:8086/api/v1/actas' }) ; E = '401' },
        @{ Caso = 'Directo a evaluacion :8086 token valido';        R = (Codigo { Invoke-RestMethod 'http://localhost:8086/api/v1/actas' -Headers (Auth $TokenCoord) }) ; E = '2xx' },
        @{ Caso = 'ESTUDIANTE, POST /postulaciones/1/admitir';      R = (Codigo { Invoke-RestMethod -Method Post "$G/api/v1/postulaciones/1/admitir" -Headers (Auth $TokenEst) }) ; E = '403' },
        @{ Caso = 'ESTUDIANTE, GET /trayectorias (todas)';          R = (Codigo { Invoke-RestMethod "$G/api/v1/trayectorias" -Headers (Auth $TokenEst) }) ; E = '403' },
        @{ Caso = 'Directo a admision :8084 sin token';             R = (Codigo { Invoke-RestMethod 'http://localhost:8084/api/v1/postulaciones' }) ; E = '401' },
        @{ Caso = 'Directo a trayectoria :8089 sin token';          R = (Codigo { Invoke-RestMethod 'http://localhost:8089/api/v1/trayectorias' }) ; E = '401' }
    )
    $filas | ForEach-Object { [pscustomobject]@{ Caso = $_.Caso; Esperado = $_.E; Obtenido = $_.R; OK = ($_.E -eq $_.R) } } | Format-Table -AutoSize | Out-Host
}

function Preparar-Datos {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'Datos base: cursos en planificacion (IS101 esta en el plan vigente, IS201 no)'
    $existentes = @(Lista "$G/api/v1/cursos")
    foreach ($c in @(@{ codigo = 'IS101'; nombre = 'Programacion I'; creditos = 4 },
                     @{ codigo = 'IS201'; nombre = 'Programacion II'; creditos = 4 },
                     @{ codigo = 'IS301'; nombre = 'Base de Datos'; creditos = 4 })) {
        if (-not ($existentes | Where-Object codigo -eq $c.codigo)) {
            Invoke-RestMethod -Method Post "$G/api/v1/cursos" -Headers (Auth $TokenCoord) -ContentType 'application/json' -Body ($c | ConvertTo-Json) | Out-Null
        }
    }
    Lista "$G/api/v1/cursos" | Format-Table | Out-Host
    Lista 'http://localhost:8088/api/v1/planes-estudio' |
        ForEach-Object { [pscustomobject]@{ id = $_.id; programa = $_.idPrograma; version = $_.version; estado = $_.estado; cursos = ($_.cursos.codigoCurso -join ',') } } |
        Format-Table | Out-Host
}

function IdCurso([string]$codigo) {
    (Lista "$G/api/v1/cursos" | Where-Object codigo -eq $codigo).id
}

function Crear-Seccion([string]$codigo = 'IS101', [string]$docente = 'Ing. Ana Torres') {
    if (-not $TokenCoord) { Demo-Login }
    $body = @{ cursoId = (IdCurso $codigo); periodo = '2026-2'; cupoMaximo = 40; horario = 'Lun 08:00' }
    if ($docente) { $body.docente = $docente }
    try {
        $s = Invoke-RestMethod -Method Post "$G/api/v1/secciones" -Headers (Auth $TokenCoord) -ContentType 'application/json' -Body ($body | ConvertTo-Json)
        Write-Host ("POST /secciones -> id={0} estado={1} motivo={2}" -f $s.id, $s.estado, $s.motivo) -ForegroundColor Green
        return $s
    } catch {
        Write-Host ("POST /secciones -> {0}" -f (Error-Api $_)) -ForegroundColor Yellow
    }
}

function Ver-Secciones {
    Lista "$G/api/v1/secciones" |
        ForEach-Object { [pscustomobject]@{ id = $_.id; curso = $_.curso.codigo; docente = $_.docente; estado = $_.estado; motivo = $_.motivo } } |
        Format-Table -AutoSize | Out-Host
}

function Ver-Actas {
    Lista "$G/api/v1/actas" | Format-Table id, idSeccion, codigoCurso, docente, estado, motivo -AutoSize | Out-Host
}

function Ver-Circuito {
    $cb = (Invoke-RestMethod 'http://localhost:8083/actuator/circuitbreakers').circuitBreakers.curriculo
    Write-Host ("Circuit breaker 'curriculo': estado={0} fallos={1}% llamadas_fallidas={2} no_permitidas={3}" -f `
        $cb.state, $cb.failureRate, $cb.failedCalls, $cb.notPermittedCalls) -ForegroundColor Magenta
}

function Ver-Logs([int]$n = 15) {
    foreach ($ms in 'curriculo', 'planificacion', 'evaluacion') {
        Write-Host "--- $ms" -ForegroundColor DarkCyan
        Get-Content (Join-Path $Raiz "services\lamb-$ms-ms\logs\$ms.log") -Tail 400 |
            Select-String 'component=|\[CURRICULO\]' | Select-Object -Last $n | ForEach-Object { $_.Line } | Out-Host
    }
}

function Ver-Lag {
    docker exec lamb-kafka-dev /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --describe --all-groups 2>$null |
        Select-String 'GROUP|eventos' | ForEach-Object { $_.Line } | Out-Host
}

function Demo-Saga {
    Preparar-Datos
    Titulo 'S6+S8 - Camino feliz: seccion con docente (Feign valida en curriculo, luego Kafka)'
    $ok = Crear-Seccion 'IS101' 'Ing. Ana Torres'
    Titulo 'S9 - Fallo de un paso: seccion SIN docente -> acta.rechazada -> compensacion'
    $mala = Crear-Seccion 'IS101' $null
    Titulo 'Regla sincronica: curso que no esta en el plan vigente -> 409 (no se crea nada)'
    Crear-Seccion 'IS201' 'Ing. Luis Rojas' | Out-Null
    Start-Sleep -Seconds 4
    Titulo 'Resultado (PENDIENTE_ACTA -> ABIERTA / ANULADA por eventos)'
    Ver-Secciones
    Ver-Actas
    $script:UltimaOk = $ok
    $script:UltimaAnulada = $mala
}

function Publicar-Kafka([string]$topic, [string]$key, [string]$json) {
    "$key|$json" | docker exec -i lamb-kafka-dev /opt/kafka/bin/kafka-console-producer.sh `
        --bootstrap-server localhost:9092 --topic $topic --property parse.key=true --property 'key.separator=|'
}

function Demo-Duplicados {
    if (-not $UltimaOk) { Demo-Saga }
    Titulo "S9 - Reenviar seccion.abierta de la seccion $($UltimaOk.id) (ya procesada)"
    $evento = @{ tipoEvento = 'seccion.abierta'; seccionId = $UltimaOk.id; codigoCurso = 'IS101'; periodo = '2026-2'
                 docente = $UltimaOk.docente; cupoMaximo = 40; origen = 'lamb-planificacion-ms'; timestamp = [DateTimeOffset]::Now.ToUnixTimeMilliseconds() } | ConvertTo-Json -Compress
    Publicar-Kafka 'planificacion-eventos' $UltimaOk.id $evento
    Titulo "S9 - Reenviar acta.rechazada de la seccion $($UltimaAnulada.id) (compensacion ya aplicada)"
    $acta = @(Invoke-RestMethod "$G/api/v1/actas/seccion/$($UltimaAnulada.id)" -Headers (Auth $TokenCoord))[0]
    $evento2 = @{ tipoEvento = 'acta.rechazada'; actaId = $acta.id; seccionId = $UltimaAnulada.id; estado = 'RECHAZADA'
                  motivo = $acta.motivo; origen = 'lamb-evaluacion-ms'; timestamp = [DateTimeOffset]::Now.ToUnixTimeMilliseconds() } | ConvertTo-Json -Compress
    Publicar-Kafka 'evaluacion-eventos' $UltimaAnulada.id $evento2
    Start-Sleep -Seconds 3
    Titulo 'Logs: deben decir status=ignored, sin excepcion'
    Get-Content (Join-Path $Raiz 'services\lamb-evaluacion-ms\logs\evaluacion.log') -Tail 50 | Select-String 'status=ignored' | Select-Object -Last 2 | ForEach-Object { $_.Line } | Out-Host
    Get-Content (Join-Path $Raiz 'services\lamb-planificacion-ms\logs\planificacion.log') -Tail 50 | Select-String 'status=ignored' | Select-Object -Last 2 | ForEach-Object { $_.Line } | Out-Host
    Titulo 'BD de evaluacion: sigue habiendo UNA sola acta por seccion (uk_actas_seccion)'
    docker exec lamb-postgres-evaluacion-dev psql -U lamb -d lamb_evaluacion_db -c "SELECT id_seccion, COUNT(*) FROM actas GROUP BY id_seccion ORDER BY id_seccion;" | Out-Host
}

function Demo-MensajeMalo {
    Titulo 'S8 - Mensaje que no es JSON: ErrorHandlingDeserializer lo descarta y el consumidor sigue vivo'
    Publicar-Kafka 'planificacion-eventos' '999' 'esto-no-es-json'
    Start-Sleep -Seconds 3
    Write-Host 'El error se registra una vez (sin reintentos infinitos) y el siguiente evento se procesa normal:'
    Get-Content (Join-Path $Raiz 'services\lamb-evaluacion-ms\logs\evaluacion.log') -Tail 30 | Select-String 'DefaultErrorHandler|component=' | Select-Object -Last 2 | ForEach-Object { $_.Line.Substring(0, [Math]::Min(220, $_.Line.Length)) } | Out-Host
}

function Detener-Servicio([string]$nombre) {
    $puertos = @{ curriculo = 8088; planificacion = 8083; evaluacion = 8086; auth = 8091 }
    $c = Get-NetTCPConnection -LocalPort $puertos[$nombre] -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($c) { Stop-Process -Id $c.OwningProcess -Force; Write-Host "lamb-$nombre-ms detenido" -ForegroundColor Yellow }
}

function Iniciar-Servicio([string]$nombre) {
    $dir = Join-Path $Raiz "services\lamb-$nombre-ms"
    $jar = (Get-ChildItem (Join-Path $dir 'target') -Filter '*.jar' | Where-Object { $_.Name -notlike '*.original' } | Select-Object -First 1).FullName
    Start-Process java -ArgumentList "-Xms64m -Xmx256m -XX:+UseSerialGC -XX:TieredStopAtLevel=1 -jar `"$jar`"" -WorkingDirectory $dir | Out-Null
    Write-Host "lamb-$nombre-ms arrancando (espera ~30 s)" -ForegroundColor Yellow
}

function Demo-CircuitBreaker {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'S6 - Circuito CERRADO con curriculo arriba'
    Ver-Circuito
    Detener-Servicio 'curriculo'
    Start-Sleep -Seconds 2
    Titulo 'S6 - curriculo caido: la seccion NO falla con 500, queda PENDIENTE_VALIDACION (fallback)'
    1..6 | ForEach-Object { Crear-Seccion 'IS101' 'Ing. Carla Diaz' | Out-Null; Ver-Circuito }
    Titulo 'S6 - Con el circuito OPEN el fallback responde al instante, sin intentar la red'
    $t = Measure-Command { Crear-Seccion 'IS101' 'Ing. Carla Diaz' | Out-Null }
    Write-Host ("Tiempo de respuesta: {0} ms" -f [int]$t.TotalMilliseconds)
    Write-Host "`nPara cerrar el ciclo:  Iniciar-Servicio curriculo ; luego Revalidar-Pendientes (o Publicar-Plan)" -ForegroundColor Green
}

function Revalidar-Pendientes {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'S6 - Reintento manual: POST /secciones/{id}/revalidar (curriculo ya responde, el circuito pasa a HALF_OPEN y CLOSED)'
    $pend = Lista "$G/api/v1/secciones" | Where-Object estado -eq 'PENDIENTE_VALIDACION'
    foreach ($s in $pend) {
        try {
            $r = Invoke-RestMethod -Method Post "$G/api/v1/secciones/$($s.id)/revalidar" -Headers (Auth $TokenCoord)
            Write-Host "seccion $($r.id) -> $($r.estado)"
        } catch { Write-Host "seccion $($s.id) -> $(Error-Api $_)" -ForegroundColor Yellow }
        Ver-Circuito
    }
    Start-Sleep -Seconds 3
    Ver-Secciones
}

function Publicar-Plan {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'S8 - curriculo publica un plan nuevo (plan.publicado en curriculo-eventos); planificacion lo consume'
    $version = 'D' + (Get-Date -Format 'MMddHHmm')
    $plan = Invoke-RestMethod -Method Post 'http://localhost:8088/api/v1/planes-estudio' -Headers (Auth $TokenCoord) -ContentType 'application/json' `
        -Body (@{ idPrograma = 10; version = $version; nombre = "Plan $version Ingenieria de Sistemas" } | ConvertTo-Json)
    foreach ($c in @(@{ codigoCurso = 'IS101'; nombreCurso = 'Programacion I'; ciclo = 1; creditos = 4; tipo = 'OBLIGATORIO' },
                     @{ codigoCurso = 'IS301'; nombreCurso = 'Base de Datos'; ciclo = 3; creditos = 4; tipo = 'OBLIGATORIO' })) {
        Invoke-RestMethod -Method Post "http://localhost:8088/api/v1/planes-estudio/$($plan.id)/cursos" -Headers (Auth $TokenCoord) -ContentType 'application/json' -Body ($c | ConvertTo-Json) | Out-Null
    }
    $pub = Invoke-RestMethod -Method Post "http://localhost:8088/api/v1/planes-estudio/$($plan.id)/publicar" -Headers (Auth $TokenCoord)
    Write-Host "Plan $($pub.id) version $($pub.version) -> $($pub.estado) (cursos IS101, IS301)" -ForegroundColor Green
    Start-Sleep -Seconds 3
    Get-Content (Join-Path $Raiz 'services\lamb-curriculo-ms\logs\curriculo.log') -Tail 200 | Select-String 'plan.publicado' | Select-Object -Last 1 | ForEach-Object { $_.Line } | Out-Host
    Get-Content (Join-Path $Raiz 'services\lamb-planificacion-ms\logs\planificacion.log') -Tail 200 | Select-String 'plan.publicado|revalidated|no hay secciones' | Select-Object -Last 4 | ForEach-Object { $_.Line } | Out-Host
    Titulo 'Ahora IS301 ya esta en el plan vigente: la seccion se puede abrir'
    Crear-Seccion 'IS301' 'Ing. Rosa Mendoza' | Out-Null
    Start-Sleep -Seconds 3
    Ver-Secciones
}

function Demo-Desacople {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'S8 - Desacople: se apaga evaluacion'
    Detener-Servicio 'evaluacion'
    Start-Sleep -Seconds 2
    $s = Crear-Seccion 'IS101' 'Ing. Pedro Quispe'
    Start-Sleep -Seconds 3
    Write-Host 'planificacion respondio igual (201). La seccion espera en PENDIENTE_ACTA:'
    Ver-Secciones
    Titulo 'Lag del grupo lamb-evaluacion-ms (mensaje esperando en Kafka)'
    Ver-Lag
    Write-Host "`nAhora:  Iniciar-Servicio evaluacion  ; espera ~30 s ; Ver-Secciones  (pasa a ABIERTA) ; Ver-Lag (lag 0)" -ForegroundColor Green
}


function Demo-Admision {
    if (-not $TokenCoord) { Demo-Login }
    Titulo 'Admision -> Trayectoria: postular, evaluar y admitir al estudiante (idPersona 4)'
    $p = Invoke-RestMethod -Method Post "$G/api/v1/postulaciones" -Headers (Auth $TokenCoord) -ContentType 'application/json' `
        -Body '{"idPersona":4,"idPrograma":10,"periodo":"2026-2"}'
    Write-Host "Postulacion $($p.id) -> $($p.estado)"
    try { Invoke-RestMethod -Method Post "$G/api/v1/postulaciones/$($p.id)/admitir" -Headers (Auth $TokenCoord) | Out-Null }
    catch { Write-Host "Admitir sin evaluar -> $(Error-Api $_)" -ForegroundColor Yellow }
    $p = Invoke-RestMethod -Method Post "$G/api/v1/postulaciones/$($p.id)/evaluar" -Headers (Auth $TokenCoord) -ContentType 'application/json' -Body '{"puntajeExamen":15.5}'
    Write-Host "Evaluada -> $($p.estado) puntaje $($p.puntajeExamen)"
    $p = Invoke-RestMethod -Method Post "$G/api/v1/postulaciones/$($p.id)/admitir" -Headers (Auth $TokenCoord)
    Write-Host "Admitida -> $($p.estado)  (publica postulante.admitido)" -ForegroundColor Green
    $script:UltimaPostulacion = $p
    Start-Sleep -Seconds 4
    Titulo 'trayectoria consumio postulante.admitido y abrio la trayectoria'
    (Invoke-RestMethod "$G/api/v1/trayectorias/persona/4" -Headers (Auth $TokenCoord)).trayectoria | Format-List | Out-Host
    Titulo 'S9 - Reenviar postulante.admitido: trayectoria lo ignora (uk_trayectorias_persona)'
    $evento = @{ tipoEvento = 'postulante.admitido'; postulacionId = $p.id; idPersona = 4; idPrograma = 10; periodo = '2026-2'
                 puntajeExamen = 15.5; origen = 'lamb-admision-ms'; timestamp = [DateTimeOffset]::Now.ToUnixTimeMilliseconds() } | ConvertTo-Json -Compress
    Publicar-Kafka 'admision-eventos' '4' $evento
    Start-Sleep -Seconds 3
    Get-Content (Join-Path $Raiz 'services\lamb-trayectoria-ms\logs\trayectoria.log') -Tail 50 | Select-String 'idPersona=4' | Select-Object -Last 3 | ForEach-Object { $_.Line } | Out-Host
    docker exec lamb-postgres-trayectoria-dev psql -U lamb -d lamb_trayectoria_db -c "SELECT id_persona, COUNT(*) FROM trayectorias GROUP BY id_persona;" | Out-Host
}

function Demo-CierreActa {
    if (-not $TokenDocente) { Demo-Login }
    $acta = Lista "$G/api/v1/actas" | Where-Object estado -eq 'ABIERTA' | Select-Object -First 1
    if (-not $acta) { Write-Host 'No hay actas ABIERTAS: corre Demo-Saga primero' -ForegroundColor Yellow; return }
    Titulo "Evaluacion: el DOCENTE registra notas de la seccion $($acta.idSeccion) y cierra el acta $($acta.id)"
    foreach ($n in @(@{ idPersona = 4; tipo = 'PRACTICA'; nota = 14 }, @{ idPersona = 4; tipo = 'PARCIAL'; nota = 13 }, @{ idPersona = 4; tipo = 'FINAL'; nota = 16 },
                     @{ idPersona = 5; tipo = 'PRACTICA'; nota = 8 }, @{ idPersona = 5; tipo = 'PARCIAL'; nota = 9 }, @{ idPersona = 5; tipo = 'FINAL'; nota = 10 })) {
        $body = @{ idPersona = $n.idPersona; idSeccion = $acta.idSeccion; tipoEvaluacion = $n.tipo; calificacion = $n.nota; periodo = $acta.periodo } | ConvertTo-Json
        Invoke-RestMethod -Method Post "$G/api/v1/notas" -Headers (Auth $TokenDocente) -ContentType 'application/json' -Body $body | Out-Null
    }
    Write-Host 'Notas registradas: persona 4 (14, 13, 16) y persona 5 (8, 9, 10)'
    $cerrada = Invoke-RestMethod -Method Post "$G/api/v1/actas/$($acta.id)/cerrar" -Headers (Auth $TokenDocente)
    Write-Host "Acta $($cerrada.id) -> $($cerrada.estado)  (publica acta.cerrada)" -ForegroundColor Green
    $script:UltimaActaCerrada = $cerrada
    Titulo 'Con el acta CERRADA ya no se pueden registrar notas (409)'
    $body = @{ idPersona = 4; idSeccion = $acta.idSeccion; tipoEvaluacion = 'FINAL'; calificacion = 20; periodo = $acta.periodo } | ConvertTo-Json
    try { Invoke-RestMethod -Method Post "$G/api/v1/notas" -Headers (Auth $TokenDocente) -ContentType 'application/json' -Body $body | Out-Null }
    catch { Write-Host "POST /notas -> $(Error-Api $_)" -ForegroundColor Yellow }
    Start-Sleep -Seconds 4
    Titulo 'trayectoria consumio acta.cerrada: historial y resumen de la persona 4'
    $tr = Invoke-RestMethod "$G/api/v1/trayectorias/persona/4" -Headers (Auth $TokenCoord)
    $tr.trayectoria | Select-Object idPersona, creditosAprobados, promedioPonderado | Format-Table | Out-Host
    @($tr.historial | ForEach-Object { $_ }) | Format-Table codigoCurso, periodo, creditos, notaFinal, aprobado | Out-Host
}

function Demo-ActaDuplicada {
    if (-not $UltimaActaCerrada) { Write-Host 'Corre Demo-CierreActa primero' -ForegroundColor Yellow; return }
    $a = $UltimaActaCerrada
    Titulo "S9 - Reenviar acta.cerrada del acta $($a.id): el historial no se duplica"
    $evento = @{ tipoEvento = 'acta.cerrada'; actaId = $a.id; seccionId = $a.idSeccion; estado = 'CERRADA'; codigoCurso = $a.codigoCurso
                 periodo = $a.periodo; creditos = $a.creditos; origen = 'lamb-evaluacion-ms'; timestamp = [DateTimeOffset]::Now.ToUnixTimeMilliseconds()
                 resultados = @(@{ idPersona = 4; notaFinal = 14.33; aprobado = $true }, @{ idPersona = 5; notaFinal = 9.0; aprobado = $false }) } | ConvertTo-Json -Compress -Depth 4
    Publicar-Kafka 'evaluacion-eventos' $a.idSeccion $evento
    Start-Sleep -Seconds 3
    Get-Content (Join-Path $Raiz 'services\lamb-trayectoria-ms\logs\trayectoria.log') -Tail 30 | Select-String 'status=ignored|registrados=' | Select-Object -Last 3 | ForEach-Object { $_.Line } | Out-Host
    docker exec lamb-postgres-trayectoria-dev psql -U lamb -d lamb_trayectoria_db -c "SELECT id_persona, id_seccion, COUNT(*) FROM historial_cursos GROUP BY id_persona, id_seccion ORDER BY 1;" | Out-Host
}

function Demo-MiTrayectoria {
    if (-not $TokenEst) { Demo-Login }
    Titulo 'S7 - El ESTUDIANTE ve SU trayectoria: el idPersona sale del JWT (/trayectorias/me), no de la URL'
    $tr = Invoke-RestMethod "$G/api/v1/trayectorias/me" -Headers (Auth $TokenEst)
    $tr.trayectoria | Select-Object idPersona, periodoIngreso, estado, creditosAprobados, promedioPonderado | Format-List | Out-Host
    Write-Host ("ESTUDIANTE pidiendo todas las trayectorias -> {0}" -f (Codigo { Invoke-RestMethod "$G/api/v1/trayectorias" -Headers (Auth $TokenEst) }))
}

# Deja los datos como antes de la demo: sin postulaciones, secciones, actas, notas ni trayectorias; plan 2027 VIGENTE
function Reset-Demo {
    docker exec lamb-postgres-planificacion-dev psql -U lamb -d lamb_planificacion_db -c "TRUNCATE secciones RESTART IDENTITY;" | Out-Host
    docker exec lamb-postgres-evaluacion-dev psql -U lamb -d lamb_evaluacion_db -c "TRUNCATE actas RESTART IDENTITY;" -c "TRUNCATE notas RESTART IDENTITY;" | Out-Host
    docker exec lamb-postgres-admision-dev psql -U lamb -d lamb_admision_db -c "TRUNCATE postulaciones RESTART IDENTITY;" | Out-Host
    docker exec lamb-postgres-trayectoria-dev psql -U lamb -d lamb_trayectoria_db -c "TRUNCATE historial_cursos RESTART IDENTITY;" -c "TRUNCATE trayectorias RESTART IDENTITY;" | Out-Host
    docker exec lamb-postgres-curriculo-dev psql -U lamb -d lamb_curriculo_db -c "DELETE FROM prerrequisitos WHERE id_plan_curso IN (SELECT c.id FROM plan_cursos c JOIN planes_estudio p ON p.id = c.id_plan WHERE p.version LIKE 'D%');" -c "DELETE FROM plan_cursos WHERE id_plan IN (SELECT id FROM planes_estudio WHERE version LIKE 'D%');" -c "DELETE FROM planes_estudio WHERE version LIKE 'D%';" -c "UPDATE planes_estudio SET estado = 'VIGENTE' WHERE id_programa = 10 AND version = '2027';" | Out-Host
    Write-Host 'Datos de demo reiniciados' -ForegroundColor Green
}

function Demo-Ayuda {
    @'
Orden sugerido para exponer:
  Demo-Login            S7  login por el Gateway, claims del JWT (realm_access.roles)
  Demo-Seguridad        S7  matriz 401/403/2xx en Gateway y llamando directo a cada MS
  Demo-Saga             S6+S8+S9  seccion OK (ABIERTA), sin docente (ANULADA por compensacion), curso fuera del plan (409)
  Demo-Duplicados       S9  reenvia seccion.abierta y acta.rechazada: status=ignored, 1 sola acta
  Demo-MensajeMalo      S8  un mensaje no-JSON no tumba al consumidor
  Demo-CircuitBreaker   S6  apaga curriculo, fallback PENDIENTE_VALIDACION, circuito OPEN
  Iniciar-Servicio curriculo ; Revalidar-Pendientes   S6  HALF_OPEN -> CLOSED, seccion validada
  Publicar-Plan         S8  plan.publicado de curriculo consumido por planificacion
  Demo-Desacople        S8  apaga evaluacion, lag en Kafka, al volver procesa lo pendiente
  Demo-Admision         S8+S9  postulacion -> admitida -> trayectoria abierta; reenvio ignorado
  Demo-CierreActa       S8  notas del docente, cierre del acta, historial y promedio en trayectoria
  Demo-ActaDuplicada    S9  reenvia acta.cerrada: el historial no se duplica
  Demo-MiTrayectoria    S7  el estudiante ve solo lo suyo (idPersona del JWT)
Utiles: Ver-Secciones, Ver-Actas, Ver-Circuito, Ver-Logs, Ver-Lag, Detener-Servicio <x>, Iniciar-Servicio <x>
Antes de empezar (opcional): Reset-Demo   deja secciones/actas vacias y el plan 2027 VIGENTE
Nota: tras apagar un consumidor a la fuerza, Kafka tarda ~45 s en reasignarle las particiones al volver
'@ | Write-Host
}

Write-Host 'Funciones de demo cargadas. Escribe Demo-Ayuda' -ForegroundColor Green

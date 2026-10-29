# Detiene los servicios Java del Modulo 02 (por puerto). Los contenedores quedan corriendo.
# Uso:  powershell -ExecutionPolicy Bypass -File scripts\demo-stop.ps1  [-Docker]
param([switch]$Docker)

$puertos = 18080, 8089, 8086, 8084, 8083, 8088, 8091, 18761, 18888
foreach ($p in $puertos) {
    $conexion = Get-NetTCPConnection -LocalPort $p -State Listen -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($conexion) {
        Stop-Process -Id $conexion.OwningProcess -Force -ErrorAction SilentlyContinue
        Write-Host "Detenido el proceso en el puerto $p"
    }
}

if ($Docker) {
    $raiz = Split-Path -Parent $PSScriptRoot
    foreach ($ms in 'auth', 'admision', 'curriculo', 'planificacion', 'evaluacion', 'trayectoria') {
        docker compose -f (Join-Path $raiz "services\lamb-$ms-ms\compose-dev.yml") stop
    }
    docker compose -f (Join-Path $raiz 'kafka\compose-dev.yml') stop
}

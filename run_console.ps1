Param(
    [switch]$Clean
)
if ([Console]::OutputEncoding.BodyName -ne 'utf-8') { try { [Console]::OutputEncoding = [System.Text.Encoding]::UTF8 } catch {} }
Write-Host "== Ejecutando versión consola (Main) =="

function Test-MavenAvailable {
    return (Get-Command mvn -ErrorAction SilentlyContinue) -ne $null
}

if (-not (Test-MavenAvailable)) {
    Write-Warning "Maven no encontrado en PATH";
    Write-Host "Instala Maven con uno de estos métodos:" -ForegroundColor Yellow
    Write-Host "  winget install -e --id Apache.Maven" -ForegroundColor Cyan
    Write-Host "  choco install maven" -ForegroundColor Cyan
    Write-Host "O descarga manual: https://maven.apache.org/download.cgi y agrega /bin al PATH" -ForegroundColor Cyan
    Write-Host "Luego: cierra y abre PowerShell, verifica con: mvn -v" -ForegroundColor Yellow
    exit 1
}

if ($Clean) { mvn -q clean }
mvn -q -DskipTests compile
mvn -q -Dexec.mainClass=Main exec:java
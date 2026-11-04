@echo off
chcp 65001 > nul
echo ========================================================
echo  [SENAI] Compilando God of War: Sons of Sparta
echo ========================================================

if not exist bin mkdir bin

set JAVAC_CMD=

REM 1. Verifica variavel de ambiente JAVA_HOME
if defined JAVA_HOME (
    if "%JAVA_HOME:~-1%"=="\" set "JAVA_HOME=%JAVA_HOME:~0,-1%"
    if exist "%JAVA_HOME%\bin\javac.exe" (
        set JAVAC_CMD="%JAVA_HOME%\bin\javac.exe"
        goto :compile
    )
)

REM 2. Procura em pastas conhecidas de instalacao do JDK (Adoptium / Eclipse Temurin, Oracle, etc.)
for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%~fi\bin\javac.exe" (
        set JAVAC_CMD="%%~fi\bin\javac.exe"
        goto :compile
    )
)

for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%~fi\bin\javac.exe" (
        set JAVAC_CMD="%%~fi\bin\javac.exe"
        goto :compile
    )
)

for /d %%i in ("C:\Program Files\Microsoft\jdk*") do (
    if exist "%%~fi\bin\javac.exe" (
        set JAVAC_CMD="%%~fi\bin\javac.exe"
        goto :compile
    )
)

for /d %%i in ("C:\Program Files\Zulu\jdk*") do (
    if exist "%%~fi\bin\javac.exe" (
        set JAVAC_CMD="%%~fi\bin\javac.exe"
        goto :compile
    )
)

REM 3. Fallback: tenta javac do PATH
where javac >nul 2>nul
if %errorlevel% equ 0 (
    set JAVAC_CMD=javac
    goto :compile
)

echo [ERRO] O compilador javac nao foi encontrado no PATH ou em pastas padrao.
echo Por favor, configure a variavel JAVA_HOME ou instale o JDK.
pause
exit /b 1

:compile
echo Usando compilador: %JAVAC_CMD%
echo Compilando arquivos Java...

%JAVAC_CMD% -encoding UTF-8 -d bin src\com\sparta\game\combat\*.java src\com\sparta\game\core\*.java src\com\sparta\game\effects\*.java src\com\sparta\game\entities\*.java src\com\sparta\game\oop\*.java src\com\sparta\game\ui\*.java src\com\sparta\game\world\*.java src\com\sparta\game\Main.java

if %errorlevel% equ 0 (
    echo [SUCESSO] Compilacao concluida com exito! Binarios gerados em /bin
) else (
    echo [ERRO] Falha durante a compilacao.
    pause
    exit /b 1
)

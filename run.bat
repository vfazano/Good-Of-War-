@echo off
chcp 65001 > nul
echo ========================================================
echo  [SENAI] Executando God of War: Sons of Sparta
echo ========================================================

if not exist bin\com\sparta\game\Main.class (
    echo [AVISO] Binarios nao encontrados. Compilando primeiro...
    call compile.bat
    if %errorlevel% neq 0 (
        echo [ERRO] Compilacao falhou. Corrija os erros antes de executar.
        pause
        exit /b 1
    )
)

set JAVA_CMD=

REM 1. Verifica variavel de ambiente JAVA_HOME
if defined JAVA_HOME (
    if "%JAVA_HOME:~-1%"=="\" set "JAVA_HOME=%JAVA_HOME:~0,-1%"
    if exist "%JAVA_HOME%\bin\java.exe" (
        set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
        goto :run
    )
)

REM 2. Procura em pastas conhecidas de instalacao do JDK / JRE
for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do (
    if exist "%%~fi\bin\java.exe" (
        set JAVA_CMD="%%~fi\bin\java.exe"
        goto :run
    )
)

for /d %%i in ("C:\Program Files\Java\jdk*") do (
    if exist "%%~fi\bin\java.exe" (
        set JAVA_CMD="%%~fi\bin\java.exe"
        goto :run
    )
)

for /d %%i in ("C:\Program Files\Microsoft\jdk*") do (
    if exist "%%~fi\bin\java.exe" (
        set JAVA_CMD="%%~fi\bin\java.exe"
        goto :run
    )
)

for /d %%i in ("C:\Program Files\Zulu\jdk*") do (
    if exist "%%~fi\bin\java.exe" (
        set JAVA_CMD="%%~fi\bin\java.exe"
        goto :run
    )
)

REM 3. Fallback: tenta java do PATH
where java >nul 2>nul
if %errorlevel% equ 0 (
    set JAVA_CMD=java
    goto :run
)

echo [ERRO] O comando java nao foi encontrado.
echo Por favor, instale o Java (JDK/JRE) ou configure a variavel JAVA_HOME.
pause
exit /b 1

:run
echo Usando Java: %JAVA_CMD%
echo Iniciando o jogo...
%JAVA_CMD% -cp bin com.sparta.game.Main

if %errorlevel% neq 0 (
    echo [ERRO] Falha ao executar o jogo.
    pause
)

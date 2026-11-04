@echo off
chcp 65001 > nul
setlocal
echo ========================================================
echo  [SENAI] Executando Good of War
echo ========================================================

if not exist "lib\jlayer-1.0.1.jar" (
    echo [ERRO] Decoder JLayer nao encontrado em lib\jlayer-1.0.1.jar.
    exit /b 1
)
if not exist "bin\com\sparta\game\Main.class" (
    echo [AVISO] Binarios nao encontrados. Compilando primeiro...
    call compile.bat
    if errorlevel 1 (
        echo [ERRO] Compilacao falhou. Corrija os erros antes de executar.
        exit /b 1
    )
)

if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
where java >nul 2>nul
if errorlevel 1 (
    echo [ERRO] java nao encontrado. Instale o JDK/JRE ou configure JAVA_HOME.
    exit /b 1
)

echo Iniciando o jogo...
java -cp "bin;lib\jlayer-1.0.1.jar" com.sparta.game.Main

if errorlevel 1 (
    echo [ERRO] Falha ao executar o jogo.
)
endlocal

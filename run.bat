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

echo Iniciando o jogo...
java -cp bin com.sparta.game.Main

if %errorlevel% neq 0 (
    echo [ERRO] Falha ao executar o jogo.
    pause
)


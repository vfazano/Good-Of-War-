@echo off
chcp 65001 > nul
setlocal
echo ========================================================
echo  [SENAI] Compilando Good of War
echo ========================================================

if not exist "lib\jlayer-1.0.1.jar" (
    echo [ERRO] Decoder JLayer nao encontrado em lib\jlayer-1.0.1.jar.
    exit /b 1
)
if not exist bin mkdir bin
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
where javac >nul 2>nul
if errorlevel 1 (
    echo [ERRO] javac nao encontrado. Instale o JDK ou configure JAVA_HOME.
    exit /b 1
)

echo Compilando arquivos Java...
javac -encoding UTF-8 -cp "lib\jlayer-1.0.1.jar" -d bin src\com\sparta\game\combat\*.java src\com\sparta\game\core\*.java src\com\sparta\game\effects\*.java src\com\sparta\game\entities\*.java src\com\sparta\game\oop\*.java src\com\sparta\game\ui\*.java src\com\sparta\game\world\*.java src\com\sparta\game\Main.java
if errorlevel 1 (
    echo [ERRO] Falha durante a compilacao.
    exit /b 1
)

echo [SUCESSO] Compilacao concluida.
endlocal

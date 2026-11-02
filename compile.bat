@echo off
chcp 65001 > nul
echo ========================================================
echo  [SENAI] Compilando God of War: Sons of Sparta
echo ========================================================

if not exist bin mkdir bin

REM Procura por javac no PATH ou em pastas padrao do JDK
where javac >nul 2>nul
if %errorlevel% equ 0 (
    set JAVAC_CMD=javac
) else (
    REM Tenta caminhos comuns de JDK caso o PATH nao esteja configurado
    if exist "%JAVA_HOME%\bin\javac.exe" (
        set JAVAC_CMD="%JAVA_HOME%\bin\javac.exe"
    ) else if exist "C:\Program Files\Java\jdk*\bin\javac.exe" (
        for /d %%i in ("C:\Program Files\Java\jdk*") do set JAVAC_CMD="%%i\bin\javac.exe"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk*\bin\javac.exe" (
        for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk*") do set JAVAC_CMD="%%i\bin\javac.exe"
    ) else (
        echo [ERRO] O compilador javac nao foi encontrado no PATH.
        echo Por favor, instale o JDK (Java Development Kit) ou abra o projeto no VS Code / Eclipse / IntelliJ.
        pause
        exit /b 1
    )
)

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


@echo off
setlocal

cd /d "%~dp0\.."

set "JAVA_EXE=java-workflow\tools\jdk\bin\java.exe"
set "APP_CLASSES=producer-vault-download\classes"

if not exist "%JAVA_EXE%" (
    echo Producer Vault could not find the bundled Java runtime.
    echo Expected: %JAVA_EXE%
    echo.
    pause
    exit /b 1
)

if not exist "%APP_CLASSES%\ProducerVaultApp.class" (
    echo Producer Vault classes are missing.
    echo Expected: %APP_CLASSES%\ProducerVaultApp.class
    echo.
    pause
    exit /b 1
)

"%JAVA_EXE%" -cp "%APP_CLASSES%" ProducerVaultApp

if errorlevel 1 (
    echo.
    echo Producer Vault closed with an error.
    pause
)

endlocal

@echo off
@REM Maven Wrapper startup script for Windows

setlocal

set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_VERSION=3.2.0
set MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar
set MAVEN_WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/%WRAPPER_VERSION%/maven-wrapper-%WRAPPER_VERSION%.jar

if not exist "%MAVEN_WRAPPER_JAR%" (
    echo Downloading maven-wrapper.jar...
    powershell -Command "& {Invoke-WebRequest -Uri '%MAVEN_WRAPPER_URL%' -OutFile '%MAVEN_WRAPPER_JAR%'}"
)

java -jar "%MAVEN_WRAPPER_JAR%" %*

endlocal

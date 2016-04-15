@echo off
set APP_HOME=#INT_PATH#
set LIB_PATH=%APP_HOME%\lib
set INTCLASSPATH=.

cd %APP_HOME%\bin

rem Add all jars....
for %%i in ("%LIB_PATH%\*.jar") do call %APP_HOME%\bin\cpappend.bat %%i

echo %INTCLASSPATH%

#JAVA_PATH# -cp %INTCLASSPATH% com.j32bit.tof.security.PassworderGUI

@echo off
set APP_HOME=#INT_PATH#
set LIB_PATH=%APP_HOME%\lib
set INTCLASSPATH=.

cd %APP_HOME%\bin

rem Add all jars....
for %%i in ("%LIB_PATH%\*.jar") do call %APP_HOME%\bin\cpappend.bat %%i

echo %INTCLASSPATH%

echo "Installing TOF Integrator V2 Service"

TOFIntegratorV2.exe //IS --Install="%APP_HOME%\bin\TOFIntegratorV2.exe" --DependsOn=ApacheDerby --Description="TOF Integrator V2 Service"    --Startup=auto    --LogPath=%APP_HOME%\logs --StdOutput=auto --StdError=auto 
TOFIntegratorV2.exe //US --StartPath="%APP_HOME%\bin" --StartMode=jvm --StartClass=com.j32bit.tof.launcher.TOFIntegratorV2 --StartMethod=startService --StartParams=%APP_HOME%\bin\TOFIntegratorV2.properties;%APP_HOME%\bin\log4j.xml
TOFIntegratorV2.exe //US --StopPath="%APP_HOME%\bin" --StopMode=jvm --StopClass=com.j32bit.tof.launcher.TOFIntegratorV2 --StopMethod=stopService

TOFIntegratorV2.exe //US --Classpath="%INTCLASSPATH%"
TOFIntegratorV2.exe //US --JvmOptions=-Duser.timezone="Europe/Istanbul"
TOFIntegratorV2.exe //US --Jvm=#JAVA_SERVICE_PATH#
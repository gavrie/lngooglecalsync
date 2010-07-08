@echo off
echo Running Lotus Notes Google Calendar Sync... 
java -jar "icalbridge.jar"

if ErrorLevel 1 goto Error
goto Exit

:Error
echo.
echo.
echo An error occurred. Look at the information above for further details.
pause


:Exit



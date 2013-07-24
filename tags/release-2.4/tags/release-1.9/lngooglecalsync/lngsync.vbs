' Run Lotus Notes to Google Calendar Synchronizer under Windows.
Option Explicit

dim oShell, oEnv, oJavawExec, oJavaExec 
dim lotusPath, lotusDataPath, classPath, processPath
dim appParm, silentMode
silentMode = false

set oShell = WScript.CreateObject("WScript.Shell")

' Read the Lotus Notes install path from the Registry. If the Registry
' read fails, the default path is used.
On Error Resume Next
lotusPath = "c:\Program Files\Lotus\Notes"
lotusPath = oShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Lotus\Notes\Path")
lotusDataPath = "c:\Program Files\Lotus\Notes\Data"
lotusDataPath = oShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Lotus\Notes\DataPath")
' Cancel previous 'On Error' statement
On Error GoTo 0

if WScript.Arguments.Count > 0 then
	appParm = WScript.Arguments(0)
	if appParm = "-silent" then silentMode = true
end if

' There are environment variables for the System, User, and Process.
' The Process PATH should be the System and User PATHs combined.
' Add the Lotus bin dir to the PATH because Notes.jar uses some Lotus Notes dlls.
' In particular, make sure the dir containing nlsxbe.dll is in the PATH.
set oEnv = oShell.Environment("Process")
processPath = oEnv.Item("PATH")
' Update the Process PATH because this is what 'javaw' will read
oEnv("PATH") = processPath & ";" & lotusPath & ";" & lotusDataPath 

' Set the classpath so Notes.jar can be found
classPath = """" & lotusPath & "\jvm\lib\ext\Notes.jar"";.\icalbridge.jar"

' Run the Java application
set oJavawExec = oShell.Exec("javaw -cp " & classPath & " LotusNotesGoogleCalendarBridge.mainGUI " & appParm)

' Wait for javaw to finish
Do While oJavawExec.Status = 0 
	WScript.Sleep 100 
Loop 

Dim oFileSystem, oOutputFile
Set oFileSystem = CreateObject("Scripting.fileSystemObject")
Set oOutputFile = oFileSystem.CreateTextFile("lngsync.log", TRUE)

if silentMode then
	' Write stdout and stderr to the log file
	oOutputFile.WriteLine(oJavawExec.StdOut.ReadAll)
	oOutputFile.WriteLine(oJavawExec.StdErr.ReadAll)
else
	oOutputFile.WriteLine("Finished running in GUI mode.")
end if
oOutputFile.Close

if oJavawExec.ExitCode > 0 then
	if silentMode then
		MsgBox "There was an error running Lotus Notes Google Calender Sync in silent mode.  " & _
			"To get more information, run the application in GUI mode or see lngsync.log.", _
			vbExclamation, "Lotus Notes Google Calender Sync Error"
	else 
		set oJavaExec = oShell.Exec("javaw -version")
		Do While oJavaExec.Status = 0 
			WScript.Sleep 100 
		Loop
		 
		MsgBox "The below error was encountered while starting Lotus Notes Google Calendar Sync.  " & _
			"If no error is shown, then an invalid command-line parameter was probably specified." & _
			vbCrLf & vbCrLf & oJavawExec.StdErr.ReadAll & vbCrLf & vbCrLf & vbCrLf & _
			"Below is the version of Java being used. Make sure the version is 1.6 or greater:" & vbCrLf & oJavaExec.StdErr.ReadAll, _
			vbExclamation, "Lotus Notes Google Calender Sync Startup Error"
	end if
end if

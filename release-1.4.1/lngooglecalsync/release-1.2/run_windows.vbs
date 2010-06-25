' Run Lotus Notes to Google Calendar Synchronizer under Windows.
Option Explicit

dim oShell, oEnv, oJavawExec, oJavaExec 
dim lotusPath, classpath, winpath

set oShell = WScript.CreateObject("WScript.Shell")

lotusPath = "c:\Program Files\Lotus\Notes"

' Get the environment for this running process
set oEnv = oShell.Environment("Process")
' Notes.jar uses some Lotus Notes dlls.  Add the Lotus path to the PATH env
' so the dlls can be found.
winpath = oShell.Environment.Item("PATH") & ";" & lotusPath
oEnv("PATH") = winpath

' Set the classpath so Notes.jar can be found
classpath = """" & lotusPath & "\jvm\lib\ext\Notes.jar"";.\icalbridge.jar"

' Run the Java application
set oJavawExec = oShell.Exec("javaw -cp " & classpath & " LotusNotesGoogleCalendarBridge.mainGUI")

' Wait for javaw to finish
Do While oJavawExec.Status = 0 
	WScript.Sleep 100 
Loop 

if oJavawExec.ExitCode > 0 then
	set oJavaExec = oShell.Exec("javaw -version")
	Do While oJavaExec.Status = 0 
		WScript.Sleep 100 
	Loop
	 
	MsgBox "The below error was encountered while starting Lotus Notes Google Calendar Sync." & _
		vbCrLf & vbCrLf & oJavawExec.StdErr.ReadAll & vbCrLf & vbCrLf & vbCrLf & _
		"Below is the version of Java being used. Make sure the version is 1.6 or greater:" & vbCrLf & oJavaExec.StdErr.ReadAll, _
		vbExclamation, "Startup Error"
end if

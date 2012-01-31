' Run Lotus Notes to Google Calendar Synchronizer under Windows.
Option Explicit

dim useLotusJVM
' If set to 1, then the JVM installed with Lotus Notes is used instead
' of the version of Java found in the PATH.
useLotusJVM = 0


dim appParm, silentMode
silentMode = false
dim oShell, oEnv, oJavawExec, oJavaExec, oFileSys
dim lotusPath, lotusDataPath, classPath, processPath, programFilesPath, notesJarPath
dim msgboxTitle
msgboxTitle = "Lotus Notes Google Sync"

set oShell = WScript.CreateObject("WScript.Shell")
set oFileSys = CreateObject("Scripting.FileSystemObject")

' There are environment variables for the System, User, and Process.
' The Process PATH should be the System and User PATHs combined.
set oEnv = oShell.Environment("Process")

' Read the Lotus Notes install path from the Registry. If the Registry
' read fails, try a default path.
On Error Resume Next
lotusPath = oShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Lotus\Notes\Path")
if lotusPath = "" then lotusPath = oShell.RegRead("HKEY_CURRENT_USER\Software\Lotus\Notes\Installer\PROGDIR")
if lotusPath = "" then
	' Try to find the path where Lotus Notes is installed
	' First, get the Program Files path from the environment
	programFilesPath = oEnv.Item("ProgramFiles")
	lotusPath = programFilesPath & "\Lotus\Notes"
	if not oFileSys.FolderExists(lotusPath) then
		lotusPath = programFilesPath & "\IBM\Lotus\Notes"
		if not oFileSys.FolderExists(lotusPath) then
			' On 64-bit systems, Lotus is probably in the "Program Files (x86)" dir
			lotusPath = oEnv.Item("SystemDrive") & "\Program Files (x86)\Lotus\Notes" 
			if not oFileSys.FolderExists(lotusPath) then
				lotusPath = oEnv.Item("SystemDrive") & "\Program Files (x86)\IBM\Lotus\Notes" 
			end if
		end if
	end if
end if

lotusDataPath = oShell.RegRead("HKEY_LOCAL_MACHINE\SOFTWARE\Lotus\Notes\DataPath")
if lotusDataPath = "" then lotusDataPath = oShell.RegRead("HKEY_CURRENT_USER\Software\Lotus\Notes\Installer\DATADIR")
if lotusDataPath = "" then lotusDataPath = lotusPath & "\Data"
' Cancel previous 'On Error' statement
On Error GoTo 0

' Try to find the Notes.jar file
notesJarPath = lotusPath & "\jvm\lib\ext\Notes.jar"
if not oFileSys.FileExists(notesJarPath) then
	notesJarPath = lotusPath & "\Notes.jar"
end if
if not oFileSys.FileExists(notesJarPath) then
	MsgBox "The Notes.jar file could not be found. It is very unlikely the application will be able to run successfully.", _
		vbExclamation, msgboxTitle
end if

if WScript.Arguments.Count > 0 then
	appParm = WScript.Arguments(0)
	if appParm = "-silent" then silentMode = true
end if

' Add the Lotus bin dir to the PATH because Notes.jar uses some Lotus Notes dlls.
' In particular, make sure the dir containing nlsxbe.dll is in the PATH.
processPath = oEnv.Item("PATH")
' Update the Process PATH because this is what 'javaw' will read
oEnv("PATH") = lotusPath & ";" & lotusDataPath & ";" & processPath  

' Set the classpath so Notes.jar can be found
classPath = """" & notesJarPath & """;.\lngsync.jar"

' Get the path to the version of Java installed with Lotus Notes.
' It is safest to use the Lotus Java for compatibility with Notes.jar.
dim javaPath
if useLotusJVM then
	javaPath = lotusPath & "\jvm\bin\javaw.exe"
else
	' Let the OS find Java via the PATH
	javaPath = "javaw.exe"
end if

'MsgBox("DEBUG" & vbCRLF & "useLotusJVM: " & useLotusJVM & vbCRLF & "lotusPath: " & lotusPath & vbCRLF & "classPath: " & classPath & vbCRLF & "javaPath: " & javaPath & vbCRLF & "appParm: " & appParm)
 
' Run the Java application
set oJavawExec = oShell.Exec("""" & javaPath & """ -cp " & classPath & " lngs.MainGUI " & appParm)

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
			vbExclamation, msgboxTitle
	else 
		set oJavaExec = oShell.Exec("""" & javaPath & """ -version")
		Do While oJavaExec.Status = 0 
			WScript.Sleep 100 
		Loop
		 
		MsgBox "The below error was encountered while starting Lotus Notes Google Calendar Sync.  " & _
			"If no error is shown, then an invalid command-line parameter was probably specified." & _
			vbCrLf & "Exit code: " & oJavawExec.ExitCode & _
			vbCrLf & oJavawExec.StdOut.ReadAll & _
			vbCrLf & oJavawExec.StdErr.ReadAll, _
			vbExclamation, msgboxTitle
		MsgBox "Below is the version of Java being used. Make sure the version is 1.6 or greater:" & vbCrLf & oJavaExec.StdErr.ReadAll, _
			vbExclamation, msgboxTitle
	end if
end if

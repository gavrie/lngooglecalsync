' This script runs LNConnectivityTest on Windows.
Option Explicit

dim useLotusJVM
' If set to 1, then the JVM installed with Lotus Notes is used instead
' of the version of Java found in the PATH.
useLotusJVM = 0

dim oShell, oEnv, oFileSys
dim lotusPath, lotusDataPath, classPath, processPath, programFilesPath, notesJarPath

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
	' Firs, get the Program Files path from the environment
	programFilesPath = oEnv.Item("ProgramFiles")
	lotusPath = programFilesPath & "\Lotus\Notes2"
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

MsgBox("DEBUG" & vbCRLF & "lotusPath: " & lotusPath)
 
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



' Add the Lotus bin dir to the PATH because Notes.jar uses some Lotus Notes dlls.
' In particular, make sure the dir containing nlsxbe.dll is in the PATH.
processPath = oEnv.Item("PATH")
' Update the Process PATH because this is what 'javaw' will read
oEnv("PATH") = processPath & ";" & lotusPath & ";" & lotusDataPath 

' Set the classpath so Notes.jar can be found
classPath = """" & notesJarPath & """;.\LNConnectivityTest.jar"

' Get the path to the version of Java installed with Lotus Notes.
' It is safest to use the Lotus Java for compatibility with Notes.jar.
dim javaPath
if useLotusJVM then
	javaPath = lotusPath & "\jvm\bin\java.exe"
else
	' Let the OS find Java via the PATH
	javaPath = "java.exe"
end if



' Run the Java application then run the 'pause' command so the cmd window doesn't close
oShell.Run "%comspec% /c """"" & javaPath & """ -cp " & classPath & " lnconnectivitytest.Main & pause""", 1, true

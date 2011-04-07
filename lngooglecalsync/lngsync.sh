#!/bin/sh
# Run Lotus Notes to Google Calendar Synchronizer under Linux and OS X.

# NOTE: If you come up with enhancments to this script, please post
# them to the project's Open Discussion forum:
# https://sourceforge.net/projects/lngooglecalsync/forums  

# The application uses Notes.jar, the Java interface file installed with the
# Lotus Notes client. Notes.jar references some .so files under Linux and .dylib
# files under OS X.
# In particular, make sure the dir containing lsxbe.so, liblsxbe.so, or lsxbe.dylib
# is in the path.
# The default locations for Notes.jar and the library files are hard-coded below.

OS_TYPE=`uname`
if [ "$OS_TYPE" = "Linux" ]; then
  NOTES_PATH=/opt/ibm/lotus/notes
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$NOTES_PATH
fi
if [ "$OS_TYPE" = "Darwin" ]; then
  export NOTES_PATH=/Applications/Notes.app/Contents/MacOS
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$NOTES_PATH
  export CLASSPATH=$CLASSPATH:$NOTES_PATH:$NOTES_PATH/jvm/lib/ext/Notes.jar
  export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:$NOTES_PATH
fi

SCRIPT_PATH=`dirname "$0"`
# Make invokable from any directory
cd "$SCRIPT_PATH"

# Make silent mode work for cronjobs with ugly X11 hack
if [ -z "$DISPLAY" ]; then
       # try with default ...
       export DISPLAY=:0.0
       # .. but test
       xset -q 1> /dev/null 2>1
       if [ $? != 0 ]; then
               echo The DISPLAY environment varialbe was not set, and the attempt to manually set did not work.
               echo The application may not startup properly.
       fi
fi

export PATH=$PATH:$NOTES_PATH
export MY_CLASSPATH=$NOTES_PATH/jvm/lib/ext/Notes.jar:./icalbridge.jar:$(printf "%s\n"  ./lib/*.jar | tr "\n" ":")

COMMAND="java -d32 -cp $MY_CLASSPATH LotusNotesGoogleCalendarBridge.mainGUI $*"


if [ -n $1 ] && [ "$1" = "-silent" ]; then
	echo Running Lotus Notes Google Calendar Sync in silent mode..
	LOG_FILE=$SCRIPT_PATH/lngsync.log
	$COMMAND -silent > $LOG_FILE
	rc=$?
	echo Synchronization complete.  See log file $LOG_FILE
else 
	echo Running Lotus Notes Google Calendar Sync in GUI mode...
	$COMMAND
	rc=$? 
fi

exit $rc

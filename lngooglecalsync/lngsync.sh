#!/bin/sh
# This script runs Lotus Notes to Google Calendar Synchronizer under Linux and OS X.

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

# Configure for Linux
if [ "$OS_TYPE" = "Linux" ]; then
  if [ -d "/opt/ibm/lotus/notes" ]; then  # Notes v8
     NOTES_PATH=/opt/ibm/lotus/notes
  elif [ -d "/opt/ibm/notes" ]; then      # Notes v9
     NOTES_PATH=/opt/ibm/notes
  else
     echo "The Linux Lotus Notes installation directory could not be determined. Exiting."
     exit 1
  fi
  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$NOTES_PATH
fi

# Configure for Apple OS X
if [ "$OS_TYPE" = "Darwin" ]; then

	# Get the verion number in a string i.e. 10.8.2
	OS_VER=`sw_vers -productVersion`

	# Create an array from the version string where
	#    [0]=Major Verion, [1]=Minor Version, [2]=Fix Level
	OS_VER_NUMS=(`echo $OS_VER | tr '.' ' '`)

	# These are the values we "test" against; they could be hard coded just as easily
	vMAJOR="10"
	vMINOR="8"

	# If true, OS X is older than 10.8, in which case we look for both 'Lotus Notes.app' and 'Notes.app'
	if [[ "${OS_VER_NUMS[0]}" -lt "$vMAJOR"  || ( "${OS_VER_NUMS[0]}" -le "$vMAJOR"  &&  "${OS_VER_NUMS[1]}" -lt "$vMINOR" )]]; then
		if [ -d "/Applications/Notes.app/Contents/MacOS" ]; then
			echo ----" 'Lotus Notes.app' exists "-
			export NOTES_PATH=/Applications/Notes.app/Contents/MacOS
		elif [ -d "/Applications/Lotus Notes.app/Contents/MacOS" ]; then
			export NOTES_PATH=/Applications/Lotus\ Notes.app/Contents/MacOS
		else
			echo "The OS X version was found to be older then v10.8."
			echo "Lotus Notes could NOT be found as 'Notes.app' or 'Lotus Notes.app'. Exiting."
			exit 1
		fi

	# If true, OS X is 10.8 or newer, in which case we only look for 'Lotus Notes.app'
	elif [ "${OS_VER_NUMS[0]}" = "$vMAJOR" ] && [ "${OS_VER_NUMS[1]}" -ge "$vMINOR" ]; then
		if [ -d "/Applications/Lotus Notes.app/Contents/MacOS" ]; then
			export NOTES_PATH=/Applications/Lotus\ Notes.app/Contents/MacOS
		else
			echo "The OS X version was found to be v10.8 or newer."
			echo "Lotus Notes could NOT be found as 'Lotus Notes.app'. Exiting."
			exit 1
		fi
	fi
	export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$NOTES_PATH"
	export DYLD_LIBRARY_PATH=$DYLD_LIBRARY_PATH:"$NOTES_PATH"
fi

SCRIPT_PATH=`dirname "$0"`
# Make invokable from any directory
cd "$SCRIPT_PATH"

# Make silent mode work for cronjobs with ugly X11 hack
if [ -z "$DISPLAY" ]; then
       # try with default ...
       export DISPLAY=:0.0
       # ... but test
       xset -q 1> /dev/null 2>1
       if [ $? != 0 ]; then
               echo The DISPLAY environment varialbe was not set, and the attempt to manually set did not work.
               echo The application may not startup properly.
       fi
fi

export PATH=$PATH:"$NOTES_PATH"
export MY_CLASSPATH="$NOTES_PATH/jvm/lib/ext/Notes.jar":./lngsync.jar

JAVA_COMMAND="java -d32"


if [ -n $1 ] && [ "$1" = "-silent" ]; then
	echo Running Lotus Notes Google Calendar Sync in silent mode...
	LOG_FILE=$SCRIPT_PATH/lngsync.log
	$JAVA_COMMAND -cp "$MY_CLASSPATH" lngs.MainGUI $* > $LOG_FILE
	rc=$?
	echo Synchronization complete.  See log file $LOG_FILE
else 
	echo Running Lotus Notes Google Calendar Sync in GUI mode...
	$JAVA_COMMAND -cp "$MY_CLASSPATH" lngs.MainGUI $*
	rc=$? 
fi

exit $rc

Lotus Notes Google Calendar Sync
--------------------------------
4/6/10

=== Usage

This application retrieves the Lotus Notes calendar data using standard LN classes
found in Notes.jar. Make sure your Windows PATH statement includes the location
for the LN main directory, e.g. "c:\Program Files\Lotus\Notes".
This is necessary because the Java classes in Notes.jar call native/C dlls found
in this directory.

Java version 1.6 or higher must be used. Otherwise you won't see anything or get an error.

To start, either execute Run.cmd or use this command:
java -jar "icalbridge.jar" 

It can take 1 minute or more to run, depending on how many calendar entries you have.
Speed improvements are planned for the future.



=== General Info

Comments on some GUI fields:
   Lotus Notes Server - The name of the Domino server to access, e.g. AcmeMailServer.
      Leave this field blank to access a local mail file. 
   Lotus Notes Mail File - The name of the mail file to open, e.g. mail/johnsmith.nsf.

Here is how Lotus Notes calendar events are translated to Google calendar events:
=Lotus Notes Event=     =Google Event=
To Do                   All day event
Appointment             Standard event
Anniversary             All day event
All Day Event           All day event
Meeting                 Standard event
Reminder                Standard event with the same start and end times
Any repeating entry     Separate, multiple events


Only a certain date range of entries are synchronized to Google.  The Lotus Notes
date must be between today - 7 days and today + 60 days.

The application will create a Lotus Notes view named "Google Calendar Sync" if it
doesn't already exist.  This view selects the LN entries that match the date range 
mentioned above. 

The application will create a Google calendar named "Lotus Notes" if it doesn't already
exist.

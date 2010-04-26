Lotus Notes Google Calendar Sync
--------------------------------
4/26/10

=== Running the Application

Make sure your Windows PATH statement includes the location
for the Lotus Notes main directory, e.g. "c:\Program Files\Lotus\Notes".
This is necessary because the Java classes in Notes.jar call native/C dlls found
in this directory.  Notes.jar is a standard Java interface to Lotus Notes.

Java version 1.6 or higher must be used. Otherwise you won't see anything or get an error.

To start, either execute Run.cmd or use this command:
java -jar "icalbridge.jar" 

It can take 1 minute or more to run, depending on how many calendar entries you have.
The mouse cursor will be an hourglass while the application syncs.
Speed improvements are planned for the future.




=== General Info

General process the application follows:
1. Get a list off all the Lotus Notes calendar entries.
2. Delete all current entries from the destination Google calendar.
3. Add the Lotus Notes entries to the Google calendar. 

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


Only a certain date range of Lotus Notes entries are synchronized to Google.  The LN
entry must be between (today - 7 days) and (today + 60 days).  Entries before or after
this range won't be copied to Google.

If it doesn't already exist, the application will create a Lotus Notes view named
"Google Calendar Sync". This view selects the LN entries that match the date range 
mentioned above. 

If it doesn't already exist, the application will create a Google calendar named
"Lotus Notes". This is the destination calendar will Lotus Notes entries are created.




=== History Log
0.4 4/26/10 Dean Hill
A large number of changes were made. Including:
o Both local and server mail files are now supported.
o Repeating events are supported.
o Some error handling was improved.

package LotusNotesGoogleCalendarBridge.LotusNotesService;

import lotus.domino.*;
import java.io.*;
import java.text.*;
import java.util.*;


public class LotusNotesExport {

    public LotusNotesExport() {
        notesVersion = "unknown";

        // Get the absolute path to this app
        String appPath = new java.io.File("").getAbsolutePath() + System.getProperty("file.separator");
        lnFoundEntriesFullFilename = appPath + lnFoundEntriesFilename;
        lnInRangeEntriesFullFilename = appPath + lnInRangeEntriesFilename;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setMailFile(String mailfile) {
        this.mailfile = mailfile;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void setCredentials(String username, String password) {
        setRequiresAuth(true);
        this.username = username;
        this.password = password;
    }

    public void setDiagnosticMode(boolean value) {
        diagnosticMode = value;
    }

    public void setMinStartDate(Date minStartDate) {
        this.minStartDate = minStartDate;
    }

    public void setMaxEndDate(Date maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public String getNotesVersion() {
        return notesVersion;
    }


    /**
     * Retrieve a list of Lotus Notes calendar entries.
     * @param dominoServer The Domino server to retrieve data from, e.g. "IBM-Domino".
     *    Pass in null to retrieve from a local mail file.
     * @param mailFileName The mail file to read from, e.g. "mail/johnsmith.nsf".
     */
    public ArrayList<NotesCalendarEntry> getCalendarEntries() throws Exception {
        boolean wasNotesThreadInitialized = false;
        ArrayList<NotesCalendarEntry> calendarEntries = new ArrayList<NotesCalendarEntry>();
        NotesCalendarEntry cal = null;

        try {
            // Some users, especially on OS X, have trouble locating Notes.jar (which
            // needs to be in the classpath) and the supporting dll/so/dylib files (which
            // need to be in the path/ld_library_path).  Try to load one of the Lotus
            // classes to make sure we can find Notes.jar.
            // The next try/catch block (with the sinitThread() call) will check if
            // the supporing libs can be found.
            ClassLoader.getSystemClassLoader().loadClass("lotus.domino.NotesThread");
        } catch (Exception ex) {
            throw new Exception("The Lotus Notes Java interface file (Notes.jar) could not be found.\nMake sure Notes.jar is in your classpath.", ex);
        }

        try {
            // Make sure your Windows PATH statement includes the location
            // for the Lotus main directory, e.g. "c:\Program Files\Lotus\Notes".
            // This is necessary because the Java classes call native/C dlls in
            // this directory.
            // If the dlls can't be found, then we will drop directly into
            // the finally section (no exception is thrown).  That's strange.
            // So, we set a flag to indicate whether things succeeded or not.
            NotesThread.sinitThread();
            wasNotesThreadInitialized = true;

            // Note: We cast null to a String to avoid overload conflicts
            Session session = NotesFactory.createSession((String)null, (String)null, password);
            notesVersion = session.getNotesVersion();
            

            String dominoServerTemp = server;
            if (server.equals(""))
                dominoServerTemp = null;
            Database db = session.getDatabase(dominoServerTemp, mailfile, false);
            if (db == null)
                throw new Exception("Couldn't create Lotus Notes Database object.");
            
            // Query Lotus Notes to get calendar entries in our date range.
            // To understand this SELECT, go to http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp
            // and search for the various keywords. Here is an overview...
            //   @IsAvailable(CalendarDateTime) is true if the LN document is a calendar entry
            //   @Explode splits a string based on the delimiters ",; "
            //   The operator *= is a permuted equal operator. It compares all entries on
            //   the left side to all entries on the right side. If there is at least one
            //   match, then true is returned.
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

            String calendarQuery = "SELECT (@IsAvailable(CalendarDateTime) & (@Explode(CalendarDateTime) *= @Explode(@TextToTime(\"" + df.format(minStartDate) + "-" + df.format(maxEndDate) + "\"))))";
            DocumentCollection queryResults = db.search(calendarQuery);

            Document doc;
            doc = queryResults.getFirstDocument();
            // Loop through all entries returned
            while (doc != null)
            {
                Item lnItem;

                // If we are in diagnostic mode, write the entry to a text file
                if (diagnosticMode) {
                    writeEntryToFile(doc);
                }

                cal = new NotesCalendarEntry();

                lnItem = doc.getFirstItem("Subject");
                if (!isItemEmpty(lnItem))
                    cal.setSubject(lnItem.getText());
                else
                    cal.setSubject("<no subject>");

                lnItem = doc.getFirstItem("Body");
                if (!isItemEmpty(lnItem))
                    cal.setBody(lnItem.getText());

                // Get the type of Lotus calendar entry
                lnItem = doc.getFirstItem("Form");
                if (!isItemEmpty(lnItem))
                    cal.setEntryType(lnItem.getText());
                else
                    // Assume we have an appointment
                    cal.setEntryType(NotesCalendarEntry.EntryType.APPOINTMENT);

                if (cal.getEntryType() == NotesCalendarEntry.EntryType.APPOINTMENT)
                {
                    lnItem = doc.getFirstItem("AppointmentType");
                    if (!isItemEmpty(lnItem))
                        cal.setAppointmentType(lnItem.getText());
                }

                lnItem = doc.getFirstItem("Room");
                if (!isItemEmpty(lnItem))
                    cal.setRoom(lnItem.getText());
                lnItem = doc.getFirstItem("Location");
                if (!isItemEmpty(lnItem))
                    cal.setLocation(lnItem.getText());

                lnItem = doc.getFirstItem("$Alarm");
                if (!isItemEmpty(lnItem)) {
                    cal.setAlarm(true);
                    lnItem = doc.getFirstItem("$AlarmOffset");
                    if (!isItemEmpty(lnItem))
                        cal.setAlarmOffsetMins(Integer.parseInt(lnItem.getText()));
                }
                
                //Get attendee info
                lnItem = doc.getFirstItem("REQUIREDATTENDEES");
                if (!isItemEmpty(lnItem)){
                	cal.setRequiredAttendees(lnItem.getText());
                }
                lnItem = doc.getFirstItem("OPTIONALATTENDEES");
                if (!isItemEmpty(lnItem)){
                	cal.setOptionalAttendees(lnItem.getText());
                }
                lnItem = doc.getFirstItem("CHAIR");
                if (!isItemEmpty(lnItem)){
                	cal.setChairperson(lnItem.getText());
                }

                cal.setModifiedDateTime(doc.getLastModified().toJavaDate());

                lnItem = doc.getFirstItem("OrgRepeat");

                // If true, this is a repeating calendar entry
                if (!isItemEmpty(lnItem))
                {
                    // Handle Lotus Notes repeating entries by creating multiple Google
                    // entries

                    Vector startDates = null;
                    Vector endDates = null;

                    lnItem = doc.getFirstItem("StartDateTime");
                    if (!isItemEmpty(lnItem))
                        startDates = lnItem.getValueDateTimeArray();

                    lnItem = doc.getFirstItem("EndDateTime");
                    if (!isItemEmpty(lnItem))
                        endDates = lnItem.getValueDateTimeArray();

                    if (startDates != null)
                    {
                        for (int i = 0 ; i < startDates.size() ; i++) {
                            DateTime notesDate = (DateTime)startDates.get(i);
                            Date startDate = notesDate.toJavaDate();
                            // Only add the entry if it is within our sync date range
                            if (isDateInRange(startDate))
                            {
                                // We are creating multiple entries from one repeating entry.
                                // We use the same Lotus UID for all entries because we will
                                // prepend another GUID before inserting into Google.
                                cal.setUID(doc.getUniversalID());

                                cal.setStartDateTime(startDate);

                                if (endDates != null) {
                                    notesDate = (DateTime)endDates.get(i);
                                    cal.setEndDateTime(notesDate.toJavaDate());
                                }

                                calendarEntries.add(cal.clone());
                            }
                        }
                    }
                }
                else
                {
                    cal.setUID(doc.getUniversalID());

                    lnItem = doc.getFirstItem("StartDateTime");
                    if (!isItemEmpty(lnItem))
                        cal.setStartDateTime(lnItem.getDateTimeValue().toJavaDate());

                    // For To Do tasks, the EndDateTime doesn't exist, but there is an EndDate value
                    lnItem = doc.getFirstItem("EndDateTime");
                    if (isItemEmpty(lnItem))
                        lnItem = doc.getFirstItem("EndDate");
                    if (!isItemEmpty(lnItem))
                        cal.setEndDateTime(lnItem.getDateTimeValue().toJavaDate());
                    
                    // Only add the entry if it is within our sync date range
                    if (isDateInRange(cal.getStartDateTime()))
                        calendarEntries.add(cal);
                }

                doc = queryResults.getNextDocument();
            }

            return calendarEntries;
        } catch (Exception ex) {
            String exMsg = "There was a problem reading Lotus Notes calendar entries.";
            if (ex instanceof NotesException) {
                exMsg = exMsg + "\nNotesException ID: " + ((NotesException)ex).id;
            }

            if (cal != null && cal.getSubject() != null) {
                throw new Exception(exMsg + "\nThe subject of the entry being processed: " + cal.getSubject(), ex);
            }
            else {
                throw new Exception(exMsg, ex);
            }
        } finally {
            // If true, the NotesThread failed to init. The LN dlls probably weren't found.
            // NOTE: Make sure this check is the first line in the finally block. When the
            // init fails, some of the finally block may get skipped.
            if (!wasNotesThreadInitialized) {
                throw new Exception("There was a problem initializing the Lotus Notes thread.\nMake sure the Lotus dll/so/dylib directory is in your path.");
            }

            if (diagnosticMode)
                writeInRangeEntriesToFile(calendarEntries);

            if (lnFoundEntriesWriter != null) {
                lnFoundEntriesWriter.close();
                lnFoundEntriesWriter = null;
            }

            NotesThread.stermThread();
        }
    }


    /**
     * Determine if the calendar entry date is in the range of dates to be processed.
     * @param entryDate - The calendar date to inspect.
     * @return True if the date is in the range, false otherwise.
     */
    public boolean isDateInRange(Date entryDate) {
        if (entryDate != null && entryDate.compareTo(minStartDate) >= 0 && entryDate.compareTo(maxEndDate) <= 0)
            return true;

        return false;
    }


    /**
     * Write all the items in a Lotus Notes Document (aka calendar entry) to a text file.
     * @param doc - The Document to process.
     */
    public void writeEntryToFile(lotus.domino.Document doc) throws IOException, NotesException {
        // Open the output file if it is not open
        if (lnFoundEntriesWriter == null) {
            lnFoundEntriesFile = new File(lnFoundEntriesFullFilename);
            lnFoundEntriesWriter = new BufferedWriter(new FileWriter(lnFoundEntriesFile));
        }

        // Add the items to a list so we can sort them later
        List<String> itemsAndValues = new ArrayList<String>();

        lnFoundEntriesWriter.write("=== Calendar Entry ===\n");

        if (doc.isDeleted()) {
            lnFoundEntriesWriter.write("  Doc is flagged Deleted.\n\n");
            return;
        }
        if (doc.isEncrypted()) {
            lnFoundEntriesWriter.write("  Doc is flagged Encrypted.\n\n");
            return;
        }
        if (doc.isSigned()) {
            lnFoundEntriesWriter.write("  Doc is flagged Signed.\n\n");
            return;
        }

        itemsAndValues.add("  LastModified: " + doc.getLastModified() + "\n");
        itemsAndValues.add("  UniversalID: " + doc.getUniversalID() + "\n");

        String itemName;

        // Loop through each item
        for (Object itemObj : doc.getItems())
        {
            if (itemObj instanceof Item)
                itemName = ((Item)itemObj).getName();
            else
                continue;

            // Get the item value using the item name
            Item lnItem = doc.getFirstItem(itemName);

            if (lnItem != null)
                itemsAndValues.add("  " + itemName + ": " + lnItem.getText() + "\n");
        }

        Collections.sort(itemsAndValues, String.CASE_INSENSITIVE_ORDER);
        for (String itemStr : itemsAndValues) {
            lnFoundEntriesWriter.write(itemStr);
        }

        lnFoundEntriesWriter.write("\n\n");
    }


    public void writeInRangeEntriesToFile(List<NotesCalendarEntry> calendarEntries) throws Exception {
        try {
            lnInRangeEntriesFile = new File(lnInRangeEntriesFullFilename);
            lnInRangeEntriesWriter = new BufferedWriter(new FileWriter(lnInRangeEntriesFile));

            if (calendarEntries == null)
                lnInRangeEntriesWriter.write("The calendar entries list is null.\n");
            else
                for (NotesCalendarEntry entry : calendarEntries) {
                    lnInRangeEntriesWriter.write("=== " + entry.getSubject() + "\n");
                    lnInRangeEntriesWriter.write("  UID: " + entry.getUID() + "\n");
                    lnInRangeEntriesWriter.write("  Start Date: " + entry.getStartDateTime() + "\n");
                    lnInRangeEntriesWriter.write("  End Date: " + entry.getEndDateTime() + "\n");
                    lnInRangeEntriesWriter.write("  Modified Date: " + entry.getModifiedDateTime() + "\n");
                    lnInRangeEntriesWriter.write("  Location: " + entry.getLocation() + "\n");
                    lnInRangeEntriesWriter.write("  Room: " + entry.getRoom() + "\n");
                    lnInRangeEntriesWriter.write("  Alarm: " + entry.getAlarm() + "\n");
                    lnInRangeEntriesWriter.write("  Alarm Offset Mins: " + entry.getAlarmOffsetMins() + "\n");
                    lnInRangeEntriesWriter.write("  Appointment Type: " + entry.getAppointmentType() + "\n");
                    lnInRangeEntriesWriter.write("  Entry Type: " + entry.getEntryType() + "\n");
                    lnInRangeEntriesWriter.write("\n");
                }
        } catch (Exception ex) {
            throw ex;
        }
        finally {
            if (lnInRangeEntriesWriter != null) {
                lnInRangeEntriesWriter.close();
                lnInRangeEntriesWriter = null;
            }
        }
    }

 
    /**
     * Returns true if the Lotus Notes Item object is empty or null.
     * @param lnItem The object to inspect.
     */
    protected boolean isItemEmpty(Item lnItem) {
        try {
            // Lotus Notes Item objects are usually read by name, e.g. lnItem = doc.getFirstItem("Subject").
            // If the name doesn't exist at all then null is returned.
            // If the name does exist, but doesn't have a value, then lnItem.getText() returns "".
            // Check for both conditions.
            if (lnItem == null || (lnItem != null && lnItem.getText().isEmpty()))
                return true;            
        } catch (Exception ex) {
            // An error means we couldn't read the Item, so consider it empty
            return true;
        }
        
        return false;
    }


    String calendarViewName = "Google Calendar Sync";
    String username, password;
    String server, mailfile;
    boolean requiresAuth;
    boolean diagnosticMode = false;
    String notesVersion;

    // Our min and max dates for entries we will process.
    // If the calendar entry is outside this range, it is ignored.
    Date minStartDate = null;
    Date maxEndDate = null;

    // These items are used when diagnosticMode = true
    File lnFoundEntriesFile = null;
    BufferedWriter lnFoundEntriesWriter = null;
    final String lnFoundEntriesFilename = "LotusNotesFoundEntries.txt";
    // Filename with full path
    String lnFoundEntriesFullFilename;
    File lnInRangeEntriesFile = null;
    BufferedWriter lnInRangeEntriesWriter = null;
    final String lnInRangeEntriesFilename = "LotusNotesInRangeEntries.txt";
    String lnInRangeEntriesFullFilename;
}
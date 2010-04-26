package LotusNotesGoogleCalendarBridge.LotusNotesService;

import lotus.domino.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LotusNotesExport {

    public LotusNotesExport() {
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void setCredentials(String username, String password) {
        setRequiresAuth(true);
        this.username = username;
        this.password = password;
    }


    /**
     * Retrieve a list of Lotus Notes calendar entries.
     * @param dominoServer The Domino server to retrieve data from, e.g. "IBM-Domino".
     *    Pass in null to retrieve from a local mail file.
     * @param mailFileName The mail file to read from, e.g. "mail/johnsmith.nsf".
     * @return A list of Lotus Notes calendar entries.
     */
    public List<NotesCalendarEntry> start(String dominoServer, String mailFileName) {
        List<NotesCalendarEntry> cals = new ArrayList<NotesCalendarEntry>();

        try {
            // For repeating events, define our min start date
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -7);
            Date minStartDate = now.getTime();
            // For repeating events, define our max end date
            now.add(Calendar.DATE, 67);
            Date maxEndDate = now.getTime();

            // Make sure your Windows PATH statement includes the location
            // for the Lotus main directory, e.g. "c:\Program Files\Lotus\Notes".
            // This is necessary because the Java classes call native/C dlls in
            // this directory.
            NotesThread.sinitThread();

            // Note: We cast null to a String to avoid overload conflicts
            Session session = NotesFactory.createSession((String)null, (String)null, password);

            String dominoServerTemp = dominoServer;
            if (dominoServer.equals(""))
                dominoServerTemp = null;
            Database db = session.getDatabase(dominoServerTemp, mailFileName, false);
            if (db == null)
                throw new Exception("Couldn't create Database object.");
            
            View v;

            v = db.getView(calendarViewName);
            // If true, the view doesn't exist
            if (v == null) {
                // Dynamically create a view that selects all calendar entries from (today - 7 days) to (today + 60 days).
                // This gives us a good range of entries.
                //
                // To understand this SELECT, go to http://publib.boulder.ibm.com/infocenter/domhelp/v8r0/index.jsp
                // and search for the various keywords. Here is an overview...
                // @IsAvailable(CalendarDateTime) is true if the LN document is a calendar entry
                // @Explode splits a string based on the delimiters ",; "
                // The operator *= is a permuted equal operator. It compares all entries on
                // the left side to all entries on the right side. If there is at least one
                // match, then true is returned.
                v = db.createView(calendarViewName,
                        "SELECT (@IsAvailable(CalendarDateTime) & (@Explode(CalendarDateTime) *= @Explode(@TextToTime(@Text(@Adjust(@Today;0;0;-7;0;0;0)) + \"-\" + @Text(@Adjust(@Today;0;0;60;0;0;0))))))");
            }

            ViewNavigator vn = v.createViewNav();
            ViewEntry e = vn.getFirstDocument();
            // Loop through all entries in the View
            while (e != null)
            {
                lotus.domino.Document doc = e.getDocument();

                NotesCalendarEntry cal = new NotesCalendarEntry();

                Item lnItem;
                lnItem = doc.getFirstItem("Subject");
                cal.setSubject(lnItem.getText());

                // Get the type of Lotus calendar entry
                lnItem = doc.getFirstItem("Form");
                if (lnItem != null)
                {
                    cal.setEntryType(lnItem.getText());
                    if (cal.getEntryType() == NotesCalendarEntry.EntryType.APPOINTMENT)
                    {
                        lnItem = doc.getFirstItem("AppointmentType");
                        if (lnItem != null)
                            cal.setAppointmentType(lnItem.getText());                    
                    }
                }

                cal.setLocation("<no location>");
                lnItem = doc.getFirstItem("Room");
                if (lnItem != null)
                    cal.setLocation(lnItem.getText());
                lnItem = doc.getFirstItem("Location");
                if (lnItem != null)
                    cal.setLocation(lnItem.getText());

                lnItem = doc.getFirstItem("OrgRepeat");
                // If true, this is a repeating calendar entry
                if (lnItem != null)
                {
                    // Handle Lotus Notes repeating entries by creating multiple Google
                    // entries

                    String[] startDates = null;
                    String[] endDates = null;
                    lnItem = doc.getFirstItem("StartDateTime");
                    if (lnItem != null)
                    {
                        startDates = lnItem.getText().split(";");
                    }

                    lnItem = doc.getFirstItem("EndDateTime");
                    if (lnItem != null)
                    {
                        endDates = lnItem.getText().split(";");
                    }

                    if (startDates != null)
                    {
                        for (int i = 0 ; i < startDates.length ; i++) {
                            Date dt = cal.toDate(startDates[i]);
                            // Only add the entry if it is within our sync date range
                            if (dt != null && dt.compareTo(minStartDate) >= 0 && dt.compareTo(maxEndDate) <= 0)
                            {
                                cal.setStartDateTime(startDates[i]);
                                cal.setEndDateTime(endDates[i]);

                                cals.add(cal.clone());
                            }
                        }
                    }
                }
                else
                {
                    lnItem = doc.getFirstItem("StartDateTime");
                    if (lnItem != null)
                        cal.setStartDateTime(lnItem.getText());

                    lnItem = doc.getFirstItem("EndDateTime");
                    if (lnItem != null)
                        cal.setEndDateTime(lnItem.getText());
                    
                    cals.add(cal);
                }


                e = vn.getNextDocument();
            }
        } catch (Exception ex) {
            Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            NotesThread.stermThread();
        }

        return cals;
    }

    String calendarViewName = "Google Calendar Sync";
    String MailFileURL, username, password;
    boolean requiresAuth;
}
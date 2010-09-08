package LotusNotesGoogleCalendarBridge.LotusNotesService;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class NotesCalendarEntry {

    public NotesCalendarEntry() {
        entryType = EntryType.NONE;
        appointmentType = AppointmentType.APPOINTMENT.NONE;
    }

    /**
     * Create a "deep copy" of this object.
     * @return The new, cloned object.
     */
    @Override
    public NotesCalendarEntry clone() {
        NotesCalendarEntry cal = new NotesCalendarEntry();

        cal.appointmentType = this.appointmentType;
        cal.entryType = this.entryType;
        cal.startDateTime = this.startDateTime;
        cal.endDateTime = this.endDateTime;
        cal.modifiedDateTime = this.modifiedDateTime;
        cal.subject = this.subject;
        cal.location = this.location;
        cal.body = this.body;
        cal.alarm = this.alarm;
        cal.alarmOffsetMins = this.alarmOffsetMins;
        cal.uid = this.uid;

        return cal;
    }

    public void setEntryType(String entryType) {
        if (entryType.equals("Task"))
            this.entryType = EntryType.TASK;
        else
            this.entryType = EntryType.APPOINTMENT;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public void setAppointmentType(String appointmentType) {
        if (appointmentType.equals("0"))
            this.appointmentType = AppointmentType.APPOINTMENT;
        else if (appointmentType.equals("1"))
            this.appointmentType = AppointmentType.ANNIVERSARY;
        else if (appointmentType.equals("2"))
            this.appointmentType = AppointmentType.ALL_DAY_EVENT;
        else if (appointmentType.equals("3"))
            this.appointmentType = AppointmentType.MEETING;
        else if (appointmentType.equals("4"))
            this.appointmentType = AppointmentType.REMINDER;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public void setModifiedDateTime(Date modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBody(String value) {
        this.body = value;
    }

    public void setAlarm(boolean value) {
        this.alarm = value;
    }

    public void setAlarmOffsetMins(int value) {
        this.alarmOffsetMins = value;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public AppointmentType getAppointmentType() {
        return appointmentType;
    }

    /**
     * @return Returns the start datetime in Lotus Notes format.
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    public String getStartDateTimeGoogle() throws ParseException {
        return getGoogleDateTimeString(startDateTime);
    }

    // Return the start date without time in Google format.
    public String getStartDateGoogle() throws ParseException {
        return getGoogleDateString(startDateTime);
    }

    /**
     * Return the start date (without time) in Google format.
     * @param addDays Add (or subtract) this many days from the returned value.
     */
    public String getStartDateGoogle(int addDays) throws ParseException {
        return getGoogleDateString(startDateTime, addDays);
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public String getEndDateTimeGoogle() throws ParseException {
        return getGoogleDateTimeString(endDateTime);
    }

    /**
     * Return the end date (without time) in Google format.
     * @param addDays Add (or subtract) this many days from the returned value.
     */
    public String getEndDateGoogle(int addDays) throws ParseException {
        return getGoogleDateString(endDateTime, addDays);
    }

    public Date getModifiedDateTime() {
        return modifiedDateTime;
    }

    public String getModifiedDateTimeGoogle() throws ParseException {
        return getGoogleDateTimeString(modifiedDateTime);
    }

    public String getSubject() {
        return subject;
    }

    public String getLocation() {
        return location;
    }

    public String getBody() {
        return body;
    }

    public boolean getAlarm() {
        return alarm;
    }

    public int getAlarmOffsetMins() {
        return alarmOffsetMins;
    }

    /**
     * Returns the UID stored in Lotus Notes.
     */
    public String getUID() {
        return uid;
    }

    /**
     * Returns a UID that includes the Lotus Notes UID and other
     * info to help sync this entry.
     */
    public String getSyncUID() {
        // Description of the SyncUID format...
        // Include a version stamp in case this format changes in the future.
        // Include the Lotus Notes UID.
        // Include the start timestamp. This is needed because we make multiple entries
        // from one repeating event. If we don't include the start timestamp, all the
        // multiple entries would have the same SyncUID.
        // Include the modified timestamp. If the Lotus entry ever changes, this value
        // will change and we'll know to update the sync.
        return currSyncUIDVersion + "-" + uid + "-" + startDateTime.getTime() + "-" + modifiedDateTime.getTime();
    }

    
    /**
     * Convert from Java Date object to Google format (YYYY-MM-DDTHH:MM:SS).
     * Note: Google format is the same as the XML standard xs:DateTime format.
     * @param sourceDate The source datetime.
     * @return The datetiem string in Google format.
     */
    private String getGoogleDateTimeString(Date sourceDate) throws ParseException {
        DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String googleDateTimeFormat = dfGoogle.format(sourceDate);

        return googleDateTimeFormat;
    }


    /**
     * This method calls the overloaded version of this method, and passes
     * zero in for addDays.
     */
    private String getGoogleDateString(Date sourceDate) throws ParseException {
        return getGoogleDateString(sourceDate, 0);
    }

    /**
     * Convert from Java Date object to Google string format (YYYY-MM-DD) with no
     * time portion.
     * Note: Google format is the same as the XML standard xs:DateTime format.
     * @param sourceDate The source datetime.
     * @param addDays Add (or subtract) this many days from the returned value.
     * @return The date string in Google format.
     */
    private String getGoogleDateString(Date sourceDate, int addDays) throws ParseException {
        // Add days to the date
        Calendar dateTimeTemp = Calendar.getInstance();
        dateTimeTemp.setTime(sourceDate);
        dateTimeTemp.add(Calendar.DATE, addDays);

        // Convert to Google format
        DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd");
        String googleDateFormat = dfGoogle.format(dateTimeTemp.getTime());

        return googleDateFormat;
    }


    // Version stamp for the SyncUID format
    protected static final int currSyncUIDVersion = 1;

    // The Lotus Notes type for this calendar entry
    public enum EntryType { NONE, APPOINTMENT, TASK };
    // The various sub-types of an appointment
    public enum AppointmentType { NONE, APPOINTMENT, ANNIVERSARY, ALL_DAY_EVENT, MEETING, REMINDER };

    protected EntryType entryType;
    protected AppointmentType appointmentType;
    // DateTime in Lotus Notes format
    protected Date startDateTime = null;
    protected Date endDateTime = null;
    // The last date/time the entry was modified
    protected Date modifiedDateTime = null;
    protected String subject = null;
    protected String location = null;
    // Body is the description for the calendar entry
    protected String body = null;
    // True if the entry has an alarm set
    protected boolean alarm = false;
    // The number of minutes until the alarm goes off. Lotus can set alarms to notify
    // before (default) or after the date.  "Before" offsets are negative values and
    // "after" offsets are positive.  So, -15 means notify 15 minutes before the event.
    protected int alarmOffsetMins = 0;
    // Unique ID for this calendar entry. This is the value created by Lotus.
    protected String uid = null;
}

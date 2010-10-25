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
        cal.subject = this.subject;
        cal.location = this.location;
        cal.body = this.body;
        cal.alarm = this.alarm;
        cal.alarmOffsetMins = this.alarmOffsetMins;
        cal.id = this.id;

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

    public void setID(String id) {
        this.id = id;
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
        Calendar endDateTimeTemp = Calendar.getInstance();
        endDateTimeTemp.setTime(endDateTime);
        endDateTimeTemp.add(Calendar.DATE, addDays);

        return getGoogleDateString(endDateTimeTemp.getTime());
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

    public String getID() {
        return id;
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
     * Convert from Java Date object to Google string format (YYYY-MM-DD) with no
     * time portion.
     * Note: Google format is the same as the XML standard xs:DateTime format.
     * @param sourceDate The source datetime.
     * @return The date string in Google format.
     */
    private String getGoogleDateString(Date sourceDate) throws ParseException {
        DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd");
        String googleDateFormat = dfGoogle.format(sourceDate);

        return googleDateFormat;
    }

    // The Lotus Notes type for this calendar entry
    public enum EntryType { NONE, APPOINTMENT, TASK };
    // The various sub-types of an appointment
    public enum AppointmentType { NONE, APPOINTMENT, ANNIVERSARY, ALL_DAY_EVENT, MEETING, REMINDER };

    protected EntryType entryType;
    protected AppointmentType appointmentType;
    // DateTime in Lotus Notes format
    protected Date startDateTime = null;
    protected Date endDateTime = null;
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
    protected String id = null;
}

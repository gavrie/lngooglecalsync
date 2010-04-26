package LotusNotesGoogleCalendarBridge.LotusNotesService;

import java.util.Date;
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
    public NotesCalendarEntry clone() {
        NotesCalendarEntry cal = new NotesCalendarEntry();

        cal.appointmentType = this.appointmentType;
        cal.entryType = this.entryType;
        cal.startDateTime = this.startDateTime;
        cal.endDateTime = this.endDateTime;
        cal.subject = this.subject;
        cal.location = this.location;
        cal.body = this.body;
        cal.id = this.id;

        return cal;
    }

    public void setEntryType(String entryType) {
        if (entryType.equals("Task"))
            this.entryType = EntryType.TASK;
        else
            this.entryType = EntryType.APPOINTMENT;
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

    public void setStartDateTime(String startTime) {
        this.startDateTime = startTime;
    }

    public void setEndDateTime(String endTime) {
        this.endDateTime = endTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getStartDateTimeGoogle() throws ParseException {
        return convertDateTimeFormat(startDateTime);
    }

    // Return the start date without time in Google format.
    public String getStartDateGoogle() throws ParseException {
        return convertDateFormat(startDateTime);
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getEndDateTimeGoogle() throws ParseException {
        return convertDateTimeFormat(endDateTime);
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

    public String getID() {
        return id;
    }

    
    /**
     * Convert from Lotus Notes Document datetime format (MM/DD/YYYY HH:MM:SS PM) to
     * Google format (YYYY-MM-DDTHH:MM:SS).  Note: Google format is the same as the
     * XML standard xs:DateTime format.
     */
    private String convertDateTimeFormat(String lotusnotesDateTimeFormat) throws ParseException {
        DateFormat dfLotus = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date dt = dfLotus.parse(lotusnotesDateTimeFormat);

        DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String googleDateTimeFormat = dfGoogle.format(dt);

        return googleDateTimeFormat;
    }


    /**
     * Convert from Lotus Notes Document datetime format (MM/DD/YYYY HH:MM:SS PM) to
     * Java Date object.
     */
    public Date toDate(String lotusnotesDateTimeFormat) {
        try {
            DateFormat dfLotus = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            Date dt = dfLotus.parse(lotusnotesDateTimeFormat);

            return dt;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Convert from Lotus Notes Document datetime format (MM/DD/YYYY HH:MM:SS PM) to
     * Google format (YYYY-MM-DD) with no time portion.
     * Note: Google format is the same as the XML standard xs:DateTime format.
     * @param lotusnotesDateTimeFormat The datetime in Lotus Notes format.
     * @return The datetime in Google format.
     */
    private String convertDateFormat(String lotusnotesDateTimeFormat) throws ParseException {
        DateFormat dfLotus = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date dt = dfLotus.parse(lotusnotesDateTimeFormat);

        DateFormat dfGoogle = new SimpleDateFormat("yyyy-MM-dd");
        String googleDateFormat = dfGoogle.format(dt);

        return googleDateFormat;
    }

    // The Lotus Notes type for this calendar entry
    public enum EntryType { NONE, APPOINTMENT, TASK };
    // The various sub-types of an appointment
    public enum AppointmentType { NONE, APPOINTMENT, ANNIVERSARY, ALL_DAY_EVENT, MEETING, REMINDER };

    protected EntryType entryType;
    protected AppointmentType appointmentType;
    protected String startDateTime, endDateTime;
    protected String subject, location;
    protected String body;
    protected String id;
}

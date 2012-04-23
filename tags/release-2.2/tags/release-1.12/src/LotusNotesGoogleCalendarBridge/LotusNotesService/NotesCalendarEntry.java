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
        cal.room = this.room;
        cal.body = this.body;
        cal.alarm = this.alarm;
        cal.alarmOffsetMins = this.alarmOffsetMins;
        cal.privateEntry = this.privateEntry;
        cal.uid = this.uid;
        cal.requiredAttendees = this.requiredAttendees;
        cal.optionalAttendees = this.optionalAttendees;
        cal.chairperson = this.chairperson;

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

    public void setRoom(String room) {
        this.room = room;
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

    public void setPrivate(boolean value) {
        this.privateEntry = value;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public void setRequiredAttendees(String names){
		requiredAttendees = names;
    }

    public void setOptionalAttendees(String names){
		optionalAttendees = names;
    }
    
    public void setChairperson(String name){
    	chairperson = name;
    }

    /**
     * Returns the chairperson name as it was retrieved from Lotus.
     * The standard format is like "CN=John A Smith/US/Acme@MAIL".
     */
    public String getChairperson(){
    	return chairperson;
    }

    /**
     * Returns the Lotus chairperson name without the Lotus metadata.
     * Given a Lotus format like "CN=John A Smith/US/Acme@MAIL", this
     * method returns "John A Smith".
     */
    public String getChairpersonPlain(){
        return getNamePlain(chairperson);
    }

    /**
     * Returns the required attendee list as it was retrieved from Lotus.
     * The standard format is like "John A Smith/US/Acme@MAIL;kelly@gmail.com;Jane B Doe/H9876/Sponge@Bob".
     */
    public String getRequiredAttendees(){
    	return requiredAttendees;
    }
    
    /**
     * Returns the attendee list without the Lotus metadata.
     * Given a Lotus list like "John A Smith/US/Acme@MAIL;kelly@gmail.com;Jane B Doe/H9876/Sponge@Bob",
     * this method returns "John A Smith; kelly@gmail.com; Jane B Doe"
     */
    public String getRequiredAttendeesPlain(){
        return getNameListPlain(requiredAttendees);
    }

    /**
     * Returns the attendee list without the Lotus metadata.
     * Given a Lotus list like "John A Smith/US/Acme@MAIL;kelly@gmail.com;Jane B Doe/H9876/Sponge@Bob",
     * this method returns "John A Smith; kelly@gmail.com; Jane B Doe"
     */
    public String getOptionalAttendees(){
        return getNameListPlain(optionalAttendees);
    }

    /**
     * Given a Lotus format like "CN=John A Smith/US/Acme@MAIL", this
     * method returns "John A Smith".
     */
    protected String getNamePlain(String lotusName){
        if (lotusName == null)
            return null;

        String s = lotusName;

        // Strip off the "CN="
        int i = s.indexOf("=");
        if (i > -1) {
            s = s.substring(i+1);
        }

        // Strip off the "/US/Acme@MAIL"
        i = s.indexOf("/");
        if (i > -1) {
            s = s.substring(0, i);
        }

        return s;
    }

    /**
     * Returns a list of names without the Lotus metadata.
     * Given a Lotus list like "John A Smith/US/Acme@MAIL;kelly@gmail.com;Jane B Doe/H9876/Sponge@Bob",
     * this method returns "John A Smith; kelly@gmail.com; Jane B Doe"
     */
    protected String getNameListPlain(String lotusNames){
        if (lotusNames == null)
            return null;

        String[] nameList = lotusNames.split(";");

        StringBuffer sb = new StringBuffer();
        for (String lotusName : nameList) {
            if (sb.length() > 0)
                sb.append("; ");
            sb.append(getNamePlain(lotusName));
        }

        return sb.toString();
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

    public String getRoom() {
        return room;
    }

    /**
     * Lotus has both a Location field and a Room field. Usually only one is filled in.
     * But it is possible to set both fields, e.g. Location = "New York Blg 3" and
     * Room = "Fancy Conf Room".
     * If only one of Location or Room has a value, than that one value is returned.
     * If both fields have values, then the concatinated value is returned.
     * If neither fields have values, null is returned.
     */
    public String getGoogleWhereString() {
        String whereStr = null;
        if (location != null && !location.isEmpty()) {
            whereStr = location;
        }

        if (room != null && !room.isEmpty()) {
            if (whereStr == null)
                whereStr = room;
            else
                // We have both a Location and Room values
                whereStr = whereStr + " : " + room;
        }

        return whereStr;
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

    public boolean getPrivate() {
        return privateEntry;
    }

    public int getAlarmOffsetMinsGoogle() {
        // Lotus Notes alarms can be before (negative value) or after (positive value)
        // the event.  Google only supports alarms before the event and the number
        // is then positive.
        // So, convert to Google as follows: alarms after (positive) are made 0,
        // alarms before (negative) are made positive.
        int alarmMins = 0;
        if (alarmOffsetMins < 0)
            alarmMins = Math.abs(alarmOffsetMins);

        return alarmMins;
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
        // Layout of the SyncUID format:
        //   Include a version stamp in case this format changes in the future.
        //   Include the Lotus Notes UID.
        //   Include the start timestamp. This is needed because we make multiple entries
        //   from one repeating event. If we don't include the start timestamp, all the
        //   multiple entries would have the same SyncUID.
        //   Include the modified timestamp. If the Lotus entry ever changes, this value
        //   will change and we'll know to update the sync.
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
    protected String room = null;
    // Body is the description for the calendar entry
    protected String body = null;
    // True if the entry has an alarm set
    protected boolean alarm = false;
    // The number of minutes until the alarm goes off. Lotus can set alarms to notify
    // before (default) or after the date.  "Before" offsets are negative values and
    // "after" offsets are positive.  So, -15 means notify 15 minutes before the event.
    protected int alarmOffsetMins = 0;
    // True if the entry has the Mark Private flag set
    protected boolean privateEntry = false;
    // Unique ID for this calendar entry. This is the value created by Lotus.
    protected String uid = null;
    protected String requiredAttendees = null;
    protected String optionalAttendees = null; 
    protected String chairperson = null;    
}

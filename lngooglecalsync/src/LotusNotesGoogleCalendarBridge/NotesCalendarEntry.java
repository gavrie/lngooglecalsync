package LotusNotesGoogleCalendarBridge;

public class NotesCalendarEntry {

    public NotesCalendarEntry() {
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

    public String getStartDateTime() {
        String convertedTime = null;
        if (startDateTime != null) {
            convertedTime = convertTimeFormat(startDateTime);
        }
        return convertedTime;
    }

    public String getEndDateTime() {
        return convertTimeFormat(endDateTime);
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

    private String convertTimeFormat(String lotusnotesDateTimeFormat) {
        String year, month, day, hour, minute, second, timezone1, timezone2, timezone;

        year = lotusnotesDateTimeFormat.substring(0, 4);
        month = lotusnotesDateTimeFormat.substring(4, 6);
        day = lotusnotesDateTimeFormat.substring(6, 8);
        hour = lotusnotesDateTimeFormat.substring(9, 11);
        minute = lotusnotesDateTimeFormat.substring(11, 13);
        second = lotusnotesDateTimeFormat.substring(13, 15);
        timezone1 = lotusnotesDateTimeFormat.substring(18, 21);
        timezone2 = lotusnotesDateTimeFormat.substring(16, 18);

        timezone = timezone1 + ":" + timezone2;
        String googleDateTimeFormat = year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":" + second + timezone;

        return googleDateTimeFormat;
    }
    String startDate, endDate, startDateTime, endDateTime, subject, location, body, id;
}

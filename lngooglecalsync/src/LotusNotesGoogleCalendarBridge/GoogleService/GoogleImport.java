package LotusNotesGoogleCalendarBridge.GoogleService;

import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleImport {

    public GoogleImport(String accountname, String password, boolean useSSL) {
        try {
            String protocol = "https:";
            if (!useSSL) {
                protocol = "http:";
            }

            mainCalendarFeedUrl = new URL(protocol + "//www.google.com/calendar/feeds/" + accountname + "/owncalendars/full");
            privateCalendarFeedUrl = new URL(protocol + "//www.google.com/calendar/feeds/" + accountname + "/private/full");
            service = new CalendarService("Corporate-LotusNotes-Calendar");
            if (useSSL) service.useSsl();
            service.setUserCredentials(accountname, password);
        } catch (IOException e) {
            System.err.println("API Error: " + e);
        } catch (ServiceException e) {
            System.err.println("API Error: " + e);
        }
    }

    public GoogleImport() {
    }

    public static void main(String[] a) {
        String cmd_user = null;
        String cmd_pwd = null;

        System.out.println(a.length);

        if (a.length == 2) {
            cmd_user = a[0];
            cmd_pwd = a[1];

            System.out.println("1: " + cmd_user);
        } else {
            System.exit(1);
        }

        try {

            GoogleImport gi = new GoogleImport(cmd_user, cmd_pwd,true);
            gi.deleteCalendar();
            CalendarEntry calentry = gi.createCalendar();

        } catch (IOException ex) {
            Logger.getLogger(GoogleImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServiceException ex) {
            Logger.getLogger(GoogleImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CalendarEntry createCalendar() throws IOException, ServiceException {

        CalendarEntry calendar = new CalendarEntry();
        calendar.setTitle(new PlainTextConstruct("Lotus Notes"));
        calendar.setSummary(new PlainTextConstruct("Lotus Notes Calendar"));
        calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
        calendar.setHidden(HiddenProperty.FALSE);
        calendar.setSelected(SelectedProperty.TRUE);
        calendar.setColor(new ColorProperty(COLOR));

        CalendarEntry returnedCalendar = service.insert(mainCalendarFeedUrl, calendar);
        returnedCalendar.update();

        // get the feed url reference so that we can add events to the new calendar.
        newCalendarFeedUrl = new URL(returnedCalendar.getLink("alternate", "application/atom+xml").getHref());

        return returnedCalendar;
    }

    public void deleteCalendar() {

        try {
            CalendarFeed calendars = service.getFeed(mainCalendarFeedUrl, CalendarFeed.class);

            for (int i = 0; i < calendars.getEntries().size(); i++) {
                CalendarEntry entry = calendars.getEntries().get(i);
                if (entry.getTitle().getPlainText().equals("Lotus Notes")) {
                    String entryId = entry.getId();
                    entry.delete();
                }

            }
        } catch (Exception e) {
            System.err.println("API Error: " + e);
        }
    }

    public void createEvent(List cals, boolean inMainCalendar) throws ServiceException, IOException {

        for (int i = 0; i < cals.size(); i++) {
            NotesCalendarEntry cal = (NotesCalendarEntry) cals.get(i);
            CalendarEventEntry event = new CalendarEventEntry();

            event.setTitle(new PlainTextConstruct(cal.getSubject()));
            Where location = new Where();
            location.setValueString(cal.getLocation());
            event.addLocation(location);
            DateTime startTime, endTime;
            try {
                startTime = DateTime.parseDateTime(cal.getStartDateTime());
                endTime = DateTime.parseDateTime(cal.getEndDateTime());
            } catch (Exception e) {
                System.err.println("Skipping a calendar entry as it is not supported yet!");
                continue;
            }
            When eventTimes = new When();
            eventTimes.setStartTime(startTime);
            eventTimes.setEndTime(endTime);
            event.addTime(eventTimes);
            try {
                if (inMainCalendar) {
                    service.insert(privateCalendarFeedUrl, event);
                } else {
                    service.insert(newCalendarFeedUrl, event);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void setCalendarColor(String color) {
        if (color.startsWith("#") && color.length() == 7) {
            COLOR = color;
        }
    }
    URL newCalendarFeedUrl = null;
    URL mainCalendarFeedUrl = null;
    URL privateCalendarFeedUrl = null;
    URL lotusNotesFeedUrl = null;
    CalendarService service;
    String COLOR = "#2952A3";  //default cal color
}

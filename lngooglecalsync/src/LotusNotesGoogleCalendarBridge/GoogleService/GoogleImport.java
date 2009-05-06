package LotusNotesGoogleCalendarBridge.GoogleService;

import LotusNotesGoogleCalendarBridge.LotusNotesService.NotesCalendarEntry;
import LotusNotesGoogleCalendarBridge.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class GoogleImport {

    public GoogleImport(String accountname, String password) {
        try {
            calendarsFeedUrl = new URL("https://www.google.com/calendar/feeds/" + accountname + "/private/full");
            calendarFeedUrl = new URL("https://www.google.com/calendar/feeds/" + accountname + "/owncalendars/full");
            service = new CalendarService("Corporate-LotusNotes-Calendar");
            service.setUserCredentials(accountname, password);
        } catch (IOException e) {
            System.err.println("API Error: " + e);
        } catch (ServiceException e) {
            System.err.println("API Error: " + e);
        }
    }

    public GoogleImport() {
    }

    private CalendarEntry createCalendar() throws IOException, ServiceException {

        CalendarEntry calendar = new CalendarEntry();
        calendar.setTitle(new PlainTextConstruct("Lotus Notes"));
        calendar.setSummary(new PlainTextConstruct("Lotus Notes Calendar"));
        calendar.setTimeZone(new TimeZoneProperty("America/Los_Angeles"));
        calendar.setHidden(HiddenProperty.FALSE);
        calendar.setColor(new ColorProperty(BLUE));
        calendar.addLocation(new Where("", "", "Yokohama"));

        CalendarEntry returnedCalendar = service.insert(calendarFeedUrl, calendar);

        CalendarEventEntry event = new CalendarEventEntry();

        return returnedCalendar;
    }

    private void deleteCalendar(CalendarService service, URL feedUrl) {

        try {
            CalendarFeed calendars = service.getFeed(feedUrl, CalendarFeed.class);

            for (int i = 0; i < calendars.getEntries().size(); i++) {
                CalendarEntry entry = calendars.getEntries().get(i);
                if (entry.getTitle().getPlainText().equals("Lotus Notes")) {
                    String entryId = entry.getId();
                    System.out.println(entryId);
                    entry.delete();
                }

            }
        } catch (Exception e) {
            System.err.println("API Error: " + e);
        }
    }

    public void createEvent(List cals) throws ServiceException, IOException {

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
            CalendarEventEntry entry = service.insert(calendarsFeedUrl, event);
        }
    }

    //URL metafeedUrl;
    //URL allcalendarsFeedUrl;
    //URL LotusNotesCalenderUrl;
    URL calendarsFeedUrl = null;
    URL calendarFeedUrl = null;
    CalendarService service;
    //String METAFEED_URL_BASE = "https://www.google.com/calendar/feeds/";
    //String OWNCALENDARS_FEED_URL_SUFFIX = "/owncalendars/full";
    String BLUE = "#2952A3";
}

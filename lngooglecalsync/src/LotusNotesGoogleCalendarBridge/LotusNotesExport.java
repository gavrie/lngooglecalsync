package LotusNotesGoogleCalendarBridge;

import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

public class LotusNotesExport {

    public void start(String mailFileURL, String account, String password) {
        try {
            String url = (mailFileURL + "/" + CalUri);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            Element root = doc.getRootElement();
            List viewentries = root.getChildren("viewentry");

            List cals = new ArrayList();

            for (int i = 0; i < viewentries.size(); i++) {
                boolean supported = true;
                NotesCalendarEntry cal = new NotesCalendarEntry();
                Element viewentry = (Element) viewentries.get(i);
                cal.setID(viewentry.getAttributeValue("unid"));
                List entries = viewentry.getChildren("entrydata");
                for (int a = 0; a < entries.size(); a++) {
                    Element entrydata = (Element) entries.get(a);
                    String field = entrydata.getAttributeValue("name");

                    if (field.equals(startDateTimeField)) {
                        cal.setStartDateTime(entrydata.getChildText("datetime"));
                    }
                    if (field.equals(endDateTimeField)) {
                        cal.setEndDateTime(entrydata.getChildText("datetime"));
                    }

                    if (field.equals(contentField)) {
                        try {
                            List textlist = entrydata.getChild("textlist").getChildren("text");
                            Element tSubject = (Element) textlist.get(0);
                            cal.setSubject(tSubject.getText());
                            if (textlist.size() == 3) {
                                Element tLocation = (Element) textlist.get(1);
                                cal.setLocation(tLocation.getText());
                            }
                        } catch (Exception e) {
                            supported = false;
                            System.err.println("Skipping a calendar entry as it is not supported yet!");
                        }
                    }

                }
                if (supported) {
                    cals.add(cal);
                }
            }

            GoogleImport a = new GoogleImport(account, password);
          
            try {
                a.createEvent(cals);

            } catch (ServiceException ex) {
                Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
            }


        } catch (IOException ex) {
            Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
        }
    }
    static String CalUri = "($calendar)?ReadViewEntries";
    static String startDateTimeField = "$144";
    static String endDateTimeField = "$146";
    static String contentField = "$147";
}



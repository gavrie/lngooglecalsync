package LotusNotesGoogleCalendarBridge.LotusNotesService;

import LotusNotesGoogleCalendarBridge.SSLModule.CustomSSLHandler;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

public class LotusNotesExport {

    public LotusNotesExport() {
        
    }

    public LotusNotesExport(String[] dateRange) {
        String startDate = dateRange[0];
        String endDate = dateRange[1];

        CalUri = ("($calendar)?ReadViewEntries&KeyType=time&StartKey="+ startDate +"&UntilKey=" + endDate);        
    }

    public List<NotesCalendarEntry> start(String mailFileURL) {
        List<NotesCalendarEntry> cals = new ArrayList<NotesCalendarEntry>();
        
        try {

            TrustManager[] ignoreCerts = new TrustManager[]{new CustomSSLHandler()};

            try {
                SSLContext context;
                context = SSLContext.getInstance("SSL");
                context.init(null, ignoreCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            } catch (KeyManagementException ex) {
                Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
            }

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            String url = (mailFileURL + "/" + CalUri);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(url);
            Element root = doc.getRootElement();
            List viewentries = root.getChildren("viewentry");           

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

        } catch (IOException ex) {
            Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
        }
        return cals;
    }
    
    static String CalUri = "($calendar)?ReadViewEntries";
    static String startDateTimeField = "$144";
    static String endDateTimeField = "$146";
    static String contentField = "$147";
}



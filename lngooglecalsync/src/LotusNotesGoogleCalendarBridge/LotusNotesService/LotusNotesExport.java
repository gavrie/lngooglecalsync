package LotusNotesGoogleCalendarBridge.LotusNotesService;

import LotusNotesGoogleCalendarBridge.SSLModule.EasySSLProtocolSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

public class LotusNotesExport {

    public LotusNotesExport() {
        configureSSL();
    }

    public LotusNotesExport(String[] dateRange) {
        String startDate = dateRange[0];
        String endDate = dateRange[1];

        CalUri = ("/($calendar)?ReadViewEntries&KeyType=time&count=9999&StartKey=" + startDate + "&UntilKey=" + endDate);
        configureSSL();
    }

    private void configureSSL() {
        Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyhttps);
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void setCredentials(String username, String password) {
        setRequiresAuth(true);
        this.username = username;
        this.password = password;
    }

    private InputStream getLotusNotesXML() {

        InputStream XMLresult = null;

        try {

            HttpClient client = new HttpClient();

            if (requiresAuth) {
                
                PostMethod post = new PostMethod(MailFileURL + "/?Login");
                post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
                NameValuePair[] data = {new NameValuePair("username", username), new NameValuePair("password", password)};
                post.setRequestBody(data);

                //login and keep cookie for this session.
                client.executeMethod(post);
            }

            GetMethod get = new GetMethod(MailFileURL + CalUri);
            
            //now get the data
            client.executeMethod(get);

            XMLresult = get.getResponseBodyAsStream();
      
        } catch (IOException ex) {
            Logger.getLogger(LotusNotesExport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return XMLresult;
    }

    public List<NotesCalendarEntry> start(String mailFileURL) {
        this.MailFileURL = mailFileURL;
        List<NotesCalendarEntry> cals = new ArrayList<NotesCalendarEntry>();

        try {

            InputStream xmldata = getLotusNotesXML();

            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(xmldata);
            Element root = doc.getRootElement();
            //System.out.println("ROOT ELEMENT: "+ root.getName());
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
    static String CalUri = "/($calendar)?ReadViewEntries";
    static String startDateTimeField = "$144";
    static String endDateTimeField = "$146";
    static String contentField = "$147";
    String MailFileURL, username, password;
    boolean requiresAuth;
}



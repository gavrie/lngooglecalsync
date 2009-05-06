package LotusNotesGoogleCalendarBridge.SSLModule;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CustomSSLHandler implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
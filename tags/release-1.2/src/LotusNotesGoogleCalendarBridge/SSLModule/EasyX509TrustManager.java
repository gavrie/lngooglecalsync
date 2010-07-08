
package LotusNotesGoogleCalendarBridge.SSLModule;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

class EasyX509TrustManager implements X509TrustManager{

    EasyX509TrustManager(Object object) {
   
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        
    }

    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        
    }

    public X509Certificate[] getAcceptedIssuers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

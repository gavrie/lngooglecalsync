package LotusNotesGoogleCalendarBridge.ProxyModule;

public class ProxyConfigBean {

    public ProxyConfigBean() {
        proxyHost = "";
        proxyPort = "";
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        debug("configured proxy");
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        debug ("set proxy host");
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
        debug ("set proxy port");
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void activateNow() {
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", proxyHost);
        System.getProperties().put("proxyPort", proxyPort);
        debug("proxy has been activated");
    }

    public void deactivateNow() {
        System.getProperties().put("proxySet", "false");
        System.getProperties().put("proxyHost", "");
        System.getProperties().put("proxyPort", "");
        debug ("proxy has been deactivated");
    }

    private void debug(String message) {
        if (debug) {
            System.out.println("DEBUG: " + message);
        }
    }
    String proxyHost, proxyPort;
    boolean enabled;
    // enable or disable debug messages concerning the proxy configuration process
    boolean debug = false;
}

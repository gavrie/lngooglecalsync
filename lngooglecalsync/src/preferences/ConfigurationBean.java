package preferences;

import java.util.Properties;

public class ConfigurationBean {

    public static void main(String[] a) {
        ConfigurationBean cb = new ConfigurationBean();
        cb.writeConfig();
    }


    public void writeConfig() {

        Properties config = new Properties();
        config.setProperty("LotusNotesMailURL", getLotusMailFileURL());
        config.setProperty("GoogleUsername", getGoogleUserName());
        config.setProperty("GooglePassword", getGooglePassword());
        config.setProperty("GoogleEnableProxy", new Boolean(getGoogleEnableProxy()).toString());
        config.setProperty("GoogleProxyIP", getGoogleProxyIP());
        config.setProperty("GoogleProxyPort", getGoogleProxyPort());
        config.setProperty("GoogleUploadToMainCalendar", new Boolean(getGoogleUploadToMainCalendar()).toString());
        config.setProperty("GoogleUseSSL", new Boolean(getGoogleUseSSL()).toString());


    }

    public void readConfig() {
    }

    public void setLotusMailFileURL(String lotusMailFileURL) {
        this.lotusMailFileURL = lotusMailFileURL;
    }

    public String getLotusMailFileURL() {
        return lotusMailFileURL;
    }

    public void setGoogleUserName(String googleUserName) {
        this.googleUserName = googleUserName;
    }

    public String getGoogleUserName() {
        return googleUserName;
    }

    public void setGooglePassword(String googlePassword) {
        this.googlePassword = googlePassword;
    }

    public String getGooglePassword() {
        return googlePassword;
    }

    public void setGoogleEnableProxy(boolean googleEnableProxy) {
        this.googleEnableProxy = googleEnableProxy;
    }

    public boolean getGoogleEnableProxy() {
        return googleEnableProxy;
    }

    public void setGoogleProxyIP(String googleProxyIP) {
        this.googleProxyIP = googleProxyIP;
    }

    public String getGoogleProxyIP() {
        return googleProxyIP;
    }

    public void setGoogleProxyPort(String googleProxyPort) {
        this.googleProxyPort = googleProxyPort;
    }

    public String getGoogleProxyPort() {
        return googleProxyPort;
    }

    public void setGoogleUploadToMainCalendar(boolean googleUploadToMainCalendar) {
        this.googleUploadToMainCalendar = googleUploadToMainCalendar;
    }

    public boolean getGoogleUploadToMainCalendar() {
        return googleUploadToMainCalendar;
    }

    public void setGoogleUseSSL(boolean googleUseSSL) {
        this.googleUseSSL = googleUseSSL;
    }

    public boolean getGoogleUseSSL() {
        return googleUseSSL;
    }
    String lotusMailFileURL;
    String googleUserName;
    String googlePassword;
    boolean googleEnableProxy;
    String googleProxyIP;
    String googleProxyPort;
    boolean googleUploadToMainCalendar;
    boolean googleUseSSL;
}

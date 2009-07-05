package preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class ConfigurationBean {

    public ConfigurationBean() {
        config = new Properties();
    }

    public void writeConfig() {

        config.setProperty("GoogleUsername", getGoogleUserName());
        config.setProperty("GooglePassword", getGooglePassword());
        config.setProperty("GoogleEnableProxy", new Boolean(getGoogleEnableProxy()).toString());
        config.setProperty("GoogleProxyIP", getGoogleProxyIP());
        config.setProperty("GoogleProxyPort", getGoogleProxyPort());
        config.setProperty("GoogleUploadToMainCalendar", new Boolean(getGoogleUploadToMainCalendar()).toString());
        config.setProperty("GoogleUseSSL", new Boolean(getGoogleUseSSL()).toString());
        config.setProperty("LotusNotesUsername", getLotusNotesUsername());
        config.setProperty("LotusNotesPassword", getLotusNotesPassword());

        // Write properties file.
        try {
            config.store(new FileOutputStream(configurationFile), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readConfig() {
        try {
            config.load(new FileInputStream(configurationFile));
        } catch (Exception e) {
        }

    }

    public void setLotusMailFileURL(String lotusMailFileURL) {
        config.setProperty("LotusNotesMailURL", lotusMailFileURL);
    }

    public void setGoogleUserName(String googleUserName) {
        config.setProperty("GoogleUsername", googleUserName);
    }

    public void setGooglePassword(String googlePassword) {
        config.setProperty("GooglePassword", googlePassword);
    }

    public void setGoogleProxyIP(String googleProxyIP) {
        config.setProperty("GoogleProxyIP", googleProxyIP);
    }

    public void setGoogleProxyPort(String googleProxyPort) {
        config.setProperty("GoogleProxyPort", googleProxyPort);
    }

    public void setGoogleEnableProxy(boolean googleEnableProxy) {
        if (googleEnableProxy) {
            config.setProperty("GoogleEnableProxy", "true");
        } else {
            config.setProperty("GoogleEnableProxy", "false");
        }
    }

    public void setGoogleUploadToMainCalendar(boolean googleUploadToMainCalendar) {
        if (googleUploadToMainCalendar) {
            config.setProperty("GoogleUploadToMainCalendar", "true");
        } else {
            config.setProperty("GoogleUploadToMainCalendar", "false");
        }
    }

    public void setGoogleUseSSL(boolean googleUseSSL) {
        if (googleUseSSL) {
            config.setProperty("GoogleUseSSL", "true");
        } else {
            config.setProperty("GoogleUseSSL", "false");
        }
    }

    public void setLotusNotesUsername(String LotusNotesUsername) {
        config.setProperty("LotusNotesUsername", LotusNotesUsername);
    }

    public void setLotusNotesPassword(String LotusNotesPassword) {
        config.setProperty("LotusNotesPassword", LotusNotesPassword);
    }

    public String getLotusMailFileURL() {
        String url = config.getProperty("LotusNotesMailURL");
        if (url == null) {
            url = "http://lotus.host/mail/mailfile.nsf";
        }
        return url;
    }

    public String getGoogleUserName() {
        String username = config.getProperty("GoogleUsername");
        if (username == null) {
            username = "user@google.com";
        }
        return username;
    }

    public String getGooglePassword() {
        return config.getProperty("GooglePassword");
    }

    public String getGoogleProxyIP() {
        return config.getProperty("GoogleProxyIP");
    }

    public String getGoogleProxyPort() {
        return config.getProperty("GoogleProxyPort");
    }

    public boolean getGoogleEnableProxy() {
        String property = config.getProperty("GoogleEnableProxy");
        boolean enable = false;
        if (property.equalsIgnoreCase("true")) {
            enable = true;
        }
        return enable;
    }

    public boolean getGoogleUploadToMainCalendar() {
        String property = config.getProperty("GoogleUploadToMainCalendar");
        boolean enable = false;
        if (property.equalsIgnoreCase("true")) {
            enable = true;
        }
        return enable;
    }

    public boolean getGoogleUseSSL() {
        String property = config.getProperty("GoogleUseSSL");
        boolean enable = false;
        if (property.equalsIgnoreCase("true")) {
            enable = true;
        }
        return enable;
    }

    public String getLotusNotesUsername() {
        return config.getProperty("LotusNotesUsername");
    }

    public String getLotusNotesPassword() {
        return config.getProperty("LotusNotesPassword");
    }
    Properties config;
    String configurationFile = "lngooglecalsync.properties";
}

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
        config.setProperty("SyncOnStartup", new Boolean(getSyncOnStartup()).toString());

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

    public void setSyncOnStartup(boolean syncOnStartup) {
        if (syncOnStartup) {
            config.setProperty("SyncOnStartup", "true");
        } else {
            config.setProperty("SyncOnStartup", "false");
        }
    }

    public void setLotusNotesUsername(String LotusNotesUsername) {
        config.setProperty("LotusNotesUsername", LotusNotesUsername);
    }

    public void setLotusNotesPassword(String LotusNotesPassword) {
        config.setProperty("LotusNotesPassword", LotusNotesPassword);
    }

    public String getLotusMailFileURL() {
        String url = null;
        try {
            url = config.getProperty("LotusNotesMailURL");
            if (url == null || url.equals("")) {
                url = "http://lotus.host/mail/mailfile.nsf";
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: LotusNotesMailURL is not set yet!");
        }
        return url;
    }

    public String getGoogleUserName() {
        String property = null;
        try {
            property = config.getProperty("GoogleUsername");
            if (property == null || property.equals("")) {
                property = "user@google.com";
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleUsername is not set yet!");
        }
        return property;
    }

    public String getGooglePassword() {
        String property = null;
        try {
            property = config.getProperty("GooglePassword");
        } catch (Exception e) {
            System.err.println("Configuration Warning: GooglePassword is not set yet!");
        }
        return property;
    }

    public String getGoogleProxyIP() {
        String property = null;
        try {
            property = config.getProperty("GoogleProxyIP");
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleProxyIP is not set yet!");
        }
        return property;
    }

    public String getGoogleProxyPort() {
        String property = null;
        try {
            property = config.getProperty("GoogleProxyPort");
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleProxyPort is not set yet!");
        }
        return property;
    }

    public boolean getGoogleEnableProxy() {
        String property = null;
        boolean enable = false;
        try {
            property = config.getProperty("GoogleEnableProxy");
            if (property.equalsIgnoreCase("true")) {
                enable = true;
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleEnableProxy is not set yet!");
        }
        return enable;
    }

    public boolean getGoogleUploadToMainCalendar() {
        String property = null;
        boolean enable = false;
        try {
            property = config.getProperty("GoogleUploadToMainCalendar");
            if (property.equalsIgnoreCase("true")) {
                enable = true;
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleUploadToMainCalendar is not set yet!");
        }
        return enable;
    }

    public boolean getGoogleUseSSL() {
        boolean enable = false;
        String property = null;
        try {
            property = config.getProperty("GoogleUseSSL");
            if (property.equalsIgnoreCase("true")) {
                enable = true;
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: GoogleUseSSL is not set yet!");
        }
        return enable;
    }

    public String getLotusNotesUsername() {
        String property = null;
        try {
            property = config.getProperty("LotusNotesUsername");
        } catch (Exception e) {
            System.err.println("Configuration Warning: LotusNotesUsername is not set yet!");
        }
        return property;
    }

    public String getLotusNotesPassword() {
        String property = null;
        try {
            property = config.getProperty("LotusNotesPassword");
        } catch (Exception e) {
            System.err.println("Configuration Warning: LotusNotesPassword is not set yet!");
        }
        return property;
    }

    public boolean getSyncOnStartup() {
        String property = null;
        boolean enable = false;

        try {
            property = config.getProperty("SyncOnStartup");
            if (property.equalsIgnoreCase("true")) {
                enable = true;
            }
        } catch (Exception e) {
            System.err.println("Configuration Warning: SyncOnStartup is not set yet!");
        }
        return enable;
    }
    Properties config;
    String configurationFile = "lngooglecalsync.properties";
}

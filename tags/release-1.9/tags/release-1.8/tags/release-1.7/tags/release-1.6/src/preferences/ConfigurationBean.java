package preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import com.google.gdata.util.common.util.Base64;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class ConfigurationBean {

    public ConfigurationBean() {
        // Get the absolute path to this app
        configFullFilename = new java.io.File("").getAbsolutePath() + System.getProperty("file.separator") + configFilename;

        config = new Properties();
    }

    /**
     * Write configuration file.
     * Important: after a writeConfig, the in-memory configuration is not valid anymore until
     * a subsequent readConfig() is performed ...
     * @throws Exception due to I/O
     */
    public void writeConfig() throws Exception {
        // Starting with v2 of the properties file, we "encrypt" passwords but keep them plain in memory
        config.setProperty(PROP_GOOGLE_PASSWORD, encodePassword(getGooglePassword()));
        config.setProperty(PROP_PROXY_PASSWORD, encodePassword(getGoogleProxyPassword()));
        config.setProperty(PROP_LOTUS_NOTES_PASSWORD, encodePassword(getLotusNotesPassword()));

        if (getConfigVersion() < currConfigVersion) {
            config.setProperty(PROP_CONFIG_VERSION, Integer.toString(currConfigVersion));
        }
        else {
            config.setProperty(PROP_CONFIG_VERSION, Integer.toString(getConfigVersion()));
        }

        // Write properties file.
        try {
            config.store(new FileOutputStream(configFilename), null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readConfig() {
        try {
            config.load(new FileInputStream(configFilename));
            if (getConfigVersion() >= 2) {
                // Starting with v2 of the properties file, we "encrypt" passwords but keep them plain in memory
                config.setProperty(PROP_GOOGLE_PASSWORD, decodePassword(getGooglePassword()));
                config.setProperty(PROP_PROXY_PASSWORD, decodePassword(getGoogleProxyPassword()));
                config.setProperty(PROP_LOTUS_NOTES_PASSWORD, decodePassword(getLotusNotesPassword()));

            }

        } catch (Exception e) {
        }

    }

    public void setConfigVersion(int value) {
        config.setProperty(PROP_CONFIG_VERSION, Integer.toString(value));
    }

    public void setLotusNotesServer(String value) {
        config.setProperty(PROP_LOTUS_NOTES_SERVER, value);
    }

    public void setLotusNotesServerIsLocal(boolean value) {
        setBooleanProperty(PROP_LOTUS_NOTES_SERVER_IS_LOCAL, value);
    }

    public void setLotusNotesMailFile(String value) {
        config.setProperty(PROP_LOTUS_NOTES_MAIL_FILE, value);
    }

    public void setGoogleUserName(String googleUserName) {
        config.setProperty("GoogleUsername", googleUserName);
    }

    public void setGooglePassword(String googlePassword) {
        config.setProperty(PROP_GOOGLE_PASSWORD, googlePassword);
    }

    public void setGoogleProxyIP(String googleProxyIP) {
        config.setProperty("GoogleProxyIP", googleProxyIP);
    }

    public void setGoogleProxyPort(String googleProxyPort) {
        config.setProperty("GoogleProxyPort", googleProxyPort);
    }

    public void setGoogleProxyUsername(String value) {
        config.setProperty(PROP_PROXY_USERNAME, value);
    }

    public void setGoogleProxyPassword(String value) {
        config.setProperty(PROP_PROXY_PASSWORD, value);
    }

    public void setGoogleEnableProxy(boolean googleEnableProxy) {
        setBooleanProperty("GoogleEnableProxy", googleEnableProxy);
    }

    public void setGoogleUseSSL(boolean googleUseSSL) {
        setBooleanProperty(PROP_USE_SSL, googleUseSSL);
    }

    public void setSyncOnStartup(boolean value) {
        setBooleanProperty(PROP_SYNC_ON_STARTUP, value);
    }

    public void setDiagnosticMode(boolean value) {
        setBooleanProperty(PROP_DIAGNOSTIC_MODE, value);
    }

    public void setSyncDescription(boolean value) {
        setBooleanProperty(PROP_SYNC_DESCRIPTION, value);
    }

    public void setSyncAlarms(boolean value) {
        setBooleanProperty(PROP_SYNC_ALARMS, value);
    }

    public void setLotusNotesUsername(String LotusNotesUsername) {
        config.setProperty(PROP_LOTUS_NOTES_USERNAME, LotusNotesUsername);
    }

    public void setLotusNotesPassword(String value) {
        config.setProperty(PROP_LOTUS_NOTES_PASSWORD, value);
    }

    protected void setBooleanProperty(String propertyName, boolean propertyValue) {
        String propertyValueStr = "false";
        if (propertyValue) {
            propertyValueStr = "true";
        }

        config.setProperty(propertyName, propertyValueStr);
    }


    public int getConfigVersion() {
        return getIntegerProperty(PROP_CONFIG_VERSION);
    }

    public String getLotusNotesServer() {
        return getStringProperty(PROP_LOTUS_NOTES_SERVER);
    }

    public boolean getLotusNotesServerIsLocal() {
        return getBooleanProperty(PROP_LOTUS_NOTES_SERVER_IS_LOCAL);
    }

    public String getLotusNotesMailFile() {
        return getStringProperty(PROP_LOTUS_NOTES_MAIL_FILE);
    }

    public String getGoogleUserName() {
        return getStringProperty("GoogleUsername");
    }

    public String getGooglePassword() throws Exception {
        return getStringProperty("GooglePassword");
    }

    public String getGoogleProxyIP() {
        return getStringProperty("GoogleProxyIP");
    }

    public String getGoogleProxyPort() {
        return getStringProperty("GoogleProxyPort");
    }

    public String getGoogleProxyUsername() {
        return getStringProperty(PROP_PROXY_USERNAME);
    }

    public String getGoogleProxyPassword() throws Exception {
        return getStringProperty(PROP_PROXY_PASSWORD);
    }

    public boolean getGoogleEnableProxy() {
        return getBooleanProperty("GoogleEnableProxy");
    }

    public boolean getGoogleUseSSL() {
        return getBooleanProperty(PROP_USE_SSL);
    }

    public String getLotusNotesUsername() {
        return getStringProperty(PROP_LOTUS_NOTES_USERNAME);
    }

    public String getLotusNotesPassword() throws Exception {
        return getStringProperty(PROP_LOTUS_NOTES_PASSWORD);
    }

    public boolean getSyncOnStartup() {
        return getBooleanProperty(PROP_SYNC_ON_STARTUP);
    }

    public boolean getDiagnosticMode() {
        return getBooleanProperty(PROP_DIAGNOSTIC_MODE);
    }

    public boolean getSyncDescription() {
        return getBooleanProperty(PROP_SYNC_DESCRIPTION);
    }

    public boolean getSyncAlarms() {
        return getBooleanProperty(PROP_SYNC_ALARMS);
    }

    protected String getStringProperty(String propertyName) {
        String property;

        property = config.getProperty(propertyName);
        if (property == null) {
            property = "";
        }

        return property;
    }

    protected boolean getBooleanProperty(String propertyName) {
        boolean property = true;

        String propertyStr = config.getProperty(propertyName);
        if (propertyStr == null || ! propertyStr.equalsIgnoreCase("true")) {
            property = false;
        }

        return property;
    }

    protected int getIntegerProperty(String propertyName) {
        int property = 0;

        String propertyStr = config.getProperty(propertyName);
        if (propertyStr != null) {
            property = Integer.parseInt(config.getProperty(propertyName));
        }

        return property;
    }

    protected String encodePassword(String password) {
    	return Base64.encode(password.getBytes());
    }

    protected String decodePassword(String encodedPassword ) throws Exception {
        try {
            byte[] data = Base64.decode(encodedPassword);
            return new String(data);
        } catch (Exception ex) {
            throw ex;
        }
    }


    // Version stamp for the config-file format
    protected static final int currConfigVersion = 2;
    protected static final String PROP_CONFIG_VERSION = "ConfigVersion";
    protected static final String PROP_LOTUS_NOTES_SERVER = "LotusNotesServer";
    protected static final String PROP_LOTUS_NOTES_SERVER_IS_LOCAL = "LotusNotesServerIsLocal";
    protected static final String PROP_LOTUS_NOTES_MAIL_FILE = "LotusNotesMailFile";
    protected static final String PROP_LOTUS_NOTES_PASSWORD = "LotusNotesPassword";
    protected static final String PROP_LOTUS_NOTES_USERNAME = "LotusNotesUsername";
    protected static final String PROP_DIAGNOSTIC_MODE = "DiagnosticMode";
    protected static final String PROP_SYNC_ON_STARTUP = "SyncOnStartup";
    protected static final String PROP_SYNC_DESCRIPTION = "SyncDescription";
    protected static final String PROP_SYNC_ALARMS = "SyncAlarms";
    protected static final String PROP_PROXY_USERNAME = "GoogleProxyUsername";
    protected static final String PROP_PROXY_PASSWORD = "GoogleProxyPassword";
    protected static final String PROP_USE_SSL = "GoogleUseSSL";
    protected static final String PROP_GOOGLE_PASSWORD = "GooglePassword";

    protected Properties config;
    protected final String configFilename = "lngooglecalsync.properties";
    // Filename with full path
    protected String configFullFilename;
}

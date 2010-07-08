package LotusNotesGoogleCalendarBridge.ProxyModule;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

class DefaultProxyAuthenticator extends Authenticator {

    public DefaultProxyAuthenticator(String user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password.toCharArray());
    }

    private String user, password;

}

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

public class AdminManager{
    
    String xmppDomainString = "alumchat.xyz";

    public XMPPTCPConnectionConfiguration get_config(String username, String password) throws XmppStringprepException{
        DomainBareJid xmppDomain = JidCreate.domainBareFrom(xmppDomainString);

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username,password)
                .setXmppDomain(xmppDomain)
                .setHost(xmppDomainString)
                .setSecurityMode(SecurityMode.disabled)
                .build();

        return config;
    }

    public String Register(String username, String password) throws XmppStringprepException{
        
        String result = "OK";
        XMPPTCPConnectionConfiguration config = get_config(username, password);

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
 
            AccountManager accManager = AccountManager.getInstance(connection);
            accManager.sensitiveOperationOverInsecureConnection(true);
            accManager.createAccount(Localpart.from(username), password);
            connection.disconnect();

            return result;
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            result = "ERROR";
            return result;
        }
    }

    public AbstractXMPPConnection Login(String username, String password) throws XmppStringprepException{
        XMPPTCPConnectionConfiguration config = get_config(username, password);
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);

        try {
            connection.connect();
            connection.login();
            return connection;

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            return null;
        }
    }

    public AbstractXMPPConnection CloseSession(AbstractXMPPConnection connection){
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
        System.out.println("Cuenta cerrada exitosamente! Vuelve pronto!");
        return null;
    }

    public void DeleteAccount(AbstractXMPPConnection connection){
        AccountManager accManager = AccountManager.getInstance(connection);
        try {
            accManager.deleteAccount();
        } catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
        }
    }

}

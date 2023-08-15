/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

public class AdminManager{
    
    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

    public XMPPTCPConnectionConfiguration get_config(String username, String password) throws XmppStringprepException{
        DomainBareJid xmppDomain = JidCreate.domainBareFrom(xmppDomainString);

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username,password)
                .setXmppDomain(xmppDomain)
                .setHost(xmppDomainString)
                .setSendPresence(true)
                .setSecurityMode(SecurityMode.disabled)
                .build();

        return config;
    }

    public void Register(String username, String password) throws XmppStringprepException{
        
        XMPPTCPConnectionConfiguration config = get_config(username, password);
        AbstractXMPPConnection connection = new XMPPTCPConnection(config);

        try {
            connection.connect();
 
            AccountManager accManager = AccountManager.getInstance(connection);
            accManager.sensitiveOperationOverInsecureConnection(true);
            accManager.createAccount(Localpart.from(username), password);
            connection.login();
            
            Roster roster = Roster.getInstanceFor(connection);
            roster.reload();

            connection.disconnect();
            System.out.println("Cuenta creada exitosamente!!!");

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            System.out.println(e);
            System.out.println("Ha ocurrido un error, intenta m\u00E1s tarde");
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

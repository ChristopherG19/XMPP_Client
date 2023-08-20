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

/**
 * This class handles administrative tasks related to user management.
 */
public class AdminManager{
    
    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

     /**
     * Generates the XMPP TCP connection configuration.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The generated connection configuration.
     * @throws XmppStringprepException If there's an issue with XMPP string preparation.
     */
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

    /**
     * Registers a new user account.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @throws XmppStringprepException If there's an issue with XMPP string preparation.
     */
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
            System.out.println("Account created successfully!!!");

        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            System.out.println(e);
            System.out.println("An error occurred, please try again later.");
        }
    }

    /**
     * Logs in a user.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The connection if login is successful, null otherwise.
     * @throws XmppStringprepException If there's an issue with XMPP string preparation.
     */
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

    /**
     * Closes the user session.
     *
     * @param connection The connection to be closed.
     * @return Null after closing the session.
     */
    public AbstractXMPPConnection CloseSession(AbstractXMPPConnection connection){
        if (connection != null && connection.isConnected()) {
            connection.disconnect();
        }
        System.out.println("Session closed successfully! Come back soon!");
        return null;
    }

    /**
     * Deletes the user account.
     *
     * @param connection The connection for the user.
     */
    public void DeleteAccount(AbstractXMPPConnection connection){
        AccountManager accManager = AccountManager.getInstance(connection);
        try {
            accManager.deleteAccount();
        } catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
        }
    }

}

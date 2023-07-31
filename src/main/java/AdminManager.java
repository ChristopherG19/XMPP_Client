import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

public class AdminManager{
    
    String xmppDomainString = "alumchat.xyz";

    public String Register(String username, String password) throws XmppStringprepException{
        
        DomainBareJid xmppDomain = JidCreate.domainBareFrom(xmppDomainString);
        String result = "OK";

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword(username,password)
                .setXmppDomain(xmppDomain)
                .setHost(xmppDomainString)
                .setSecurityMode(SecurityMode.disabled)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            connection.connect();

            System.out.println(username);
            System.out.println(password);
            
            AccountManager accManager = AccountManager.getInstance(connection);
            accManager.sensitiveOperationOverInsecureConnection(true);
            accManager.createAccount(Localpart.from(username), password);

            System.out.println("Cuenta creada exitosamente");
            connection.disconnect();
            return result;
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            e.printStackTrace();
            result = "ERROR";
            return result;
        }
    }

    public void Login(){

    }

    public void CloseSession(){

    }

    public void DeleteAccount(){

    }

}

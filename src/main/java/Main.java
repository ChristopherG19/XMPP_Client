import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws XmppStringprepException {

        XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                .setUsernameAndPassword("garcia20541redes","Christopher1910")
                .setXmppDomain("alumchat.xyz")
                .setHost("alumchat.xyz")
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        AbstractXMPPConnection connection = new XMPPTCPConnection(config);
        try {
            connection.connect();
            connection.login();
            
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            EntityBareJid jid = JidCreate.entityBareFrom("prueba20541test@alumchat.xyz");
            Chat chat = chatManager.chatWith(jid);

            chat.send("Hello! Prueba");

            System.out.println("Mensaje enviado exitosamente");
            connection.disconnect();
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            // Maneja las excepciones aquí
            e.printStackTrace();
        }
    }
}

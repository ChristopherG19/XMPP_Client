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

        terminal Terminal = new terminal();

        int adminOption = 0;
        int userOption = 0;
        while (adminOption != 5){
            adminOption = Terminal.adminMenu();
        
            switch (adminOption) {
                case 1:
                    System.out.println("Opci\u00F3n 1");
                    break;

                case 2:
                    System.out.println("Opci\u00F3n 2");
                    break;

                case 3:
                    System.out.println("Opci\u00F3n 3");
                    break;

                case 4:
                    System.out.println("Opci\u00F3n 4");
                    break;

                case 5:
                    System.out.println("Opci\u00F3n 5");
                    break;
            
                default:
                    System.out.println("Opci\u00F3n invalida");;
            }
        
        }

        // Imprimir despedida

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
            
            // ChatManager chatManager = ChatManager.getInstanceFor(connection);
            // EntityBareJid jid = JidCreate.entityBareFrom("prueba20541test@alumchat.xyz");
            // Chat chat = chatManager.chatWith(jid);

            // chat.send("Hello! Prueba");

            System.out.println("Mensaje enviado exitosamente");
            connection.disconnect();
        } catch (SmackException | IOException | XMPPException | InterruptedException e) {
            // Maneja las excepciones aquí
            e.printStackTrace();
        }
    }
}

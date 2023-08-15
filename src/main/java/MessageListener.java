import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class MessageListener implements IncomingChatMessageListener{
    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        System.out.println("\nReceived message from " + from + ": " + message.getBody());
    }
}

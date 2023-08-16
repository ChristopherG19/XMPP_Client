import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class MessageListener implements IncomingChatMessageListener, OutgoingChatMessageListener{
    private AtomicBoolean chatActive;

    public MessageListener(AtomicBoolean chatActive) {
        this.chatActive = chatActive;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        if (chatActive.get()) {
            System.out.println("\n(" + from + "): " + message.getBody());
        }
    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
        if (chatActive.get()) {
            System.out.println("\n(You): " + message.getBody());
        }
    }
}


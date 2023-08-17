/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class MessagesListener implements IncomingChatMessageListener, OutgoingChatMessageListener, MessageListener{
    private AtomicBoolean chatActive;

    public MessagesListener(AtomicBoolean chatActive) {
        this.chatActive = chatActive;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        if (chatActive.get()) {
            System.out.println("\n>> (" + from + "): " + message.getBody() + "\n");
        }
    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
        if (chatActive.get()) {
            System.out.println("\n>> (You): " + message.getBody() + "\n");
        }
    }

    @Override
    public void processMessage(Message message) {
        if (chatActive.get()) {
            String sender = message.getFrom().getResourceOrEmpty().toString();
            String body = message.getBody();
            System.out.println("\n>> (" + sender + "): " + body + "\n");
        }
    }
}


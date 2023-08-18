/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;

public class NotificationsListener extends Thread {

    private AbstractXMPPConnection connection;
    private boolean isRunning;
    private UserManager userManager;

    public NotificationsListener(AbstractXMPPConnection connection, UserManager userManager) {
        this.connection = connection;
        this.isRunning = true;
        this.userManager = userManager;
    }

    public void stopRunning() {
        this.isRunning = false;
    }

    public void run() {
        Roster roster = Roster.getInstanceFor(connection);
        roster.setSubscriptionMode(Roster.SubscriptionMode.accept_all);
        roster.addSubscribeListener((from, subscribeRequest) -> {
            NotificationP notification = new NotificationP(from.toString(), subscribeRequest.getType().toString(), null);
            userManager.addPendingNotification(notification);
            Presence subscribedPresence = new Presence(Presence.Type.subscribed);
            subscribedPresence.setTo(from);
            try {
                connection.sendStanza(subscribedPresence);
            } catch (NotConnectedException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });

        try {
            // Filtro para capturar todas las notificaciones
            StanzaListener notificationListener = new StanzaListener() {
                @Override
                public void processStanza(Stanza stanza) {
                    if (stanza instanceof Presence) {
                        Presence presence = (Presence) stanza;
                        boolean ver = connection.getUser().asBareJid().toString().equals(presence.getFrom().asBareJid().toString());
                        if(!ver){
                            NotificationP notification = new NotificationP(presence.getFrom().asBareJid().toString(), presence.getType().toString(), presence.getMode().toString());
                            userManager.addPendingNotification(notification);
                        }
                    } else if (stanza instanceof Message) {
                        Message message = (Message) stanza;
                        NotificationP notification = new NotificationP(message.getFrom().asBareJid().toString(), message.getBody(), null);
                        userManager.addPendingNotification(notification);
                    }
                }
            };



            // Agregar el filtro y el oyente al connection
            connection.addAsyncStanzaListener(notificationListener, null);

            // Keep the thread alive to continue listening for notifications
            while (isRunning) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }  finally {
            // Disconnect the connection when the thread is finished
            if (connection != null && connection.isConnected()) {
                connection.disconnect();
            }
        }
    }
}


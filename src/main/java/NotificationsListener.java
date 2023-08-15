import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

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
        try {
            // Filtro para capturar todas las notificaciones
            StanzaFilter notificationFilter = new StanzaFilter() {
                @Override
                public boolean accept(Stanza stanza) {
                    return stanza instanceof Presence || stanza instanceof Message;
                }
            };
            StanzaListener notificationListener = new StanzaListener() {
                @Override
                public void processStanza(Stanza stanza) {
                    if (stanza instanceof Presence) {
                        Presence presence = (Presence) stanza;
                        NotificationP notification = new NotificationP(presence.getFrom().toString(), presence.getStatus());
                        userManager.addPendingNotification(notification);
                    } else if (stanza instanceof Message) {
                        Message message = (Message) stanza;
                        NotificationP notification = new NotificationP(message.getFrom().toString(), message.getBody());
                        userManager.addPendingNotification(notification);
                    }
                }
            };

            // Agregar el filtro y el oyente al connection
            connection.addAsyncStanzaListener(notificationListener, notificationFilter);

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


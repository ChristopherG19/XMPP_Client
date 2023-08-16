import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.StanzaFilter;
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
        roster.addSubscribeListener((from, suscribeRequest) -> {
            NotificationP notification = new NotificationP(from.toString(), suscribeRequest.getType().toString());
            userManager.addPendingNotification(notification);
            return null;
        });
        
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
                    System.out.print("");
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


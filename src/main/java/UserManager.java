/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/
import java.util.Collection;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

public class UserManager {

    public UserManager(){ }

    public void getAllContacts(AbstractXMPPConnection connection) {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            String userStatus = (roster.getPresence(entry.getJid()).isAvailable()) ? "Disponible" : "No disponible";
            System.out.println("Usuario: "+entry.getJid()+", Estado: "+userStatus);
        }
    }
}

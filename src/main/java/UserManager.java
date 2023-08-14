/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/
import java.util.Collection;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class UserManager {

    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

    public UserManager(){ }

    public void getAllContacts(AbstractXMPPConnection connection) {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
 
            String userStatus;
            if (!presence.isAvailable()) {
                userStatus = "Desconectado";
            } else if (presence.getType() == Presence.Type.available) {
                if (presence.getMode() == Presence.Mode.available) {
                    userStatus = "Disponible";
                } else if (presence.getMode() == Presence.Mode.away) {
                    userStatus = "Ausente";
                } else if (presence.getMode() == Presence.Mode.dnd) {
                    userStatus = "Ocupado";
                } else {
                    userStatus = "No disponible";
                }
            } else {
                userStatus = "No disponible";
            }
 
            System.out.println("Usuario: " + entry.getJid() + ", Estado: " + userStatus);
        }
    }

    public void addContact(AbstractXMPPConnection connection, String userJID, String name) {
        Roster roster = Roster.getInstanceFor(connection);
        try {
            roster.createEntry(JidCreate.entityBareFrom(userJID), name, null);
            System.out.println("Invitacion de contacto enviada correctamente\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserDetails(AbstractXMPPConnection connection, String userJID) throws XmppStringprepException, NotLoggedInException, NotConnectedException, InterruptedException {
        String userStatus;
        Roster roster = Roster.getInstanceFor(connection);
        RosterEntry entry = roster.getEntry(JidCreate.entityBareFrom(userJID));
        if(entry != null){
            Presence presence = roster.getPresence(entry.getJid());
            if (!presence.isAvailable()) {
                userStatus = "No disponible";
            } else if (presence.getType() == Presence.Type.available) {
                if (presence.getMode() == Presence.Mode.available) {
                    userStatus = "Disponible";
                } else if (presence.getMode() == Presence.Mode.away) {
                    userStatus = "Ausente";
                } else if (presence.getMode() == Presence.Mode.dnd) {
                    userStatus = "Ocupado";
                } else {
                    userStatus = "No disponible";
                }
            } else {
                userStatus = "Desconectado";
            }

            String seeMy = entry.canSeeMyPresence() ? "Si" : "No";
            String seeHis = entry.canSeeHisPresence() ? "Si" : "No";
            String status = (presence.getStatus() == null) ? "Sin estado" : presence.getStatus();
    
            return "Informaci\u00F3n del usuario: " + entry.getJid().toString() + "\nEstado: " +  userStatus + "\nUsername: " + entry.getName() + "\nStatus: " + status +"\nPuede ver mi presencia: " + seeMy + "\nPuedo ver su presencia: " + seeHis;
        } else {
            return "Informaci\u00F3n del usuario: " + connection.getUser().asBareJid().toString();
        }
    }

    public void manageChat(AbstractXMPPConnection connection){

    }

    public void updateUserStatus(AbstractXMPPConnection connection, String status) {
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(status);
        try {
            connection.sendStanza(presence);    
            System.out.println("Mensaje de presencia actualizado correctamente!");

        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}

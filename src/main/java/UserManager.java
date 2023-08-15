/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

public class UserManager {

    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

    public UserManager(){ }

    public void getAllContacts(AbstractXMPPConnection connection) {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();
        
        // Impresión bonita gracias a chatGPT
        System.out.println("\n+-----------------+--------------------------+-----------------+-----------------------+");
        System.out.println("|                                 Lista de contactos                                   |");
        System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");
        System.out.printf("| %-15s | %-24s | %-15s | %-21s |\n", "Nombre", "JID", "Estado", "Status");
        System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");
    
        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
            String userStatus = get_presence_type(presence);
            String status = (presence.getStatus() == null) ? "Sin estado" : presence.getStatus();
    
            System.out.printf("| %-15s | %-24s | %-15s | %-21s |\n", entry.getName(), entry.getJid(), userStatus, status);
            System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");
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
    
        if (entry != null) {
            Presence presence = roster.getPresence(entry.getJid());
            userStatus = get_presence_type(presence);
    
            String seeMy = entry.canSeeMyPresence() ? "Si" : "No";
            String seeHis = entry.canSeeHisPresence() ? "Si" : "No";
            String status = (presence.getStatus() == null) ? "Sin estado" : presence.getStatus();
    
            System.out.println("\n\t\t\tInformación del usuario \n");

            String userDetails = "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", "JID", "Username", "Estado");
            userDetails += "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", entry.getJid(), entry.getName(), userStatus);
            userDetails += "+--------------------------+------------------------+-----------------+\n";
            
            userDetails += "\n+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-20s | %-15s |\n", "Puede ver mi presencia", "Puede ver su presencia", "Status");
            userDetails += "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", seeMy, seeHis, status);
            userDetails += "+--------------------------+------------------------+-----------------+\n";
    
            return userDetails;
        } else {
            String ownJID = connection.getUser().asBareJid().toString();
            String ownDetails = "Información del usuario: " + ownJID;
            return ownDetails;
        }
    }
    
    public void manageChat(AbstractXMPPConnection connection) throws XMPPException{
        ChatManager chatManager = ChatManager.getInstanceFor(connection);

        int res = Terminal.get_type_chat();
        switch (res) {
            case 1:
                System.out.println("Estos contactos están disponibles...");
                Map<Integer, RosterEntry> availableContacts = getAvailableContacts(connection);
                for (Map.Entry<Integer, RosterEntry> entry : availableContacts.entrySet()) {
                    Roster roster = Roster.getInstanceFor(connection);
                    RosterEntry rosterEntry = entry.getValue();
                    Presence presence = roster.getPresence(rosterEntry.getJid());
                    String presenceStatus = get_presence_type(presence);

                    System.out.println(entry.getKey() + ") " + " - Usuario: " + rosterEntry.getJid() + " | Estado: " + presenceStatus);
                }

                int us = Terminal.get_user_chat();
                RosterEntry selectedUser = getContactAtPosition(availableContacts, us);

                startChatWithContact(chatManager, selectedUser);
                break;
       
            case 2:
                

            default:
                System.out.println("Ingresa una opción válida!");
                break;
        }


    }

    private void startChatWithContact(ChatManager chatManager, RosterEntry contact) throws XMPPException {
        String contactJID = contact.getJid().toString();
        Chat chat = chatManager.chatWith(contact.getJid().asEntityBareJidIfPossible());
        chatManager.addIncomingListener(new MyMessageListener());

        System.out.println("Chat con " + contactJID + " inició. Escribe 'exit' para terminar la conversación.");

        try {
            String input;
            do {
                input = Terminal.get_message_to_Send();
                chat.send(input);
            } while (!input.equalsIgnoreCase("exit"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Chat con " + contactJID + " finalizado.");
    }

    private Map<Integer, RosterEntry> getAvailableContacts(AbstractXMPPConnection connection) {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();

        Map<Integer, RosterEntry> availableContacts = new HashMap<>();
        int contactCounter = 1;

        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());

            if (presence.isAvailable()) {
                availableContacts.put(contactCounter, entry);
                contactCounter++;
            }
        }

        return availableContacts;
    }

    private RosterEntry getContactAtPosition(Map<Integer, RosterEntry> availableContacts, int position) {
        return availableContacts.get(position);
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

    public String get_presence_type(Presence presence){
        String userStatus = "";
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
        return userStatus;
    }
}

class MyMessageListener implements IncomingChatMessageListener {
    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        String incomingMessage = message.getBody();
        System.out.println(from.asEntityBareJidString() + " says: " + incomingMessage);
    }
}
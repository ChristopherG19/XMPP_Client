/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.packet.DataForm;
import org.jivesoftware.smackx.muc.MultiUserChatException.MissingMucCreationAcknowledgeException;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucAlreadyJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucConfigurationNotSupportedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;

public class UserManager {

    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

    private List<NotificationP> pendingNotifications;

    public UserManager() {
        pendingNotifications = new ArrayList<>();
    }

    public void addPendingNotification(NotificationP notification) {
        pendingNotifications.add(notification);
    }

    public List<NotificationP> getPendingNotifications() {
        return pendingNotifications;
    }

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
            String name = (entry.getName() == null) ? entry.getJid().toString().substring(0, entry.getJid().toString().indexOf("@")) : entry.getName();

            System.out.printf("| %-15s | %-24s | %-15s | %-21s |\n", name, entry.getJid(), userStatus, status);
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

    public void manageChat(AbstractXMPPConnection connection) throws XMPPException, XmppStringprepException{
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Roster roster = Roster.getInstanceFor(connection);

        int res = Terminal.get_type_chat();
        switch (res) {
            case 1:
                System.out.println("\nEstos contactos están disponibles...\n");
                Map<Integer, RosterEntry> availableContacts = getAvailableContacts(connection);
                if(availableContacts.size() > 0){
                    for (Map.Entry<Integer, RosterEntry> entry : availableContacts.entrySet()) {
                        RosterEntry rosterEntry = entry.getValue();
                        Presence presence = roster.getPresence(rosterEntry.getJid());
                        String presenceStatus = get_presence_type(presence);

                        System.out.println(entry.getKey() + ") " + " Usuario: " + rosterEntry.getJid() + " | Estado: " + presenceStatus);
                    }
                    int us = Terminal.get_user_chat();
                    if(us != 0){
                        RosterEntry selectedUser = getContactAtPosition(availableContacts, us);
                        String userJIDE = selectedUser.getJid().asBareJid().toString();
                        startChatWithContact(chatManager, userJIDE);
                    } else {
                        System.out.println("Cancelando operación...");
                        break;
                    }
                } else {
                    System.out.println("No hay contactos disponibles");
                    break;
                }
                break;

            case 2:
                String userN = Terminal.get_contact_info();
                if(!(userN == null)){
                    String userJID = userN + "@alumchat.xyz";
                    startChatWithContact(chatManager, userJID);
                    break;
                } else {
                    System.out.println("Cancelando operación...");
                    break;
                }
            default:
                System.out.println("Ingresa una opción válida!");
                break;
        }


    }

    private void startChatWithContact(ChatManager chatManager, String contact) throws XMPPException, XmppStringprepException {
        Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(contact));
        AtomicBoolean chatActive = new AtomicBoolean(true);
        chatManager.addIncomingListener(new MessagesListener(chatActive));
        chatManager.addOutgoingListener(new MessagesListener(chatActive));

        System.out.println("Chat con " + contact + " inició. Escribe 'exit' para terminar la conversación.");

        try {
            String input;
            do {
                input = Terminal.get_message_to_Send();
                if (!input.equalsIgnoreCase("exit")){
                    chat.send(input);
                }
            } while (!input.equalsIgnoreCase("exit"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chatActive.set(false);
        }

        System.out.println("\nChat con " + contact + " finalizado.");
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

    public void manageGroupChat(AbstractXMPPConnection connection) throws XmppStringprepException, NotAMucServiceException, NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException, MucConfigurationNotSupportedException, MucAlreadyJoinedException, MissingMucCreationAcknowledgeException{
        int type_gc = Terminal.get_type_groupChat();
        AtomicBoolean chatActive = new AtomicBoolean(true);
        switch (type_gc) {
            case 0:
                System.out.println("Cancelando operación...");
                break;
            case 1:
                // Join
                System.out.println("Necesito que ingreses un par de datos");
                String props = Terminal.get_GC_join_props();
                String[] parts = props.split("\\$");

                String groupName = parts[0];
                String username = parts[1];

                String JIDGroup = groupName + "@conference.alumchat.xyz";

                MultiUserChatManager groups = MultiUserChatManager.getInstanceFor(connection);
                MultiUserChat muc = groups.getMultiUserChat(JidCreate.entityBareFrom(JIDGroup));
                muc.join(Resourcepart.from(username));

                muc.addMessageListener(new MessagesListener(chatActive));
                muc.addParticipantStatusListener(new ParticipantsListener());
                muc.addUserStatusListener(new ParticipantsListener());

                try {
                    String input;
                    do {
                        input = Terminal.get_message_to_Send();
                        if (!input.equalsIgnoreCase("exit")){
                            muc.sendMessage(input);
                        }
                    } while (!input.equalsIgnoreCase("exit"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    chatActive.set(false);
                }

                break;

            case 2:
                // Create
                System.out.println("Necesito que ingreses un par de datos");
                String propsC = Terminal.get_GC_join_props();
                String[] partsC = propsC.split("\\$");

                String groupNameC = partsC[0];
                String usernameC = partsC[1];
                String JIDGroupC = groupNameC + "@conference.alumchat.xyz";
                MultiUserChatManager groupsC = MultiUserChatManager.getInstanceFor(connection);
                MultiUserChat mucC = groupsC.getMultiUserChat(JidCreate.entityBareFrom(JIDGroupC));
                mucC.create(Resourcepart.from(groupNameC)).makeInstant();
                mucC.sendConfigurationForm(new Form(DataForm.Type.submit));

                // To set owners
                // Set<Jid> owners = JidUtil.jidSetFrom(new String[] { connection.getUser().asBareJid().toString() });
                // mucC.create(Resourcepart.from("room_name"))
                //     .getConfigFormManager()
                //     .setRoomOwners(owners)
                //     .submitConfigurationForm();

                mucC.join(Resourcepart.from(usernameC));

                mucC.addMessageListener(new MessagesListener(chatActive));
                mucC.addParticipantStatusListener(new ParticipantsListener());
                mucC.addUserStatusListener(new ParticipantsListener());

                try {
                    String input;
                    do {
                        input = Terminal.get_message_to_Send();
                        if (!input.equalsIgnoreCase("exit")){
                            mucC.sendMessage(input);
                        }
                    } while (!input.equalsIgnoreCase("exit"));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    chatActive.set(false);
                }

                break;

            default:
                System.out.println("Ingresa una opción válida!");
                break;
        }
    }

    public void sendFile(AbstractXMPPConnection connection) throws XmppStringprepException, MucAlreadyJoinedException, MissingMucCreationAcknowledgeException, NotAMucServiceException, NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException{
        String props = Terminal.get_file_props();
        String[] parts = props.split("\\$");
        AtomicBoolean chatActive = new AtomicBoolean(true);

        if (parts.length < 2) {
            System.out.println("Formato de entrada incorrecto.");
            return;
        }

        if (parts.length == 2) {
            // Envío a usuario
            String user = parts[0];
            String filePath = parts[1];
            String extension = "";
            int lastIndex = filePath.lastIndexOf(".");
        
            if (lastIndex != -1 && lastIndex < filePath.length() - 1) {
                extension = filePath.substring(lastIndex + 1);
                System.out.println("La extensión del archivo es: " + extension);
            } else {
                System.out.println("El archivo no tiene una extensión válida.");
            }

            String recipientJID = user + "@alumchat.xyz";

            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(recipientJID));

            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);

                Message message = new Message();
                message.setSubject(extension);
                message.setBody("FILE:" + base64Encoded);
                chat.send(message);

                System.out.println("Archivo enviado correctamente.");
            } catch (IOException | SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error al enviar el archivo.");
            } finally {
                chatActive.set(false);
            }

        } else if (parts.length == 3) {
            // Envío a grupo
            String group = parts[0];
            String nickname = parts[1];
            String filePath = parts[2];
            String JIDGroupC = group + "@conference.alumchat.xyz";

           MultiUserChatManager groups = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat mucC = groups.getMultiUserChat(JidCreate.entityBareFrom(JIDGroupC));
            mucC.join(Resourcepart.from(nickname));

            mucC.addMessageListener(new MessagesListener(chatActive));
            mucC.addParticipantStatusListener(new ParticipantsListener());
            mucC.addUserStatusListener(new ParticipantsListener());

            try {
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
    
                Message message = new Message();
                message.setBody("FILE:" + base64Encoded);
                mucC.sendMessage(message);
    
                System.out.println("Archivo enviado correctamente.");
            } catch (IOException | SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error al enviar el archivo.");
            }

        } else {
            System.out.println("Formato de entrada incorrecto.");
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

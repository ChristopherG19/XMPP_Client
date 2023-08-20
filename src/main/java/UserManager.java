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

/**
 * This class manages user interactions and operations.
 */
public class UserManager {

    Terminal Terminal = new Terminal();
    String xmppDomainString = "alumchat.xyz";

    private List<NotificationP> pendingNotifications;

    /**
     * Constructor for UserManager class.
     */
    public UserManager() {
        pendingNotifications = new ArrayList<>();
    }

    /**
     * Adds a pending notification to the list.
     *
     * @param notification NotificationP: The notification to be added
     */
    public void addPendingNotification(NotificationP notification) {
        pendingNotifications.add(notification);
    }

    /**
     * Retrieves the list of pending notifications.
     *
     * @return List<NotificationP>: List of pending notifications
     */
    public List<NotificationP> getPendingNotifications() {
        return pendingNotifications;
    }

    /**
     * Retrieves and prints details of all user contacts.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     */
    public void getAllContacts(AbstractXMPPConnection connection) {
        Roster roster = Roster.getInstanceFor(connection);
        Collection<RosterEntry> entries = roster.getEntries();

        // Impresión bonita gracias a chatGPT
        System.out.println("\n+-----------------+--------------------------+-----------------+-----------------------+");
        System.out.println("|                                    Contact List                                      |");
        System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");
        System.out.printf("| %-15s | %-24s | %-15s | %-21s |\n", "Name", "JID", "Status", "Status message");
        System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");

        for (RosterEntry entry : entries) {
            Presence presence = roster.getPresence(entry.getJid());
            String userStatus = get_presence_type(presence);
            String status = (presence.getStatus() == null) ? "No status" : presence.getStatus();
            String name = (entry.getName() == null) ? entry.getJid().toString().substring(0, entry.getJid().toString().indexOf("@")) : entry.getName();

            System.out.printf("| %-15s | %-24s | %-15s | %-21s |\n", name, entry.getJid(), userStatus, status);
            System.out.println("+-----------------+--------------------------+-----------------+-----------------------+");
        }
    }

    /**
     * Adds a contact to the user's roster.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @param userJID    String: The JID of the user to add
     * @param name       String: The name of the user to add
     */
    public void addContact(AbstractXMPPConnection connection, String userJID, String name) {
        Roster roster = Roster.getInstanceFor(connection);
        try {
            roster.createEntry(JidCreate.entityBareFrom(userJID), name, null);
            System.out.println("Contact invitation sent successfully.\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves and displays details of a user.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @param userJID    String: The JID of the user to retrieve details for
     * @return String: The user details as a formatted string
     * @throws XmppStringprepException      If there's an issue with XMPP stringprep
     * @throws NotLoggedInException        If the user is not logged in
     * @throws NotConnectedException       If the user is not connected
     * @throws InterruptedException       If the operation is interrupted
     */
    public String getUserDetails(AbstractXMPPConnection connection, String userJID) throws XmppStringprepException, NotLoggedInException, NotConnectedException, InterruptedException {
        String userStatus;
        Roster roster = Roster.getInstanceFor(connection);
        RosterEntry entry = roster.getEntry(JidCreate.entityBareFrom(userJID));

        if (entry != null) {
            Presence presence = roster.getPresence(entry.getJid());
            userStatus = get_presence_type(presence);

            String seeMy = entry.canSeeMyPresence() ? "Yes" : "No";
            String seeHis = entry.canSeeHisPresence() ? "Yes" : "No";
            String status = (presence.getStatus() == null) ? "No status" : presence.getStatus();

            System.out.println("\n\t\t\tUser Information \n");

            String userDetails = "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", "JID", "Username", "Status");
            userDetails += "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", entry.getJid(), entry.getName(), userStatus);
            userDetails += "+--------------------------+------------------------+-----------------+\n";

            userDetails += "\n+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-20s | %-15s |\n", "Can See My Presence", "Can See His Presence", "Status");
            userDetails += "+--------------------------+------------------------+-----------------+\n";
            userDetails += String.format("| %-24s | %-22s | %-15s |\n", seeMy, seeHis, status);
            userDetails += "+--------------------------+------------------------+-----------------+\n";

            return userDetails;
        } else {
            String ownJID = connection.getUser().asBareJid().toString();
            String ownDetails = "User Information: " + ownJID;
            return ownDetails;
        }
    }

    /**
     * Manages the user's chat interactions.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @throws XMPPException              If there's an XMPP exception
     * @throws XmppStringprepException    If there's an issue with XMPP stringprep
     */
    public void manageChat(AbstractXMPPConnection connection) throws XMPPException, XmppStringprepException{
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        Roster roster = Roster.getInstanceFor(connection);

        int res = Terminal.get_type_chat();
        switch (res) {
            case 1:
                System.out.println("\nThese contacts are available...\n");
                Map<Integer, RosterEntry> availableContacts = getAvailableContacts(connection);
                if(availableContacts.size() > 0){
                    for (Map.Entry<Integer, RosterEntry> entry : availableContacts.entrySet()) {
                        RosterEntry rosterEntry = entry.getValue();
                        Presence presence = roster.getPresence(rosterEntry.getJid());
                        String presenceStatus = get_presence_type(presence);

                        System.out.println(entry.getKey() + ") " + " User: " + rosterEntry.getJid() + " | Status: " + presenceStatus);
                    }
                    int us = Terminal.get_user_chat();
                    if(us != 0){
                        RosterEntry selectedUser = getContactAtPosition(availableContacts, us);
                        String userJIDE = selectedUser.getJid().asBareJid().toString();
                        startChatWithContact(chatManager, userJIDE);
                    } else {
                        System.out.println("Cancelling operation...");
                        break;
                    }
                } else {
                    System.out.println("No available contacts");
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
                    System.out.println("Cancelling operation...");
                    break;
                }
            default:
                System.out.println("Enter a valid option!");
                break;
        }
    }

    /**
     * Starts a chat with the specified contact.
     *
     * @param chatManager ChatManager: The chat manager
     * @param contact     String: The contact's JID
     * @throws XMPPException            If there's an XMPP exception
     * @throws XmppStringprepException  If there's an issue with XMPP stringprep
     */
    private void startChatWithContact(ChatManager chatManager, String contact) throws XMPPException, XmppStringprepException {
        Chat chat = chatManager.chatWith(JidCreate.entityBareFrom(contact));
        AtomicBoolean chatActive = new AtomicBoolean(true);
        chatManager.addIncomingListener(new MessagesListener(chatActive));
        chatManager.addOutgoingListener(new MessagesListener(chatActive));

        System.out.println("Chat with " + contact + " initiated. Type 'exit' to end the conversation.");

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

        System.out.println("\nChat with " + contact + " ended.");
    }

    /**
     * Retrieves a map of available contacts.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @return Map<Integer, RosterEntry>: Map of available contacts
     */
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

    /**
     * Retrieves a contact from the available contacts map at a given position.
     *
     * @param availableContacts Map<Integer, RosterEntry>: Map of available contacts
     * @param position          int: Position of the contact
     * @return RosterEntry: The selected contact
     */
    private RosterEntry getContactAtPosition(Map<Integer, RosterEntry> availableContacts, int position) {
        return availableContacts.get(position);
    }

    /**
     * Updates the user's presence status.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @param status     String: The new status
     */
    public void updateUserStatus(AbstractXMPPConnection connection, String status) {
        Presence presence = new Presence(Presence.Type.available);
        presence.setStatus(status);
        try {
            connection.sendStanza(presence);
            System.out.println("Presence message updated successfully!");

        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Manages the group chat functionality.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @throws XmppStringprepException           If there's an XMPP stringprep exception
     * @throws NotAMucServiceException           If the service is not a Multi-User Chat service
     * @throws NoResponseException               If there's no response
     * @throws XMPPErrorException               If there's an XMPP error
     * @throws NotConnectedException             If not connected
     * @throws InterruptedException             If the thread is interrupted
     * @throws MucConfigurationNotSupportedException  If MUC configuration is not supported
     * @throws MucAlreadyJoinedException         If already joined the MUC
     * @throws MissingMucCreationAcknowledgeException  If the MUC creation acknowledgement is missing
     */
    public void manageGroupChat(AbstractXMPPConnection connection) throws XmppStringprepException, NotAMucServiceException, NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException, MucConfigurationNotSupportedException, MucAlreadyJoinedException, MissingMucCreationAcknowledgeException{
        int type_gc = Terminal.get_type_groupChat();
        AtomicBoolean chatActive = new AtomicBoolean(true);
        switch (type_gc) {
            case 0:
                System.out.println("Cancelling operation...");
                break;
            case 1:
                // Join
                System.out.println("Please provide the necessary data");
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
                System.out.println("Please provide the necessary data");
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
                System.out.println("Please enter a valid option!");
                break;
        }
    }

    /**
     * Sends a file through the XMPP connection.
     *
     * @param connection AbstractXMPPConnection: The XMPP connection
     * @throws XmppStringprepException           If there's an XMPP stringprep exception
     * @throws MucAlreadyJoinedException        If already joined the MUC
     * @throws MissingMucCreationAcknowledgeException  If the MUC creation acknowledgement is missing
     * @throws NotAMucServiceException           If the service is not a Multi-User Chat service
     * @throws NoResponseException               If there's no response
     * @throws XMPPErrorException               If there's an XMPP error
     * @throws NotConnectedException             If not connected
     * @throws InterruptedException             If the thread is interrupted
     */
    public void sendFile(AbstractXMPPConnection connection) throws XmppStringprepException, MucAlreadyJoinedException, MissingMucCreationAcknowledgeException, NotAMucServiceException, NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException{
        String props = Terminal.get_file_props();
        String[] parts = props.split("\\$");
        AtomicBoolean chatActive = new AtomicBoolean(true);

        if (parts.length < 2) {
            System.out.println("Incorrect input format.");
            return;
        }

        if (parts.length == 2) {
            // Sending to a user
            String user = parts[0];
            String filePath = parts[1];
            String extension = "";
            int lastIndex = filePath.lastIndexOf(".");
        
            if (lastIndex != -1 && lastIndex < filePath.length() - 1) {
                extension = filePath.substring(lastIndex + 1);
                System.out.println("File extension: " + extension);
            } else {
                System.out.println("The file does not have a valid extension.");
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

                System.out.println("File sent successfully.");
            } catch (IOException | SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error sending the file.");
            } finally {
                chatActive.set(false);
            }

        } else if (parts.length == 3) {
            // Sending to a group
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
    
                System.out.println("File sent successfully.");
            } catch (IOException | SmackException.NotConnectedException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error sending the file.");
            }

        } else {
            System.out.println("Incorrect input format.");
        }
    }

    /**
     * Retrieves the presence type of a user.
     *
     * @param presence Presence: The presence object
     * @return String: The presence type
     */
    public String get_presence_type(Presence presence){
        String userStatus = "";
        if (!presence.isAvailable()) {
            userStatus = "Not available";
        } else if (presence.getType() == Presence.Type.available) {
            if (presence.getMode() == Presence.Mode.available) {
                userStatus = "Available";
            } else if (presence.getMode() == Presence.Mode.away) {
                userStatus = "Away";
            } else if (presence.getMode() == Presence.Mode.dnd) {
                userStatus = "Busy";
            } else {
                userStatus = "Not available";
            }
        } else {
            userStatus = "Disconnected";
        }
        return userStatus;
    }
}

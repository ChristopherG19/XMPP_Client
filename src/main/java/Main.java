/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smackx.muc.MultiUserChatException.MissingMucCreationAcknowledgeException;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucAlreadyJoinedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.MucConfigurationNotSupportedException;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jxmpp.stringprep.XmppStringprepException;
import java.util.List;

// Principal Reference: https://www.baeldung.com/xmpp-smack-chat-client

public class Main {

    public static void main(String[] args) throws XmppStringprepException, NotLoggedInException, NotConnectedException, InterruptedException, XMPPException, NotAMucServiceException, NoResponseException, MucConfigurationNotSupportedException, MucAlreadyJoinedException, MissingMucCreationAcknowledgeException {

        Terminal Terminal = new Terminal();
        AdminManager AM = new AdminManager();
        UserManager UM  = new UserManager();

        int adminOption = 0;
        AbstractXMPPConnection actualSession = null;
        NotificationsListener notificationListener = null;
        while (adminOption != 5){
            adminOption = Terminal.adminMenu();
        
            switch (adminOption) {
                case 1:
                    System.out.println("\n---- Register ----");
                    // Get new user information
                    List<String> values = Terminal.getNewUserInfo();
                    // Register the user
                    AM.Register(values.get(0), values.get(1));
                    break;
                case 2:
                    System.out.println("\n---- Login ----");
                    // Get user credentials
                    List<String> valuesU = Terminal.getUserCredentials();
                    // Attempt to log in
                    actualSession = AM.Login(valuesU.get(0), valuesU.get(1));
                    if (actualSession == null){
                        System.out.println("\nSomething went wrong, please try again");
                        System.out.println("Your username or password is incorrect");
                        System.out.println("Or your account doesn't exist on the server");
                    } else {
                        System.out.println("Welcome!!!");

                        // Start notification listener
                        notificationListener = new NotificationsListener(actualSession, UM);
                        notificationListener.start();

                        String username = "";

                        boolean exit = false;
                        while(!exit){
                            int userP = Terminal.userMenu(valuesU.get(0));
                            switch (userP) {
                                case 1:
                                    // Get all contacts
                                    UM.getAllContacts(actualSession);
                                    break;
                                case 2:
                                    // Add a contact
                                    username = Terminal.get_contact_info();
                                    if (username == null){
                                        System.out.println("Understood, returning to the menu\n");
                                        break;
                                    } else {
                                        String JID = username+"@alumchat.xyz";
                                        UM.addContact(actualSession, JID, username);
                                    }
                                    break;
                                case 3:
                                    // Get user details
                                    username = Terminal.get_contact_info();
                                    if (username == null){
                                        System.out.println("Understood, returning to the menu\n");
                                        break;
                                    } else {
                                        String JID = username+"@alumchat.xyz";
                                        String response = UM.getUserDetails(actualSession, JID);
                                        System.out.println(response);
                                    }
                                    break;

                                case 4:
                                    // Chat 1 to 1
                                    UM.manageChat(actualSession);
                                    break;

                                case 5:
                                    // Groups
                                    UM.manageGroupChat(actualSession);
                                    break;

                                case 6:
                                    // Update user status
                                    String status = Terminal.get_new_status();
                                    UM.updateUserStatus(actualSession, status);
                                    break;

                                case 7:
                                    // Show notifications
                                    List<NotificationP> notis = UM.getPendingNotifications();
                                    if(notis.size() > 0){
                                        Terminal.print_notis(notis);
                                    } else {
                                        System.out.println("You don't have notifications yet!");
                                    }
                                    break;
                                    
                                case 8:
                                    // Send files
                                    UM.sendFile(actualSession);
                                    break;

                                case 9:
                                    // Close session
                                    actualSession = AM.CloseSession(actualSession);
                                    notificationListener.stopRunning();
                                    exit = true;
                                    break;
                            
                                default:
                                    System.out.println("Enter a valid option!!");
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    // Close session
                    System.out.println("---- Close session ----");
                    if(actualSession != null){
                        actualSession = AM.CloseSession(actualSession);
                    } else {
                        System.out.println("No active session\n");
                    }

                    break;
                case 4:
                    // Delete account
                    System.out.println("---- Delete account ----");
                    List<String> valuesLog = Terminal.getUserCredentials();
                    actualSession = AM.Login(valuesLog.get(0), valuesLog.get(1));
                    int opc = Terminal.get_close_session_answer();
                    if(actualSession != null && opc == 1){
                        AM.DeleteAccount(actualSession);
                        System.out.println("Account deleted from the server successfully");
                    } else if(actualSession != null && opc == 0){
                        System.out.println("Understood, this account will not be deleted");
                    } else if(actualSession != null && opc == 2){
                        System.out.println("An error occurred, invalid option!");
                    } else {
                        System.out.println("No active session\n Login to delete your account\n");
                    }
                    break;

                case 5:
                    // Exit
                    System.out.println("Thanks for use this Client!!\n");
                    break;
            
                default:
                    System.out.println("Invalid option");
            }
        
        }
    }
}

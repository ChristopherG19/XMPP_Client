/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import java.util.List;
import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class handles terminal input and output for user interactions.
 */
public class Terminal {

    Scanner scan = new Scanner(System.in);

    /**
    * Displays the admin menu and retrieves the selected option.
    *
    * @return int: User-selected option
    */
    public int adminMenu(){
        int option = 0;

        System.out.println("\n----- Chat Redes -----");
        System.out.println("Available options: ");
        System.out.println("1) Register a new account on the server");
        System.out.println("2) Log in with an account");
        System.out.println("3) Delete account from the server");
        System.out.println("4) Exit");
        System.out.print("\nEnter an option: ");
    
        try{
           option = Integer.valueOf(scan.nextLine());
        } catch (Exception e){
            System.out.println("Error! Only numeric values are allowed");
        }

        return option;
    }

    /**
     * Displays the user menu and retrieves the selected option.
     *
     * @param username String: Username of the user
     * @return int: User-selected option
     */
    public int userMenu(String username){
        int option = 0;

        System.out.println("\n----------- Chat Network -----------");
        String welcome = String.format("Connected User: %s", username);
        System.out.println(welcome);
        System.out.println("------------------------------------");
        System.out.println("\nAvailable options: ");
        System.out.println("1) Show all contacts and their status");
        System.out.println("2) Add a user to contacts");
        System.out.println("3) Show contact details");
        System.out.println("4) One-to-one communication with any user/contact");
        System.out.println("5) Participate in group conversations");
        System.out.println("6) Set presence message");
        System.out.println("7) Notifications");
        System.out.println("8) Send/receive files");
        System.out.println("9) Log out");
        System.out.print("\nEnter an option: ");

        try{
           option = Integer.valueOf(scan.nextLine());
        } catch (Exception e){
            System.out.println("Error! Only numeric values are allowed");
        }

        return option;
    }

    /**
     * Retrieves new user registration information.
     *
     * @return List of strings containing user information
     */
    public List<String> getNewUserInfo(){
        List<String> values = new ArrayList<>();
        Boolean allOK = false;

        while (!allOK){
            System.out.println("\nTo register a new account, we need some information...");
            System.out.print("1) Username: ");
            String username = scan.nextLine();
            Console console = System.console();
            char[] pswd = console.readPassword("2) Enter password: ");
            String password = toString(pswd);
            char[] pswdConf = console.readPassword("3) Confirm password: ");
            String passwordConf = toString(pswdConf);

            values.add(username);
            if(password.equals(passwordConf)){
                values.add(password);
                allOK = true;
            } else {
                System.out.println("Passwords do not match!!!");
            }
        }
        
        return values;
    }

    /**
     * Retrieves user login credentials.
     *
     * @return List of strings containing username and password
     */
    public List<String> getUserCredentials(){
        List<String> values = new ArrayList<>();

        System.out.println("\nEnter the following data to log in...");
        System.out.print("1) Username: ");
        String username = scan.nextLine();
        Console console = System.console();
        char[] pswd = console.readPassword("2) Enter password: ");
        String password = toString(pswd);

        values.add(username);
        values.add(password);
        return values;
    }
    
    /**
     * Prompts the user for a choice regarding closing the session.
     *
     * @return int: User-selected option (1 for yes, 0 for no, 2 for invalid)
     */
    public int get_close_session_answer(){
        int opc = 0;
        System.out.print("Do you want to delete this account from the server? (y/n): ");
        String opca = scan.nextLine();
        switch (opca.toLowerCase()) {
            case "y":
                opc = 1;
                break;
            case "n":
                opc = 0;
                break;
            default:
                System.out.println("Invalid option");
                opc = 2;
                break;
        }
        return opc;
    }

    /**
     * Prompts the user for a choice regarding staying online after login.
     *
     * @return int: User-selected option (1 for yes, 0 for no, 2 for invalid)
     */
    public int get_stay_online_answer(){
        int ans = 0;
        System.out.print("Do you want to log in immediately and stay online? (y/n): ");
        String ansU = scan.nextLine();
        switch (ansU.toLowerCase()) {
            case "y":
                ans = 1;
                break;
            case "n":
                ans = 0;
                break;
            default:
                System.out.println("Invalid option");
                ans = 2;
                break;
        }
        return ans;
    }

    /**
     * Prompts the user for contact information.
     *
     * @return String: Contact username
     */
    public String get_contact_info() {
        String user = "";
        boolean isValid = false;
        
        while (!isValid) {
            System.out.print("Contact username: ");
            user = scan.nextLine();
            
            if (!user.trim().isEmpty()) {
                if (user.equals("exit")) {
                    user = null;
                }
                isValid = true;
            } else {
                System.out.println("\nTry again or type 'exit' to cancel\n");
            }
        }
        
        return user;
    }

    /**
     * Prompts the user to enter a new status message.
     *
     * @return String: User-entered status message
     */
    public String get_new_status(){
        String status = "";
        boolean confirm = false;
        while(!confirm){
            System.out.print("Enter your presence message: ");
            status = scan.nextLine();
            boolean confirmB = false;
            while(!confirmB){
                System.out.print("Are you sure? (y/n): ");
                String op = scan.nextLine();
                switch (op.toLowerCase()) {
                    case "y":
                        confirmB = true;
                        confirm = true;
                        break;
                    case "n":
                        confirmB = true;
                        break;
                    default:
                        System.out.println("Enter a valid option\n");
                        break;
                }
            }
        }
        
        return status;
    }

    /**
     * Prompts the user to choose a chat type.
     *
     * @return int: User-selected chat type (1 for contact, 2 for new user, 3 to exit)
     */
    public int get_type_chat(){
        int res = 0;
        boolean isValid = false;

        while(!isValid){
            System.out.println("Available options\n");
            System.out.println("1) Chat with contacts");
            System.out.println("2) Chat with a new user");
            System.out.println("3) Exit");
            System.out.print("What would you like to do?: ");
            try {
                res = scan.nextInt();
                isValid = true;
            } catch (Exception e) {
                System.out.println("Enter numeric values!!!\n");
            }
        }
        return res;
    }

    /**
     * Prompts the user to choose a contact for a conversation.
     *
     * @return int: User-selected contact number (0 to exit)
     */
    public int get_user_chat() {
        int us = 0;
        boolean isValid = false;
        while (!isValid) {
            System.out.print("Enter the contact number to start a conversation (0 to exit): ");
            try {
                if (scan.hasNextInt()) {
                    us = scan.nextInt();
                    isValid = true;
                } else {
                    System.out.println("Enter numeric values!!!\n");
                    scan.next();
                }
            } catch (Exception e) {
                System.out.println("An error occurred in input. Enter numeric values!!!\n");
                scan.next();
            }
        }
        return us;
    }

    /**
     * Prompts the user to choose a group chat action.
     *
     * @return int: User-selected group chat action (1 to join, 2 to create group, 0 to exit)
     */
    public int get_type_groupChat(){
        int us = 0;
        boolean isValid = false;
        while (!isValid) {
            System.out.println("Group Conversations");
            System.out.println("1) Join a conversation");
            System.out.println("2) Create a group");
            System.out.print("Enter an option (0 to exit): ");
            try {
                if (scan.hasNextInt()) {
                    us = scan.nextInt();
                    isValid = true;
                } else {
                    System.out.println("Enter numeric values!!!\n");
                    scan.next();
                }
            } catch (Exception e) {
                System.out.println("An error occurred in input. Enter numeric values!!!\n");
                scan.next();
            }
        }
        return us;
    }

    /**
     * Prompts the user for a message to send.
     *
     * @return String: User-entered message
     */
    public String get_message_to_Send(){
        System.out.print(">> ");
        String res = scan.nextLine();
        return res;
    }

    /**
     * Prints the list of pending notifications.
     *
     * @param notis List<NotificationP>: List of pending notifications
     */
    public void print_notis(List<NotificationP> notis){
        System.out.println("Notifications:");
        for (NotificationP notification : notis) {
            System.out.println("----------------------------------------------------------");
            System.out.println(notification);
            System.out.println("----------------------------------------------------------");
        }
    }

    /**
     * Gets the properties for joining a group chat.
     *
     * @return String: Group name and username separated by '$'
     */
    public String get_GC_join_props() {
        String groupName = getValidInput("Enter the group name: ");
        String username = getValidInput("Enter the username: ");

        return groupName + "$" + username;
    }

    /**
     * Prompts the user for valid input and confirms the input.
     *
     * @param prompt String: The prompt to display for input
     * @return String: Valid user-entered input
     */
    private String getValidInput(String prompt) {
        String input = "";

        Boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            if(scan.hasNextLine()){
                input = scan.nextLine().trim();
                if(input.isEmpty()){
                    continue;
                }
                System.out.print("\nConfirm that " + input + " is correct? (y/n): ");
                String confirmation = scan.nextLine().toLowerCase();
                
                if (confirmation.equals("y")) {
                    valid = true;
                } else if (confirmation.equals("n")) {
                    continue;
                }
            }
        }

        return input;
    }

    /**
     * Gets the properties for sending a file.
     *
     * @return String: File properties separated by '$'
     */
    public String get_file_props(){
        String props = "";
        boolean confi = false;

        while(!confi){
            System.out.println("Enter the following data to send files...");
            try {
                String username = getValidInput("Enter the username: ");
                String path = getValidInput("Enter the path: ");
                props = username + "$" + path;
                confi = true;
                break;
            } catch (Exception e) {
                System.out.println("An error occurred in input. Enter numeric values!!!\n");
                scan.next();
            }
        }

        return props;
    }

    /**
     * Converts a character array to a string.
     *
     * @param a char[]: Character array
     * @return String: Converted string
     */
    public static String toString(char[] a)
    {
        String string = new String(a);
        return string;
    }

}

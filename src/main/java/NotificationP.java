/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

/**
 * This class represents a notification with sender information, content, and mode.
 */
public class NotificationP {
    private String sender; 
    private String content;
    private String mode;

    /**
     * Constructs a notification with the given sender, content, and mode.
     *
     * @param sender The sender of the notification.
     * @param content The content of the notification.
     * @param mode The mode of the notification.
     */
    public NotificationP(String sender, String content, String mode) {
        this.sender = sender;
        this.content = content;
        this.mode = mode;
    }

    /**
     * Returns the sender of the notification.
     *
     * @return The sender of the notification.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the content of the notification.
     *
     * @return The content of the notification.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the appropriate content type based on the content.
     *
     * @param content The content of the notification.
     * @return The translated content based on its type.
     */
    public String get_type_content(String content){
        String newContent = "";
        switch (content) {
            case "subscribe":
                newContent = "Hello! I'd like to have you as a contact.";
                break;

            case "available":
                if (this.mode != null){
                    if (this.mode.equals("available")) {
                        newContent = "I'm online and available.";
                    } else if (this.mode.equals("away")) {
                        newContent = "I'm currently away.";
                    } else if (this.mode.equals("dnd")) {
                        newContent = "I'm busy right now.";
                    } else {
                        newContent = "I'm not available at the moment.";
                    }
                }
                break;

            case "unavailable":
                newContent = "I've gone offline. See you next time!";
                break;
        
            default:
                String body = (this.content.equals("subscribed")) ? "Accepted your invitation, now we're friends" : this.content;
                if(!body.equals("exit")){
                    newContent = "New message\n(" + this.sender + ") says: " + body;
                }
                break;
        }
        return newContent;
    }

    /**
     * Returns a string representation of the notification.
     *
     * @return A string representation of the notification.
     */
    @Override
    public String toString() {
        return "Notification from: " + sender + "\nContent: " + ((content != null) ? get_type_content(content) : "---");
    }
}

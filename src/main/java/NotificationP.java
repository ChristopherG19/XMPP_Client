public class NotificationP {
    private String sender; 
    private String content;

    public NotificationP(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Notificación de: " + sender + "\nContenido: " + content;
    }
}

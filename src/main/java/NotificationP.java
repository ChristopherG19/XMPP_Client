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

    public String get_type_content(String content){
        String newContent = "";
        switch (content) {
            case "subscribe":
                newContent = "Hola!!! Me gustar\u00EDa tenerte de contacto";
                break;

            case "available":
                newContent = "Me he conectado! Estoy en l\u00EDnea";
                break;

            case "unavailable":
                newContent = "Me he desconectado! Hasta la pr\u00F3xima";
                break;
        
            default:
                String body = (this.content.equals("subscribed")) ? "Acepte tu invitación, ahora somos amigos" : this.content;
                newContent = "Nuevo mensaje\n("+ this.sender + ") dice: " + body;
                break;
        }
        return newContent;
    }

    @Override
    public String toString() {
        return "Notificación de: " + sender + "\nContenido: " + get_type_content(content);
    }
}

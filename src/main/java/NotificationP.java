/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

public class NotificationP {
    private String sender; 
    private String content;
    private String mode;

    public NotificationP(String sender, String content, String mode) {
        this.sender = sender;
        this.content = content;
        this.mode = mode;
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
                if (this.mode != null){
                    if (this.mode.equals("available")) {
                        newContent = "Me he conectado! Estoy en l\u00EDnea";
                    } else if (this.mode.equals("away")) {
                        newContent = "Me encuentro ausente en este momento";
                    } else if (this.mode.equals("dnd")) {
                        newContent = "Me encuentro ocupado en este momento";
                    } else {
                        newContent = "No me encuentro disponible en este momento";
                    }
                }
                break;

            case "unavailable":
                newContent = "Me he desconectado! Hasta la pr\u00F3xima";
                break;
        
            default:
                String body = (this.content.equals("subscribed")) ? "Acepte tu invitación, ahora somos amigos" : this.content;
                if(!body.equals("exit")){
                    newContent = "Nuevo mensaje\n("+ this.sender + ") dice: " + body;
                }
                break;
        }
        return newContent;
    }

    @Override
    public String toString() {
        return "Notificación de: " + sender + "\nContenido: " + ((content != null) ? get_type_content(content) : "---");
    }
}

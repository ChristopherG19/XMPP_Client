/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jxmpp.jid.EntityBareJid;

public class MessagesListener implements IncomingChatMessageListener, OutgoingChatMessageListener, MessageListener{
    private AtomicBoolean chatActive;

    public MessagesListener(AtomicBoolean chatActive) {
        this.chatActive = chatActive;
    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        if (chatActive.get()) {
            String body = message.getBody();
            if (!from.equals("") && body != null){
                if (body != null && body.startsWith("FILE:")) {
                    String base64Encoded = body.substring(5); // Eliminar "FILE:" del mensaje
                    String ext = message.getSubject();
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);

                    if(ext.equals("jpg") || ext.equals("png")){
                        try {
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
                            BufferedImage bufferedImage = ImageIO.read(inputStream);

                            if (bufferedImage != null) {
                                JFrame frame = new JFrame("Received Image from "+from.toString());
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                
                                JLabel label = new JLabel(new ImageIcon(bufferedImage), SwingConstants.CENTER);
                                frame.getContentPane().add(label);
                                
                                frame.pack();
                                frame.setLocationRelativeTo(null); // Center the frame
                                frame.setVisible(true);
                            }
                        } catch (IOException ex) {
                            System.out.println("Received unknown file type.");
                        }
                    } else if(ext.equals("txt")){
                        String textContent = new String(decodedBytes, StandardCharsets.UTF_8);
                        System.out.println("Received text file content:");
                        System.out.println(textContent);
                    } else {
                        System.out.println("Recibiendo archivo ."+ext);
                        System.out.println("\nNo podemos abrir este tipo de archivos perdón");
                    }

                } else {
                    System.out.println("\n>> (" + from + "): " + body);
                }
            }
        }
    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, Message message, Chat chat) {
        if (chatActive.get()) {
            System.out.println("\n>> (You): " + message.getBody());
        }
    }

    @Override
    public void processMessage(Message message) {
        if (chatActive.get()) {
            String sender = message.getFrom().getResourceOrEmpty().toString();
            String body = message.getBody();
            if (!sender.equals("") && body != null){
                if (body != null && body.startsWith("FILE:")) {
                    String base64Encoded = body.substring(5); // Eliminar "FILE:" del mensaje
                    String ext = message.getSubject();
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Encoded);

                    if(ext.equals("jpg") || ext.equals("png")){
                        try {
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
                            BufferedImage bufferedImage = ImageIO.read(inputStream);

                            if (bufferedImage != null) {
                                JFrame frame = new JFrame("Received Image from "+sender.toString());
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                
                                JLabel label = new JLabel(new ImageIcon(bufferedImage), SwingConstants.CENTER);
                                frame.getContentPane().add(label);
                                
                                frame.pack();
                                frame.setLocationRelativeTo(null); // Center the frame
                                frame.setVisible(true);
                            }
                        } catch (IOException ex) {
                            System.out.println("Received unknown file type.");
                        }
                    } else if(ext.equals("txt")){
                        String textContent = new String(decodedBytes, StandardCharsets.UTF_8);
                        System.out.println("Received text file content:");
                        System.out.println(textContent);
                    } else {
                        System.out.println("Recibiendo archivo ."+ext);
                        System.out.println("\nNo podemos abrir este tipo de archivos perdón");
                    }

                } else {
                    System.out.println("\n>> (" + sender + "): " + body);
                }
            }
        }
    }
}


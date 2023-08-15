/*
    Universidad del Valle de Guatemala
    Redes de Computadora
    Christopher García 20541
    Proyecto#1: Cliente XMPP
*/

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jxmpp.stringprep.XmppStringprepException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws XmppStringprepException, NotLoggedInException, NotConnectedException, InterruptedException, XMPPException {

        Terminal Terminal = new Terminal();
        AdminManager AM = new AdminManager();
        UserManager UM  = new UserManager();

        int adminOption = 0;
        AbstractXMPPConnection actualSession = null;
        while (adminOption != 5){
            adminOption = Terminal.adminMenu();
        
            switch (adminOption) {
                case 1:
                    System.out.println("\n---- Register ----");
                    List<String> values = Terminal.getNewUserInfo();
                    AM.Register(values.get(0), values.get(1));
                    break;
                case 2:
                    System.out.println("\n---- Login ----");
                    List<String> valuesU = Terminal.getUserCredentials();
                    actualSession = AM.Login(valuesU.get(0), valuesU.get(1));
                    if (actualSession == null){
                        System.out.println("\nAlgo ha salido mal, intente nuevamente");
                        System.out.println("Tu usuario o contrase\u00F1a est\u00E1n incorrectos");
                        System.out.println("O tu cuenta no existe en el servidor");
                    } else {
                        System.out.println("Bienvenido!!!");

                        String username = "";

                        boolean exit = false;
                        while(!exit){
                            int userP = Terminal.userMenu(valuesU.get(0));
                            switch (userP) {
                                case 1:
                                    UM.getAllContacts(actualSession);
                                    break;
                                case 2:
                                    username = Terminal.get_contact_info();
                                    if (username == null){
                                        System.out.println("Entendido, saliendo de esta opci\u00F3n...\n");
                                        break;
                                    } else {
                                        String JID = username+"@alumchat.xyz";
                                        UM.addContact(actualSession, JID, username);
                                    }
                                    break;
                                case 3:
                                    username = Terminal.get_contact_info();
                                    if (username == null){
                                        System.out.println("Entendido, saliendo de esta opci\u00F3n...\n");
                                        break;
                                    } else {
                                        String JID = username+"@alumchat.xyz";
                                        String response = UM.getUserDetails(actualSession, JID);
                                        System.out.println(response);
                                    }
                                    break;

                                case 4:
                                    UM.manageChat(actualSession);
                                    break;

                                case 6:
                                    String status = Terminal.get_new_status();
                                    UM.updateUserStatus(actualSession, status);
                                    break;

                                case 9:
                                    actualSession = AM.CloseSession(actualSession);
                                    exit = true;
                                    break;
                            
                                default:
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("---- Close session ----");
                    if(actualSession != null){
                        actualSession = AM.CloseSession(actualSession);
                    } else {
                        System.out.println("No hay ninguna sesi\u00F3n activa\n");
                    }

                    break;
                case 4:
                    System.out.println("---- Delete account ----");
                    List<String> valuesLog = Terminal.getUserCredentials();
                    actualSession = AM.Login(valuesLog.get(0), valuesLog.get(1));
                    int opc = Terminal.get_close_session_answer();
                    if(actualSession != null && opc == 1){
                        AM.DeleteAccount(actualSession);
                        System.out.println("Cuenta eliminada del servidor exitosamente");
                    } else if(actualSession != null && opc == 0){
                        System.out.println("Entendido, no se borrara esta cuenta");
                    } else if(actualSession != null && opc == 2){
                        System.out.println("Ha ocurrido un error, opcion invalida!");
                    } else {
                        System.out.println("No hay ninguna sesi\u00F3n activa\nInicia sesi\u00F3n para poder eliminar tu cuenta\n");
                    }
                    break;

                case 5:
                    break;
            
                default:
                    System.out.println("Opci\u00F3n invalida");;
            }
        
        }

        // Imprimir despedida

    }
}

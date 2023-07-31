import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jxmpp.stringprep.XmppStringprepException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws XmppStringprepException {

        Terminal Terminal = new Terminal();
        AdminManager AM = new AdminManager();

        int adminOption = 0;
        AbstractXMPPConnection actualSession;
        while (adminOption != 5){
            adminOption = Terminal.adminMenu();
        
            switch (adminOption) {
                case 1:
                    System.out.println("\n---- Registro de cuenta ----");
                    List<String> values = Terminal.getNewUserInfo();
                    String result = AM.Register(values.get(0), values.get(1));
                    if(result.equals("OK")){
                        System.out.println("Cuenta creada exitosamente");
                    } else {
                        System.out.println("Algo ha salido mal, intente nuevamente");
                    }
                    break;
                case 2:
                    System.out.println("\n---- Login ----");
                    List<String> valuesU = Terminal.getUserCredentials();
                    actualSession = AM.Login(valuesU.get(0), valuesU.get(1));
                    if (actualSession == null){
                        System.out.println("Algo ha salido mal, intente nuevamente");
                    } else {
                        System.out.println("Bienvenido!!!");
                        Terminal.userMenu(valuesU.get(0));
                    }
                    break;
                case 3:
                    System.out.println("Opci\u00F3n 3");
                    break;

                case 4:
                    System.out.println("Opci\u00F3n 4");
                    break;

                case 5:
                    System.out.println("Opci\u00F3n 5");
                    break;
            
                default:
                    System.out.println("Opci\u00F3n invalida");;
            }
        
        }

        // Imprimir despedida

    }
}

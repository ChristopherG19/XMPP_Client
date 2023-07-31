import org.jxmpp.stringprep.XmppStringprepException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws XmppStringprepException {

        Terminal Terminal = new Terminal();
        AdminManager AM = new AdminManager();

        int adminOption = 0;
        while (adminOption != 5){
            adminOption = Terminal.adminMenu();
        
            switch (adminOption) {
                case 1:
                    System.out.println("\n---- Registro de cuenta ----");
                    List<String> values = Terminal.getNewUserInfo();
                    AM.Register(values.get(0), values.get(1));
                    break;

                case 2:
                    System.out.println("Opci\u00F3n 2");
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

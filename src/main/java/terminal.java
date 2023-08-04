
import java.util.List;
import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;


public class Terminal {

    Scanner scan = new Scanner(System.in);

    /** 
     * adminMenu Method: print the menu and request an option
     * @return int: user option
     */
    public int adminMenu(){
        int option = 0;

        System.out.println("\n----- Chat Redes -----");
        System.out.println("Opciones disponibles: ");
        System.out.println("1) Registrar una nueva cuenta en el servidor");
        System.out.println("2) Iniciar sesi\u00F3n con una cuenta");
        System.out.println("3) Cerrar sesi\u00F3n con una cuenta");
        System.out.println("4) Eliminar la cuenta del servidor");
        System.out.println("5) Salir");
        System.out.print("\nIngrese una opci\u00F3n: ");
    
        try{
           option = Integer.valueOf(scan.nextLine());
        } catch (Exception e){
            System.out.println("Error! Solo valores numericos permitidos");
        }

        return option;
    }

    public int userMenu(String username){
        int option = 0;

        System.out.println("\n----- Chat Redes -----");
        String welcome = String.format("Usuario conectado: %s", username);
        System.out.println(welcome);
        System.out.println("-----------------------");
        System.out.println("\nOpciones disponibles: ");
        System.out.println("1) Mostrar todos los contactos y su estado");
        System.out.println("2) Agregar un usuario a los contactos");
        System.out.println("3) Mostrar detalles de contacto de un usuario");
        System.out.println("4) Comunicación 1 a 1 con cualquier usuario/contacto");
        System.out.println("5) Participar en conversaciones grupales");
        System.out.println("6) Definir mensaje de presencia");
        System.out.println("7) Enviar/recibir notificaciones");
        System.out.println("8) Enviar/recibir archivos");
        System.out.println("9) Cerrar sesi\u00F3n");
        System.out.print("\nIngrese una opci\u00F3n: ");

        try{
           option = Integer.valueOf(scan.nextLine());
        } catch (Exception e){
            System.out.println("Error! Solo valores numericos permitidos");
        }

        return option;
    }

    public List<String> getNewUserInfo(){
        List<String> values = new ArrayList<>();
        Boolean allOK = false;

        while (!allOK){
            System.out.println("\nPara poder registrar una nueva cuenta necesitamos algunos datos...");
            System.out.print("1) Nombre de usuario: ");
            String username = scan.nextLine();
            Console console = System.console();
            char[] pswd = console.readPassword("2) Ingresa contrase\u00F1a: ");
            String password = toString(pswd);
            char[] pswdConf = console.readPassword("3) Confirma contrase\u00F1a: ");
            String passwordConf = toString(pswdConf);

            values.add(username);
            if(password.equals(passwordConf)){
                values.add(password);
                allOK = true;
            } else {
                System.out.println("Las contrase\u00F1as no coinciden!!!");
            }
        }
        
        return values;
    }

    public List<String> getUserCredentials(){
        List<String> values = new ArrayList<>();

        System.out.println("\nIngresa los siguientes datos para loggearte...");
        System.out.print("1) Nombre de usuario: ");
        String username = scan.nextLine();
        Console console = System.console();
        char[] pswd = console.readPassword("2) Ingresa contrase\u00F1a: ");
        String password = toString(pswd);

        values.add(username);
        values.add(password);
        return values;
    }

    public int get_close_session_answer(){
        int opc = 0;
        System.out.print("Deseas eliminar esta cuenta del servidor (y/n)");
        String opca = scan.nextLine();
        switch (opca) {
            case "y":
                opc = 1;
                break;
            case "n":
                opc = 0;
                break;
            default:
                System.out.println("Opcion invalida");
                opc = 2;
                break;
        }
        return opc;
    }

    public static String toString(char[] a)
    {
        String string = new String(a);
        return string;
    }

}

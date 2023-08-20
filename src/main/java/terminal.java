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

        System.out.println("\n----------- Chat Redes -----------");
        String welcome = String.format("Usuario conectado: %s", username);
        System.out.println(welcome);
        System.out.println("----------------------------------");
        System.out.println("\nOpciones disponibles: ");
        System.out.println("1) Mostrar todos los contactos y su estado");
        System.out.println("2) Agregar un usuario a los contactos");
        System.out.println("3) Mostrar detalles de contacto de un usuario");
        System.out.println("4) Comunicación 1 a 1 con cualquier usuario/contacto");
        System.out.println("5) Participar en conversaciones grupales");
        System.out.println("6) Definir mensaje de presencia");
        System.out.println("7) Notificaciones");
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
        System.out.print("Deseas eliminar esta cuenta del servidor (y/n): ");
        String opca = scan.nextLine();
        switch (opca.toLowerCase()) {
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

    public int get_stay_online_answer(){
        int ans = 0;
        System.out.print("Deseas ingresar inmediatamente y quedarte conectado/a? (y/n)");
        String ansU = scan.nextLine();
        switch (ansU.toLowerCase()) {
            case "y":
                ans = 1;
                break;
            case "n":
                ans = 0;
                break;
            default:
                System.out.println("Opcion invalida");
                ans = 2;
                break;
        }
        return ans;
    }

    public String get_contact_info() {
        String user = "";
        boolean isValid = false;
        
        while (!isValid) {
            System.out.print("Usuario del contacto: ");
            user = scan.nextLine();
            
            if (!user.trim().isEmpty()) {
                if (user.equals("exit")) {
                    user = null;
                }
                isValid = true;
            } else {
                System.out.println("\nPrueba de nuevo o escribe exit si deseas salir\n");
            }
        }
        
        return user;
    }

    public String get_new_status(){
        String status = "";
        boolean confirm = false;
        while(!confirm){
            System.out.print("Ingresa tu mensaje de presencia: ");
            status = scan.nextLine();
            boolean confirmB = false;
            while(!confirmB){
                System.out.print("Est\u00E1s seguro? (y/n): ");
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
                        System.out.println("Ingresa una opción válida\n");
                        break;
                }
            }
        }
        
        return status;
    }

    public int get_type_chat(){
        int res = 0;
        boolean isValid = false;

        while(!isValid){
            System.out.println("Opciones disponibles\n");
            System.out.println("1) Chat con contactos");
            System.out.println("2) Chat con nuevo usuario");
            System.out.println("3) Salir");
            System.out.print("¿Que deseas hacer?: ");
            try {
                res = scan.nextInt();
                isValid = true;
            } catch (Exception e) {
                System.out.println("Ingresa valores numéricos!!!\n");
            }
        }
        return res;
    }

    public int get_user_chat() {
        int us = 0;
        boolean isValid = false;
        while (!isValid) {
            System.out.print("Ingresa el número de contacto para iniciar conversación (0 para salir): ");
            try {
                if (scan.hasNextInt()) {
                    us = scan.nextInt();
                    isValid = true;
                } else {
                    System.out.println("Ingresa valores numéricos!!!\n");
                    scan.next();
                }
            } catch (Exception e) {
                System.out.println("Hubo un error en la entrada. Ingresa valores numéricos!!!\n");
                scan.next();
            }
        }
        return us;
    }

    public int get_type_groupChat(){
        int us = 0;
        boolean isValid = false;
        while (!isValid) {
            System.out.println("Conversaciones grupales");
            System.out.println("1) Ingresar a conversación");
            System.out.println("2) Crear grupo");
            System.out.print("Ingresa una opción (0 para salir): ");
            try {
                if (scan.hasNextInt()) {
                    us = scan.nextInt();
                    isValid = true;
                } else {
                    System.out.println("Ingresa valores numéricos!!!\n");
                    scan.next();
                }
            } catch (Exception e) {
                System.out.println("Hubo un error en la entrada. Ingresa valores numéricos!!!\n");
                scan.next();
            }
        }
        return us;
    }

    public String get_message_to_Send(){
        System.out.print(">> ");
        String res = scan.nextLine();
        return res;
    }

    public void print_notis(List<NotificationP> notis){
        System.out.println("Notifications:");
        for (NotificationP notification : notis) {
            System.out.println("----------------------------------------------------------");
            System.out.println(notification);
            System.out.println("----------------------------------------------------------");
        }
    }

    public String get_GC_join_props() {
        String groupName = getValidInput("Enter the group name: ");
        String username = getValidInput("Enter the username: ");

        return groupName + "$" + username;
    }

    private String getValidInput(String prompt) {
        String input = "";

        Boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            input = scan.nextLine().trim();
            
            if (!input.isEmpty()) {
                System.out.print("\nConfirm that " + input + " is correct? (y/n): ");
                String confirmation = scan.nextLine().toLowerCase();
                
                if (confirmation.equals("y")) {
                    valid = true;
                } else if (confirmation.equals("n")) {
                    continue;
                }
            } else {
                System.out.println("Invalid input. Please try again.");
            }
            
        }

        return input;
    }

    public String get_file_props(){
        String props = "";
        boolean confi = false;
        int option = 0;

        while(!confi){
            System.out.println("Opciones disponibles para env\u00EDo de archivos");
            System.out.println("1) Enviar a usuario");
            System.out.println("2) Enviar a grupo");
            System.out.print("Selecciona una opci\u00F3n: ");
            try {
                if (scan.hasNextInt()) {
                    option = scan.nextInt();
                    switch (option) {
                        case 1:
                            String username = getValidInput("Enter the username: ");
                            String path = getValidInput("Enter the path: ");
                            props = username + "$" + path;
                            confi = true;
                            break;
                        case 2:
                            String groupName = getValidInput("Enter the groupname: ");
                            String nickName = getValidInput("Enter the nickname: ");
                            String pathB = getValidInput("Enter the path: ");
                            props = groupName + "$" + nickName + "$" + pathB;
                            confi = true;
                            break;
                    
                        default:
                            System.out.println("Opcion invalida!!!");
                            break;
                    }
                    
                } else {
                    System.out.println("Ingresa valores numéricos!!!\n");
                    scan.next();
                }
            } catch (Exception e) {
                System.out.println("Hubo un error en la entrada. Ingresa valores numéricos!!!\n");
                scan.next();
            }
        }

        return props;
    }

    public static String toString(char[] a)
    {
        String string = new String(a);
        return string;
    }

}

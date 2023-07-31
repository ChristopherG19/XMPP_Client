
import java.util.Scanner;


public class terminal {

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
        String welcome = String.format("Bienvenido/a %s", username);
        System.out.println(welcome);
        System.out.println("Opciones disponibles: ");
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

}

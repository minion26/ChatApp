import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLOutput;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter 1 to start a new chat or 2 to join some already existing and 3 to exit. ");
        int input = scanner.nextInt();


            if(input == 1){
                System.out.println("Enter your username: ");
                String username = scanner.next();
                System.out.println("Hi, " + username +"! Creating a new chat");
                System.out.println("Enter the port number: ");
                int port = scanner.nextInt();
                //creating the server
                Server server = new Server(port, username);
                server.start();
            } else if (input == 2) {
                System.out.println("Enter your username: ");
                String username = scanner.next();
                System.out.println("Hi, " + username +"! To join a chat please enter the port number: ");
//                int port = scanner.nextInt();
//                //creating the client
//                Client client = new Client(port, username);
//                client.start();

                // if the client types the wrong port, he can enter again
                while (true) {
                    int port = scanner.nextInt();
                    // creating the client
                    Client client = new Client(port, username);
                    try {
                        client.start();
                        break; // exit the loop if connection is successful
                    } catch (ConnectException e) {
                        System.out.println("Failed to connect to the server. Please enter the port number again: ");
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else if(input == 3){
                System.out.println("Good bye.");
               System.exit(0);
            }else{
                System.out.println("Please run again and enter a valid number.");
            }
        }

    }

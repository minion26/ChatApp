import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    private int port;
    private String username;
    private volatile boolean running = true;

    public Client(int port, String username){
        this.port = port;
        this.username = username;
    }

    public void start() throws ConnectException, IOException, InterruptedException{


            Socket socket = new Socket("localhost", port);

            System.out.println("[CLIENT] Connected to the server.");

            //creating streams for communication
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("A new client connected: " + username);

            Thread readerThread = new Thread(() -> {
                try {
                    String serverResponse;
                    while (running && ((serverResponse = in.readLine()) != null)) {
                        System.out.println(serverResponse);
                    }
                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            });

            readerThread.start();

            Scanner scanner = new Scanner(System.in);
            String userInput;
            while(running){
                userInput = scanner.nextLine(); //take form console
                if(Objects.equals(userInput, "exit")){
                    out.println("Client disconnected: " + username);
                    running = false;
                    readerThread.interrupt();
                    try {
                        socket.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out.close();

                    break;
                }
                out.println(username +": "+userInput); //write in stream for others
            }

            readerThread.join();



        }
    }


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private int port;
    private String username;
    private static CopyOnWriteArrayList<ClientHandler> clientsConnected = new CopyOnWriteArrayList<>();

    public Server(int port, String username){
        this.port = port;
        this.username = username;
    }

    public void start() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("[SERVER] Server waiting for clients to join.");

            new Thread(() -> {
                Client serverClient = new Client(port, username);
                try {
                    serverClient.start();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            while(true){
                //a client is connected
                Socket clientSocket = serverSocket.accept();

                //create a new thread for each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, username);
                clientsConnected.add(clientHandler);
                new Thread(clientHandler).start();
            }

        }catch(IOException e) {
            e.printStackTrace();
        }

    }


    public static void broadcast(String message, ClientHandler sender){
        for(ClientHandler client : clientsConnected){
            if(client != sender){
                client.sendMessage(message);
            }
        }
    }


    private static class ClientHandler implements Runnable{
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String clientUsername;

        public ClientHandler(Socket socket, String username){
            this.clientSocket = socket;
            this.clientUsername = username;

            try{
                //creating streams for communication
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        public void sendMessage(String message) {
            out.println(message);
        }

        @Override
        public void run() {
            try{
                System.out.println("[SERVER] User " + clientUsername + " connected.");
                //write messages in the stream
                out.println("Type your messages.");
                String inputLine;
                // while i have messages to read from the client
                while((inputLine = in.readLine())!=null){
                    String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                    String formattedMessage = "[" + timeStamp + "] "+ inputLine;
                    broadcast(formattedMessage, this);

                }

                clientsConnected.remove(this);

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }


    }
}

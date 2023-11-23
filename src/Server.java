import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int currentPlayerNumber = 1;
    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(12345);) {
            while (true) {
                Socket socketToClient = serverSocket.accept();
                System.out.println("Socket connected");

                System.out.println(currentPlayerNumber);

                ServerThreaded serverThreaded = new ServerThreaded(socketToClient, currentPlayerNumber);
                Thread thread = new Thread(serverThreaded);
                thread.start();
                currentPlayerNumber++;

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}

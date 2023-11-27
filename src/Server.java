import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Socket connected");
            while (true) {
                // Skapa två spelare
                Socket player1Socket = serverSocket.accept();
                System.out.println("User 1 is online");
                Socket player2Socket = serverSocket.accept();
                System.out.println("User 2 is online");

                ServerThreaded serverThreaded = new ServerThreaded(player1Socket, player2Socket);
                System.out.println(serverThreaded.isAlive());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}

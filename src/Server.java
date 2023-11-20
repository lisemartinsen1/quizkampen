import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Socket connected");
            while (true) {
                // Skapa två spelare
                ServerThreaded serverThreaded = new ServerThreaded(serverSocket.accept());
                ServerThreaded serverThreaded1 = new ServerThreaded(serverSocket.accept());

                serverThreaded.start();
                serverThreaded1.start();
                System.out.println(serverThreaded.isAlive());
                System.out.println(serverThreaded1.isAlive());

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}

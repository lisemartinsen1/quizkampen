import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private int currentPlayerNumber = 1;
    //private boolean player1CompletedRound = false;
    //private boolean player2CompletedRound = false;
    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(12345);) {
            while (true) {
                Socket socketToClient = serverSocket.accept();
                System.out.println("Socket connected");
                int playerNumber = currentPlayerNumber;
                currentPlayerNumber++;
                ServerThreaded serverThreaded = new ServerThreaded(socketToClient, playerNumber);
                Thread thread = new Thread(serverThreaded);
                thread.start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}

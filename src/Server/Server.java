package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server() {
        try (ServerSocket serverSocket = new ServerSocket(12345);) {

            while (true) {
                Socket player1Socket = serverSocket.accept();
                System.out.println("Player1 connected");
                Socket player2Socket = serverSocket.accept();
                System.out.println("Player2 connected");

                ServerThreaded serverThreaded = new ServerThreaded(player1Socket, player2Socket);
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreaded extends Thread implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();

    public ServerThreaded(Socket socket) {
        this.socket = socket;
        try {
            String clientMessage;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // while ((clientMessage = in.readline()) != null) {
            clientMessage = in.readLine();
            if (clientMessage.startsWith("RUN")) {
                out.println("WAIT");
                /*String response = protocol.getOutput();
                   out.println(response);*/
            } else {
                sendResponse(clientMessage);
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        out.println("CONNECTED");
        try {
            String clientMessage;

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("category")) {
                    sendNextQuestion(clientMessage);
                }
                    else if (clientMessage.startsWith("NEXT_QUESTION")){
                        sendNextQuestion(clientMessage);
                    }
                 else {
                    sendResponse(clientMessage);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNextQuestion(String category){    //ny metod för att inte upprepa kod
        String response = protocol.getOutput(category);
        out.println(response);
    }

    private void sendResponse (String message){
        out.println(message);
        out.flush();
    }
}
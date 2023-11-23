import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreaded implements Runnable {

    Socket socket1;
    Socket socket2;
    private PrintWriter out1;
    private PrintWriter out2;
    private BufferedReader in1;
    private BufferedReader in2;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();

    public ServerThreaded(Socket socket1, Socket socket2) {
        this.socket1 = socket1;
        this.socket2 = socket2;


    }

    @Override
    public void run() {
        try {
            in1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            out1 = new PrintWriter(socket1.getOutputStream(), true);

            in2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            out2 = new PrintWriter(socket2.getOutputStream(), true);

            out1.println("START"); //I Client anropas categoryUI.
            out2.println("WAIT"); //I Client hamnar man i "väntrum"

            String player1Message;
            String player2Message;

            while ((player1Message = in1.readLine()) != null) {
                if (player1Message.startsWith("CATEGORY")) {
                    sendNextQuestion(player1Message);
                }
                    else if (player1Message.startsWith("NEXT_QUESTION")){
                        sendNextQuestion(player1Message);
                    }
                 else {
                    sendResponse(player1Message);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNextQuestion(String category){    //ny metod för att inte upprepa kod
        String response = protocol.getOutput(category);
        out1.println(response);
    }

    private void sendResponse (String message){
        out1.println(message);
        out1.flush();
    }
}
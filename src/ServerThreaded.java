import javax.swing.*;
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
    private ServerThreaded opponent;

    String clientMessage;

    String categoryMessage;

    public ServerThreaded(Socket socket) {
        this.socket = socket;
        try {
            String clientMessage;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // while ((clientMessage = in.readline()) != null) {
            out.println("WAIT Waiting for response");
            //if (clientMessage.startsWith("RUN")) {
                //out.println("WAIT");
                /*String response = protocol.getOutput();
                   out.println(response);*/
                /*else if (clientMessage.startsWith("category")) {
                    setCategory(getClientMessage());
                } else if (opponent.getClientMessage().startsWith("category")) {
                    setCategory(opponent.getCategory());
                    int n = JOptionPane.showConfirmDialog(null, "Motståndet valde " + clientMessage +
                            ". Vill du fortsätta");
                    if (n == JOptionPane.YES_OPTION) {
                        sendMessageToClient("ACCEPT " + getCategory());
                        opponent.sendMessageToClient("ACCEPT" + getCategory());
                        sendNextQuestion(getCategory());
                        opponent.sendNextQuestion(getCategory());
                    }*/
            //} else {
                //sendResponse(clientMessage);
            //}

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setOpponent(ServerThreaded opponent) {
        this.opponent = opponent;
    }
    public void setClientmessage(String s) {
        this.clientMessage = s;
    }
    public String getClientMessage() {
        return clientMessage;
    }
    public void sendMessageToClient(String s) {
        out.println(s);
    }
    public void setCategory(String s) {
        this.categoryMessage = s;
    }
    public String getCategory() {
        return categoryMessage;
    }

    @Override
    public void run() {
        out.println("CONNECTED");
        try {
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("CHOOSECATEGORY")) {
                    setCategory(clientMessage.substring(14));
                    if (categoryProcess()) {
                        break;
                    }
                }
                if (clientMessage.startsWith("category")) {
                    sendNextQuestion(getCategory());
                } else if (clientMessage.startsWith("NEXT_QUESTION")) {
                    sendNextQuestion(clientMessage);
                } else {
                    sendResponse(clientMessage);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean categoryProcess() {
        int n = JOptionPane.showConfirmDialog(null, "Motståndet valde " + getCategory() +
                ". Vill du fortsätta");
        if (n == JOptionPane.YES_OPTION) {
            sendMessageToClient("ACCEPT " + getCategory());
            opponent.sendMessageToClient("ACCEPT" + getCategory());
            return true;
        }
        return false;
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
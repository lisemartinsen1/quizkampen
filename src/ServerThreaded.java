import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerThreaded extends Thread implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();
    private ServerThreaded opponent;

    String clientMessage;

    String categoryMessage;

    List<String> questionLists = new ArrayList<>();
    List<String> usedQuestionLists = new ArrayList<>();
    public ServerThreaded(Socket socket) {
        this.socket = socket;
        try {
            String clientMessage;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            // while ((clientMessage = in.readline()) != null) {
            out.println("WAIT Waiting for response");
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
                opponent.setClientmessage(clientMessage);
                if (clientMessage.startsWith("CHOOSECATEGORY")) {
                    System.out.println("step1 "+clientMessage.trim());
                    setCategory(clientMessage.substring(14));
                    opponent.sendNextQuestion("OPPONENTCHOOSED " + getCategory().trim());
                } else if (clientMessage.startsWith("ACCEPT")) {
                    setCategory(clientMessage.substring(6).trim());
                    sendNextQuestion("GO " + getCategory().trim());
                    opponent.sendNextQuestion("GO " + getCategory().trim());
                }
                if (clientMessage.startsWith("category") && opponent.getClientMessage().startsWith("category")) {
                    String response = protocol.getOutput(clientMessage);
                    questionLists.add(response);
                    opponent.questionLists.add(response);
                    for (String message : questionLists) {
                        sendNextQuestion(message);
                        opponent.sendNextQuestion(message);
                        usedQuestionLists.add(message);
                        opponent.usedQuestionLists.add(message);
                        questionLists.remove(message);
                        opponent.questionLists.remove(message);
                        break;
                    }
                } else if (clientMessage.startsWith("NEXT_QUESTION")) {
                    System.out.println("Next q: "+clientMessage);
                    String response = protocol.getOutput(getCategory());
                    questionLists.add(response);
                    opponent.questionLists.add(response);
                    System.out.println("Catnext "+ getCategory().trim());
                    for (String message : questionLists) {
                        System.out.println(message);
                        int count = 0;
                        System.out.println("COUNT: " + count);
                        for (String usedQuestion : usedQuestionLists) {
                            if (message.equals(usedQuestion)) {
                                questionLists.remove(message);
                                opponent.questionLists.remove(message);
                                count = 0;
                                break;
                            } else {
                                count++;
                            }
                        }
                        if (count == usedQuestionLists.size()) {
                            System.out.println("sorted: "+message);
                            sendNextQuestion(message);
                            opponent.sendNextQuestion(message);
                            usedQuestionLists.add(message);
                            opponent.usedQuestionLists.add(message);
                            questionLists.remove(message);
                            opponent.questionLists.remove(message);
                            break;
                        }
                    }
                    //sendNextQuestion(clientMessage);
                } else {
                    sendResponse(clientMessage);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*private boolean categoryProcess() {
        int n = JOptionPane.showConfirmDialog(null, "Motståndet valde " + getCategory() +
                ". Vill du fortsätta");
        if (n == JOptionPane.YES_OPTION) {
            sendMessageToClient("ACCEPT " + getCategory().trim());
            opponent.sendMessageToClient("ACCEPT " + getCategory().trim());
            return true;
        }
        return false;
    }*/

    private void sendNextQuestion(String response){    //ny metod för att inte upprepa kod
        //String response = protocol.getOutput(category);
        //System.out.println(response);
        out.println(response);
    }

    private void sendResponse (String message){
        out.println(message);
        out.flush();
    }
}
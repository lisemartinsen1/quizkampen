import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerThreaded extends Thread implements Runnable {
    private Socket socketP1;
    private Socket socketP2;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader in2;
    private PrintWriter out2;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();
    private ServerThreaded opponent;
    String clientMessage;
    String categoryMessage;
    List<String> questionLists = new ArrayList<>();
    List<String> usedQuestionLists = new ArrayList<>();
    public ServerThreaded(Socket socketP1, Socket socketP2) {
        this.socketP1 = socketP1;
        this.socketP2 = socketP2;
        try {
            in = new BufferedReader(new InputStreamReader(socketP1.getInputStream()));
            out = new PrintWriter(socketP1.getOutputStream(), true);
            in2 = new BufferedReader(new InputStreamReader(socketP2.getInputStream()));
            out2 = new PrintWriter(socketP2.getOutputStream(), true);
            // while ((clientMessage = in.readline()) != null) {
            if (socketP2.isConnected()) {
                out.println("CONNECTED");
                out2.println("CONNECTED");
            }
            String clientMessage = in.readLine();
            if (clientMessage.startsWith("CHOOSECATEGORY")) {
                System.out.println("P1 " + clientMessage);
                setCategory(clientMessage.substring(14));
                out2.println("OPPONENTCHOOSED" + getCategory());
            } else if (clientMessage.startsWith("ACCEPT")) {
                System.out.println("You Accepted");
                //setCategory(clientMessage.substring(6).trim());
                out.println("GO " + getCategory().trim());
                out2.println("GO " + getCategory().trim());
                start();
            }
            String clientMessageP2 = in2.readLine();
            if (clientMessageP2.startsWith("CHOOSECATEGORY")) {
                System.out.println("P2 " + clientMessageP2);
                setCategory(clientMessageP2.substring(14));
                out.println("OPPONENTCHOOSED" + getCategory());
            } else if (clientMessageP2.startsWith("ACCEPT")) {
                System.out.println("A player accepted");
                //setCategory(clientMessage.substring(6).trim());
                out.println("GO " + getCategory().trim());
                out2.println("GO " + getCategory().trim());
                start();
            }
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
        //out.println("CONNECTED");
        //out2.println("CONNECTED");
        try {
            String clientMessage;
            String clientMessageP2;
            while (true) {
                int q = 0;
                clientMessage = in.readLine();
                clientMessageP2 = in2.readLine();
                if (clientMessage.startsWith("category") && clientMessageP2.startsWith("category")) {
                    System.out.println("P1 cats " + clientMessage);
                    System.out.println("P2 cats " + clientMessageP2);
                    String response = protocol.getOutput(clientMessage);
                    questionLists.add(response);
                    Iterator<String> it = questionLists.iterator();
                    while (it.hasNext()) {
                        String message = it.next();
                        out.println(message);
                        out2.println(message);
                        usedQuestionLists.add(message);
                        it.remove();
                        q++;
                    }
                } else if (clientMessage.startsWith("NEXT_QUESTION") && clientMessageP2.startsWith("NEXT_QUESTION")) {
                    System.out.println("Next q: " + clientMessage);
                    String response = protocol.getOutput(getCategory());
                    questionLists.add(response);
                    for (String message : questionLists) {
                        System.out.println(message);
                        int count = 0;
                        System.out.println("COUNT: " + count);
                        for (String usedQuestion : usedQuestionLists) {
                            if (message.equals(usedQuestion)) {
                                questionLists.clear();
                                questionLists.add(response);
                                count = 0;
                                break;
                            } else {
                                count++;
                            }
                        }
                        if (count == usedQuestionLists.size()) {
                            System.out.println("sorted: " + message);
                            System.out.println("C " + count);
                            System.out.println("S " + usedQuestionLists.size());
                            out.println(message);
                            out2.println(message);
                            usedQuestionLists.add(message);
                            questionLists.remove(message);
                            q++;
                            break;
                        }
                    }
                    //sendNextQuestion(clientMessage);
                } else if (q == 3) {
                    break;
                } else {
                    sendResponse(clientMessage);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
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
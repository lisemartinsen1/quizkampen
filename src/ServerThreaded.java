import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThreaded implements Runnable {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();

    public ServerThreaded(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String clientMessage;

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("START")) {
                    QuestionAndAnswers qa = protocol.getOutput();

                    String question = qa.getQuestion();
                    String answers = dao.getAnswersForQuestion(qa);

                    out.println(question);
                    out.println(answers);
                } else {
                    sendResponse(clientMessage);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendResponse (String message){
        out.println(message);
        out.flush();
    }
}
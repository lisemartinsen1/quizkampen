import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThreaded implements Runnable {

    private Socket socket;
    private PrintWriter out;
    private int playerNumber;
    Protocol protocol = new Protocol();
    DAO dao = new DAO();

    public ServerThreaded(Socket socket, int playerNumber) {
        this.socket = socket;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            String clientMessage;

            while ((clientMessage = in.readLine()) != null) {
                if (clientMessage.startsWith("category")) {
                    sendNextQuestion(clientMessage);
                } else if (clientMessage.startsWith("NEXT_QUESTION")) {
                    sendNextQuestion(clientMessage);
                } else {
                    sendResponse(clientMessage);
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNextQuestion(String category) {
        QuestionManager questionManager = new QuestionManager(category);
        QuestionAndAnswers response = questionManager.getNextQuestion();
        if (response != null) {
            String question = response.getQuestion();
            String answers = response.getAnswers();
            String responseStr = question + "|" + answers;
            out.println(responseStr);
        } else {
            System.err.println("Okänt fel");
            /*När alla frågor för rundan har ställts.
              Här kanske man kan se till att det blir nästa
              spelares tur att svara på frågorna.
             */
        }
    }

    private void sendResponse(String message) {
        out.println(message);
        out.flush();
    }


    public class QuestionManager {
        private final ArrayList<QuestionAndAnswers> poolOfQuestions;
        private int currentQuestionIndex;

        public QuestionManager(String category) {
            this.poolOfQuestions = getQuestionPool(category);
            this.currentQuestionIndex = -1;
        }

        private ArrayList<QuestionAndAnswers> getQuestionPool(String category) {
            //Metod för att hämta ett visst antal frågor&svar
            ArrayList<QuestionAndAnswers> poolOfQuestions = new ArrayList<>();

            PropertiesClass propertiesClass = new PropertiesClass();
            propertiesClass.loadProperties();
            int questionsEachRound = propertiesClass.getAmountOfQuestions();

            for (int i = 0; i < questionsEachRound; i++) {
                poolOfQuestions.add(protocol.getOutput(category));
            }
            return poolOfQuestions;
        }

        public QuestionAndAnswers getNextQuestion() { //Går igenom frågor&svar från questionPool
            if (currentQuestionIndex < poolOfQuestions.size()) {
                return poolOfQuestions.get(currentQuestionIndex++);
            } else {
                return null;
            }
        }
    }
}
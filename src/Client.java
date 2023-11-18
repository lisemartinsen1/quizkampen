import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements ActionListener {
    Integer[] numberRounds = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Integer[] numberQuestions = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    DefaultComboBoxModel<Integer> numberRoundsModel = new DefaultComboBoxModel<>(numberRounds);
    DefaultComboBoxModel<Integer> numberQuestionsModel = new DefaultComboBoxModel<>(numberQuestions);
    private JFrame mainFrame = new JFrame("Quizkampen");
    private JPanel titlePanel = new JPanel();
    private JLabel gameTitle = new JLabel("Quizkampen");
    private JPanel howManyPanel = new JPanel();
    private JLabel howManyRounds = new JLabel("Antal ronder: ");
    private JComboBox<Integer> howManyRoundsBox = new JComboBox<>(numberRoundsModel);
    private JLabel howManyQuestions = new JLabel("Antal frågor: ");
    private JComboBox<Integer> howManyQuestionsBox = new JComboBox<>(numberQuestionsModel);
    private JPanel bottomPanel = new JPanel();
    private JButton newGame = new JButton("Nytt Spel");
    private JButton quitGame = new JButton("Avsluta");
    private JFrame questionsFrame = new JFrame();
    private JPanel questionPanel = new JPanel();
    private JLabel questionText = new JLabel();
    private JPanel answerPanel = new JPanel();
    private JButton answerOne = new JButton("Svar ett");
    private JButton answerTwo = new JButton("Svar två");
    private JButton answerThree = new JButton("Svar tre");
    private JButton answerFour = new JButton("Svar fyra");


    PrintWriter out;
    BufferedReader in;

    public Client() {
    }

    public void mainUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(240, 160);
        mainFrame.setVisible(true);
        mainFrame.setResizable(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(howManyPanel, BorderLayout.WEST);
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);
        howManyPanel.setLayout(new GridLayout(2, 2));


        titlePanel.add(gameTitle);

        howManyPanel.add(howManyRounds);
        howManyPanel.add(howManyRoundsBox);
        howManyPanel.add(howManyQuestions);
        howManyPanel.add(howManyQuestionsBox);

        bottomPanel.add(newGame);
        bottomPanel.add(quitGame);
        mainFrame.pack();

        newGame.addActionListener(this);
        quitGame.addActionListener(this);
    }

    public void questionsUI() {
        questionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        questionsFrame.setSize(350, 300);
        questionsFrame.setVisible(true);
        questionsFrame.setResizable(true);
        questionsFrame.setLocationRelativeTo(null);
        questionsFrame.setLayout(new BorderLayout());
        questionsFrame.add(questionPanel, BorderLayout.NORTH);
        questionsFrame.add(answerPanel, BorderLayout.SOUTH);

        questionPanel.add(questionText);
        answerPanel.setLayout(new GridLayout(2,2));
        answerPanel.add(answerOne);
        answerPanel.add(answerTwo);
        answerPanel.add(answerThree);
        answerPanel.add(answerFour);

        out.println("START");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newGame) {
            connectToServer();
            readResponseFromServer();
            mainFrame.dispose();
            questionsUI();

        }else if (e.getSource() == quitGame){


        }
    }

    public static void main(String[] args){
        Client client = new Client();
        client.mainUI();
    }

    public void connectToServer() {
        try {
            Socket sock = new Socket("127.0.0.1", 12345);
            out = new PrintWriter(sock.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void readResponseFromServer() {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    String[] parts = fromServer.split("\\|");
                    if (parts.length == 2) {

                        String questionAndAnswersText = parts[0].trim();
                        String answersTextfromServer = parts[1].trim();

                        String[] questionParts = questionAndAnswersText.split("QUESTION", 2);
                        if (questionParts.length > 1) {
                            String questionTextfromServerToLabel = questionParts[1].trim();
                            SwingUtilities.invokeLater(() -> {
                                questionText.setText(questionTextfromServerToLabel);
                            });
                        }

                        String[] answersParts = answersTextfromServer.split("ANSWERS", 2);
                        if (answersParts.length > 1) {
                            String answersText = answersParts[1].trim();
                            String[] individualAnswers = answersText.split(", ");
                            answerOne.setText(individualAnswers[0]);
                            answerTwo.setText(individualAnswers[1]);
                            answerThree.setText(individualAnswers[2]);
                            answerFour.setText(individualAnswers[3]);

                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

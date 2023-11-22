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
import java.util.*;
import java.util.List;


public class Client implements ActionListener {
    private int currentRound = 1;
    private int currentQuestion = 2;
    private int totalRounds = 4;
    private int questionsPerRound;
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
    private JPanel bottomQuestionPanel = new JPanel();
    private JButton newGame = new JButton("Nytt Spel");
    private JButton quitGame = new JButton("Avsluta");
    private JButton nextQuestionButton = new JButton("Nästa Fråga");
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
        answerOne.addActionListener(this);
        answerTwo.addActionListener(this);
        answerThree.addActionListener(this);
        answerFour.addActionListener(this);
    }

    public void questionsUI() {
        questionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        questionsFrame.setSize(350, 300);
        questionsFrame.setVisible(true);
        questionsFrame.setResizable(true);
        questionsFrame.setLocationRelativeTo(null);
        questionsFrame.setLayout(new BorderLayout());
        questionsFrame.add(questionPanel, BorderLayout.NORTH);
        questionsFrame.add(answerPanel, BorderLayout.CENTER);


        bottomQuestionPanel.add(nextQuestionButton);
        questionsFrame.add(bottomQuestionPanel,BorderLayout.SOUTH);
        nextQuestionButton.addActionListener(this);

        questionPanel.add(questionText);
        answerPanel.setLayout(new GridLayout(2, 2));


        List<JButton> answerButtons = Arrays.asList(answerOne, answerTwo, answerThree, answerFour);


        Collections.shuffle(answerButtons);

        answerButtons.forEach(button -> {
            answerPanel.add(button);
            button.addActionListener(this);
        });

        out.println("START");
    }

    @Override
    public void actionPerformed(ActionEvent e) {    //Uppdaterat actionPerformed lite
        if (e.getSource() == newGame) {
            connectToServer();
            readResponseFromServer();
            mainFrame.dispose();
            questionsUI();

        } else if (e.getSource() == quitGame) {

        } else if (e.getSource() == nextQuestionButton) {
            currentQuestion++;

            if(currentQuestion > questionsPerRound){
                currentQuestion = 1;
                currentRound++;
                if(currentRound > totalRounds){
                    questionsFrame.dispose();
                    mainUI();
                }
            }

            out.println("NEXT_QUESTION");
            answerOne.setBackground(null);
            answerTwo.setBackground(null);
            answerThree.setBackground(null);
            answerFour.setBackground(null);

            answerOne.setEnabled(true);
            answerTwo.setEnabled(true);
            answerThree.setEnabled(true);
            answerFour.setEnabled(true);
        }

                if (e.getSource() == answerOne) {
                    answerOne.setBackground(Color.RED);
                    answerTwo.setEnabled(false);
                    answerThree.setEnabled(false);
                    answerFour.setEnabled(false);

                } else if (e.getSource() == answerTwo) {
                    answerTwo.setBackground(Color.RED);
                    answerOne.setEnabled(false);
                    answerThree.setEnabled(false);
                    answerFour.setEnabled(false);

                } else if (e.getSource() == answerThree) {
                    answerThree.setBackground(Color.RED);
                    answerOne.setEnabled(false);
                    answerTwo.setEnabled(false);
                    answerFour.setEnabled(false);

                } else if (e.getSource() == answerFour) {
                    answerFour.setBackground(Color.GREEN);
                    answerOne.setEnabled(false);
                    answerTwo.setEnabled(false);
                    answerThree.setEnabled(false);

                }


        }


    public static void main(String[] args) {
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

                            String rightAnswer = "";
                            int rightAnswerIndex = -1;

                            for (int i = 0; i < individualAnswers.length; i++) {
                                String answer = individualAnswers[i];
                                if (answer.startsWith("RIGHT_ANSWER")) {
                                    rightAnswer = answer.replace("RIGHT_ANSWER", "").trim();
                                    rightAnswerIndex = i;
                                    break;
                                }
                            }

                            if (rightAnswerIndex != -1) {

                                List<String> answersList = new ArrayList<>(Arrays.asList(individualAnswers));
                                answersList.remove(rightAnswerIndex);

                                String finalRightAnswer = rightAnswer;
                                SwingUtilities.invokeLater(() -> {
                                    answerOne.setText(answersList.get(0));
                                    answerTwo.setText(answersList.get(1));
                                    answerThree.setText(answersList.get(2));
                                    answerFour.setText(finalRightAnswer);

                                });
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    }

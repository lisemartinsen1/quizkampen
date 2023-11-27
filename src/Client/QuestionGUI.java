package Client;
import Server.PropertiesClass;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuestionGUI extends JFrame implements ActionListener {
    private JFrame questionsFrame = new JFrame();
    private JPanel questionPanel = new JPanel();
    private JPanel bottomQuestionPanel = new JPanel();
    private JLabel questionText = new JLabel();
    private JPanel answerPanel = new JPanel();
    private JButton answerOne = new JButton("Svar ett");
    private JButton answerTwo = new JButton("Svar två");
    private JButton answerThree = new JButton("Svar tre");
    private JButton answerFour = new JButton("Svar fyra");
    private JButton nextQuestionButton = new JButton("Nästa Fråga");
    private List<JButton> answerButtons;
    PropertiesClass propertiesClass = new PropertiesClass();
    private int currentRound = 1;
    private int currentQuestion = 1;
    private int howManyPointsInRound;
    BufferedReader in;
    PrintWriter out;
    int questionsPerRound;
    String playerNr;
    public QuestionGUI(BufferedReader in, PrintWriter out, String playerNr) {
        this.in = in;
        this.out = out;
        this.playerNr = playerNr;

        SwingUtilities.invokeLater(() -> {
            questionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            questionsFrame.setSize(640, 480);
            questionsFrame.setVisible(true);
            questionsFrame.setResizable(true);
            questionsFrame.setLocationRelativeTo(null);
            questionsFrame.setLayout(new BorderLayout());
            questionsFrame.add(questionPanel, BorderLayout.NORTH);
            questionsFrame.add(answerPanel, BorderLayout.CENTER);
            questionsFrame.setTitle(playerNr);
            bottomQuestionPanel.add(nextQuestionButton);
            nextQuestionButton.setEnabled(false);
            questionsFrame.add(bottomQuestionPanel, BorderLayout.SOUTH);
            nextQuestionButton.addActionListener(this);

            questionPanel.add(questionText);
            answerPanel.setLayout(new GridLayout(2, 2));

            answerButtons = Arrays.asList(answerOne, answerTwo, answerThree, answerFour);

            Collections.shuffle(answerButtons);

            answerButtons.forEach(button -> {
                answerPanel.add(button);
                button.addActionListener(this);
            });

        });
        readFromServer();
    }
    public void readFromServer(){
        int questionsRead = 0;
        try {
            String fromServer;

            while (questionsRead < propertiesClass.getAmountOfQuestions() && (fromServer = in.readLine()) != null) {
                System.out.println(fromServer);
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

                            questionsRead++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextQuestionButton) {
            nextQuestionButton.setEnabled(false);
            propertiesClass.loadProperties();
            int totalRounds = propertiesClass.getAmountOfRounds();
            questionsPerRound = propertiesClass.getAmountOfQuestions();

            currentQuestion++;

            if (currentQuestion > questionsPerRound) {

                if (currentRound == totalRounds) { //Kommer currentRound någonsin bli större än totalRounds?
                    out.println("GAME_FINISHED");
                    System.out.println("GAME_FIN");
                    questionsFrame.dispose();
                } else {
                    currentQuestion = 1; //Nollställer
                    currentRound++;
                    out.println("OPEN_RESULT");
                    out.flush();
                    questionsFrame.dispose();
                }

            } else {
                out.println("NEXT_QUESTION");
                answerOne.setBackground(null);
                answerTwo.setBackground(null);
                answerThree.setBackground(null);
                answerFour.setBackground(null);

                answerOne.setEnabled(true);
                answerTwo.setEnabled(true);
                answerThree.setEnabled(true);
                answerFour.setEnabled(true);

                System.out.println("Nuvarande Poäng i rundan: " + howManyPointsInRound);
                Collections.shuffle(answerButtons);

                answerButtons.forEach(button -> {
                    answerPanel.add(button);
                });
            }

        } else if (e.getSource() == answerOne) {
            answerOne.setBackground(Color.RED);
            answerOne.setEnabled(false);
            answerTwo.setEnabled(false);
            answerThree.setEnabled(false);
            answerFour.setEnabled(false);
            nextQuestionButton.setEnabled(true);
            if (currentQuestion == questionsPerRound) {
                nextQuestionButton.setText("Visa resultat");
            }

        } else if (e.getSource() == answerTwo) {
            answerTwo.setBackground(Color.RED);
            answerOne.setEnabled(false);
            answerTwo.setEnabled(false);
            answerThree.setEnabled(false);
            answerFour.setEnabled(false);
            nextQuestionButton.setEnabled(true);
            if (currentQuestion == questionsPerRound) {
                nextQuestionButton.setText("Visa resultat");
            }

        } else if (e.getSource() == answerThree) {
            answerThree.setBackground(Color.RED);
            answerOne.setEnabled(false);
            answerTwo.setEnabled(false);
            answerThree.setEnabled(false);
            answerFour.setEnabled(false);
            nextQuestionButton.setEnabled(true);
            if (currentQuestion == questionsPerRound) {
                nextQuestionButton.setText("Visa resultat");
            }

        } else if (e.getSource() == answerFour) {
            answerFour.setBackground(Color.GREEN);
            answerOne.setEnabled(false);
            answerTwo.setEnabled(false);
            answerThree.setEnabled(false);
            answerFour.setEnabled(false);
            nextQuestionButton.setEnabled(true);
            if (currentQuestion == questionsPerRound) {
                nextQuestionButton.setText("Visa resultat");
            }

            howManyPointsInRound++;
        }
    }
}

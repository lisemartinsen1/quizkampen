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
import java.util.Timer;

public class QuestionGUI extends JFrame implements ActionListener {
    private final JFrame questionsFrame = new JFrame();
    private final JPanel questionPanel = new JPanel();
    private final JPanel bottomQuestionPanel = new JPanel();
    private final JLabel questionText = new JLabel();
    private final JPanel answerPanel = new JPanel();
    private final JButton answerOne = new JButton("Svar ett");
    private final JButton answerTwo = new JButton("Svar två");
    private final JButton answerThree = new JButton("Svar tre");
    private final JButton answerFour = new JButton("Svar fyra");
    private final JButton nextQuestionButton = new JButton("Nästa Fråga");
    private final JButton giveUpButton = new JButton("Ge Upp");
    private List<JButton> answerButtons;
    PropertiesClass propertiesClass = new PropertiesClass();
    private final int currentRound;
    private int currentQuestion = 1;
    private int scoreTracker;
    private final BufferedReader in;
    private final PrintWriter out;
    private int questionsPerRound;
    private final String playerNr;
    private final int defaultTime = 15;
    private int time;
    private final JLabel timer = new JLabel("Timer");
    private final Thread timerThread;
    Color background;
    Color font;

    Font fontOp = new Font(Font.SANS_SERIF, Font.BOLD, 12);

    public QuestionGUI(BufferedReader in, PrintWriter out, String playerNr, String currentRound, Color background, Color font) {
        this.in = in;
        this.out = out;
        this.playerNr = playerNr;
        this.currentRound = Integer.parseInt(currentRound);
        this.background = background;
        this.font = font;

        SwingUtilities.invokeLater(() -> {
            questionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            questionsFrame.setSize(800, 480);
            questionsFrame.setVisible(true);
            questionsFrame.setResizable(true);
            questionsFrame.setLocationRelativeTo(null);
            questionsFrame.setLayout(new BorderLayout());
            questionsFrame.add(questionPanel, BorderLayout.NORTH);
            questionsFrame.add(answerPanel, BorderLayout.CENTER);
            questionsFrame.setTitle(playerNr);
            giveUpButton.addActionListener(this);
            bottomQuestionPanel.add(giveUpButton);


            bottomQuestionPanel.add(timer);
            bottomQuestionPanel.add(nextQuestionButton);
            nextQuestionButton.setEnabled(false);
            questionsFrame.add(bottomQuestionPanel, BorderLayout.SOUTH);
            nextQuestionButton.addActionListener(this);

            questionPanel.add(questionText);
            questionPanel.setBackground(Color.WHITE);
            answerPanel.setLayout(new GridLayout(2, 2));

            answerButtons = Arrays.asList(answerOne, answerTwo, answerThree, answerFour);

            Collections.shuffle(answerButtons);

            answerButtons.forEach(button -> {
                answerPanel.add(button);
                button.setBackground(background);
                button.setForeground(font);
                button.setFont(fontOp);
                button.addActionListener(this);
            });

        });
        time = defaultTime;
        timerThread = new Thread(this::Timer);
        timerThread.start();
        readFromServer();

    }

    public void readFromServer() {
        int questionsRead = 0;
        try {
            String fromServer;

            while (questionsRead < propertiesClass.getAmountOfQuestions() && (fromServer = in.readLine()) != null) {

                String[] parts = fromServer.split("\\|");
                if (parts.length == 2) {
                    String questionAndAnswersText = parts[0].trim();
                    String answersTextfromServer = parts[1].trim();

                    String[] questionParts = questionAndAnswersText.split("QUESTION", 2);
                    if (questionParts.length > 1) {
                        String questionTextfromServerToLabel = questionParts[1].trim();
                        SwingUtilities.invokeLater(() -> {
                            questionText.setText(questionTextfromServerToLabel);
                            questionText.setFont(fontOp);

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
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    private void ToNextQuestion() {

        nextQuestionButton.setEnabled(false);
        propertiesClass.loadProperties();
        int totalRounds = propertiesClass.getAmountOfRounds();
        questionsPerRound = propertiesClass.getAmountOfQuestions();

        currentQuestion++;

        if (currentQuestion > questionsPerRound) {

            if (currentRound == totalRounds) {

                out.println(scoreTracker + "#GAME_FINISHED");
            } else {

                currentQuestion = 1; //Nollställer
                out.println(scoreTracker + "#OPEN_RESULT");
                out.flush();
            }
            questionsFrame.dispose();
            timerThread.interrupt();

        } else {
            out.println("NEXT_QUESTION");
            time = defaultTime;
            answerOne.setBackground(background);
            answerTwo.setBackground(background);
            answerThree.setBackground(background);
            answerFour.setBackground(background);
            answerOne.setForeground(font);
            answerTwo.setForeground(font);
            answerThree.setForeground(font);
            answerFour.setForeground(font);

            answerOne.setEnabled(true);
            answerTwo.setEnabled(true);
            answerThree.setEnabled(true);
            answerFour.setEnabled(true);

            Collections.shuffle(answerButtons);

            answerButtons.forEach(answerPanel::add);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextQuestionButton) {
            ToNextQuestion();

        } else if (e.getSource() == answerOne) {
            answerOne.setBackground(Color.RED);
            answerFour.setBackground(Color.GREEN);
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
            answerFour.setBackground(Color.GREEN);
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
            answerFour.setBackground(Color.GREEN);
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

            scoreTracker++;
        }
        if (e.getSource() == giveUpButton) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Är du säker?",
                    "Bekräfta avslut", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                out.println("GIVE_UP-" + playerNr);
                questionsFrame.dispose();
            }
        }
    }

    private void Timer() {
        try {
            while (time >= -1) {
                SwingUtilities.invokeLater(() -> {
                    timer.setText("Timer: " + time);
                });
                Thread.sleep(1000);
                time--;
                if (time == -1) {
                    ToNextQuestion();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("");
        }
    }

}

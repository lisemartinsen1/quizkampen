package Client;
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
import Server.PropertiesClass;

public class Client implements ActionListener {
    private int howManyPointsInRound;
    private JFrame mainFrame = new JFrame("Quizkampen");
    private JPanel titlePanel = new JPanel();
    private JLabel gameTitle = new JLabel("Quizkampen");
    private JPanel howManyPanel = new JPanel();
    private JLabel howManyRounds = new JLabel();
    private JLabel howManyQuestions = new JLabel();
    private JPanel bottomPanel = new JPanel();
    private JButton newGame = new JButton("Nytt Spel");
    private JButton quitGame = new JButton("Avsluta");
    private String currentRound;

    PrintWriter out;
    BufferedReader in;
    String player;

    PropertiesClass propertiesClass = new PropertiesClass();

    public Client() {
    //Gamla MainUI ligger nu i konstruktorn för Client.Client


        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(640, 480);
            mainFrame.setVisible(true);
            mainFrame.setResizable(true);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setLayout(new BorderLayout());
            mainFrame.add(titlePanel, BorderLayout.NORTH);
            mainFrame.add(howManyPanel, BorderLayout.CENTER); //Varför hamnar den inte i mitten?
            mainFrame.add(bottomPanel, BorderLayout.SOUTH);
            howManyPanel.setLayout(new GridLayout(2, 1));
            titlePanel.add(gameTitle);

            propertiesClass.loadProperties();
            int amountOfRounds = propertiesClass.getAmountOfRounds();
            int amountOfQuestions = propertiesClass.getAmountOfQuestions();
            howManyRounds.setText("Antal ronder: " + amountOfRounds);
            howManyQuestions.setText("Antal frågor/rond: " + amountOfQuestions);

            howManyPanel.add(howManyRounds);
            howManyPanel.add(howManyQuestions);

            bottomPanel.add(newGame);
            bottomPanel.add(quitGame);

            newGame.addActionListener(this);
            quitGame.addActionListener(this);
        });

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

    @Override
    public void actionPerformed(ActionEvent e) {    //Uppdaterat actionPerformed lite

        if (e.getSource() == newGame) {
            connectToServer();
            startGame();
            mainFrame.setTitle("Waiting for player...");
            newGame.setEnabled(false);

        } else if (e.getSource() == quitGame) {
            System.exit(0);

        }
    }

    public static void main(String[] args) {
        Client client = new Client();

    }

    private void resetMainFrame(){
        mainFrame.getContentPane().removeAll();
        mainFrame.repaint();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(640, 480);
        mainFrame.setVisible(true);
        mainFrame.setResizable(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(titlePanel, BorderLayout.NORTH);
        mainFrame.add(howManyPanel, BorderLayout.CENTER); //Varför hamnar den inte i mitten?
        mainFrame.add(bottomPanel, BorderLayout.SOUTH);
        howManyPanel.setLayout(new GridLayout(2, 1));
        titlePanel.add(gameTitle);

        propertiesClass.loadProperties();
        int amountOfRounds = propertiesClass.getAmountOfRounds();
        int amountOfQuestions = propertiesClass.getAmountOfQuestions();
        howManyRounds.setText("Antal ronder: " + amountOfRounds);
        howManyQuestions.setText("Antal frågor/rond: " + amountOfQuestions);

        howManyPanel.add(howManyRounds);
        howManyPanel.add(howManyQuestions);

        bottomPanel.add(newGame);
        bottomPanel.add(quitGame);

        newGame.addActionListener(this);
        quitGame.addActionListener(this);
        mainFrame.revalidate();
    }

    public String getScoreStr1(String message) {
        String[] parts = message.split("\\|");
        System.out.println(parts[0]);
        return parts[0];
    }

    public String getScoreStr2(String message) {
        String[] parts = message.split("\\|");
        System.out.println(parts[1]);
        return parts[1];
    }

    public String getCurrentRound(String message){
        String[] parts = message.split("\\|");
        System.out.println(parts[1]);
        return parts[1];
//
    }
    public void startGame() {
        new Thread(() -> {
            try {
                String fromServer;
                String scoreStr1;
                String scoreStr2;


                while ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);

                    if (fromServer.contains("START")) {

                        if (fromServer.equals("START")) {
                            mainFrame.dispose();
                            CategoryGUI categoryGUI = new CategoryGUI(out, player, "", "");
                        } else {
                            scoreStr1 = getScoreStr1(fromServer);
                            scoreStr2 = getScoreStr2(fromServer);
                            mainFrame.dispose();
                            CategoryGUI categoryGUI = new CategoryGUI(out, player, scoreStr1, scoreStr2);
                        }

                    } else if (fromServer.startsWith("WAIT")) {
                        mainFrame.setTitle("PLAYER 2\tWaiting for player to complete round...");

                    } else if (fromServer.startsWith("PLAYER")) {
                        player = fromServer;

                    } else if (fromServer.startsWith("QUESTIONS")) {
                        currentRound = getCurrentRound(fromServer);

                        QuestionGUI questionGUI = new QuestionGUI(in, out, player, currentRound);


                    } else if (fromServer.contains("OPEN_RESULT")) {
                        System.out.println(fromServer + " received in Client from ServerThr");

                        scoreStr1 = getScoreStr1(fromServer);
                        scoreStr2 = getScoreStr2(fromServer);

                        ResultGUI resultGUI = new ResultGUI(out, player, scoreStr1, scoreStr2);
                    } else if (fromServer.contains("OPEN_LAST_RESULT")) {
                        scoreStr1 = getScoreStr1(fromServer);
                        scoreStr2 = getScoreStr2(fromServer);

                        ResultGUI resultGUI = new ResultGUI(out, player, scoreStr1, scoreStr2);
                        resultGUI.disablePlayButton();
                }else if (fromServer.startsWith("OPPONENT_GAVE_UP")) {
                        SwingUtilities.invokeLater(() -> {
                            mainFrame.dispose();
                            JOptionPane.showMessageDialog(mainFrame, "Your opponent gave up. You win!");
                            resetMainFrame();
                            mainFrame.setVisible(true);
                        });
                        break;
                    } else if (fromServer.startsWith("OPPONENT_DONE")) {
                        mainFrame.dispose();
                        currentRound = getCurrentRound(fromServer);
                        QuestionGUI questionGUI = new QuestionGUI(in, out, player, currentRound);

                        //När spelare 1 är klar m sista rundan vore det bättre att personen hamnar i ett
                        //väntrum, då behöver resultatet inte uppdateras när spelare 2 är klar.
                    } else if (fromServer.startsWith("GAME_PLAYERONE_FINISHED")) {
                        mainFrame.dispose();
                        currentRound = getCurrentRound(fromServer);
                        QuestionGUI questionGUI = new QuestionGUI(in, out, player, currentRound);

                } else if (fromServer.contains("GAME_FINISHED")) {
                        scoreStr1 = getScoreStr1(fromServer);
                        scoreStr2 = getScoreStr2(fromServer);

                     ResultGUI resultGUI = new ResultGUI(out, player, scoreStr1, scoreStr2);
                     resultGUI.showFinalResult();
                     resultGUI.disablePlayButton();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    }

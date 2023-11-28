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

    private String namn;
    private String time;


    PrintWriter out;
    BufferedReader in;

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
            mainFrame.setTitle("Waiting for player...");

        } else if (e.getSource() == quitGame) {
            System.exit(0);

        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.startGame();

    }

    public void startGame() {
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    System.out.println(fromServer);
                    if (fromServer.equals("START")) {
                        mainFrame.dispose();
                        CategoryGUI categoryGUI = new CategoryGUI(out);

                    } else if (fromServer.equals("WAIT")) {
                        mainFrame.setTitle("Waiting for player to complete round...");


                    } else if (fromServer.startsWith("QUESTIONS")) {
                        QuestionGUI questionGUI = new QuestionGUI(in, out, namn);

                    } else if (fromServer.equals("ALL_QUESTIONS_ANSWERED")) {
                        System.out.println(fromServer + " received in Client from ServerThr"); //Kommer aldrig hit
                        ResultGUI resultGUI = new ResultGUI(out);

                    } else if (fromServer.startsWith("OPPONENT_DONE")) {
                        mainFrame.dispose();
                        QuestionGUI questionGUI = new QuestionGUI(in, out, namn);

                    } else if (fromServer.startsWith("GAME_FINISHED")) {
                        ResultGUI resultGUI = new ResultGUI(out);
                        resultGUI.disablePlayButton();
                    } else if (fromServer.startsWith("PLAYER")) {
                        namn = fromServer;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    }

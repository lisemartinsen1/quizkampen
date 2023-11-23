import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
    private JFrame resultFrame = new JFrame();
    private JPanel resultUpperPanel = new JPanel(new GridBagLayout());
    private JPanel resultCenterPanel = new JPanel(new GridBagLayout());
    private JPanel resultBottomPanel = new JPanel();
    private JLabel whosTurnText = new JLabel("Din tur/Motståndarens tur");
    private JLabel resultPreviousRounds = new JLabel("1-3");
    private JLabel playerOneName = new JLabel("Spelare 1");
    private JLabel playerTwoName = new JLabel("Spelare 2");
    private JButton playButton = new JButton("Spela");

    PrintWriter out;
    BufferedReader in;

    PropertiesClass propertiesClass = new PropertiesClass();
    private int currentRound = 1;
    private int currentQuestion = 1;

    public Client() {
    //Gamla MainUI ligger nu i konstruktorn för Client

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

            connectToServer();
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

    public void resultUI(){
        SwingUtilities.invokeLater(() -> {
            resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            resultFrame.setSize(640, 480);
            resultFrame.setVisible(true);
            resultFrame.setLocationRelativeTo(null);
            resultFrame.setLayout(new BorderLayout());
            resultFrame.add(resultUpperPanel, BorderLayout.NORTH);
            resultFrame.add(resultCenterPanel, BorderLayout.CENTER);
            resultFrame.add(resultBottomPanel, BorderLayout.SOUTH);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.HORIZONTAL;
            //ändra left/right för distansen mellan spelar1/2 och vems tur texten
            constraints.insets = new Insets(5, 50, 5, 50);

            constraints.gridy = 0;
            constraints.gridx = 1;
            resultUpperPanel.add(playerOneName, constraints);

            constraints.gridx = 2;
            resultUpperPanel.add(whosTurnText, constraints);

            constraints.gridx = 3;
            resultUpperPanel.add(playerTwoName, constraints);

            constraints.gridy++;
            constraints.gridx = 2;
            resultPreviousRounds.setBorder(new EmptyBorder(0, 60, 0, 0));
            resultUpperPanel.add(resultPreviousRounds, constraints);

            //generar text för så många rundor det är satt i properties filen


            propertiesClass.loadProperties();
            int numberOfRounds = propertiesClass.getAmountOfRounds();
            JLabel[][] labels = new JLabel[numberOfRounds][4];
            for (int i = 1; i <= numberOfRounds; i++) {
                JLabel roundLabel1 = new JLabel("Runda " + i);
                JLabel pointsLabel1 = new JLabel("Poäng " + i);
                JLabel roundLabel2 = new JLabel("Runda " + i);
                JLabel pointsLabel2 = new JLabel("Poäng " + i);
                JLabel fillLabel1 = new JLabel("-");
                JLabel fillLabel2 = new JLabel("-");

                constraints.gridy++;
                constraints.gridx = 0;
                resultCenterPanel.add(roundLabel1, constraints);
                labels[i - 1][0] = roundLabel1;
                constraints.gridx = 1;
                resultCenterPanel.add(fillLabel1, constraints);

                constraints.gridx = 2;
                resultCenterPanel.add(roundLabel2, constraints);
                labels[i - 1][1] = roundLabel2;

                constraints.gridy++;
                constraints.gridx = 0;
                resultCenterPanel.add(pointsLabel1, constraints);
                labels[i - 1][2] = pointsLabel1;
                constraints.gridx = 1;
                resultCenterPanel.add(fillLabel2, constraints);
                constraints.gridx = 2;
                resultCenterPanel.add(pointsLabel2, constraints);
                labels[i - 1][3] = pointsLabel2;
            }

            resultBottomPanel.add(playButton);
            playButton.setEnabled(false);
            whosTurnText.setText("Motståndarens tur");
            labels[0][2].setText(String.valueOf(howManyPointsInRound));

        });
    }



    @Override
    public void actionPerformed(ActionEvent e) {    //Uppdaterat actionPerformed lite

        if (e.getSource() == newGame) {
            connectToServer();
            startGame();
            mainFrame.setTitle("Waiting for player...");
            //mainFrame.dispose();
            //categoryUI();
            //questionsUI();

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

                    if (fromServer.equals("START")) {
                        mainFrame.dispose();
                        CategoryGUI categoryGUI = new CategoryGUI(out);
                        mainFrame.add(categoryGUI);
                        mainFrame.revalidate();
                        mainFrame.repaint();

                    } else if (fromServer.equals("WAIT")) {
                        mainFrame.setTitle("Waiting for player to complete round...");

                    } else if (fromServer.startsWith("CATEGORY")) {
                        mainFrame.dispose();
                        QuestionGUI questionGUI = new QuestionGUI(in, out);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    }

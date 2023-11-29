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
    ImageIcon imgIcon = new ImageIcon("src/Pics/Banner.jpg");
    private Font mainFrameFont = new Font("Ariel", Font.ITALIC, 45);
    private JPanel titlePanel = new JPanel();
    private JLabel gameTitle = new JLabel();
    private JPanel howManyPanel = new JPanel();
    private JPanel amountOfRoundPanel = new JPanel();
    private JPanel colorPanel = new JPanel();
    private JPanel colorInsideColorPanel = new JPanel();
    private JLabel bgColor = new JLabel("Välj bakgrundsfärg:");
    private JLabel fColor = new JLabel("Välj font färg:");
    private Color backgroundColor = Color.white;
    private Color fontColor = Color.BLACK;
    private JPanel examplePanel = new JPanel();
    private JLabel exampleText = new JLabel("Example");
    private JLabel howManyRounds = new JLabel();
    private JLabel howManyQuestions = new JLabel();
    private JPanel bottomPanel = new JPanel();
    private JButton newGame = new JButton("Nytt Spel");
    private JButton quitGame = new JButton("Avsluta");
    private JComboBox<String> colorChoose = new JComboBox<>();
    private JComboBox<String> fontColorChoose = new JComboBox<>();
    private String namn;
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
            howManyPanel.setLayout(new GridLayout(1, 2));
            titlePanel.add(gameTitle);
            gameTitle.setIcon(imgIcon);
            //gameTitle.setFont(mainFrameFont);
            titlePanel.setBackground(Color.BLACK);

            propertiesClass.loadProperties();
            int amountOfRounds = propertiesClass.getAmountOfRounds();
            int amountOfQuestions = propertiesClass.getAmountOfQuestions();
            howManyRounds.setText("Antal ronder: " + amountOfRounds);
            howManyQuestions.setText("Antal frågor/rond: " + amountOfQuestions);

            howManyPanel.add(amountOfRoundPanel);
            howManyPanel.add(colorPanel);
            amountOfRoundPanel.setLayout(new GridLayout(2, 1));
            amountOfRoundPanel.add(howManyRounds);
            amountOfRoundPanel.add(howManyQuestions);
            colorPanel.setLayout(new GridLayout(2, 1));
            colorPanel.add(colorInsideColorPanel);
            colorPanel.add(examplePanel);
            colorInsideColorPanel.setLayout(new GridLayout(4, 1));
            colorInsideColorPanel.add(bgColor);
            ColorBox(colorChoose);
            colorInsideColorPanel.add(fColor);
            FontColorBox(fontColorChoose);
            examplePanel.setLayout(new FlowLayout());
            examplePanel.setBackground(Color.WHITE);
            examplePanel.add(exampleText);
            exampleText.setForeground(Color.BLACK);

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

    private void setBgColor(Color color) {
        backgroundColor = color;
    }

    private void setFontColor(Color color) {
        fontColor = color;
    }
    @Override
    public void actionPerformed(ActionEvent e) {    //Uppdaterat actionPerformed lite

        if (e.getSource() == newGame) {
            mainFrame.setTitle("Waiting for player...");

        } else if (e.getSource() == quitGame) {
            System.exit(0);

        }
        if (colorChoose.getSelectedIndex() == 0) {
            examplePanel.setBackground(Color.white);
            setBgColor(Color.white);
        } else if (colorChoose.getSelectedIndex() == 1) {
            examplePanel.setBackground(Color.BLACK);
            setBgColor(Color.BLACK);
        } else if (colorChoose.getSelectedIndex() == 2) {
            examplePanel.setBackground(Color.BLUE);
            setBgColor(Color.BLUE);
        } else if (colorChoose.getSelectedIndex() == 3) {
            examplePanel.setBackground(Color.PINK);
            setBgColor(Color.PINK);
        } else if (colorChoose.getSelectedIndex() == 4) {
            examplePanel.setBackground(Color.CYAN);
            setBgColor(Color.CYAN);
        }
        if (fontColorChoose.getSelectedIndex() == 0) {
            exampleText.setForeground(Color.BLACK);
            setFontColor(Color.BLACK);
        } else if (fontColorChoose.getSelectedIndex() == 1) {
            exampleText.setForeground(Color.WHITE);
            setFontColor(Color.WHITE);
        } else if (fontColorChoose.getSelectedIndex() == 2) {
            exampleText.setForeground(Color.BLUE);
            setFontColor(Color.BLUE);
        } else if (fontColorChoose.getSelectedIndex() == 3) {
            exampleText.setForeground(Color.PINK);
            setFontColor(Color.PINK);
        } else if (fontColorChoose.getSelectedIndex() == 4) {
            exampleText.setForeground(Color.CYAN);
            setFontColor(Color.CYAN);
        }
    }
    private void ColorBox(JComboBox<String> colorChoose) {
        colorInsideColorPanel.add(colorChoose);
        colorChoose.addItem("Vit(default)");
        colorChoose.addItem("Svart");
        colorChoose.addItem("Blå");
        colorChoose.addItem("Rosa");
        colorChoose.addItem("Cyan");

        colorChoose.setPreferredSize(new Dimension(100, 30));
        colorChoose.addActionListener(this);
    }
    private void FontColorBox(JComboBox<String> colorChoose) {
        colorInsideColorPanel.add(colorChoose);
        colorChoose.addItem("Svart(default)");
        colorChoose.addItem("Vit");
        colorChoose.addItem("Blå");
        colorChoose.addItem("Rosa");
        colorChoose.addItem("Cyan");

        colorChoose.setPreferredSize(new Dimension(100, 30));
        colorChoose.addActionListener(this);
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
                        CategoryGUI categoryGUI = new CategoryGUI(out, namn);

                    } else if (fromServer.equals("WAIT")) {
                        mainFrame.setTitle("Waiting for player to complete round...");


                    } else if (fromServer.startsWith("QUESTIONS")) {
                        QuestionGUI questionGUI = new QuestionGUI(in, out, namn, backgroundColor, fontColor);

                    } else if (fromServer.equals("ALL_QUESTIONS_ANSWERED")) {
                        System.out.println(fromServer + " received in Client from ServerThr"); //Kommer aldrig hit
                        ResultGUI resultGUI = new ResultGUI(out);

                    } else if (fromServer.startsWith("OPPONENT_DONE")) {
                        mainFrame.dispose();
                        QuestionGUI questionGUI = new QuestionGUI(in, out, namn, backgroundColor, fontColor);

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

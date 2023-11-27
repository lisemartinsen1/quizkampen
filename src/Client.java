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

    private JFrame categoryFrame = new JFrame();
    private JPanel categoryTopPanel = new JPanel();
    private JLabel categoryTitle = new JLabel("Välj en kategori");
    private JPanel categoryPanel = new JPanel();

    //private JPanel category1Panel = new JPanel();
   // private JPanel category2Panel = new JPanel();
    private JButton category1Button = new JButton("Kategori 1");
    private JButton category2Button = new JButton("Kategori 2");
    private JButton category3Button = new JButton("Kategori 3");
    private JButton category4Button = new JButton("Kategori 4");
    private JPanel categoryBottomPanel = new JPanel();
    private JButton goBackButton = new JButton("Gå Tillbaka");
    private List<JButton> answerButtons;

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
    private String categories;

    PropertiesClass propertiesClass = new PropertiesClass();
    private int currentRound = 1;
    private int currentQuestion = 1;

    public Client() {
    }

    public void mainUI() {
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

    public void questionsUI(String category) {
        SwingUtilities.invokeLater(() -> {
        questionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        questionsFrame.setSize(640, 480);
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


        answerButtons = Arrays.asList(answerOne, answerTwo, answerThree, answerFour);


        Collections.shuffle(answerButtons);

        answerButtons.forEach(button -> {
            answerPanel.add(button);
            button.addActionListener(this);
        });
        System.out.println(category);
        out.println(category);
        });
    }
    public void categoryUI() {
            SwingUtilities.invokeLater(() -> {
        categoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        categoryFrame.setVisible(true);
        categoryFrame.setSize(640, 480);
        categoryFrame.setLayout(new BorderLayout());
        categoryFrame.setLocationRelativeTo(null);
        categoryFrame.add(categoryTopPanel, BorderLayout.NORTH);
        categoryFrame.add(categoryPanel, BorderLayout.CENTER);
        categoryFrame.add(categoryBottomPanel, BorderLayout.SOUTH);

        categoryTopPanel.add(categoryTitle, BorderLayout.CENTER);

        categoryPanel.setLayout(new GridLayout(2, 2, 40, 40));
        categoryPanel.add(category1Button);
        categoryPanel.add(category2Button);
        categoryPanel.add(category3Button);
        categoryPanel.add(category4Button);
        //categoryPanel.setSize(400, 300);

        EmptyBorder emptyBorder1 = new EmptyBorder(60, 60, 60, 60);
        categoryPanel.setBorder(emptyBorder1);

        categoryBottomPanel.setLayout(new GridLayout(1, 2, 30, 0));
        categoryBottomPanel.add(goBackButton);
        categoryBottomPanel.add(quitGame);
        EmptyBorder emptyBorder2 = new EmptyBorder(0, 100, 0, 100);
        categoryBottomPanel.setBorder(emptyBorder2);

        category1Button.addActionListener(this);
        category2Button.addActionListener(this);
        category3Button.addActionListener(this);
        category4Button.addActionListener(this);

        goBackButton.addActionListener(this);
        quitGame.addActionListener(this);
            });
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
        new Thread(() -> {
        if (e.getSource() == newGame) {
            // Prova med separat metod
            connectToServer();
            //readResponseFromServer();
            //mainFrame.dispose();
            //categoryUI();
            // Didn't work, don't try it!
            /*try {
                if (ConfirmConnected()) {
                    readResponseFromServer();
                    mainFrame.dispose();
                    categoryUI();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }*/
            //questionsUI();

        } else if (e.getSource() == quitGame) {
            System.exit(0);

        } else if (e.getSource() == nextQuestionButton) {
            propertiesClass.loadProperties();
            int totalRounds = propertiesClass.getAmountOfRounds();
            int questionsPerRound = propertiesClass.getAmountOfQuestions();

            currentQuestion++;
            if (currentQuestion > questionsPerRound) {
                currentQuestion = 1;
                currentRound++;
                if (currentRound > totalRounds) {
                    questionsFrame.dispose();
                    mainUI();
                    //TESSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSST
                    return;
                }
                //Kod för vad som händer efter varje runda, ny UI??
                questionsFrame.dispose();
                resultUI();

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
            System.out.println("Nuvarande Poäng i rundan: "+ howManyPointsInRound);

            //Shufflar rätt svar till random plats vid nästa fråga
            Collections.shuffle(answerButtons);
            answerButtons.forEach(button -> {
                answerPanel.add(button);
            });
        }

        else if (e.getSource() == answerOne) {
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

            howManyPointsInRound ++;


        } else if (e.getSource() == goBackButton) {
            categoryFrame.dispose();
            mainUI();

        } else if (e.getSource() == category1Button) {
            out.println("CHOOSECATEGORY category1");
            setCategories("category1");
            //connectToServer();

        } else if (e.getSource() == category2Button) {
            out.println("CHOOSECATEGORY category2");
            setCategories("category2");
        }
        }).start();
    }
    public void setCategories(String categories) {
        this.categories = categories;

    }
    public String getCategories() {
        return categories;
    }
    public void CatDialog() {
        SwingUtilities.invokeLater(() -> {
            int n = JOptionPane.showConfirmDialog(null, "Motståndet valde " + getCategories() +
                    ". Vill du fortsätta");
            if (n == JOptionPane.YES_OPTION) {
                out.println("ACCEPT " + getCategories().trim());
            }
        });
    }
    public boolean readCategory(String fromServer) throws IOException {
        if (fromServer.startsWith("GO")) {
            setCategories(fromServer.substring(2));
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Client client = new Client();
      client.mainUI();
     // client.categoryUI();


    }
    // BLeached out
    /*public boolean ConfirmConnected() throws IOException {
        String read;
        while (true) {
            read = in.readLine();
            if (read.startsWith("CONNECTED")) {
                System.out.println(read);
                readResponseFromServer();
                return true;
            }
            return false;
        }
    }*/

    public void connectToServer() {
        new Thread(() -> {
            String read;
            try {
                Socket sock = new Socket("127.0.0.1", 1234);
                in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                out = new PrintWriter(sock.getOutputStream(), true);
                // Tror att måste fixa något hit.
                while (true) {
                    read = in.readLine();
                    System.out.println("Read from serv " + read);
                    if (read.startsWith("CONNECTED")) {
                        //readResponseFromServer();
                        mainFrame.dispose();
                        categoryUI();
                    } else if (read.startsWith("OPPONENTCHOOSED")) {
                        setCategories(read.substring(15));
                        CatDialog();
                    } else if (readCategory(read)) {
                        readResponseFromServer();
                        categoryFrame.dispose();
                        questionsUI(getCategories().trim());
                    } else if (read.startsWith("QUESTION")) {
                        System.out.println("q sec: "+read);
                        readResponseFromServer();
                        //questionsUI(getCategories().trim());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void readResponseFromServer() {
        new Thread(() -> {
            try {
                String fromServer;
                // Problem: Readresponse:  = null;
                // Make it Readresponse:  show category and number.
                // Chance to be fixed 65 modulo
                System.out.println("Readresponse: "+getCategories());
                while ((fromServer = in.readLine()) != null) {
                    System.out.println("Readresponse2: "+ fromServer);
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

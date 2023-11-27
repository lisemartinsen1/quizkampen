package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import Server.PropertiesClass;
public class ResultGUI extends JFrame {
    private JFrame resultFrame = new JFrame();
    private JPanel resultUpperPanel = new JPanel(new GridBagLayout());
    private JPanel resultCenterPanel = new JPanel(new GridBagLayout());
    private JPanel resultBottomPanel = new JPanel();
    private JLabel whosTurnText = new JLabel("Din tur/Motståndarens tur");
    private JLabel resultPreviousRounds = new JLabel("1-3");
    private JLabel playerOneName = new JLabel("Spelare 1");
    private JLabel playerTwoName = new JLabel("Spelare 2");
    private JButton playButton = new JButton("Klar");
    PropertiesClass propertiesClass = new PropertiesClass();
    PrintWriter out;
    String playerNr;

    public ResultGUI(PrintWriter out, String playerNr){
        this.out = out;
        this.playerNr = playerNr;

        SwingUtilities.invokeLater(() -> {
            System.out.println("ResultGUI running...");
            resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            resultFrame.setSize(640, 480);
            resultFrame.setVisible(true);
            resultFrame.setLocationRelativeTo(null);
            resultFrame.setLayout(new BorderLayout());
            resultFrame.add(resultUpperPanel, BorderLayout.NORTH);
            resultFrame.add(resultCenterPanel, BorderLayout.CENTER);
            resultFrame.add(resultBottomPanel, BorderLayout.SOUTH);
            resultFrame.setTitle(playerNr);

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

            playButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    out.println("ALL_Q_ANSWERED");
                    resultFrame.dispose();
                }
            });
            resultBottomPanel.add(playButton);
            whosTurnText.setText("Motståndarens tur");


            //labels[0][2].setText(String.valueOf(howManyPointsInRound));
            //Behöver få tag på howManyPointsInRound från Client.QuestionGUI  <---

        });
    }

    public void disablePlayButton() {
        playButton.setEnabled(false);
    }
}

package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import Server.PropertiesClass;

public class ResultGUI extends JFrame {
    private JPanel resultBottomPanel = new JPanel();
    private JButton playButton = new JButton("Klar");
    PrintWriter out;
    String playerNr;
    String[] listWithPlayer1Points;
    String[] listWithPlayer2Points;
    String strWithPlayer1Points;
    String strWithPlayer2Points;
    JLabel resultLabel = new JLabel("RESULT", SwingConstants.CENTER);



    public ResultGUI(PrintWriter out, String playerNr, String strWithPlayer1Points, String strWithPlayer2Points) {
        this.out = out;
        this.playerNr = playerNr;
        this.strWithPlayer1Points = strWithPlayer1Points;
        this.strWithPlayer2Points = strWithPlayer2Points;


        SwingUtilities.invokeLater(this::initializeGUI);
    }

    private void initializeGUI() {
        JFrame resultFrame = new JFrame();
        resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        resultFrame.setSize(640, 480);
        resultFrame.setVisible(true);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setLayout(new GridLayout(0, 1));
        resultFrame.setTitle(playerNr);

        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultFrame.add(resultLabel, BorderLayout.CENTER);

        // För att skapa lite mer space mellan komponenter
        resultFrame.add(new JLabel());

        JPanel playerNamesPanel = new JPanel(new GridLayout(1, 2));
        JLabel player1Label = new JLabel("Player 1", SwingConstants.CENTER);
        playerNamesPanel.add(player1Label);
        JLabel player2Label = new JLabel("Player 2", SwingConstants.CENTER);
        playerNamesPanel.add(player2Label);
        resultFrame.add(playerNamesPanel);
        resultFrame.add(new JLabel());

        listWithPlayer1Points = getArray(strWithPlayer1Points);
        listWithPlayer2Points = getArray(strWithPlayer2Points);

        // Iterera genom rundor
        for (int i = 0; i < Math.max(listWithPlayer1Points.length, listWithPlayer2Points.length); i++) {
            JPanel panel = new JPanel(new GridLayout(2, 1));
            JLabel roundLabel = new JLabel("Round " + (i + 1), SwingConstants.CENTER);
            panel.add(roundLabel);

            JPanel scorePanel = new JPanel(new GridLayout(1, 3));
            JLabel player1ScoreLabel = new JLabel(getPointsForRound(listWithPlayer1Points, i), SwingConstants.CENTER);
            JLabel player2ScoreLabel = new JLabel(getPointsForRound(listWithPlayer2Points, i), SwingConstants.CENTER);

            scorePanel.add(player1ScoreLabel);
            scorePanel.add(new JLabel("-", SwingConstants.CENTER));
            scorePanel.add(player2ScoreLabel);
            panel.add(scorePanel);

            resultFrame.add(panel);
            resultFrame.add(scorePanel);
        }
        resultFrame.add(new JLabel());
        resultFrame.add(resultBottomPanel);
        resultBottomPanel.add(playButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                out.println("ALL_Q_ANSWERED");
                resultFrame.dispose();

            }
        });
    }

    private String getPointsForRound(String[] points, int index) {
        if (index >= 0 && index < points.length) {
            return points[index];
        }
        return "";
    }

    private String[] getArray(String s) {
        return s.split(",");
    }
    public void showFinalResult() {
        playButton.setEnabled(false);
        String[] listWithPlayer1 = getArray(strWithPlayer1Points);
        String[] listWithPlayer2 = getArray(strWithPlayer2Points);

        int sumPlayer1 = 0;
        int sumPlayer2 = 0;

        for (int i = 0; i < listWithPlayer1.length; i++) {
            int intValue = Integer.parseInt(listWithPlayer1[i]);

            sumPlayer1 += intValue;
        }
        for (int i = 0; i < listWithPlayer2.length; i++) {
            int intValue = Integer.parseInt(listWithPlayer2[i]);

            sumPlayer2 += intValue;
        }
        if (sumPlayer1 > sumPlayer2) {
            if (playerNr.equals("PLAYER1")) {
                resultLabel.setText("YOU WIN");
            } else {
                resultLabel.setText("YOU LOSE");
            }
            System.out.println("Player 1 has a higher score: " + sumPlayer1);
        } else if (sumPlayer1 < sumPlayer2) {
            if (playerNr.equals("PLAYER2")) {
                resultLabel.setText("YOU WIN");
            } else {
                resultLabel.setText("YOU LOSE");
            }
            System.out.println("Player 2 has a higher score: " + sumPlayer2);
        } else {
            resultLabel.setText("DRAW");
            System.out.println("Both players have the same score: " + sumPlayer1);
        }



        System.out.println("Spelare 1 poäng: " +strWithPlayer1Points);
        System.out.println("Spelare 2 poäng: " +strWithPlayer2Points);


    }
}

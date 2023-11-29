package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import Server.PropertiesClass;

public class ResultGUI extends JFrame {
    private final JPanel resultBottomPanel = new JPanel();
    private final JButton playButton = new JButton("Klar");
    private final PrintWriter out;
    private final String playerNr;
    private final String strWithPlayer1Points;
    private final String strWithPlayer2Points;
    private final JLabel resultLabel = new JLabel("RESULTAT", SwingConstants.CENTER);
    private final JButton close = new JButton("Stäng");
    private final Font font = new Font("Arial", Font.PLAIN, 16);



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
        resultFrame.setAlwaysOnTop(true);
        resultFrame.setTitle(playerNr);
        resultFrame.getContentPane().setBackground(new Color(255, 182, 193));

        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));
        resultFrame.add(resultLabel, BorderLayout.CENTER);
        resultFrame.add(new JLabel());

        JPanel playerNamesPanel = new JPanel(new GridLayout(1, 2));
        JLabel player1Label = new JLabel("Player 1", SwingConstants.CENTER);
        playerNamesPanel.add(player1Label);
        JLabel player2Label = new JLabel("Player 2", SwingConstants.CENTER);
        playerNamesPanel.add(player2Label);
        resultFrame.add(playerNamesPanel);
        resultFrame.add(new JLabel());
        playerNamesPanel.setBackground(new Color(255, 182, 193));

        String[] listWithPlayer1Points = getArray(strWithPlayer1Points);
        String[] listWithPlayer2Points = getArray(strWithPlayer2Points);

        for (int i = 0; i < Math.max(listWithPlayer1Points.length, listWithPlayer2Points.length); i++) {
            JPanel panel = new JPanel(new GridLayout(2, 1));
            JLabel roundLabel = new JLabel("Runda " + (i + 1), SwingConstants.CENTER);
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
            panel.setBackground(new Color(255, 182, 193));
            scorePanel.setBackground(new Color(255, 182, 193));
        }
        resultFrame.add(new JLabel());
        resultFrame.add(resultBottomPanel);
        resultBottomPanel.setBackground(new Color(255, 182, 193));
        resultBottomPanel.add(playButton);

        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                out.println("ALL_Q_ANSWERED");
                resultFrame.dispose();

            }
        });
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                resultLabel.setText("DU VANN!");
                resultLabel.setForeground(new Color(0, 128, 0));

            } else {
                resultLabel.setText("DU FÖRLORA :(");
                resultLabel.setForeground(new Color(128, 0, 0));

            }
        } else if (sumPlayer1 < sumPlayer2) {
            if (playerNr.equals("PLAYER2")) {
                resultLabel.setText("DU VANN!");
                resultLabel.setForeground(new Color(0, 128, 0));

            } else {
                resultLabel.setText("DU FÖRLORA :(");
                resultLabel.setForeground(new Color(128, 0, 0));

            }
        } else {
            resultLabel.setText("OAVGJORT");
        }
    }

    public void disablePlayButton() {
        playButton.setEnabled(false);
        resultBottomPanel.add(close);
    }

}

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
    PrintWriter out;
    String playerNr;
    String[] listWithPlayer1Points;
    String[] listWithPlayer2Points;

    public ResultGUI(String playerNr, String[] listWithPlayer1Points, String[] listWithPlayer2Points) {
        //this.out = out;
        this.playerNr = playerNr;
        this.listWithPlayer1Points = listWithPlayer1Points;
        this.listWithPlayer2Points = listWithPlayer2Points;

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

        JLabel resultLabel = new JLabel("RESULT", SwingConstants.CENTER);
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
    }

    private String getPointsForRound(String[] points, int index) {
        if (index >= 0 && index < points.length) {
            return points[index];
        }
        return "";
    }

    public static void main(String[] args) {
        String playerNr = "Player 1";
        String[] player1Points = {"10", "15", "20"};
        String[] player2Points = {"12", "18"};

        ResultGUI resultGUI = new ResultGUI(playerNr, player1Points, player2Points);
    }
}

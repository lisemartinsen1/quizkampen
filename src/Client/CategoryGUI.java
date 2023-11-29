package Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class CategoryGUI extends JFrame implements ActionListener {

    private final JFrame categoryFrame = new JFrame();
    private final JPanel categoryTopPanel = new JPanel();
    private final JLabel categoryTitle = new JLabel("Välj Kategori", SwingConstants.CENTER);
    private final JPanel categoryPanel = new JPanel();
    private final JButton category1Button = new JButton("Film & Serier");
    private final JButton category2Button = new JButton("Teknik & Vetenskap");
    private final JButton category3Button = new JButton("Sport");
    private final JButton category4Button = new JButton("Historia");
    private final JPanel categoryBottomPanel = new JPanel();
    private final JButton goBackButton = new JButton("Gå Tillbaka");
    private final JButton quitGame = new JButton("Avsluta");
    private final JLabel lastRound = new JLabel("Poäng Förra Rundan", SwingConstants.CENTER);
    private final Font font = new Font("Arial", Font.BOLD, 16);
    PrintWriter out;
    private final JPanel scorePanel = new JPanel();


    public CategoryGUI(PrintWriter out, String playerNr, String scorePlayer1, String scorePlayer2) {
        this.out = out;
        SwingUtilities.invokeLater(() -> {
            categoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            categoryFrame.setVisible(true);
            categoryFrame.setSize(640, 600);
            categoryFrame.setLayout(new BorderLayout());
            categoryFrame.setLocationRelativeTo(null);
            categoryFrame.setAlwaysOnTop(true);
            categoryFrame.add(categoryTopPanel, BorderLayout.NORTH);
            categoryFrame.add(categoryPanel, BorderLayout.CENTER);
            categoryFrame.add(categoryBottomPanel, BorderLayout.SOUTH);
            categoryFrame.setTitle(playerNr);

            categoryTitle.setFont(new Font("Arial", Font.BOLD, 24));
            category1Button.setFont(font);
            category2Button.setFont(font);
            category3Button.setFont(font);
            category4Button.setFont(font);

            if (!scorePlayer1.isEmpty() || !scorePlayer2.isEmpty()) {

                categoryTopPanel.setLayout(new GridLayout(0, 1));
                categoryTopPanel.add(new JLabel());
                categoryTopPanel.add(categoryTitle, BorderLayout.CENTER);
                categoryTopPanel.add(new JLabel());
                lastRound.setFont(new Font("Arial", Font.ITALIC, 14));
                categoryTopPanel.add(lastRound, BorderLayout.CENTER);
                categoryTopPanel.add(new JLabel());
                JPanel player1player2 = new JPanel(new GridLayout(1, 3));
                player1player2.add(new JLabel("Player 1", SwingConstants.CENTER));
                player1player2.add(new JLabel(" "));
                player1player2.add(new JLabel("Player 2", SwingConstants.CENTER));

                categoryTopPanel.add(player1player2);
                scorePanel.setLayout(new GridLayout(1, 3));
                scorePanel.add(new JLabel(scorePlayer1, SwingConstants.CENTER));
                scorePanel.add(new JLabel("-", SwingConstants.CENTER));
                scorePanel.add(new JLabel(scorePlayer2, SwingConstants.CENTER));
                categoryTopPanel.add(scorePanel);
            } else {
                categoryTopPanel.add(categoryTitle, BorderLayout.CENTER);
            }
            categoryPanel.setLayout(new GridLayout(2, 2, 40, 40));
            categoryPanel.add(category1Button);
            categoryPanel.add(category2Button);
            categoryPanel.add(category3Button);
            categoryPanel.add(category4Button);

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


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == quitGame) {
            System.exit(0);

        } else if (e.getSource() == goBackButton) {
            categoryFrame.dispose();

        } else if (e.getSource() == category1Button) {
            out.println("CATEGORY1");
            categoryFrame.dispose();

        } else if (e.getSource() == category2Button) {
            out.println("CATEGORY2");
            categoryFrame.dispose();

        } else if (e.getSource() == category3Button) {
            out.println("CATEGORY3");
            categoryFrame.dispose();

        } else if (e.getSource() == category4Button) {
            out.println("CATEGORY4");
            categoryFrame.dispose();
        }

    }
}

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

    private JFrame categoryFrame = new JFrame();
    private JPanel categoryTopPanel = new JPanel();
    private JLabel categoryTitle = new JLabel("Välj en kategori");
    private JPanel categoryPanel = new JPanel();
    private JButton category1Button = new JButton("Kategori 1");
    private JButton category2Button = new JButton("Kategori 2");
    private JButton category3Button = new JButton("Kategori 3");
    private JButton category4Button = new JButton("Kategori 4");
    private JPanel categoryBottomPanel = new JPanel();
    private JButton goBackButton = new JButton("Gå Tillbaka");
    private JButton quitGame = new JButton("Avsluta");
    PrintWriter out;


    public CategoryGUI(PrintWriter out, String playerNr) {
        this.out = out;
        SwingUtilities.invokeLater(() -> {
            categoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            categoryFrame.setVisible(true);
            categoryFrame.setSize(640, 480);
            categoryFrame.setLayout(new BorderLayout());
            categoryFrame.setLocationRelativeTo(null);
            categoryFrame.add(categoryTopPanel, BorderLayout.NORTH);
            categoryFrame.add(categoryPanel, BorderLayout.CENTER);
            categoryFrame.add(categoryBottomPanel, BorderLayout.SOUTH);
            categoryFrame.setTitle(playerNr);
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


    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == quitGame) {
            System.exit(0);

        } else if (e.getSource() == goBackButton) {
            categoryFrame.dispose();
            //mainUI();

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

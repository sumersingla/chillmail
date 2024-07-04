package client;

import javax.swing.*;

public class EmailClientGUI extends JFrame {
    public EmailClientGUI() {
        setTitle("Chill Mail");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        //TODO: Initialize UI components
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }
}

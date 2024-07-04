package client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmailClientGUI extends JFrame {
    public EmailClientGUI() {
        setTitle("Chill Mail");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        //Inbox listing component
        DefaultListModel<List> emailListModel = new DefaultListModel<>();
        JList<List> emailList = new JList<>(emailListModel);
        add(new JScrollPane(emailList), BorderLayout.WEST);

        //Reading Component
        JTextArea emailTextArea = new JTextArea();
        emailTextArea.setEditable(false);
        add(new JScrollPane(emailTextArea), BorderLayout.CENTER);

        //Compose email component
        JButton composeButton = new JButton("Compose");
        add(composeButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }
}

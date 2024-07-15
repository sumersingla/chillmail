package client;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EmailClientGUI extends JFrame {

    private static JTextField jUsername = new JTextField(20);
    private static JPasswordField jPassword = new JPasswordField(20);

    public EmailClientGUI() {
        setTitle("Chill Mail");
        setSize(800, 600);
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
        
        //Login dialogue box
        SwingUtilities.invokeLater(this::loginUI);
    }

    private void loginUI() {
        JPanel panel = new JPanel(new GridLayout(0,1));
        panel.add(new JLabel("Email:"));
        panel.add(jUsername);
        panel.add(new JLabel("Password:"));
        panel.add(jPassword);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            String username = jUsername.getText();
            String password = new String(jPassword.getPassword());
            try{
                EmailSessionManager.getManager(username, password);
                refreshInbox();
            } catch (Exception e){
                JOptionPane.showMessageDialog(this,
                        "Failed to initialize email session: " + e.getMessage(),
                        "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("Login Cancelled.");
        }
    }

    private void refreshInbox() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }
}

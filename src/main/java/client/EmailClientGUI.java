package client;

import utilities.serverUtils.AttachmentChooser;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmailClientGUI extends JFrame {

    private static JTextField jUsername = new JTextField(20);
    private static JPasswordField jPassword = new JPasswordField(20);
    DefaultListModel<String> emailListModel = new DefaultListModel<>();
    JList<String> emailList = new JList<>(emailListModel);
    JTextArea emailContent = new JTextArea();
    Message[] messages;


    public EmailClientGUI() {
        setTitle("Chill Mail");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        setVisible(true);

//        Window listener to handle application close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if(EmailSessionManager.getManager() != null){
                        EmailSessionManager.getManager().close();
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initUI() {
        //Inbox listing component
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);

        emailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        emailList.addListSelectionListener(this::emailListSelectionChanged);
        JScrollPane listScrollPane = new JScrollPane(emailList);

        //Reading Component
        emailContent.setEditable(false);
        JScrollPane contentScrollPane = new JScrollPane(emailContent);

        splitPane.setLeftComponent(listScrollPane);
        splitPane.setRightComponent(contentScrollPane);

        getContentPane().add(splitPane, BorderLayout.CENTER);

        //Compose email component
        JButton composeButton = new JButton("Compose");
        composeButton.addActionListener(e -> showComposeDialogue("", "", ""));
        JButton refreshInboxButton = new JButton("Refresh Inbox");

        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        bottomPanel.add(composeButton);
        bottomPanel.add(refreshInboxButton);
        add(bottomPanel, BorderLayout.SOUTH);

        //Login dialogue box
        SwingUtilities.invokeLater(this::loginUI);
    }

    private void showComposeDialogue(String to, String subject, String body) {
        JDialog composeDialogue = new JDialog(this, "Compose email", true);
        composeDialogue.setLayout(new BorderLayout(5,5));

        JTextArea bodyArea = new JTextArea(10,20);
        bodyArea.setText(body);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        JTextField toField = new JTextField(to);
        JTextField subjectField = new JTextField(subject);
        Box fieldsPanel = Box.createVerticalBox();

        fieldsPanel.add(new JLabel("To:"));
        fieldsPanel.add(toField);
        fieldsPanel.add(new JLabel("Subject:"));
        fieldsPanel.add(subjectField);

        JPanel bottomPanel = new JPanel();
        JButton attachButton = new JButton("Attach Files");
        JButton sendButton = new JButton("Send");
        JLabel attachedFilesLabel = new JLabel("No files attached");

        List<File> attachedFiles = new ArrayList<>();
        attachButton.addActionListener(e -> {
            File[] files = AttachmentChooser.chooseAttachments();
            attachedFiles.addAll(Arrays.asList(files));
            attachedFilesLabel.setText(attachedFiles.size() + "Files attached");
        });

        sendButton.addActionListener(e -> {
            EmailSender.sendEmailViaGmailWithAttachments(toField.getText(), subjectField.getText(),
                    bodyArea.getText(), attachedFiles.toArray(new File[0]));
            composeDialogue.dispose();
        });

        bottomPanel.add(attachButton);
        bottomPanel.add(sendButton);

        composeDialogue.add(fieldsPanel, BorderLayout.NORTH);
        composeDialogue.add(new JScrollPane(bodyArea), BorderLayout.CENTER);
        composeDialogue.add(bottomPanel, BorderLayout.SOUTH);
        composeDialogue.pack();
        composeDialogue.setLocationRelativeTo(this);
        composeDialogue.setVisible(true);
    }

    private void emailListSelectionChanged(ListSelectionEvent listSelectionEvent) {
        try {
            if(!listSelectionEvent.getValueIsAdjusting() && emailList.getSelectedIndex() != -1){
                Message selectedMessage = messages[emailList.getSelectedIndex()];
                emailContent.setText("");
                emailContent.append("Subject: " + selectedMessage.getSubject() + "\n\n");
                emailContent.append("From: " + InternetAddress.toString(selectedMessage.getFrom()) + "\n\n");
                emailContent.append(getTextFromMessage(selectedMessage));
            }
        }catch (MessagingException e) {
            emailContent.setText("Error reading email content: " + e.getMessage());
            e.printStackTrace();
        };
    }

    private String getTextFromMessage(Message selectedMessage) {
        try {
//            If simple message, get the content else get content from content bodypart of Multipart
            if(selectedMessage.isMimeType("text/plain")){
                return (String) selectedMessage.getContent();
            } else if(selectedMessage.isMimeType("multipart/*")){
                MimeMultipart multipart = (MimeMultipart) selectedMessage.getContent();
                for(int i = 0; i < multipart.getCount(); ++i){
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if(bodyPart.isMimeType("text/plain")){
                        return (String) bodyPart.getContent();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No readable content found.";
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
        try {
            messages = EmailSessionManager.getManager().receiveEmails();
            emailListModel.clear();
            Arrays.stream(messages).forEach(message -> {
                try {
                    emailListModel.addElement(message.getSubject() + " - From: "
                            + InternetAddress.toString(message.getFrom()));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch emails: "
                    + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmailClientGUI::new);
    }
}

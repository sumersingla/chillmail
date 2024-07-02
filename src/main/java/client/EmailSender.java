package client;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String to, String subject, String body) {
        final String username = "sumersingla14@gmail.com";
        final String password = "gwjk uqlm fcfk jgof";

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }});

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(body);

            Transport.send(mimeMessage);
            System.out.println("Email sent successfully via Gmail");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String to = "chillingbruhh@gmail.com";
        String subject = "Test Email: From the chillmail App";
        String body = "This is a test email sent via gmail from the chillmail App.";

        sendEmail(to, subject, body);
    }
}

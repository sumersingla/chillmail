package client;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailSender implements IEmailSender {
    private final String username = "sumersingla14@gmail.com";
    private final String password = "gwjk uqlm fcfk jgof";
    private final Properties smtpProps = new Properties();

    public void sendEmailViaGmail(String to, String subject, String body) {

        getSMTPProperties(smtpProps);

        Session session = Session.getInstance(smtpProps, new Authenticator() {
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
            e.printStackTrace();
        }

    }

    /* Each file selected for attachment is added as a separate MimeBodyPart,
    and all parts (text and attachments) are combined into a Multipart object. */
    public void sendEmailViaGmailWithAttachments(String to, String subject, String body, File[] attachments){
        getSMTPProperties(smtpProps);

        Session session = Session.getInstance(smtpProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }});

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            for (File attachment : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                try {
                    attachmentPart.attachFile(attachment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            System.out.println("Email sent successfully via Gmail w Attachments");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void getSMTPProperties(Properties props) {
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
    }
}

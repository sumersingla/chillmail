package client;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailSender implements IEmailSender {

//    private final String username = "sumersingla14@gmail.com";
//    private final String password = "gwjk uqlm fcfk jgof";
    private static Properties smtpProps = new Properties();

    /* Each file selected for attachment is added as a separate MimeBodyPart,
    and all parts (text and attachments) are combined into a Multipart object. */
    public static void sendEmailViaGmailWithAttachments(String to, String subject, String body, File[] attachments){
        String username = EmailSessionManager.getCurrentUsername();
        String password = EmailSessionManager.getCurrentPassword();

        getSMTPProperties(smtpProps);

        Session session = Session.getInstance(smtpProps, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }});

        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            mimeMessage.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);

            for (File attachment : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                try {
                    attachmentPart.attachFile(attachment);
                    multipart.addBodyPart(attachmentPart);
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

    private static void getSMTPProperties(Properties props) {
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
    }
}

package client;

import javax.mail.*;
import java.util.Arrays;
import java.util.Properties;

public class EmailReciever implements IEmailReciever {

    public void receiveEmail(String emailId, String password) {
        Properties props = new Properties();
        props.put("mail.store.protocols", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        try{
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", emailId, password);

            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            System.out.println("Number of emails: " + messages.length);
//            Arrays.stream(messages).forEach(message -> {
//                try {
//                    System.out.println("Email Subject: " + message.getSubject());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        EmailReciever emailReciever = new EmailReciever();
        emailReciever.receiveEmail("sumersingla14@gmail.com", "gwjk uqlm fcfk jgof");
    }
}

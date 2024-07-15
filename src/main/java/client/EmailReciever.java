package client;

import javax.mail.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailReciever implements IEmailReciever {

    private static String username = "";
    private static String password = "";

    private static void setCreds(String usrname, String pass){
        username = usrname;
        password = pass;
    }

    @Override
    public Message[] receiveEmail(String emailId, String password) {
        Properties props = new Properties();
        props.put("mail.store.protocols", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        List<Message> messageList = new ArrayList<Message>();

        try{
            Session emailSession = Session.getInstance(props);
            Store emailStore = emailSession.getStore("imaps");
            emailStore.connect("imap.gmail.com", emailId, password);

            Folder emailFolder = emailStore.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();
            messageList.addAll(Arrays.asList(messages));
            emailFolder.close(false);
            emailStore.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList.toArray(new Message[0]);
    }
}

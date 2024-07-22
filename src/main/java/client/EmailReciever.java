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
    public Message[] receiveEmail(String emailId, String password) throws MessagingException {
        EmailSessionManager manager = EmailSessionManager.getManager();
        return manager.receiveEmails();
    }
}

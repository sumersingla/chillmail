package client;

import javax.mail.Message;
import javax.mail.MessagingException;

public interface IEmailReciever {
    Message[] receiveEmail(String emailId, String password) throws MessagingException;
}

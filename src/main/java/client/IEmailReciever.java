package client;

import javax.mail.Message;

public interface IEmailReciever {
    Message[] receiveEmail(String emailId, String password);
}

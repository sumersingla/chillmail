package client;

public interface IEmailSender {
    void sendEmailViaGmail(String to, String subject, String body);
}

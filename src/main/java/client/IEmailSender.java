package client;

public interface IEmailSender {
    public void sendEmailViaGmail(String to, String subject, String body);
}

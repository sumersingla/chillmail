package client;

import java.io.File;

public interface IEmailSender {
    void sendEmailViaGmail(String to, String subject, String body);
    void sendEmailViaGmailWithAttachments(String to, String subject, String body, File[] attachments);

    }

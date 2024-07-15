package client;

import java.io.File;

public interface IEmailSender {
    void sendEmailViaGmailWithAttachments(String to, String subject, String body, File[] attachments);

    }

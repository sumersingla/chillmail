package client;

import javax.mail.*;
import java.util.Properties;

public class EmailSessionManager {
    private Session session;
    private Folder folder;
    private Store store;
    private static EmailSessionManager manager;

    private static String currentUsername = "";
    private static String currentPassword = "";

    private EmailSessionManager(String username, String password) {
        Properties props = new Properties();
        props.put("mail.store.protocols", "imaps");
        props.put("mail.imaps.host", "imap.gmail.com");
        props.put("mail.imaps.port", "993");
        props.put("mail.imaps.ssl.enable", "true");
        this.session = Session.getInstance(props, null);
        try {
            this.store = session.getStore("imaps");
            this.store.connect(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentUsername = username;
        currentPassword = password;
    }

    public Message[] receiveEmails() throws MessagingException {
        if(folder == null || !folder.isOpen()) {
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
        }
        return folder.getMessages();
    }

    public static EmailSessionManager getManager(String username, String password) {
        if (manager == null) {
            manager = new EmailSessionManager(username, password);
        }
        return manager;
    }
    public static EmailSessionManager getManager() {
        if (manager == null) {
            throw new IllegalStateException("EmailSessionManager has not been initialized. Please login first.");
        }
        return manager;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentPassword() {
        return currentPassword;
    }

    public void close() throws MessagingException {
        if(folder != null){
            folder.close();
            folder = null;
        }
        if(store != null){
            store.close();
            store = null;
        }
        manager = null;

        currentPassword = "";
        currentUsername = "";
    }
}

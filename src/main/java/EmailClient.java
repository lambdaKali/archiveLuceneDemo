import javax.mail.*;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailClient {
    private final Store store;
    private Folder folder;

    public EmailClient(String protocol, String host, String email, String password) throws MessagingException {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", protocol);
        props.setProperty("mail.imaps.socketFactory.class", SSLSocketFactory.class.getName());
        props.setProperty("mail.imaps.socketFactory.fallback", "false");
        props.setProperty("mail.imaps.port", "993");
        props.setProperty("mail.imaps.socketFactory.port", "993");

        Session session = Session.getDefaultInstance(props, null);
        store = session.getStore(protocol);
        store.connect(host, email, password);
    }

    public List<Email> getEmails(int start, int end) throws IOException, MessagingException {
        folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);

        int messageCount = folder.getMessageCount();
        if (start < 1) {
            start = 1;
        }
        if (end > messageCount) {
            end = messageCount;
        }

        List<Email> emails = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            Message message = folder.getMessage(i);
            emails.add(new Email(message));
        }

        return emails;
    }

    public int getEmailCount() throws MessagingException {
        folder = store.getFolder("inbox");
        folder.open(Folder.READ_ONLY);
        return folder.getMessageCount();
    }

    public void close() throws MessagingException {
        if (folder != null && folder.isOpen()) {
            folder.close(false);
        }
        if (store != null && store.isConnected()) {
            store.close();
        }
    }
}

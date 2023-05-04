//import java.util.Properties;
//
//        import javax.mail.Folder;
//        import javax.mail.Message;
//        import javax.mail.Session;
//        import javax.mail.Store;
//
//public class Test {
//    public static void main(String[] args) {
//        try {
//            // set up properties for the mail session
//            Properties props = new Properties();
//            props.put("mail.imap.host", "outlook.office365.com");
//            props.put("mail.imap.port", "993");
//            props.put("mail.imap.starttls.enable", "true");
//            props.put("mail.imap.ssl.enable", "true");
//
//            // create the session
//            Session session = Session.getInstance(props);
//
//            // create the store
//            Store store = session.getStore("imap");
//
//            // connect to the store
//            store.connect("outlook.com", "testCONVERTERHTML@outlook.com", "Testing1212");
//
//            // open the inbox folder
//            Folder inbox = store.getFolder("inbox");
//            inbox.open(Folder.READ_ONLY);
//
//            // get all the messages in the inbox
//            Message[] messages = inbox.getMessages();
//
//            // print the subject of each message
//            for (Message message : messages) {
//                System.out.println("Subject: " + message.getSubject());
//            }
//
//            // close the store and folder
//            inbox.close(false);
//            store.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}


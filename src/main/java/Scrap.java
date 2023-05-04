//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.security.Security;
//import java.util.Properties;
//
//import javax.mail.*;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMultipart;
//import javax.net.ssl.SSLSocketFactory;
//
//public class Main {
//    private static final String USERNAME = "testCONVERTERHTML@outlook.com";
//    private static final String PASSWORD = "Testing1212";
//    private static final String HTML_FILE = "messages.html";
//    private static final String ATTACHMENTS_DIR = "attachments";
//
//    public static void main(String[] args) throws Exception {
//
//        Properties props = System.getProperties();
//        props.setProperty("mail.store.protocol", "imaps");
//
//        Session session = Session.getDefaultInstance(props, null);
//        Store store = session.getStore("imaps");
//        store.connect("imap-mail.outlook.com", USERNAME, PASSWORD);
//
//        Folder inbox = store.getFolder("Inbox");
//        inbox.open(Folder.READ_ONLY);
//        Message[] messages = inbox.getMessages();
//
//        File attachmentsDir = new File(ATTACHMENTS_DIR);
//        attachmentsDir.mkdir();
//
//        FileWriter writer = new FileWriter(HTML_FILE);
//        writer.write("<html><body>");
//        for (int i = 0; i < messages.length; i++) {
//            Message message = messages[i];
//            Object content = message.getContent();
//            if (content instanceof MimeMultipart) {
//                MimeMultipart mimeMultipart = (MimeMultipart) content;
//                String messageContent = "";
//                for (int j = 0; j < mimeMultipart.getCount(); j++) {
//                    BodyPart bodyPart = mimeMultipart.getBodyPart(j);
//                    if (bodyPart.getContentType().startsWith("text/plain")) {
//                        messageContent = bodyPart.getContent().toString();
//                    } else if (bodyPart.getContentType().startsWith("image")) {
//                        File attachment = new File(ATTACHMENTS_DIR + File.separator + bodyPart.getFileName());
//                        FileOutputStream outputStream = new FileOutputStream(attachment);
//                        outputStream.write(((MimeBodyPart) bodyPart).getRawInputStream().readAllBytes());
//                        outputStream.close();
//                        messageContent += "<img src='" + attachment.toURI().toURL().toString() + "'>";
//                    } else if (bodyPart.getContentType().startsWith("application")) {
//                        File attachment = new File(ATTACHMENTS_DIR + File.separator + bodyPart.getFileName());
//                        FileOutputStream outputStream = new FileOutputStream(attachment);
//                        outputStream.write(((MimeBodyPart) bodyPart).getRawInputStream().readAllBytes());
//                        outputStream.close();
//                        messageContent += "<br><a href='" + attachment.toURI().toURL().toString() + "'>" + bodyPart.getFileName() + "</a>";
//                    }
//                }
//                writer.write("<h2><a href='#message" + (i + 1) + "'>" + message.getSubject() + "</a></h2>");
//                writer.write("<div id='message" + (i + 1) + "'>" + messageContent + "</div>");
//            }
//        }
//        writer.write("</body></html>");
//        writer.close();
//        inbox.close(false);
//        store.close();
//    }
//}
//
//
//
//
//

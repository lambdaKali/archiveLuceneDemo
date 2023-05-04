import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Base64;
import java.util.List;

public class Email {
    private int messageNumber;
    private Address[] sender;
    private String subject;
    private Date sentDate;
    private Date receivedDate;
    private String content;

    private List<String> attachments;

    public Email(Message message) throws MessagingException, IOException {
        messageNumber = message.getMessageNumber();
        sender = message.getFrom();
        subject = message.getSubject();
        sentDate = message.getSentDate();
        receivedDate = message.getReceivedDate();
        attachments = new ArrayList<>();
        content = getText(message);
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getSender() {
        if (sender != null && sender.length > 0) {
            return sender[0].toString();
        }
        return "";
    }

    public String getSubject() {
        return subject;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public String getContent() {
        return content;
    }

    public List<String> getAttachments() {
        return attachments;
    }

    private void extractAttachments(Part p) throws MessagingException, IOException {
        if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    attachments.add(part.getFileName());
                }
                extractAttachments(mp.getBodyPart(i));
            }
        }
    }

    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/plain")) {
            String content = (String) p.getContent();
            if (isBase64Encoded(content)) {
                return new String(Base64.getDecoder().decode(content));
            } else {
                return content;
            }
        }

        if (p.isMimeType("text/html")) {
            return (String) p.getContent();
        }

        if (p.isMimeType("multipart/alternative")) {
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            String html = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        html = s;
                    continue;
                }
            }
            if (html != null)
                return html;
            else
                return text;
        }

        if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        extractAttachments(p);
        return "Unable to display message: unsupported content type.";
    }

    private boolean isBase64Encoded(String content) {
        try {
            Base64.getDecoder().decode(content);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

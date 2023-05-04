import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBuilder {

    private static final String INDEX_DIR = "lucene_index";
    private static Directory indexDirectory;
    private static IndexWriter indexWriter;

    static {
        try {
            indexDirectory = FSDirectory.open(Paths.get(INDEX_DIR));
            StandardAnalyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(indexDirectory, indexWriterConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateFiles(String outputPath, Email email) throws IOException {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>" + email.getSubject() + "</title>");
        sb.append("<style>");
        sb.append("a { text-decoration: none; }");
        sb.append(".email-content { display: block; }");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<div class='email-content'>");
        sb.append("<h1>" + email.getSubject() + "</h1>");
        sb.append("<p>From: " + email.getSender() + "</p>");
        sb.append("<p>Sent: " + email.getSentDate() + "</p>");
        if (!email.getAttachments().isEmpty()) {
            sb.append("<p>Attachments: " + email.getAttachments() + "</p>");
        }
        sb.append("<div>" + email.getContent() + "</div>");
        sb.append("</div>");

        sb.append("</body>");
        sb.append("</html>");

        try (FileWriter fw = new FileWriter(outputPath)) {
            addToLuceneIndex(email);
            fw.write(sb.toString());
        }
    }

    public static void generateHome(String filePath, List<Email> emails, int totalPages, int currentPage) {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write("<html><head><title>Email Archive</title></head><body>");
            writer.write("<h1>Email Archive - Page " + currentPage + "</h1>");

            for (Email email : emails) {
                writer.write("<div>");
                writer.write("<h2><a href=\"mail" + email.hashCode() + ".html\">" + email.getSubject() + "</a></h2>");
                writer.write("<p>Date: " + email.getReceivedDate() + "</p>");
                writer.write("<p>From: " + email.getSender() + "</p>");
                writer.write("</div>");
            }

            writer.write("<div>");
            for (int i = 1; i <= totalPages; i++) {
                if (i == currentPage) {
                    writer.write("<span>" + i + "</span> ");
                } else {
                    writer.write("<a href=\"index_page_" + i + ".html\">" + i + "</a> ");
                }
            }
            writer.write("</div>");

            writer.write("</body></html>");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void addToLuceneIndex(Email email) throws IOException {
        Document document = new Document();

        document.add(new StringField("messageNumber", String.valueOf(email.getMessageNumber()), Field.Store.YES));
        document.add(new TextField("subject", email.getSubject(), Field.Store.YES));
        document.add(new TextField("sender", email.getSender(), Field.Store.YES));
        document.add(new StringField("sentDate", email.getSentDate().toString(), Field.Store.YES));
        document.add(new StringField("htmlFilePath", "mail" + email.hashCode() + ".html", Field.Store.YES));

        indexWriter.addDocument(document);
        indexWriter.commit();
    }

    public static void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }

    private static Map<String, List<Integer>> createIndex(List<Email> emails) throws IOException {
        Map<String, List<Integer>> index = new HashMap<>();

        for (int i = 0; i < emails.size(); i++) {
            Email email = emails.get(i);
            String[] words = (email.getSubject() + " " + email.getContent()).toLowerCase().split("\\s+");
            for (String word : words) {
                List<Integer> indices = index.getOrDefault(word, new ArrayList<>());
                if (!indices.contains(i)) {
                    indices.add(i);
                }
                index.put(word, indices);
            }
        }
        return index;
    }
}


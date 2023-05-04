import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//          SwingUtilities.invokeLater(() -> new GUI());
        String queryStr = "Microsoft";
        List<Document> searchResults;

        try {
            searchResults = EmailSearcher.search(queryStr);
            for (Document document : searchResults) {
                System.out.println("Message Number: " + document.get("messageNumber"));
                System.out.println("Subject: " + document.get("subject"));
                System.out.println("Sender: " + document.get("sender"));
                System.out.println("Sent Date: " + document.get("sentDate"));
                System.out.println("HTML File Path: " + document.get("htmlFilePath"));
                System.out.println("------------------------------------");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }
}
//testCONVERTERHTML@outlook.com
//Testing1212
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmailSearcher {

    private static final String INDEX_DIR = "lucene_index";

    public static List<Document> search(String queryStr) throws IOException, ParseException {
        Directory indexDirectory = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader indexReader = DirectoryReader.open(indexDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        StandardAnalyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("subject", analyzer);
        Query query = queryParser.parse(queryStr);

        TopDocs topDocs = indexSearcher.search(query, 10);
        List<Document> searchResults = new ArrayList<>();

        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            searchResults.add(document);
        }

        indexReader.close();
        return searchResults;
    }
}

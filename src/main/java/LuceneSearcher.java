import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LuceneSearcher {

    private Directory indexDirectory;
    private IndexSearcher searcher;

    public LuceneSearcher(Path indexPath) throws IOException {
        indexDirectory = FSDirectory.open(indexPath);
        searcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
    }

    public List<Document> search(String queryString, int maxResults) throws Exception {
        QueryParser parser = new QueryParser("content", new StandardAnalyzer());
        Query query = parser.parse(queryString);
        TopDocs topDocs = searcher.search(query, maxResults);

        List<Document> documents = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = searcher.doc(scoreDoc.doc);
            documents.add(document);
        }

        return documents;
    }

    public void close() throws IOException {
        indexDirectory.close();
    }
}

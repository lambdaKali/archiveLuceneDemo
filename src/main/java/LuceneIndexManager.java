import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;

public class LuceneIndexManager {

    private Directory indexDirectory;
    private IndexWriter indexWriter;

    public LuceneIndexManager(Path indexPath) throws IOException {
        indexDirectory = FSDirectory.open(indexPath);
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        indexWriter = new IndexWriter(indexDirectory, config);
    }

    public void addDocument(String id, String content) throws IOException {
        Document document = new Document();
        document.add(new StringField("id", id, Field.Store.YES));
        document.add(new TextField("content", content, Field.Store.YES));
        indexWriter.addDocument(document);
    }

    public void updateDocument(String id, String content) throws IOException {
        Document document = new Document();
        document.add(new StringField("id", id, Field.Store.YES));
        document.add(new TextField("content", content, Field.Store.YES));
        indexWriter.updateDocument(new Term("id", id), document);
    }

    public void deleteDocument(String id) throws IOException {
        indexWriter.deleteDocuments(new Term("id", id));
    }

    public void commit() throws IOException {
        indexWriter.commit();
    }

    public void close() throws IOException {
        indexWriter.close();
        indexDirectory.close();
    }
}

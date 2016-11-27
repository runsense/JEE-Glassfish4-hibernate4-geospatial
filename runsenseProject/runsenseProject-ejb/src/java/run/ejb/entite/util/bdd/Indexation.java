/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import run.ejb.entite.geo.interf.RsEntite;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import run.ejb.entite.geo.Ligne;

/**
 *
 * @author Administrateur
 */
public class Indexation 
{

    private RsEntite entite;
    private File indexDir;
    
    private IndexWriter indexWriter;
    public Indexation(String dossier) 
    {
         indexDir = new File("C:/home/runsense/index/"+dossier);
         
        
        
    }
    public Indexation(File indexDir) 
    {
         this.indexDir = indexDir;
       
    }
    //MANQUE DOCUMENT
   /* public void createIndex()
    {
        Boolean indexer=false;
        try {
            indexWriter = new IndexWriter(
                FSDirectory.open(indexDir),       
                new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT)));
        
        
        indexDirectory(indexWriter);
        
        int numIndexed = indexWriter.maxDoc();
     
        if(numIndexed!=0)
            indexer=true;
        
        
        indexWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(Indexation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("indexed :?" + indexer);
    }*/
   /* public void updatIndex(String id, Object aindx)
    {
        
        Boolean indexer=false;
        try {
            FSDirectory fsd = FSDirectory.open(indexDir);
          
             indexWriter = new IndexWriter(
                fsd,       
                new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT)));
             Directory directory=(Directory)fsd;
            IndexReader reader= DirectoryReader.open(directory);
            Document doc = reader.document(Integer.valueOf(id));
            
          for(IndexableField idxfld: doc.getFields())
          {
               System.out.println("doc"+doc); System.err.println(idxfld);
          }
        
        
            indexWriter.updateDocument(new Term("ID",id ),doc, new FRAnalyzer());

            int numIndexed = indexWriter.maxDoc();

            if(numIndexed!=0)
                indexer=true;


            indexWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(Indexation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("indexed :?" + indexer);
    }*/
    public void clearlock()
    {
        try {
            indexWriter = new IndexWriter(
                FSDirectory.open(indexDir),       
                new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT)));
        
            indexWriter.getDirectory().deleteFile("write.lock");
        }catch(Exception ex)
        {
            
        }
    }
    public void deleteIndex(Query query)
    {
        Boolean indexer=false;
        try {
            indexWriter = new IndexWriter(
                FSDirectory.open(indexDir),       
                new IndexWriterConfig(Version.LUCENE_CURRENT, new StandardAnalyzer(Version.LUCENE_CURRENT)));
        
            indexWriter.getDirectory().clearLock("write");
            indexWriter.deleteDocuments(query);

            int numIndexed = indexWriter.maxDoc();

            if(numIndexed!=0)
                indexer=true;


            indexWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(Indexation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("indexed :?" + indexer);
    }
   
    private void indexDirectory(IndexWriter indexWriter
           ) throws IOException {

        
        
        System.out.println("Indexing file " + entite.getNom()+entite.getDescription());
        
        Document doc = new Document();
        doc.add(new Field("id", new FileReader(entite.getId().toString())));        
        doc.add(new Field("nom", new FileReader(entite.getNom())));   
        doc.add(new Field("description", new FileReader(entite.getDescription())));   
        indexWriter.addDocument(doc);

    }
    
    public RsEntite getEntite() {
        return entite;
    }

    public void setEntite(RsEntite entite) {
        this.entite = entite;
    }
    
}

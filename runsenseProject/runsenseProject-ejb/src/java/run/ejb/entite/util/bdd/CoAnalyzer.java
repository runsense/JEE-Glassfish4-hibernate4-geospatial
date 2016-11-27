/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.SolrAnalyzer;


/**
 *
 * @author Administrateur
 */
public class CoAnalyzer extends SolrAnalyzer {

    //impementation lucene 3.6 pour glassfish hibernatesearch 4
    @Override
    public Analyzer.TokenStreamComponents createComponents(String string, Reader reader)
    {
        StandardTokenizer standardTokenizer = new StandardTokenizer(Version.LUCENE_CURRENT, reader);        
        Analyzer.TokenStreamComponents tks=new Analyzer.TokenStreamComponents(standardTokenizer,  getTokenStream(standardTokenizer, string, reader) );
        return tks;
    }

    @Override
    public SolrAnalyzer.TokenStreamInfo getStream(String string, Reader reader) {
                
       
            StandardTokenizer standardTokenizer = new StandardTokenizer(Version.LUCENE_CURRENT, reader);
            SolrAnalyzer.TokenStreamInfo tks=new SolrAnalyzer.TokenStreamInfo(standardTokenizer, getTokenStream(standardTokenizer, string, reader) );
        return tks;
    }
    public TokenStream getTokenStream(Tokenizer standardTokenizer, String string, Reader reader) {
        TokenStream result;
            if(!string.equals("French"))
            {
                string="French";
            }
            result = new StandardFilter(Version.LUCENE_CURRENT, standardTokenizer);

            //spatial indexation
            
            
            
        
        return result;
    } 
    
}

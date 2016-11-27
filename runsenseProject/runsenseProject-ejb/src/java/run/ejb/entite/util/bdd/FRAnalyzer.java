/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.fr.FrenchLightStemFilter;


import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.ElisionFilter;
import org.apache.lucene.util.Version;
import org.apache.solr.analysis.SolrAnalyzer;
import run.ejb.util.ex.Xcption;



/**
 *
 * @author Administrateur
 */
public class FRAnalyzer extends SolrAnalyzer 
{
    //private Set stpw;
    private final Version matchVersion=Version.LUCENE_CURRENT;
    
    public FRAnalyzer() 
    {
       /*  stpw=new HashSet();
            stpw.add(" ");stpw.add("-");stpw.add("'");
            stpw.add("_");stpw.add("/");*/
    }

    
    //impementation lucene 3.6
    @Override
    public TokenStreamComponents createComponents(String string, Reader reader)
    {
        StandardTokenizer standardTokenizer = new StandardTokenizer(Version.LUCENE_CURRENT, reader);        
        TokenStreamComponents tks=new TokenStreamComponents(standardTokenizer,  getTokenStream(standardTokenizer, string, reader) );
        return tks;
    }

    @Override
    public TokenStreamInfo getStream(String string, Reader reader) {
                
       
            StandardTokenizer standardTokenizer = new StandardTokenizer(Version.LUCENE_CURRENT, reader);
            TokenStreamInfo tks=new TokenStreamInfo(standardTokenizer, getTokenStream(standardTokenizer, string, reader) );
        return tks;
    }
    public TokenStream getTokenStream(Tokenizer standardTokenizer, String string, Reader reader) 
    {
        
        
          
                 final Tokenizer source = new StandardTokenizer(matchVersion, reader);
                 TokenStream result = new StandardFilter(matchVersion, source);
                 /*result = new ElisionFilter(result, FrenchAnalyzer.DEFAULT_ARTICLES);
                 result = new StopFilter(matchVersion, result, FrenchAnalyzer.getDefaultStopSet());*/
                 result = new ASCIIFoldingFilter(result);
                // result = new LowerCaseFilter(matchVersion, result);
                 result = new FrenchLightStemFilter(result);
                 //result = new LowerCaseFilter(matchVersion, result);
           
    
           return  result;
    }
}
    

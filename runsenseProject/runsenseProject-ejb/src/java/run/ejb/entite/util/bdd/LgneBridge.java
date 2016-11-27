/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import java.io.StringReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 *
 * @author Administrateur
 */
public class LgneBridge implements  FieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        Field field = new Field(
				name,  
				"",
				luceneOptions.getStore(),
				luceneOptions.getIndex(),
				luceneOptions.getTermVector()
		);
		
                   
			FRAnalyzer analyzer = new FRAnalyzer( );
			field.setTokenStream( analyzer.tokenStream( name, new StringReader( value.toString() ) ) );
			//document.add( field );
                        
		}
		
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import java.io.StringReader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.spatial.SpatialFieldBridge;

/**
 *
 * @author Administrateur
 */
public class CordBridge extends SpatialFieldBridge {

    @Override
    public void set(String name, Object value, Document document, LuceneOptions luceneOptions) {
        Field field = new Field(
				name,  
				"",
				luceneOptions.getStore(),
				luceneOptions.getIndex(),
				luceneOptions.getTermVector()
		);
		
                   
			CoAnalyzer analyzer = new CoAnalyzer();
			field.setTokenStream( analyzer.tokenStream( name, new StringReader( value.toString() ) ) );
			//document.add( field );
                        
		}
    
}

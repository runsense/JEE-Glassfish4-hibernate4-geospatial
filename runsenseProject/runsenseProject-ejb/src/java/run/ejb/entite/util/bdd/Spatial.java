/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.bdd;

import com.spatial4j.core.shape.Point;
import com.spatial4j.core.shape.Rectangle;
import com.spatial4j.core.shape.Shape;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.function.ValueSource;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.query.SpatialArgs;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import run.ejb.entite.geo.Ligne;

/**
 *
 * @author Administrateur
 */
public class Spatial {

    private Document document;
    private SpatialStrategy sptst;
    private FullTextEntityManager ftem;
    public Spatial() 
    {
       
        
    }
    
    
    public Field[] createIndexableFields(Shape shape) {
        Rectangle boundingBox = shape.getBoundingBox();
        QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Ligne.class).get();
        return createIndexableFields(shape);
    }

    
    public ValueSource makeDistanceValueSource(Point point) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Filter makeFilter(SpatialArgs sa) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}

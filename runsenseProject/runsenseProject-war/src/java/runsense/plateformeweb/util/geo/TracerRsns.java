/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.util.geo;

import de.micromata.opengis.kml.v_2_2_0.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class TracerRsns
{
    private Feature plan;
    private Folder dossier;
    private Placemark p=null;
    private Folder d;

    public TracerRsns(Feature plan) {
        this.plan = plan;
    }
    public Folder formeTracer()
    {
        List<Feature> lFeat=new ArrayList<Feature>();
        
         dossier=new Folder();
            if(plan instanceof Placemark)
            {
                p = (Placemark) plan;
                fontTracer();
            }
            else if(plan instanceof Folder)
            {
                d=  (Folder) plan;
                
                List<Feature> lPl = d.getFeature();
                                      
                for(Object tr : lPl)
                   {
                      if(tr instanceof Placemark)
                      {
                          p = (Placemark) tr;
                            fontTracer();
                           
                      }
                      else if(tr instanceof Polygon)
                      {
                         p = (Placemark) tr;
                         fontPlace();
                            
                      }
            
                   }
            }
            lFeat.add(plan);

            dossier.setFeature(lFeat);
                
        return dossier;
    }
                

    public Boolean addInDossier(Feature addFeature)
    {
        return dossier.getFeature().add(addFeature);
    }
    public Feature getPlan() {
        return plan;
    }

    public void setPlan(Feature plan) {
        this.plan = plan;
    }
    private void fontTracer() 
    {
        //Placemark p=(Placemark) p;
        Style style = p.createAndAddStyle();
        LineStyle lineStyle = style.createAndSetLineStyle();
        lineStyle.setColor("ff0000ff");
        lineStyle.setWidth(2.1);
        
    }

    private void fontPlace() 
    {
        //Placemark p=(Placemark) planf;
        Style style = p.createAndAddStyle();
        PolyStyle polyStyle = style.createAndSetPolyStyle();
        polyStyle.setColor("7dff0000");
        polyStyle.setOutline(Boolean.TRUE);
        polyStyle.setFill(false);
    }
}

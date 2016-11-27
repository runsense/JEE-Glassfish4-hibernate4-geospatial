/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.kml;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author runsense
 */
public class LatLng
{
    private Map<String, String> latlng;
    private List<Coordinate> lCo;

    public LatLng() {
    }

    public LatLng(List<Coordinate> lCo) {
        this.lCo = lCo;
    }

    public List<Coordinate> getlCo() {
        return lCo;
    }

    public void setlCo(List<Coordinate> lCo) {
        this.lCo = lCo;
    }
    
    public String[] cherch()
    {
        //centrage sur objet de coordonne
        String[] latlng = new String[2];
        double[] tla={0d,0d};
        double[] tlng={0d,0d};
        for(Iterator<Coordinate> itco=lCo.iterator(); itco.hasNext();)
        {
            Coordinate next = itco.next();
            if(tla[0]==tla[1])
            {
                if(next.getLatitude()<tla[0])
                {
                    tla[0] = next.getLatitude();
                }
                else if(next.getLatitude()>tla[1])
                {
                    tla[1] = next.getLatitude();
                }
                
                if(next.getLongitude()<tlng[0])
                {
                    tlng[0] = next.getLongitude();
                }
                else if(next.getLongitude()>tlng[1])
                {
                    tlng[1] = next.getLongitude();
                }
                
            }
            else{
                tla[1]=next.getLatitude();
                tla[0]=tla[1];
                
                 tlng[1]=next.getLongitude();
                tlng[0]=tlng[1];
            }
            
            
        }
        
        latlng[0]=String.valueOf((tla[0]-tla[1])/2+ tla[0]);
        latlng[1]=String.valueOf((tlng[0]-tlng[1])/2+ tlng[0]);
        return latlng;
    }
    
}

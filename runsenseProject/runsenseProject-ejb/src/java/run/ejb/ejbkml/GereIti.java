/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import org.primefaces.context.RequestContext;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.entite.util.kml.LatLng;
import run.ejb.iti.entite.util.Itineraire;

/**
 *
 * @author selekta
 */
@Stateless
public class GereIti implements GereItiLocal {

  
      private String itid;
    private String itia;
    
    private String iti;
  
    private Itineraire itineraire;
    private ArrayList<Itineraire> liti;
    private String tracer;
    private String[] latlng;
     public GereIti() 
    {
        
    }

    
      @Override
     public void traitIti() 
    {
       System.err.println("traititi iti");System.out.println(iti);
       System.err.println("responsebean traitIti iti");System.out.println(itia);System.out.println(itid);
       if(iti!=null) 
       {
         liti=new ArrayList<Itineraire>();
        ArrayList<String> dloc=new ArrayList<String>();
        ArrayList<String> floc=new ArrayList<String>();
        ArrayList<String> dure=new ArrayList<String>();
        ArrayList<String> inform=new ArrayList<String>();
        ArrayList<String> distance=new ArrayList<String>();
       
        String[] tabstep = iti.split("<step>");
        for(int i=1; i<tabstep.length; i++)
        {
            
            
            String[] strtloc = tabstep[i].split("<start_location>");
            String[] tabfin = strtloc[1].split("</start_location>");
             dloc.add(tabfin[0]);

             
             String[] endtloc = tabfin[1].split("<end_location>");
             tabfin = endtloc[1].split("</end_location>");
             floc.add(tabfin[0]);
             
             
              String[] duration = tabfin[1].split("<duration>");
              tabfin = duration[1].split("</duration>");
              dure.add(tabfin[0]);
              
              
              String[] inf = tabfin[1].split("<html_instructions>");
              tabfin = inf[1].split("</html_instructions>");
              inform.add(tabfin[0]);
              
              
              String[] dist = tabfin[1].split("<distance>");
              tabfin = dist[1].split("</distance>");
              distance.add(tabfin[0]);
              
        
        iti=null;
        System.out.println(dloc);System.out.println(floc);
        System.out.println(dure);
        System.out.println(inform);
        System.out.println(distance);
 
        ChrSpe chgmt=new ChrSpe();
        ArrayList<Coord> lco=new ArrayList<Coord>();
        for(int j=0; j<dloc.size(); j++)
        {
            itineraire=new Itineraire();
            String du,di;
            
            String[] tpre = dure.get(j).split("<text>");
             String[]  tfin = tpre[1].split("</text>");
              du=tfin[0];
              itineraire.setDure(du);
              
               tpre = distance.get(j).split("<text>");
               tfin = tpre[1].split("</text>");
              di=tfin[0];
              itineraire.setDistance(di);
            //incremente information

            itineraire.setInform(inform.get(j));
            //incremente long lat
            Coord co=new Coord();
            
            String deb = dloc.get(j);String fin = floc.get(j);
            String[] tlat = deb.split("<lat>");
              String[] lat = tlat[1].split("</lat>");
              
              String[] tlng = deb.split("<lng>");
              String[] lng = tlng[1].split("</lng>");
              
            co.setLatitude(Double.valueOf(lat[0]));
            co.setLongitude(Double.valueOf(lng[0]));
            lco.add(co);
            itineraire.setDloc(co);
            
            
             tlat = fin.split("<lat>");
               lat = tlat[1].split("</lat>");
              
               tlng = fin.split("<lng>");
               lng = tlng[1].split("</lng>");
              
            co.setLatitude(Double.valueOf(lat[0]));
            co.setLongitude(Double.valueOf(lng[0]));
            lco.add(co);
            
            itineraire.setFloc(co);
            
             String chrchBaliseCh = chgmt.chrchBaliseCh(itineraire.getInform());
                itineraire.setInform(chrchBaliseCh);
            liti.add(itineraire);
        }
           GereKml gKml = new GereKml();
           gKml.cEm();
           gKml.createIti(lco);
              tracer = gKml.getTracer();
              latlng = gKml.getLatlng();
       
        
        System.err.println("responsebean traitIti iti");System.out.println(itia);System.out.println(itid);
       }
       }else
           System.err.println("responsebean traitIti iti");System.out.println("iti null");;
    }
    
      @Override
    public void vuePortIti(Itineraire itineraire)
    {
       
        LatLng chlatlng = new LatLng();
        List lCo=new ArrayList();
        Coordinate cord=new Coordinate(itineraire.getDloc().getLongitude(), itineraire.getDloc().getLatitude());
        Coordinate corf=new Coordinate(itineraire.getFloc().getLongitude(), itineraire.getFloc().getLatitude());
            lCo.add(cord);
            lCo.add(corf);
        chlatlng.setlCo(lCo);
        
         System.err.println("responsebean vuePortIti LatLng");System.out.println(cord);System.out.println(corf);
        latlng = chlatlng.cherch();
       
        
        
      
    }

    public String getItid() {
        return itid;
    }

      @Override
    public void setItid(String itid) {
        this.itid = itid;
    }

    public String getItia() {
        return itia;
    }

      @Override
    public void setItia(String itia) {
        this.itia = itia;
    }

    public String getIti() {
        return iti;
    }

      @Override
    public void setIti(String iti) {
        this.iti = iti;
    }

    @Override
    public ArrayList<Itineraire> getLiti() {
        return liti;
    }

    public void setLiti(ArrayList<Itineraire> liti) {
        this.liti = liti;
    }

      @Override
    public String getTracer() {
        return tracer;
    }

    public void setTracer(String tracer) {
        this.tracer = tracer;
    }

      @Override
    public String[] getLatlng() {
        return latlng;
    }

    public void setLatlng(String[] latlng) {
        this.latlng = latlng;
    }
    
}

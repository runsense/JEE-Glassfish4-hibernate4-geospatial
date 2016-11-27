/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runsense.plateformeweb.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import run.ejb.base.Variable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;
import run.ejb.ejbkml.GereKml;
import run.ejb.ejbkml.GereKmlLocal;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.interf.RsEntite;

/**
 *
 * @author runsense
 */
@ManagedBean(name="mobileBean")
@ViewScoped
public class MobileBean implements Serializable {

    /**
     * Creates a new instance of MobileBean
     */

    
    //@EJB
    private GereKmlLocal gKml;
    private RsEntite[] trs;
    private Map<String, List> mcateg;
    private List<String>lcateg;
    private List<RsEntite>lresult;
    private List<String>lzone;
    private List<String>lville;
    private List<RsEntite>lsentier;
    private List<String> lprmtr;
    private String res;
    private String temp;
    private RsEntite ligne;
    
    private String tracer;
    private String lat;
    private String lng;
    
    public MobileBean() 
    {
        trs=new RsEntite[2];
            
        lprmtr= Variable.getPrmtr();
        mcateg=new HashMap<String, List>();
            List temp=new ArrayList();
                temp.add("restaurant");temp.add("snack");temp.add("cafet");
                mcateg.put("repas", temp);
                temp=new ArrayList();
                
                temp.add("gite");temp.add("hotel");temp.add("chambre d'hote");temp.add("appartement");
                mcateg.put("hebergement", temp);
                temp=new ArrayList();
                
            temp.add("équitation");temp.add("randonné");temp.add("plongé");temp.add("surf");
            temp.add("loisir");temp.add("nautique");temp.add("vtt");temp.add("pêche");
            temp.add("golf");temp.add("croisière");temp.add("visio sous-marine");temp.add("bateau");
            temp.add("catamaran");temp.add("jet ski");temp.add("micro offshore");temp.add("paint-ball");
            temp.add("luge");temp.add("barque");temp.add("kart");temp.add("quad");temp.add("4X4");
            temp.add("moto");temp.add("hydrospeed");temp.add("Kayakraft");temp.add("parapente");
            mcateg.put("activité", temp);
                
           
       ligne=new Ligne();
        gKml=new GereKml();
            gKml.cEm();
        lcateg=new ArrayList<String>();
        lcateg.addAll(mcateg.keySet());
        lzone=new ArrayList(Variable.getRegion().keySet());
    }
    
    public String navVille()
    {
        return "pm:ville";
    }
    public List<RsEntite> slcVille()
    {//combo selection des villes
        
        temp=res;
        ArrayList<RsEntite> rchBdd=new ArrayList();
        if(res!=null)
        {
            try {
                System.err.printf("MobileBean slcVille utf8 to iso");System.out.println(new String(res.getBytes(),"ISO-8859-1"));
            } catch (UnsupportedEncodingException ex) {
                System.err.printf("MobileBean slcVille UnsupportedEncodingException");System.out.println(ex.getMessage());
            }
           
                
                    
                    
            
            String rch=res.replace(" ", "").replace("-", "");
        
           
        
            if(rch!="")
            {
                rchBdd = gKml.rchBdd(rch, "ligne", "fix","adresse");
                rchBdd.addAll(gKml.rchBdd(rch, "point", "fix","adresse"));
               try{ 
                   if(rchBdd.isEmpty())
                   {
                       rchBdd = gKml.rchBdd(res, "ligne", "fix","adresse");
                       rchBdd.addAll(gKml.rchBdd(res, "point", "fix","adresse"));
                   }
               ligne= (RsEntite) rchBdd.get(0);
                trs[1]=ligne;

                    //tracer principale

                       lsentier=rchBdd;
                       

                }catch(Exception ex)
                {
                     System.err.println("MobileBean slcVille Exception");
                      System.err.print("res"+res);System.out.println(ex.getMessage());
                }
            }
        }
        
        if(!rchBdd.isEmpty())
        {
          //  handleMobSelect(rchBdd, "Ville");
        }
 
        return lsentier;
    }
    public void complete()
    {
        lresult=new ArrayList<RsEntite>();
        lresult = gKml.rchBdd(res,"point","otocple", "nom");
        if(lresult!=null)
        {
            lresult.addAll(gKml.rchBdd(res,"ligne","otocple", "nom"));
        }else
        {
           lresult = gKml.rchBdd(res,"ligne","otocple", "nom"); 
        }
        
        if(lresult!=null)
        {
            lresult.addAll(gKml.rchBdd(res,"ligne","aprx", "description"));
        }else
        {
           lresult = gKml.rchBdd(res,"ligne","aprx", "description"); 
        }
        
        if(lresult!=null)
        {
            lresult.addAll(gKml.rchBdd(res,"point","aprx", "description"));
        }else
        {
           lresult = gKml.rchBdd(res,"point","aprx", "description"); 
        }
        
        
    }
    public void slcLCateg()
    {
        List<String> list=new ArrayList<String>();
        List<String> lField=new ArrayList<String>();
        list.add(res);lField.add("description");
        if(temp!=null&&res!=null)
        {
            list.add(temp);
            lresult = gKml.rchMulti(list, new Pt(), lField, "MUST_MUST", null);
            
        }else
        if(res!=null)
        {
           lresult = gKml.rchBdd(res, "point", "fix", "description");
          
        }
        
        
        
    }
    public void faireTracer(String tracer, String field)
    {
        gKml.createTracer(tracer, field);
    }
    public List<RsEntite>  filtreMob()
    {
        Iterator<RsEntite> iter=lresult.iterator();
        lresult=new ArrayList<RsEntite>();
       for(; iter.hasNext();)
       {
           RsEntite next = iter.next();
           if(next.getDescription().contains(res))
           {
               lresult.add(next);
           }
       }
        return lresult;
    }
    public ArrayList<Ligne> slcSentier()
    {
        ArrayList listTracer=null;
        trs=new RsEntite[2];
        if(res!=null)
        {
            listTracer = gKml.createTracer(res, "nom");
            String[] latlng = gKml.getLatlng();
                lat=latlng[0];
                lng=latlng[1];
                
            Ligne aTcer = (Ligne) listTracer.get(0);
            /*Set<Coord> coords = aTcer.getCoords();
            tracer="";
            for(Iterator itco=coords.iterator(); itco.hasNext();)
            {
                Coord next = (Coord) itco.next();
                tracer+=next.getLatitude()+"_"+next.getLongitude()+"/";
            }*/
            
            tracer = gKml.getTracer();
            
            //prise de coordonnée
            String[] split= tracer.split("coordinates>");
            if(split.length>1)
            {
                String[] split1 = split[1].split("</");
                String[] split0 = split1[0].split(" ");
                tracer="";
                for(int i=0; i<split0.length; i++)
                {
                    tracer+=split0[i]+"_";
                }
            }
            //addPolyline(aTcer.getCoords());
        }
        return listTracer;
        
    }
    public void addPolyline(Set<Coord> coords)
    {
         LatLng coord;
         Polyline polyline = new Polyline(); 
        for(Iterator<Coord> itco=coords.iterator(); itco.hasNext();)
        {
            Coord co = itco.next();
            coord = new LatLng(co.getLatitude(), co.getLongitude());  
            polyline.getPaths().add(coord); 
        }
   
         polyline.setStrokeWeight(10);  
        polyline.setStrokeColor("#FF9900");  
        polyline.setStrokeOpacity(1.0d);  
        
    }
    
   
    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public List<String> getLzone() {
        return lzone;
    }

    public void setLzone(List<String> lzone) {
        this.lzone = lzone;
    }

    public List<String> getLville() 
    {
        lville=slcZone();
        return lville;
    }

    public void setLville(List<String> lville) {
        this.lville = lville;
    }

    public List<RsEntite> getLsentier() 
    {
        lsentier=slcVille();
        return lsentier;
    }

    public void setLsentier(List<RsEntite> lsentier) {
        this.lsentier = lsentier;
    }
    
    public RsEntite getLigne() {
        return ligne;
    }

    public void setLigne(RsEntite ligne) {
        this.ligne = ligne;
    }

    public String getTracer()
    {
        
        slcSentier();
        return tracer;
    }

    public void setTracer(String tracer) 
    {
        this.tracer = tracer;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<String> getLcateg()
    {
        if(mcateg.containsKey(res))
        {
            lcateg=mcateg.get(res);
        }else if(mcateg.containsValue(res))
        {
            slcLCateg();
        }
        
        return lcateg;
    }

    public void setLcateg(List<String> lcateg) {
        this.lcateg = lcateg;
    }

    public Map<String, List> getMcateg() {
        return mcateg;
    }

    public void setMcateg(Map<String, List> mcateg) {
        this.mcateg = mcateg;
    }

    public List<RsEntite> getLresult() 
    {
        complete();
        return lresult;
    }

    public void setLresult(List<RsEntite> lresult) {
        this.lresult = lresult;
    }

    
    
     public ArrayList slcZone()
    {
         lville= Variable.getRegion().get(res);
        ArrayList<RsEntite> rchBdd = gKml.rchBdd(res, "ligne", "fix", "nom");
        if(rchBdd!=null)
            for(Iterator<RsEntite> iter=rchBdd.iterator(); iter.hasNext();)
                {
                    RsEntite next = iter.next();
                    if(next.getNom().equals(res))
                    {
                         ligne=(Ligne) next;
                    }
                }
               
            
        
        trs[0]=ligne;
        temp=res;
        return (ArrayList) lville;
    }

    public ArrayList slcMCateg()
    {
         lcateg=mcateg.get(res);
        
        return (ArrayList) lcateg;
    }

    public List<String> getLprmtr() {
        return lprmtr;
    }

    public void setLprmtr(List<String> lprmtr) {
        this.lprmtr = lprmtr;
    }

    public RsEntite[] getTrs() {
        return trs;
    }

    public void setTrs(RsEntite[] trs) {
        this.trs = trs;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.ejbkml;


import com.google.common.collect.Lists;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Circle;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleState;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.spatial.SpatialStrategy;
import org.apache.lucene.spatial.prefix.RecursivePrefixTreeStrategy;
import org.apache.lucene.spatial.prefix.tree.GeohashPrefixTree;
import org.apache.lucene.spatial.prefix.tree.SpatialPrefixTree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import run.ejb.base.entite.TempKml;

import run.ejb.entite.client.Compte;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.climat.PrevMt;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Localite;
import run.ejb.entite.geo.Prmtr;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.interf.IntBaz;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.kml.ChargeKml;
import run.ejb.entite.util.kml.LatLng;
import run.ejb.entite.util.kml.TourRsns;
import run.ejb.entite.util.kml.TracerRsns;
import run.ejb.entite.util.kml.YahooRep;
import run.ejb.util.ex.Xcption;
import run.ejb.util.kml.schema.IconKml;
import run.ejb.util.kml.schema.LigneKml;
import run.ejb.util.kml.schema.ObjetKml;
import run.ejb.util.kml.schema.Ref;
import run.ejb.util.kml.schema.RsnsObj;
import run.ejb.util.rsentite.schema.PrmtrSchm;

/**
 *
 * @author Administrateur
 */
//@Stateless
public class GereKml implements GereKmlLocal
{
    
    private boolean bc;
    private String lieu;

   
    private GereDonne gereDonne;
    
    private String[] color = {"0000ff","ff0000","4080ff","80ffff","00ff00","50af04","007295"};
    //private String meteo;
    private String tracer;
    private String tour;
    private String[] latlng;

    private boolean bex=false;
    private boolean bCoord;
    private boolean bcrit;
    
    private DefaultTreeNode root;
    private List slCrit;
   
    private YahooRep meteoKml;
    private boolean bclim=false;
    private ChargeKml charge;

   
    
    public GereKml()
    {
        //System.runFinalization();
        System.gc();
        gereDonne = new GereDonne();
        
//       gereDonne.initLuc();
        
        
    }

    public GereKml(Exception ex) 
    {
        bex=true;
        System.gc();
        gereDonne = new GereDonne();
        gereDonne.createEM();
    }
    
    public void cEm()
    {
        gereDonne.createEM();
    }
    
    public void fEm()
    {
        gereDonne.finish();
    }
    
    public void spaialRch(Coord co)
    {
        SpatialContext ctx=SpatialContext.GEO;
            int maxlevels=11;
            SpatialPrefixTree grille=new GeohashPrefixTree(ctx, maxlevels);
            SpatialStrategy strategie=new RecursivePrefixTreeStrategy(grille, "testgeo");
            
        double dist2Degrees = DistanceUtils.dist2Degrees(50, DistanceUtils.EARTH_MEAN_RADIUS_KM);
        Circle makeCircle = ctx.makeCircle(dist2Degrees, dist2Degrees, dist2Degrees);
    }
  
    @Override
    public ArrayList rchMulti(Object rech, Object entcl,List<String> fields, String precis, String genre) throws org.hibernate.search.SearchException 
    {
        ArrayList lrech=null;
        ArrayList rchBoolean=null;
        if(entcl instanceof String)
            entcl = castRsEntite((String) entcl);
        try{
     
            if(rech instanceof List)
            {
                lrech=(ArrayList) rech;
                if(lrech.get(0)instanceof Long)
                    rchBoolean = (ArrayList) gereDonne.rchRange(lrech, new Meteo(), fields.get(0), null);
                else
                    rchBoolean = (ArrayList) gereDonne.rchBoolean(lrech, entcl, fields, precis, genre);
            }
            else if(rech instanceof String)
            {
                rchBoolean =(ArrayList) gereDonne.rchPhrase((String)rech,(RsEntite)entcl, fields.get(0));
            }
            else if(rech instanceof RsEntite)
            {
                lrech=new ArrayList(2);
                RsEntite entite=(RsEntite) rech;
                if(entite instanceof Ligne)
                {
                    Ligne lg=(Ligne) entite;
                    lg.getNom();
                    lg.getAdresse();
                    
                    //recherche sur l'adresse
                        String[] split =  lg.getAdresse().split(" ");
                        
                        
                          String ville;
                          if(split[0].equals("place"))
                          {
                              ville=split[2];
                          }else
                          {
                              ville=split[1];
                          }
                          //secteur=split[0];
                          
                        lrech.add(lg.getNom());lrech.add(ville);
                        
                }
                try{
                rchBoolean = (ArrayList) gereDonne.rchBoolean(lrech, rech, fields, precis, genre);
                }catch(NullPointerException npex)
                {
                    rchBoolean=new ArrayList(0);
                }
            }
            else if(rech instanceof Coordinate ||rech instanceof Coord)
            {
                Coord c=new Coord();
                if(rech instanceof Coordinate)
                {
                    Coordinate co=(Coordinate) rech;
                    c.setLatitude(co.getLatitude());c.setLongitude(co.getLongitude());
                }
                else{
                    c=(Coord) rech;
                }
                    
                
                
                lrech=new ArrayList<Double>(2);
                    lrech.add(c.getLatitude());lrech.add(c.getLongitude());

                rchBoolean = (ArrayList) gereDonne.rchBoolean(lrech, c, fields, precis, genre);
            }
            

       
            
        }catch(org.hibernate.search.SearchException sex)
        {
            
        }catch(Exception ex)
        {
           if("Session is closed!".equals(ex.getMessage())||!bex )
           {
              GereKml gex=new GereKml(ex);
               gex.rchMulti(rech, entcl, fields, precis, genre);
           }else
           {
               Xcption.Xcption("GereKml rchMulti Exception ",ex.getMessage()+rchBoolean + ex);
                rchBoolean=new ArrayList<RsEntite>(0);
           }
            
            
        }
        return rchBoolean;
    }
    
    public Object castRsEntite(String entcl)
    {
        Object e = null;
        if(entcl.equals("tempkml"))
        {
            e = new TempKml();
        }else if(entcl.equals("ligne"))
        {
            e = new Ligne();
        }else if(entcl.equals("localite"))
        {
            e = new Localite();
        }else if(entcl.equals("tour"))
        {
            e = new run.ejb.entite.geo.Tour();
        }else if(entcl.equals("prmtr"))
        {
            e = new Prmtr();
        }else if(entcl.equals("compte"))
        {
            e = new Compte();
        }else if(entcl.equals("point"))
        {
            e = new Pt();
        }else if(entcl.equals("coord"))
        {
            e = new Coord();
        }else if(entcl.equals("evenement"))
        {
            e = new Evnmt();
        }else if(entcl.equals("meteo"))
        {
            e = new Meteo();
        }else if(entcl.equals("prevMt"))
        {
            e = new PrevMt();
        }
        
        return e;
    }
    @Override
    public List<RsEntite> rchIds(List<Long> lids)
    {
        List<RsEntite> rch=new ArrayList(lids.size());
        lids.stream().
                map(id->gereDonne.rchLuc(String.valueOf(id), new Ligne(), "id")).
                forEach(rch::addAll);
       /* for(Long id:lids)
        {
            rch.addAll(gereDonne.rchLuc(String.valueOf(id), new Ligne(), "id"));
        }*/
        
        return rch;
    }
    @Override  
    public ArrayList rchBdd(Object recherche, String entcl, String ctgrch, String field)
    {//reponseBean slRing
        Object e =null;
        ArrayList rchLuc = new ArrayList();
     
        e=castRsEntite(entcl);
      
        try {
            if(recherche!=null&&recherche!="")
                if(ctgrch.equals("otocple"))
                {// categorie AUTOCOMPLETE

                    rchLuc = gereDonne.rchWild((String) recherche, e, field);
                }            
                else if(ctgrch.equals("fix"))
                {// categorie simple
                    String r ;
                    if(recherche instanceof java.util.Date && (field.equals("debut")||field.equals("fin")))
                    {
                         r = DateTools.dateToString((java.util.Date)recherche, DateTools.Resolution.DAY);
                    }
                    else
                        r=(String) recherche;


                    /* r=(String) recherche;
                     split = r.split(split[0]);
                    if(split.length>1)
                    {
                        recherche=split[1];
                    }*/
                   rchLuc = gereDonne.rchLuc(r, e, field);
                }else if(ctgrch.equals("aprx"))
                {// categorie aproximatif fix sur l'adresse ou a 0.5  
                    recherche=recherche+"~";
                   rchLuc = gereDonne.rchFuzzy((String) recherche, e, field, 0.9f); 
                }else if(ctgrch.equals("contains"))
                {// categorie aproximatif fix sur l'adresse ou a 0.5 
                    recherche="~"+recherche+"~";
                   rchLuc = gereDonne.rchFuzzy((String) recherche, e, field, 1f); 
                }else if(ctgrch.equals("ad"))
                {//// categorie ville et son entourage

                    String precis;
                     
                    List<Object> rc=new ArrayList(2);
                    List<String> lF=new ArrayList(2);


                    String f=(String) recherche;
                    String ville = null;
                    String secteur;
                    if(!f.contains("Sud"))
                    {


                    String[] split = f.split(" ");

                        if(split.length>2)
                        {
                            if(split[0].equals("place"))
                            {
                                ville=split[2];
                                //secteur=split[1];
                            }
                            else
                            {
                                ville=split[1];
                                //secteur=split[0];
                            }

                        }else if(split.length>1)
                        {
                            rc.add(ville);
                            ville=split[0]+split[1];
                        }
                        else{
                            ville=(String) recherche;
                        }
                    }else{
                        ville=(String) recherche;
                    }   

                        rc.add(ville);
                    String[] spltvil = ville.split("-");
                        if(spltvil.length>1)
                        {

                            ville=spltvil[0]+spltvil[1];
                        }
                        rc.add(ville);
                        lF.add("nom");lF.add("adresse");
                        //temp
                        precis=(String) rc.get(0);
                        if(precis.contains("Sud"))
                        {
                            precis="MUST_MUST";
                        }
                        else if(rc.get(0).equals(rc.get(1))&&!precis.split("Saint").equals(precis))
                        {
                            precis="SHOULD_MUST";
                        }
                        else
                        {
                            precis="SHOULD_MUST";
                        }
                        
                rchLuc = gereDonne.rchBoolean(rc, e, lF, precis, null);
                /* rchLuc= gereDonne.rch((String) ville, e, field);
                RsEntite entite=null;
                for(Iterator<RsEntite> iter=rchLuc.iterator();iter.hasNext();)
                {
                RsEntite next = iter.next();
                if(next.getNom().equals(ville))
                {
                entite=next;
                }
                }
                String adresse = entite.getAdresse();
                secteur=adresse.split("île de la Reunion ")[1].split(" ")[0];
                rc.set(1, adresse+" "+ville);
                if(field!=null)
                {
                temp.addAll(
                gereDonne.rchBoolean(rc, e, lF, precis)//sup a 1
                );
                }
                //suppr doublons
                for(Iterator iter=rchLuc.iterator(); iter.hasNext();)
                {
                Object next = iter.next();
                if(temp.contains(next))
                {
                temp.remove(next);
                }
                }
                rchLuc.addAll(0,temp);
                if(rchLuc.contains(entite))
                {
                rchLuc.remove(entite);
                }
                rchLuc.add(0, entite);*/
                }

        } catch (Exception ex) 
        {
           if("Session is closed!".equals(ex.getMessage())||!bex )
           {
              GereKml gex=new GereKml(ex);
               rchLuc=gex.rchBdd(recherche,  entcl,  ctgrch,  field);
           }else
           {
               Xcption.Xcption("GereKml rchBdd Exception ",ex.getMessage()+" : "+recherche +"  _ "+entcl+"  _ "+ctgrch+"  _ "+ field);
                if(rchLuc!=null)
                    rchLuc=new ArrayList(0);
           }
            
            
        }
        
        
        
        return rchLuc;
    }
   public void boolcreateObj(boolean b)
   {
       bc=b;
   }
   
    @Override
    public Object addBdd( Object entite) throws javax.persistence.PersistenceException
    {
        boolean cfm=false;
        
            //gereDonne=new GereDonne();
            
            
            Xcption.Xcption("GereKml addBdd new GereDonne() "+bc,"creation "+entite.getClass().getCanonicalName());
        
  
            if(entite instanceof RsEntite)
            {
                RsEntite rs=(RsEntite) entite;
             if(rs.getNom()!=null)
                if(!rs.getNom().isEmpty() && !rs.getNom().equals(" "))
                    cfm=true;
            }else if(entite instanceof IntBaz)
            {
                IntBaz ib=(IntBaz) entite;
             if(ib.getNom()!=null)
                if(!ib.getNom().isEmpty() && !ib.getNom().equals(" "))
                    cfm=true;
            
            }else if(entite instanceof TempKml)
            {
                TempKml tmp=(TempKml) entite;
             if(tmp.getRch()!=null)
                if(!tmp.getRch().isEmpty() && !tmp.getRch().equals(" "))
                    cfm=true;
            }else if(entite instanceof Meteo)
            {
                Meteo tmp=(Meteo) entite;
             if(tmp.getJour()!=null)
                    cfm=true;
            }else if(entite instanceof PrevMt)
            {
                PrevMt tmp=(PrevMt) entite;
             if(tmp.getJour()!=null)
                    cfm=true;
            }else if(entite instanceof Compte)
            {
                Compte tmp=(Compte) entite;
                if(tmp.getNom()!=null&&tmp.getPassword()!=null)
                    cfm=true;
            }
            else if(entite instanceof Evnmt)
            {
                Evnmt tmp=(Evnmt) entite;
                if(tmp.getNom()!=null&&tmp.getLieux()!=null)
                    cfm=true;
            }
                   
                           
                            try{
                              
                                
                                if(cfm)
                                  entite=gereDonne.createObj(entite, bc);
                           
                                    
                            }catch(Exception ex)
                            {
                                
                                
                                cfm=false;
                                gereDonne.finish();
                           
                                gereDonne.createEM();
                                entite=null;
                                System.err.print("GereKml addBdd Exception ");
                                
                                Xcption.Xcption("creation "+entite.getClass().getCanonicalName(),ex.getMessage());

                            }
                           
                 
        return entite;
    }
    @Override
    public RsEntite findrs(RsEntite rs)
    {
        return gereDonne.getEm().find(rs.getClass(), rs.getId());
    }
    
    @Override
    public  Object find(Object o, long id)
    {
        return gereDonne.getEm().find(o.getClass(), id);
    }
    @Override
    public boolean supBdd(Object entite)
    {
        boolean rtr=false;
       
        System.gc();
        gereDonne.createEM();
        long id = 0;
        if(entite instanceof RsEntite)
        {   
            RsEntite rs=(RsEntite) entite;
            
                List rchBdd = null;
                try {
                     chTout(new TempKml()).stream().
                            map(gereDonne::deleteObject);
                } catch (Exception ex) {
                    
                        Xcption.Xcption("supBdd(Object entite) TempKml Exception",ex.getMessage());
                }
              
                
               
            rs = (RsEntite) gereDonne.getEm().find(rs.getClass(), rs.getId());
           
            rtr = gereDonne.deleteObject(rs);
            gereDonne.finish();
             gereDonne.createEM();
        }else if(entite instanceof IntBaz)
        {
            IntBaz ib=(IntBaz) entite;
            ib = (IntBaz) gereDonne.getEm().find(ib.getClass(), ib.getId());
            rtr = gereDonne.deleteObject(ib);
            gereDonne.finish();
             gereDonne.createEM();
        }else
        {
            
           rtr = gereDonne.deleteObject(entite); 
           gereDonne.finish();
             gereDonne.createEM();
        }
        
       return false;
    }
    @Override
    public void dcpchg(TreeNode  noeud)
    {
        charge.dcplance(noeud);
        List<TreeNode> lparent= new ArrayList<TreeNode>();
        TreeNode inter=noeud;
        while(!lparent.contains(root))
        {
            inter=inter.getParent();
            lparent.add(inter);
            
        }
        
        //supNode(lparent.size(), root, null);
        
    }
    
    private void supNode(int nbl, TreeNode inter, TreeNode p)
    {
        if(nbl>0)
        {
            nbl--;
            List<TreeNode> children = inter.getChildren();
           for(int i=0;i<children.size(); i++)
            {
                
                supNode(nbl, children.get(i), inter);
            } 
        }else
        {
            p.getChildren().remove(inter);
        }
        
    }
 
    /*public void chargeKml(File fichier, boolean[] bchg) 
    {//boolean[]: 0nmad 1nmdesc 2nmco 3adco 4addesc 5descco

        
        Kml kml;
           
         try{
          
              kml = Kml.unmarshal(fichier);

         document = (de.micromata.opengis.kml.v_2_2_0.Document)kml.getFeature();
         charge=new ChargeKml(document);
         
         for(int i=0; i<bchg.length; i++)
        {
            if(bchg[i]==true)
            {
                charge.setBchg(bchg);
            }
        }
         
         charge.lancenew();
         root=charge.getRoot();
         }catch(NullPointerException npe)
         {
             System.err.println("NullPointerException");System.out.println(npe.getMessage());
        
         }catch(Exception ex)
         {
             System.err.println("Systeme de fichier ne correspondant pas a la norme : \"http://www.opengis.net/kml/2.2\""+ex.getMessage());
         }
	        
                
	try {
            ArrayList<RsEntite> chchTout = (ArrayList<RsEntite>) gereDonne.chchTout(new Ligne());
            System.out.println("cherche tout"+chchTout);
        } catch (Exception ex) {
            System.err.print("GereKml charge rch * Exception ");System.out.println(""+ex.getMessage());
        }       
    }*/

    @Override
    public List chTout(Object entite) throws Exception
    {
        if(entite instanceof String)
            entite=castRsEntite((String) entite);
        
        return gereDonne.chchTout(entite);
    }
    
    @Override
     public String createTracerbyRs(RsEntite rs)
    {
        Kml msh=new Kml();
        Ref ref=new ObjetKml();
        
        
        Ligne ligne = null;
        Pt pt=null;
        
        boolean blieu=false;
       List<String> lprmtr = run.ejb.base.Variable.getPrmtr().subList(6, 9);
       for(String str:lprmtr)
            if(rs.getAdresse().contains(str))
                blieu=true;
       
            Placemark l=null;
            if(blieu)
            {
                ref=new ObjetKml("50002814","641400FF",null,null);
            }
            else
                if(rs instanceof Ligne)
            {
                ref=new LigneKml(3.6d, "54140078");
               
            }else
                if(rs instanceof Pt)
            {
               ref=new IconKml(3.6d,"imageurl");   
            }
            
            RsnsObj schema=new RsnsObj("tour", ref,"tour");
        List list=new ArrayList(1);
            list.add(rs);
            List lc=new ArrayList(1);
                lc.add(list);
                schema.setPourKml(lc);
              
                TracerRsns trc =new TracerRsns();
                    trc.createTracerbySchema(schema);
                       
                        
            Document finish = trc.finish();
                            msh.setFeature(finish);
                        StringWriter stringWriter = new StringWriter();
                            msh.marshal(stringWriter);
                            
                        tracer=stringWriter.getBuffer().substring(0);
                      
        tour= formTourbyRs(rs);
        
        
        return tracer;
    }
    @Override
    public ArrayList createTracer(String dmd, String field)
    {
        Kml msh=new Kml();
        TracerRsns trc =new TracerRsns();
        Ligne ligne = null;
        Pt pt=null;
        ArrayList rech=null;
       try{
         rech = rchBdd(dmd,"ligne","fix", field);
       }catch(Exception ex)
       {//si point
            rech = rchBdd(dmd,"point","fix", field);
       }
        if(rech!=null) if(!rech.isEmpty())
        {
            Placemark l=null;
            if(rech.get(0) instanceof Ligne)
            {
                ligne=(Ligne) rech.get(0);
                 l= trfOj(ligne, msh, true);
                 trc.setCouleur("ff"+color[chCouleur(ligne)]);
                 trc.setTaille(2.1f);
            }else
            if(rech.get(0) instanceof Pt)
            {
                pt=(Pt) rech.get(0);
                 l= trfOj(pt , msh, true);
                 
                 trc.setCouleur("ff"+color[chCouleur(pt)]);
                 
            }
            
                        Feature feat= l;

                        trc.setPlan(feat);

                        trc.formeTracer();
                        
            Document finish = trc.finish();
                            msh.setFeature(finish);
                        StringWriter stringWriter = new StringWriter();
                            msh.marshal(stringWriter);
                            
                        tracer+=stringWriter.getBuffer().substring(0);
                        createLatLng(l);
                        
        }
        
        return rech;
    }

    @Override
    public String createIti(ArrayList<Coord> lCo)
    {
        
        Kml kml = new Kml();
        TracerRsns trc =new TracerRsns();
        Folder folder = kml.createAndSetFolder();
        Placemark centrage = kml.createAndSetPlacemark();
        
        LineString lLoc = centrage.createAndSetLineString();
        List<Coordinate> lcoLoc = lLoc.createAndSetCoordinates();
            
       for(int i=0; i<lCo.size(); i++)
       {
       Placemark plcmk = kml.createAndSetPlacemark();
       Point point = plcmk.createAndSetPoint();
       List coordinats =  point.createAndSetCoordinates();
            Coord get = lCo.get(i);
            coordinats.add(new Coordinate(get.getLongitude(), get.getLatitude()));
            folder.addToFeature(plcmk);
            
            lcoLoc.add(new Coordinate(get.getLongitude(), get.getLatitude()));
       }
        
        Feature feat= folder;

                        trc.setCouleur("ff"+"8F459C");
                        trc.setTaille(2.1f);
                        trc.setPlan(feat);

                          TourRsns rsns=new TourRsns(kml, centrage,this);
                        rsns.setBiti(true);
      
                            //HabVue(rsns.getFeat());
                       
                        de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour = rsns.formeTour();  
                        
                        
                        //trc.addlFeat(formeTour);
                        trc.formeTracer();
        Document finish = trc.finish();
        StringWriter stringWriter = new StringWriter();
                        kml.setFeature(finish);
                        kml.marshal(stringWriter);
                        tracer=stringWriter.getBuffer().substring(0);
                        
                        Xcption.Xcption("GereKml createTracer tracer", String.valueOf(stringWriter.getBuffer().substring(0)));
    
                        try{
                           kml.setFeature(formeTour);
                        stringWriter = new StringWriter();
                            kml.marshal(stringWriter);    
                        tour=stringWriter.getBuffer().substring(0);
                        createLatLng(centrage);
                        }catch(Exception ex)
                        {
                            System.out.println(ex.getMessage());
                        }

        return stringWriter.getBuffer().substring(0);
     }
    
    private List<RsEntite> AjustByCrit(List<RsEntite> rech)
    {
        List<RsEntite> retour=new ArrayList();
        for(Iterator<RsEntite> iter=rech.iterator(); iter.hasNext();)
        {
            RsEntite next = iter.next();
                String description = next.getDescription();
                if(description.split("activité").length>1)
                {
                    for(Iterator<String> iterC=slCrit.iterator(); iter.hasNext();)
                    {
                        String[] split = description.split(iterC.next());
                        if(split.length>1)
                        {
                            retour.add(next);
                        }
                    }
                }
        }
        return retour;
    }
   
    public void createJsonKml()
    {
        
    }
  
    @Override
    public String formTourbyRs(RsEntite rch)
    {
        String rtr=null;
        ArrayList rchBdd = rchBdd("tour :"+rch.getAdresse(), "tour", "fix", "nmTour");
       Kml msh=new Kml();
       de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour = null;
       TourRsns rsns=new TourRsns(msh, null,this);
                    try{
                            if(rchBdd.isEmpty())
                            {


                                     rtr=rsns.ctourbyRs(rch);
                                    


                            }else
                            {

                                    formeTour = rsns.formeTour(rchBdd);
                                    msh.setFeature(formeTour);
                                    StringWriter stringWriter = new StringWriter();
                                        msh.marshal(stringWriter);
                                        
                                        rtr=stringWriter.getBuffer().substring(0);

                            }
                        }catch(Exception ex)
                            {
                                System.out.println(ex.getMessage());
                            }
                        //HashMap<Integer, Coordinate> mapPente = rsns.getMapPente();  
                        
                       
                        

                           
                 return rtr;
    }
    @Override
    public List<Long>  createKml(Object res, String field, PrmtrSchm schema)
    {
        List<Long> rtr=new ArrayList<Long>();
        RsEntite rsentite = null;
       
        float tailleautour=1.7f;
        tracer="";
        tour="";
       
        List<Feature> lfMeteo = null;
            
        Kml msh=new Kml();
        ArrayList<RsEntite> rech = null;
         List<RsEntite> rchAd = null;
        System.err.println("create kml");
        
        RsEntite rsdetour=null;
        TracerRsns trc=null;
        cEm();
     if(schema==null)
     {
         if(res instanceof String)
        {
            if(res.equals(""))
            {//a l'initialisation
                try {
                    
                    rech=new ArrayList(chTout(new Ligne()));
                } catch (Exception ex) {
                    Logger.getLogger(GereKml.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(field!=null&&!"point".equals(field)) 
            {//selectOneMenu
                rchAd = new ArrayList(
                        rchBdd(res,"ligne","aprx", field)
                        );
                if(bcrit)
                {
                    rchAd .addAll(rchBdd(res,"point","aprx", field));
                }
                        
                        
                rech= new ArrayList(
                        rchBdd(res,"ligne","fix", field)
                        );
                
            }
            else if(res!=null&&res!="rien") 
            {//liste fix recherche du tracer 
                
                String[] bs={"sentier","voie","route", "place"};
                String s=(String) res;
              for(int i=0; i<bs.length; i++)  
              {
                    String[] split = s.split(bs[i]);
                if(!split[0].equals(s))
                {
                    s= split[split.length-1];
                }
              }
             try{ 
                 this.gereDonne=new GereDonne();
                 gereDonne.createEM();
                 if("point".equals(field))
                 {
                     rech=gereDonne.rch(s, new Pt(), "nom");
                 }else
                 {
                     rech=gereDonne.rch(s, new Ligne(), "nom");
                 }
                     
              
                /*rech = new ArrayList(
                        rchBdd(s,"ligne","fix", "nom")
                        );*/
             }catch(Exception npex)
             {
                 rech = new ArrayList(
                        AjustByCrit(rchBdd(s,"point","fix", "nom"))
                        );
                 rech.addAll(
                         AjustByCrit(rchBdd(s,"point","fix", "nom"))
                         );
             }
                if(bcrit)
                {
                    rech = new ArrayList(
                        AjustByCrit(rchBdd(s,"point","fix", "nom"))
                        );
                }
            }else if("point".equals(field))
            {
                this.gereDonne=new GereDonne();
                gereDonne.createEM();
                rech=gereDonne.rch(res, new Pt(), "nom");
            }
         
           rsentite=rech.get(0);
        }else if(res instanceof ArrayList )
        {//cas de RsEntite dans la list
            if(field!=null)
                if(field.equals("tout"))
                {
                    tailleautour=2.1f;
                }else if(field.equals("description"))
                {
                    tailleautour=3f;
                }else if(field.equals("date"))
                {
                    tailleautour=5f;
                }
            rech=(ArrayList<RsEntite>) res;
          rchAd=new ArrayList<RsEntite>();
        }else if(res instanceof RsEntite)
        {
            rech=new ArrayList<RsEntite>();
            rech.add((RsEntite)res);
            rchAd=new ArrayList<RsEntite>();
            res=rech.get(0).getNom();
            
           
        }
        
        ///////////////////////////////////////////////
            //instance de lancement
        //////////////////////////////////////////////    
            
       
            if(rech!=null)
            { 
                
                Xcption.Xcption("gereKml createKml rech", String.valueOf(rech));
            
                  rsdetour=(RsEntite) rech.get(0);
                   
                   /*if(rchAd==null||rchAd.isEmpty())
                   {

                    String[] splitAd = rsdetour.getAdresse().split(" ");
                    if(splitAd[0].equals("place")||splitAd[1].equals("place")||field!=null)
                    {
                        String rh=splitAd[1]+" "+splitAd[2];
                       
                        
                        rchAd =  rchBdd(rh, "ligne", "ad", "adresse");
                        if(bcrit)
                        {
                            rchAd.addAll(AjustByCrit(rchBdd(rh, "point", "ad", "adresse")));
                        }
                        System.err.print("GereKml createKml recherche famille : split "+splitAd[splitAd.length-1]); System.out.println("rh "+rh+"  "+rchAd);
                        
                    }
                    else
                    {
                        
                        String rcad=splitAd[0]+" "+splitAd[1];
                        
                        
                        rchAd= rchBdd(rcad, "ligne", "ad", "adresse");
                        if(bcrit)
                        {
                            rchAd.addAll(AjustByCrit(rchBdd(rcad, "point", "ad", "adresse")));
                        }
                        
                    }
                   }*/ 
                
                 trc=new TracerRsns();
                   
                
                //recherche famille tracer
                if(!rchAd.isEmpty()) 
                {
                    rech=(ArrayList<RsEntite>) (List)rech;
                rchAd=(ArrayList<RsEntite>) (List)rchAd;
                    rchAd.remove(rsdetour);
                    
                    
                        for(int i=0; i< rchAd.size(); i++)
                        {
                            Placemark l= null;
                             if(rchAd.get(i) instanceof Ligne)
                               {
                                    rsdetour =  (Ligne) rchAd.get(i);
                                     l= trfOj((Ligne) rsdetour, msh, false);
                                     
                                      Feature feat= msh.getFeature();
                               
                                      trc.setCouleur("56"+color[chCouleur(rsdetour)]);
                                    trc.setTaille(tailleautour);
                                    trc.setPlan(feat);
                               }else if(rchAd.get(i) instanceof Pt)
                               {
                                   rsdetour =  (Pt) rchAd.get(i);
                                    l= trfOj((Pt) rsdetour, msh, false);
                                    
                                     Feature feat= msh.getFeature();

                                    //trc.fontPoint();
                                   //feat= trc.setformePoint(feat);
                                    trc.setPlan(feat);
                               }
                       
                                
                               

                        }
                        
                }
                Placemark tmp=new Placemark();
                    tmp.setName("tour : ");
                    LineString lineString = tmp.createAndSetLineString();
                        
                    for(RsEntite rs:rech)
                        {
                            tmp.setName(
                                        tmp.getName()+" "+rs.getNom());
                            Placemark pl= null;
                             if(rs instanceof Ligne)
                               {
                                   
                                   Ligne lgn=(Ligne) rs;
                                  
                                     pl= trfOj((Ligne) rs, msh, false);
                                Coord get = Lists.newArrayList(lgn.getCoords()).get(
                                                     (int)lgn.getCoords().size()/2);
                                      Feature feat= msh.getFeature();
                                       lineString.addToCoordinates(
                                              get.getLongitude(),get.getLatitude());
                                      trc.setCouleur("56"+color[chCouleur(rs)]);
                                    trc.setTaille(tailleautour);
                                    trc.setPlan(feat);
                               }else if(rs instanceof Pt)
                               {
                                   Pt pt=(Pt) rs;
                                    pl= trfOj((Pt) rs, msh, false);
                                    if(pt.getLatitude()!=null&&pt.getLongitude()!=null)
                                        lineString.addToCoordinates(pt.getLongitude(),pt.getLatitude());
                                     Feature feat= msh.getFeature();

                                    trc.setPlan(feat);
                               }
                       
                                  
                        }
                msh.setFeature(tmp);
            }
     }else
     {
        trc=new TracerRsns();
        
        trc.setSchema(schema);
        List<RsnsObj> lschmvu = schema.getLschmvu();
        
        lschmvu.forEach(trc::createTracerbySchema);
              /*  for(RsnsObj rsnsSchema:lschmvu)
                {
                    trc.createTracerbySchema(rsnsSchema);
                }*/
        rtr=trc.getLid();
            // lfMeteo = trc.getlFMeteo();
       
        boolean tr=false;
        
        tr=lschmvu.stream().anyMatch((rsschm)->
                rsschm.getRef().contains("tour")
                        &&Optional.ofNullable(rsschm.getPourKml()).isPresent());
        /*for(RsnsObj rsschm:lschmvu)
            if(rsschm!=null)
                if(rsschm.getRef()!=null&&rsschm.getPourKml()!=null)
                if(rsschm.getRef().contains("tour")&&rsschm.getPourKml().get(0)!=null)
                    tr=true;*/
        if(tr)
             msh.setFeature(trc.getRetour());
        else
           {
               Folder doc=new Folder();
               doc.setFeature(trc.getlFeat());
               msh.setFeature(doc);
           }
     }
     
     
                //creation pour tracer et tour
                 
                        Placemark l = null;
                        
                        trc.formeTracer();
                        
                              Feature feat= msh.getFeature();
                             
        
                            msh=new Kml();
                            StringWriter stringWriter=new StringWriter();
                        TourRsns rsns=new TourRsns(msh, feat,this);
                            rsns.setSchema(schema);
                            //HabVue(rsns.getFeat());
                        de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour = null;
                    
                        try{
                            //formeTour.addToFeatureSimpleExtension(rsns.addFeat(lfMeteo));
                            formeTour = rsns.formeTour();
                            
                        }catch(Exception ex)
                        {
                            System.out.println(ex.getMessage());
                        }

                        //HashMap<Integer, Coordinate> mapPente = rsns.getMapPente();  
                        
                       
                        msh.setFeature(formeTour);
                         stringWriter = new StringWriter();
                            msh.marshal(stringWriter);

                            tour+=stringWriter.getBuffer().substring(0);
                             msh=new Kml();
                            // trc.setPlan(formeTour);
                        Document finish = trc.finish();
                            msh.setFeature(finish);
                         stringWriter = new StringWriter();
                            msh.marshal(stringWriter);
                            
                            tracer+=stringWriter.getBuffer().substring(0);
                          /*if(!lfMeteo.isEmpty())
                            {
                                Folder dossier=new Folder();
                                Document doc=new Document();
                                doc.setOpen(true);
                                doc.setName("Météo");
                                    dossier.setFeature(lfMeteo);
                                    doc.addToFeature(dossier);
                                    
                                msh=new Kml();
                                msh.setFeature(doc);
                                stringWriter = new StringWriter();
                                msh.marshal(stringWriter);
                                    meteo+=stringWriter.getBuffer().substring(0);
                                
                            }   /   
                            
                            
                       
                        
                        
                    
                        
                        trc.setPlan(feat);
                        if(feat instanceof de.micromata.opengis.kml.v_2_2_0.Document)
                        {
                            de.micromata.opengis.kml.v_2_2_0.Document doc=
                                    (de.micromata.opengis.kml.v_2_2_0.Document) feat;
                            
                            for(Feature f:doc.getFeature())
                                if(f instanceof Placemark)
                                    createLatLng((Placemark) feat);
                        }else if(feat instanceof Placemark)
                            createLatLng((Placemark) feat);
                        
                       /*  TourRsns rsns=new TourRsns(msh, feat);
                       if(res instanceof Ligne) 
                            rsns.setLigne((Ligne) res);
                            //HabVue(rsns.getFeat());
                         formeTour = null;
                        try{
                            formeTour = rsns.formeTour();
                        }catch(Exception ex)
                        {
                            System.out.println(ex.getMessage());
                        }
                        
                        trc.formeTracer();
                        
                         finish = trc.finish();
                            msh.setFeature(finish);
                         stringWriter = new StringWriter();
                            msh.marshal(stringWriter);

                            tracer+=stringWriter.getBuffer().substring(0);
                            msh=new Kml();
                            stringWriter=new StringWriter();
                        

                        HashMap<Integer, Coordinate> mapPente = rsns.getMapPente();       
                        msh.setFeature(formeTour);
                            msh.marshal(stringWriter);    
                        tour=stringWriter.getBuffer().substring(0);
                        System.out.println("trcer kml : "+tracer);
                        System.out.println("tour kml : "+tour);
                        System.out.println("tour et tracer creer");*/
                        
                        
                    
                    
               
            
           
            
        
        
            bclim=false;
            fEm();
        return rtr;
        
    }
    private void chMil(List<Coordinate> lcod)
    {
        String[] min=new String[2];String[] max=new String[2];
        min[0]="0";min[1]="40";max[0]="0";max[1]="40";
        for(Coordinate next:lcod)
        {
            
            if(Double.parseDouble(max[0])<Math.abs(next.getLatitude()))
            {
                max[0]=String.valueOf(next.getLatitude());
                
            }
            else if(Double.parseDouble(min[0])>Math.abs(next.getLatitude()))
            {
                min[0]=String.valueOf(next.getLatitude());
            }
            
            if(Double.parseDouble(max[1])<Math.abs(next.getLongitude()))
            {
                max[1]=String.valueOf(next.getLongitude());
            }
            else if(Double.parseDouble(min[1])>Math.abs(next.getLongitude()))
            {
                min[1]=String.valueOf(next.getLongitude());
            }
           if(!"0".equals(max[0])&&!"0".equals(min[0])) 
           {
                latlng[0]=String.valueOf(
                        (Double.parseDouble(max[0])+Double.parseDouble(min[0]))
                        /2
                        );
           }else if(!"0".equals(max[0]))
           {
               latlng[0]=max[0];
           }else if(!"0".equals(min[0]))
           {
               latlng[0]=min[0];
           }
           
           
           if(!"0".equals(max[1])&&!"0".equals(min[1])) 
           {
                latlng[1]=String.valueOf(
                        (Double.parseDouble(max[1])+Double.parseDouble(min[1]))
                        /2
                        );
           }else if(!"0".equals(max[1]))
           {
               latlng[1]=max[1];
           }else if(!"0".equals(min[1]))
           {
               latlng[1]=min[1];
           }
           
              
           
        }
        
    }    
    private void createLatLng(Placemark plc)
    {
        latlng=new String[2];
        LatLng chlatlng = new LatLng();
                              if(plc.getGeometry() instanceof LineString)
                              {
                                  LineString lstrg= (LineString) plc.getGeometry();
                                  if(lstrg.getCoordinates()!=null)
                                  {
                                    chMil(lstrg.getCoordinates()); 
                                    
                                  }
                              }
                              else if(plc.getGeometry() instanceof LinearRing)
                              {
                                  LinearRing lrg= (LinearRing) plc.getGeometry();
                                  if(lrg.getCoordinates()!=null)
                                  {
                                    chMil(lrg.getCoordinates()); 
                                    
                                  }
                              }else if(plc.getGeometry() instanceof Point)
                              {
                                  Point pt= (Point) plc.getGeometry();
                                  if(pt.getCoordinates()!=null)
                                  {
                                    chMil(pt.getCoordinates()); 
                                    
                                  }
                              }
                           
                        
    }
    private int chCouleur(RsEntite entite )
    {
        int ch=0;
        if(entite.getNom().split(" ")[0].equals("zone")&& entite.getAdresse().split(" ")[0].equals("place"))
        {
            ch=4;
        }
        else  if(entite.getAdresse().split(" ")[0].equals("place"))
        {
            ch=3;
        }else if(entite.getNom().split(" ")[0].equals("sentier"))
        {
            ch=0;
        }else if(entite.getNom().split(" ")[0].equals("route"))
        {
            ch=1;
        }else if(entite.getNom().split(" ")[0].equals("voie"))
        {
            ch=2;
        }
        return ch;
    }
     private void HabVue(Feature feat)
    {
         //Create a style map.
        StyleMap styleMap = feat.createAndAddStyleMap();

        //Create the style for normal edge.
        Pair pNormal = styleMap.createAndAddPair();
        Style normalStyle = pNormal.createAndSetStyle();
        pNormal.setKey(StyleState.NORMAL);

        LineStyle normalLineStyle =
            normalStyle.createAndSetLineStyle();
        normalLineStyle.setColor("YELLOW");
        normalLineStyle.setWidth(2);


        //Create the style for highlighted edge.
        Pair pHighlight = styleMap.createAndAddPair();
        Style highlightStyle = pHighlight.createAndSetStyle();
        pHighlight.setKey(StyleState.HIGHLIGHT);

        LineStyle highlightLineStyle =
            highlightStyle.createAndSetLineStyle();
        highlightLineStyle.setColor("WHITE");
 
        highlightLineStyle.setWidth(1);

    }

     private Placemark trfOj(RsEntite ob, Kml msh, boolean chmil)
    {
   
        
        
         Placemark plmk = msh.createAndSetPlacemark();
         String[] split={" "};
         if(ob.getAdresse()!=null)
         {
             split = ob.getAdresse().split(" ");
         }
        
          if(split[0].equals("place")||split[0].equals("secteur"))
          {
              Ligne fig=(Ligne) findrs(ob);
              if(fig.getCoords()!=null)
              {
                  LinearRing lr =  plmk.createAndSetLinearRing();


                  lr=(LinearRing) trCoFg(
                          new HashSet<Coord>(fig.getCoords()), lr);

                  plmk.setName(fig.getNom());
               //   plmk.setDescription(fig.getDescription());
                  plmk.setAddress(fig.getAdresse());
              }



          }
        else if(ob instanceof Ligne)
        {
            Ligne lgn=(Ligne)findrs(ob);
            
            if(lgn.getCoords()!=null)
            {
                LineString ls =(LineString) plmk.createAndSetLineString();
           
                List<Coordinate> lc = ls.createAndSetCoordinates();
            
            
                ls= trCoLg(
                        lgn.getCoords(), ls);
                
                plmk.setName(lgn.getNom());
               // plmk.setDescription(lgn.getDescription());
                plmk.setAddress(lgn.getAdresse());
            }
            if(chmil)
                createLatLng(plmk);
          
        }
        else if(ob instanceof Pt)
        {
            Pt pt=(Pt) findrs(ob);
            
            if(pt.getLatitude()!=null&&pt.getLongitude()!=null)
            {
                Point point = plmk.createAndSetPoint();
            List<Coordinate> lc = point.createAndSetCoordinates();
            
                
                Coordinate coordinate =null;
                // coordinate = new Coordinate(coord.getNom());
                if(pt.getAlt()!=null)
                {
                    coordinate=new Coordinate(pt.getLongitude(),pt.getLatitude(),pt.getAlt());
                }
                else
                {
                    coordinate=new Coordinate(pt.getLongitude(),pt.getLatitude());
                }
          latlng=new String[2];
                    lc.add(coordinate);
                    latlng[0]=String.valueOf(coordinate.getLatitude());
                    latlng[1]=String.valueOf(coordinate.getLongitude());
                point.setCoordinates(lc);
                plmk.setName(pt.getNom());
                plmk.setDescription(pt.getDescription());
                plmk.setAddress(pt.getAdresse());
            }
             
        }
        else 
        {
            plmk.setName(ob.getNom());
            plmk.setDescription(ob.getDescription());
            plmk.setAddress(ob.getAdresse());
        }
      
        
        
        return plmk;
    }
     
      public LineString trCoLg(List<Coord> lco, LineString geom)
    {
        Map<Long, Coordinate> orderList=new HashMap<Long, Coordinate>();
       
        
      
        
        Xcption.Xcption("lco", lco.toString());
         if(!lco.isEmpty())       
         {
       
            for(Coord co:lco)
                if(co.getAlt()!=null)
                    {
                        orderList.put(co.getId(), new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt()));
                        
                    }else
                    {
                        orderList.put(co.getId(), new Coordinate(co.getLongitude(), co.getLatitude()));
                       
                    }
            SortedSet<Long> sort=new TreeSet<Long>( );
            sort.addAll(orderList.keySet());
                    
            List<Coordinate> lcoates = (ArrayList<Coordinate>) geom.createAndSetCoordinates();
            for(Iterator iter=sort.iterator(); iter.hasNext();)
            {
                lcoates.add(orderList.get(iter.next()));
            }
            /*  List<Long> lRes=new ArrayList<Long>(lco.size());
            Set<Long> keySet = orderList.keySet();
            Object[] idCos = keySet.toArray();
            Arrays.sort(idCos);
            System.err.println("idCos");System.out.println(idCos);
            for(int i=0; i<idCos.length; i++ )
            {
            Coord co= (Coord) orderList.get(idCos[i]);
            if(co.getAlt()!=null)
            {
            geom.addToCoordinates(co.getLongitude(), co.getLatitude(), co.getAlt());
            }else
            {
            geom.addToCoordinates(co.getLongitude(), co.getLatitude());
            }
             */
               
            }
         Xcption.Xcption("geom",geom.toString());
       
        return geom;
    }
    public LinearRing trCoFg(Set<Coord> lco, LinearRing geom)
    {
        try{
        List lc=new ArrayList<Coordinate>(lco.size());
         if(lco!=null&&!lco.isEmpty())       
         {
            
                boolean balt=false;
            for (Coord co : lco) 
            {
                if(co.getAlt()!=null)
                {
                    balt=true;
                    lc.add(new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt()));
                   
                }else
                {
                   lc.add(new Coordinate(co.getLongitude(), co.getLatitude()));
                  
                }
      
               
            }
            geom.setCoordinates(lc);
             /*Iterator iter=lco.iterator();
             Coord co= (Coord)iter.next();
             if(balt)
                    {
                    
                        geom.addToCoordinates(co.getLongitude(), co.getLatitude(), co.getAlt());
                    }else
                    {
                       
                        geom.addToCoordinates(co.getLongitude(), co.getLatitude());
                    }*/
             
         }
        }catch(ClassCastException ccex)
        {
             Xcption.Xcption("GereKml trCoFg ClassCastException",ccex.getMessage());
        }
        return geom;
    }

    @Override
    public String getTracer() {
        return tracer;
    }

     @Override
    public String geTour() {
        return tour;
    }

    private String verifTracer(String t)
    {
        if(t.contains("INFO:"))
            t = t.replace("INFO:", "");
        return t;
    }
    @Override
    public void setTracer(String tracer) 
    {
        tracer = verifTracer(tracer);
        this.tracer = tracer;
    }


    
    @Override
    public boolean isbCoord() {
        return bCoord;
    }

    @Override
    public String[] getLatlng() {
        return latlng;
    }

    @Override
    public void setLatlng(String[] latlng) {
        this.latlng = latlng;
    }

    public void setbCoord(boolean bCoord) {
        this.bCoord = bCoord;
    }

    @Override
    public GereDonneLocal getGereDonne() {
        return gereDonne;
    }

    public void setGereDonne(GereDonne gereDonne) {
        this.gereDonne = gereDonne;
    }

    @Override
    public boolean isBcrit() {
        return bcrit;
    }

    @Override
    public void setBcrit(boolean bcrit) {
        this.bcrit = bcrit;
    }

    public List getSlCrit() {
        return slCrit;
    }

    @Override
    public void setSlCrit(List slCrit) {
        this.slCrit = slCrit;
    }

    @Override
    public YahooRep getMeteoKml() {
        return meteoKml;
    }

    @Override
    public void setMeteoKml(YahooRep meteoKml) {
        this.meteoKml = meteoKml;
    }

    @Override
    public boolean isBclim() {
        return bclim;
    }

    @Override
    public void setBclim(boolean bclim) {
        this.bclim = bclim;
    }

    @Override
    public DefaultTreeNode getRoot() {
        return root;
    }

    @Override
    public void setRoot(DefaultTreeNode root) {
        this.root = root;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    
    public void chLoc( RsEntite principal)
    {
        String[] decad;
        String[] split = principal.getAdresse().split(" ");
    }

   
    
    
    
}
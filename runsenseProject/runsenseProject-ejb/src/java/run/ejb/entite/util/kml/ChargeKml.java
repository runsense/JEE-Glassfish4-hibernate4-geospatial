package run.ejb.entite.util.kml;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.google.common.collect.Lists;
import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.apache.lucene.document.DateTools;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import run.ejb.base.Variable;
import run.ejb.ejbkml.GereKml;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.client.Compte;
import run.ejb.entite.client.Contact;
import run.ejb.entite.geo.Evnmt;
import run.ejb.entite.geo.Localite;
import run.ejb.entite.geo.Prmtr;
import run.ejb.entite.geo.interf.IntBaz;

import run.ejb.entite.util.runsense.UtilRsns;
import run.ejb.util.ex.Xcption;

/**
 *
 * @author Fujitsu
 */
public class ChargeKml 
{
  
    private boolean cfm;
    private Compte cpte;
    private String lieu;
    private GereKml gKml;
    
    private List<RsEntite> chgCpte;
    private boolean bvisualise;
    private String name="";
    
    private ArrayList<Coordinate> coordinates;
    
    private DefaultTreeNode root;
    private TreeNode parent = null;
 
    
    private Kml kml;
    private Document doc;
    
    private int classement=-1;
    private Folder[] f;
    
    private Map<String, Placemark > mplcchg;
    private HashMap<String, Folder> mdchg;
     
    private ChrSpe env;
    
    private List<String> tbverif;
    
    private Map<String,List<Evnmt>> mevnmts;
    private List<Evnmt> levnmts;
   
   
     private final  List<String> verifEntrer=Lists.newArrayList(Variable.getPrmtr().get(0), Variable.getPrmtr().get(1),Variable.getPrmtr().get(2),
             Variable.getLtitre().get(21), Variable.getPrmtr().get(38),Variable.getPrmtr().get(32),Variable.getPrmtr().get(33),Variable.getPrmtr().get(34)
                      ,Variable.getPrmtr().get(35),Variable.getPrmtr().get(36),Variable.getPrmtr().get(37),
                      Variable.getLtitre().get(22), Variable.getPrmtr().get(39),Variable.getPrmtr().get(43),Variable.getPrmtr().get(60),Variable.getPrmtr().get(61),
                              Variable.getLtitre().get(23), Variable.getPrmtr().get(19),Variable.getPrmtr().get(26),Variable.getPrmtr().get(31),Variable.getPrmtr().get(45),
                      Variable.getPrmtr().get(47),Variable.getPrmtr().get(48),Variable.getPrmtr().get(50),Variable.getPrmtr().get(52),Variable.getPrmtr().get(49),Variable.getPrmtr().get(55),Variable.getPrmtr().get(60),
                      Variable.getLtitre().get(24), Variable.getPrmtr().get(31),Variable.getPrmtr().get(45),Variable.getPrmtr().get(46),Variable.getPrmtr().get(53)
                      ,Variable.getPrmtr().get(54),Variable.getPrmtr().get(56),Variable.getPrmtr().get(57),Variable.getPrmtr().get(58),Variable.getPrmtr().get(59),
                      Variable.getPrmtr().get(6),Variable.getPrmtr().get(23),Variable.getPrmtr().get(24),Variable.getPrmtr().get(25),Variable.getPrmtr().get(26),Variable.getPrmtr().get(29),
                              Variable.getPrmtr().get(4),Variable.getLinf()[0],Variable.getLinf()[1],Variable.getLinf()[2],Variable.getLinf()[3],
                              Variable.getLtitre().get(28),Variable.getLtitre().get(27),Variable.getPrmtr().get(25),Variable.getPrmtr().get(14),Variable.getPrmtr().get(15),
               Variable.getPrmtr().get(17),
               Variable.getLtitre().get(19),Variable.getLtitre().get(20),Variable.getZact(),Variable.getLzact()[0],Variable.getLzact()[1],Variable.getLzact()[2],Variable.getLzact()[3],Variable.getLzact()[4],Variable.getLzact()[5]);
    private boolean boolcoord;
    
    public ChargeKml(Document doc,GereKml g)
    {
        
        this.chgCpte=new ArrayList<RsEntite>();
       // if(gKml==null)
            this.gKml=g;
        this.doc=doc;
           if(doc!=null)
               this.doc.setVisibility(true);
        f=new Folder[50];
        
        mplcchg=new HashMap<String, Placemark>();
        mdchg=new HashMap<String, Folder>();
        mevnmts=new HashMap<String, List<Evnmt>>();
    }
    public ChargeKml(GereKml gKml)
    {
        
        this.gKml=gKml;
    }
    private void addLocalite( String adresse)
    {
        Localite localite ;
        Localite rch ;
        Localite parent = null;
        gKml.fEm();
        gKml.cEm();
        if(adresse!=null)
        {
            String[] splitad = adresse.split(" ");
            for(int i=0; i<splitad.length-1; i++)
            {
                String adloc=splitad[i];
                ArrayList rchBdd=new ArrayList();
                 if(!adloc.isEmpty()&&adloc!=" ")                
                 {
                     rchBdd = gKml.rchBdd(adloc, "localite", "fix", "nom");
                     if(rchBdd.isEmpty()&&parent!=null)
                        {
                            localite = new Localite();
                            localite.setNom(adloc);
                            localite.setParent(parent);
                                gKml.boolcreateObj(true);
                                gKml.addBdd(localite);
                                if(parent.getEnfant()==null)
                                    parent.setEnfant(new HashSet<Localite>());
                                
                                parent.getEnfant().add(localite);
                                gKml.boolcreateObj(false);
                                gKml.addBdd(localite);
                                parent=localite;
                        }else if(rchBdd.isEmpty())
                        {
                            localite = new Localite();
                            localite.setNom(adloc);
                                gKml.boolcreateObj(true);
                                gKml.addBdd(localite );
                                
                            parent=localite;
                        }else 
                        {
                            
                            parent= (Localite) rchBdd.get(0);
                        }
                 }
                
            }
        }
         gKml.fEm();
    }
    public Object verifKml(Object verif)
    {
        RchJAK rch=new RchJAK(doc);
        if(verif instanceof RsEntite)
        {
            RsEntite l=(RsEntite) verif;
            verif = rch.rch( l);
            
        }
       
        
       return verif;
    }
    public Coord updCoord(Coord nouv)
    {
        
       // Indexation indexation=new Indexation(chchRsentite(old));
             
             
             try{
               /* List rch=new ArrayList();List field=new ArrayList();
                rch.add(old.getLatitude());field.add("LATITUDE");
                rch.add(old.getLongitude());field.add("LONGITUDE");
                
            gKml.rchMulti(rch, new Coord(), field,"MUST_MUST", null);*/
         
                //gKml.supBdd(old);
                 gKml.boolcreateObj(false);
                gKml.addBdd(nouv);
            }catch(java.lang.ClassCastException ccex)
            {
                return null;
            }
             
             return nouv;
    }
    public RsEntite upRs(RsEntite nouv)
    {
        
       //Indexation indexation=new Indexation(chchRsentite(old));
         //indexation.deleteIndex(gKml.getGereDonne().getLqn());
    
                  //  System.err.print("gKml.supBdd :"+gKml.supBdd(refresh(old)));
             if(nouv instanceof Ligne)
             {
                 Ligne tmp=(Ligne) nouv;
                 
                 gKml.cEm();
            Ligne find = (Ligne) gKml.getGereDonne().getEm().find(nouv.getClass(), nouv.getId());
                 coordinates=(ArrayList<Coordinate>)  find.getCoords().stream().
                         map(co->new Coordinate(co.getLongitude(), co.getLatitude())).
                         collect(Collectors.toList());
                
                upBdd(new Ligne(find.getId(), tmp.getAdresse(), tmp.getDescription(), tmp.getNom(), tmp.getType(), null,  null), coordinates);
               
                 gKml.fEm();
             }else if(nouv instanceof Pt)
             {
                 Pt tmp=(Pt) gKml.getGereDonne().getEm().find(nouv.getClass(), nouv.getId());
                 Pt pt=new Pt();
                 pt.setNom(tmp.getNom());pt.setAdresse(tmp.getAdresse());pt.setDescription(tmp.getDescription());pt.setLatitude(tmp.getLatitude());pt.setLongitude(tmp.getLongitude());pt.setCpte(tmp.getCpte());
                 nouv=pt;
                 gKml.cEm();
               gKml.boolcreateObj(true);
                gKml.addBdd(nouv);
                gKml.fEm();
                 
             }
             
             
            
             
             return nouv;
    }
    private List<RsEntite> supDoublonsRs(List<RsEntite> lorg,List<RsEntite> lcomp)
    { 
        try{
        lorg.addAll(
                lcomp.stream().
                filter(rs->!lorg.contains(rs)).
                collect(Collectors.toList()));
        
        }catch(NullPointerException npex)
        {

        }
        return lorg;
    }
   
    
     public Ligne addLigne(ArrayList<Coordinate> cods, String nm, String ad, String desc, String styleUrl)
     {
     
        boolean balt=false;
        Xcption.Xcption("","GereKml modif bdd addLigne"); Xcption.Xcption("","nm "+nm+"ad "+ad+"desc "+desc+"styleUrl "+styleUrl);
        Ligne lg=new Ligne();
        try{
        
        ChrSpe chspe=new ChrSpe();        
        
           // nm=chspe.chrchChIso(nm);
            List<RsEntite> rchnmad =null;
            List<RsEntite> rchnmdesc =null;
            List<RsEntite> rchaddesc =null;
            gKml.fEm();
            System.gc();
            //this.gKml=new GereKml();
                gKml.cEm();
            try{
                List rch=new ArrayList();List field=new ArrayList();
                rch.add(nm);field.add("nom");
                rch.add(ad);field.add("adresse");
                
                if(desc!=null)        
                  { 
                      if(ad.contains("alt Tracks"))
                        {
                            balt=true;
                            ad.replace("alt Tracks", "");
                           nm=chspe.chrchChIso(nm);
                           ad=chspe.chrchChIso(ad);
                                rch=new ArrayList();
                                       rch.add(nm);
                                       rch.add(ad);
                           desc=chspe.chrchChIso(desc);
                           rchnmad = gKml.rchMulti(rch, new Ligne(), field,"MUST_SHOULD", null);
                           rch=new ArrayList(); field=new ArrayList();
                                    rch.add(ad);field.add("adresse");
                                    rch.add(desc);field.add("description");
                              if(desc!=null)       
                                rchaddesc = gKml.rchMulti(rch, new Ligne(), field,"MUST_MUST", null);
                              else
                                rchaddesc = new ArrayList() ; 
                        }else
                          rchnmad = gKml.rchMulti(rch, new Ligne(), field,"MUST_MUST", null);
                 }else
                    rchnmad = new ArrayList();
          
            rch=new ArrayList(); field=new ArrayList();
                        rch.add(nm);field.add("nom");
                        rch.add(desc);field.add("description");
                  if(desc!=null)        
                    rchnmdesc = gKml.rchMulti(rch, new Ligne(), field,"MUST_MUST", null);
                  else
                    rchnmdesc = new ArrayList();
                    
            
                
               /* gKml.getGereDonne().finish();
                 this.gKml=new GereKml();
                gKml.getGereDonne().createEM();*/
            }catch( org.hibernate.search.SearchException hshex)
            {
                 Xcption.Xcption("Verif entrer ligne  "+nm+ad, rchnmad.toString());
                wait(1000l);
                addLigne(cods, nm, ad, desc, styleUrl);
            }catch(java.lang.ClassCastException ccex)
            {
                
            }

             boolean bdoub=false;   
           if(balt)
           {
                List<RsEntite> rchbdd=rchnmdesc;
                //rchbdd=supDoublonsRs(rchbdd, rchnmdesc);
                //rchbdd=supDoublonsRs(rchbdd, rchaddesc);
                Double latitude = cods.get(0).getLatitude();Double longitude = cods.get(0).getLongitude();
                           Short shfcolat = latitude.shortValue();Short shfcolng = longitude.shortValue();
                latitude = cods.get(cods.size()-1).getLatitude(); longitude = cods.get(cods.size()-1).getLongitude();
                           Short shdcolat = latitude.shortValue();Short shdcolng = longitude.shortValue();
                           
                           Double alt;Short shdcoalt=null;
                     try{
                         alt=cods.get(0).getAltitude();
                         shdcoalt=alt.shortValue();
                     }catch(Exception ex)
                     {
                         
                     }
                   if(shdcoalt==0&&!bdoub)
                     for(RsEntite rs: rchbdd)      
                     {
                         
                         Ligne l=(Ligne) gKml.findrs(rs);
                         
                            Coord first = Lists.newArrayList(l.getCoords()).get(0);
                             Short shfirstcolat = first.getLatitude().shortValue();
                             Short shfirstcolng = first.getLongitude().shortValue();
                            
                            
                            if(shfirstcolat.equals(shfcolat)&&shfcolng.equals(shfirstcolng))
                            {
                            
                                Xcption.Xcption("","first.getAlt()************ "+l); Xcption.Xcption("",first.getAlt());
                                Xcption.Xcption("","cods.get(0).getAltitude()************ "+l); Xcption.Xcption("",cods.get(0).getAltitude());
                                try{
                                    List<Coord> ltmp=Lists.newArrayList(l.getCoords());
                                  List list = new ArrayList();
                                    for (int i=0; i<cods.size();i++) 
                                    {
                                        Coordinate updco = cods.get(i);
                                        
                                            
                                            Coord co=
                                                    (Coord) gKml.find(ltmp.get(i).getClass(), ltmp.get(i).getId());

                                            co.setLatitude(updco.getLatitude());
                                            co.setLongitude(updco.getLongitude());
                                            co.setAlt(updco.getAltitude());
                                            gKml.boolcreateObj(false);
                                             gKml.addBdd(co);
                                             list.add(co);
                                             if(Math.abs(i/18)==i/18)
                                             {
                                                 gKml.fEm();
                                                 gKml.cEm();
                                             }
                                             bdoub=true;
                                    }
                                    l.setCoords(list);
                                
                                }catch(Exception ex)
                                {
                                    Xcption.Xcption("Verif entrer update altitude Coords ligne "+nm+ad,ex.getMessage());
                                }
                                    
                            }

                                   // gKml.addBdd(l, false);
                     }          
                            
           }
           else
            {
                if(nm==null) nm="";
                if(ad==null) ad="";    
                if(desc==null) desc="";
                
                
                boolean aj=true;
                
                List<RsEntite> rchbdd=rchnmad;
                rchbdd=supDoublonsRs(rchbdd, rchnmdesc);
                cods = cods.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
                //rchbdd=supDoublonsRs(rchbdd, rchaddesc);
                Double dbfcolat = cods.get(0).getLatitude();Double dbfcolng = cods.get(0).getLongitude();
                           
                Double dbdcolat = cods.get(cods.size()-1).getLatitude(); Double dbdcolng = cods.get(cods.size()-1).getLongitude();
                           
                for(RsEntite rs:rchbdd)
                {
                     Coord first=null;
                       Coord last=null;
                       Ligne l =new Ligne(null, null, null, null, null, null, null);
                        try{      
                            
                            l= (Ligne) gKml.find(rs.getClass(), rs.getId());
                      
                            List<Coord> coords = l.getCoords();
                           if(coords.isEmpty())
                           {
                                /*insertBdd(l, cods);
                                aj=false;*/
                                
                              first=coords.get(0);
                              last=coords.get(coords.size()-1);
                           }else
                           {
                                /*first = l.getCoords().get(0);
                                 first = gKml.getGereDonne().getEm().find(first.getClass(), first.getId());
                                last = l.getCoords().get(l.getCoords().size()-1);
                                if(last.getLatitude().equals(first.getLatitude())
                                        &&last.getLongitude().equals(first.getLongitude()))
                                         last = l.getCoords().get(l.getCoords().size()-2);
                               last = gKml.getGereDonne().getEm().find(last.getClass(), last.getId());
                            
                                Xcption.Xcption("","first************ "+l); Xcption.Xcption("",first.getLatitude()+"   "+first.getLongitude()+"   ");
                                Xcption.Xcption("","last************ "+l); Xcption.Xcption("",last.getLatitude()+"   "+last.getLongitude()+"   ");
                                */        
                           }
                            }catch(Exception ex)
                            {

                                Xcption.Xcption("************ Null"+l, ex.getMessage());
                                gKml.fEm();
                                     //gKml.supBdd(l);
                                     Xcption.Xcption("","sup"+l);

                                      Xcption.Xcption("Null************ "+l,l.getNom());
                            }
                if(first!=null&&last!=null)       
                {
                    Double dbflat = first.getLatitude();Double dbflng = first.getLongitude();
                    Double dbdlat = last.getLatitude();Double dbdlng = last.getLongitude();
                             
                           
                    if((dbflat.equals(dbfcolat)&&
                            dbflng.equals(dbfcolng)&&
                            dbdlat.equals(dbdcolat)&&
                            dbdlng.equals(dbdcolng)) )
                    {
                       if(bdoub)
                       {
                           gKml.fEm();
                                //gKml.supBdd(l);
                                Xcption.Xcption("","sup"+l);
                                Xcption.Xcption("","Doublons************ "+l); Xcption.Xcption("",l.getNom());
                       }
                       else if(!l.getAdresse().equals(ad)
                               ||!l.getDescription().equals(desc))
                       {
                         /*  gKml.getGereDonne().finish();
                            gKml.getGereDonne().createEM(); */
                            if(ad!=null) 
                                lg.setAdresse(ad);
                            if(desc!=null) 
                                lg.setDescription(desc);
                            gKml.boolcreateObj(false);
                            gKml.addBdd(lg);
                       }
                         aj=false;
                           bdoub=true;  
                            
                    }else if(nm.equals(l.getNom())&&ad.equals(l.getAdresse())&&desc.equals(l.getDescription()))
                    {
                        
                        gKml.fEm();
                               // gKml.supBdd(l);
                                Xcption.Xcption("","sup"+l);
                                Xcption.Xcption("","Doublons************ "+l); Xcption.Xcption("",l.getNom());
                               aj=false; 
                       /* if(l.getCoords().isEmpty())
                           {
                               gKml.getGereDonne().createEM();
                               insertBdd(l, cods);
                               aj=false;
                           }*/
                    }
                }else 
                    {
                       
                               
                          
                       /*  gKml.getGereDonne().finish();
                                gKml.supBdd(l);*/
                                Xcption.Xcption("","sup"+l);
                                Xcption.Xcption("","Doublons************ "+l); Xcption.Xcption("",l.getNom());
                                
                                insertBdd(l, cods);
                               aj=false;
                    }   
                
            }
              //  gKml.getGereDonne().finish();
                
               // gKml=new GereKml();
                if(aj)
                {
                 //   System.runFinalization();
                   // System.gc();
               //   gKml.getGereDonne().createEM();  
                  
                        gKml.cEm();
                 if(nm!=null)  
                    lg.setNom(nm);
                  if(ad!=null) 
                    lg.setAdresse(ad);
                  if(desc!=null) 
                    lg.setDescription(desc);
                  if(styleUrl!=null) 
                    lg.setType(styleUrl);
                
                 insertBdd(lg, cods);
                                                
              
                }else
                {
                    lg=null;
                    cfm=false;
                }
                                        
            }            
            /*else{
                
                if(nm!=null)  
                lg.setNom(nm);
              if(ad!=null) 
                lg.setAdresse(ad);
              if(desc!=null) 
                lg.setDescription(desc);
              if(styleUrl!=null) 
                lg.setType(styleUrl);
                
                insertBdd(lg, cods);
                                                
               
                
            }*/
          
           //gKml.getGereDonne().getEm().clear();
            gKml.fEm();
            System.runFinalization();
            System.gc();
           
        }catch(Exception ex)
        {
          
             Xcption.Xcption("addligne Exception "+nm+ad+desc+cods,ex);
        }
          
           
     return lg;
    }
     private File chchRsentite(Object pers)
    {
            String ent="";
             if(pers instanceof RsEntite)    
             {
                 ent=pers.getClass().getCanonicalName().substring(18);
             }else
             if(pers.getClass().getCanonicalName().split("geo.").length>1)
             {
                 ent=pers.getClass().getCanonicalName().substring(18);
             }else
             {
                 ent=pers.getClass().getCanonicalName().substring(10);
             }
             
        return new File("/home/runsense/index/"+ent.toUpperCase());
             
    }
     public List<Tour> rchTour(Tour t)
     {
          return gKml.rchBdd(t.getNmTour(), "tour", "fix","nmTour");
     }
    public void addTour(Tour t) 
    {
        cfm=false;
        if(gKml.getGereDonne().isSynk())
        {
        gKml.fEm();
        //this.gKml=new GereKml();
        gKml.cEm();
        
               //verif
               boolean aj=false;
               boolean bdoub=false;
            List<Tour> rchBdd = rchTour(t);
                if(rchBdd.isEmpty())
                    aj=true;
                else
                    for(Tour test:rchBdd)
                        if(test.getNom().equals(t.getNom()))
                        {
                           
                              if(bdoub)
                            {
                                
                                gKml.fEm();
                                gKml.supBdd(test);
                                Xcption.Xcption("","sup"+test);
                            }  
                           else if(test.getParametreTour().size()!=t.getParametreTour().size()) 
                           {  
                                   aj=true;  
                                   
                              
                                   try{
                                      
                                       List<Prmtr> prmtrTour = Lists.newArrayList(test.getParametreTour());
                                       cfm =gKml.supBdd(test);
                                    for (int i=0; i<prmtrTour.size();i++) 
                                    {
                                        Prmtr updprmtr = prmtrTour.get(i);
                                        
                                             cfm =gKml.supBdd(updprmtr);

                                             if(Math.abs(i/9)==i/9)
                                             {
                                                 gKml.cEm();
                                             }
                                    }
                                    
                                
                                }catch(Exception ex)
                                {
                                   
                                    Xcption.Xcption("Verif entrer tour Exception test.getParametreTour().size()!=t.getParametreTour().size() "+test.getNom()+" ... "+t.getNom(), ex.getMessage());
                                }
                                   gKml.boolcreateObj(false);
                                  gKml.addBdd(test);
                                  
                              
                              bdoub=true;
                           }else
                                bdoub=true;
                        }
            
              if(aj)
              {
            
                List<Prmtr> prmrtrs = t.getParametreTour();
                t.setParametreTour(new ArrayList<Prmtr>(prmrtrs.size()));
                gKml.boolcreateObj(true);
                
                        prmrtrs.stream().
                        peek(p->gKml.addBdd(p)).
                                forEach( p->t.getParametreTour().add(p));
                       /* for(Prmtr p:prmrtrs)
                        {

                            gKml.addBdd(p); 
                                t.getParametreTour().add(p);
                          
                        }*/
                       
                       gKml.fEm();  
                        gKml.cEm();
                         gKml.addBdd(t);
                    //}
                    
                
              }
            
         gKml.fEm();
         gKml=null;
        }
       
    } 
    public Object addObject(Object o)
    {
       
        
         gKml.cEm();
         gKml.boolcreateObj(true);
         o=gKml.addBdd(o);
            
            refresh(cpte);
           
            
            gKml.fEm();
         return o;
    }
    public void addDonneCompte(Compte cpte,Object object)
    { 
        cfm=false;
        
        gKml.cEm();
        refresh(cpte);
        Xcption.Xcption("","ChargeKml addDonneCompte");Xcption.Xcption("",object);
        if(cpte.getNom().equals("kassiel"))
            cfm=true;
        else
        {

            

          int inc=0;
          if(object instanceof List)
          {
              List list =(List) object;
              List tmplg=new ArrayList<Ligne>(28);List tmppt=new ArrayList<Pt>(28);List tmpevnmt=new ArrayList<Evnmt>(28);
              List tmptr=new ArrayList<Tour>(28);List tmpctc=new ArrayList<Contact>(28);

              for(Object o:list)
            {

                inc++;
                refresh(o);
                


                     if(o instanceof Ligne )
                     {

                            tmplg.add((Ligne) o);


                     }else if(o instanceof Pt)
                     {

                            tmppt.add((Pt) o);


                     }else if(o instanceof Tour)                
                     {


                            tmptr.add((Tour) o);


                    }else if(o instanceof Evnmt)
                     {

                           tmpevnmt.add((Evnmt) o);

                     }else if(o instanceof Contact)
                     {

                           tmpctc.add((Contact) o);

                     }

                


              // addBdd(cpte, false);

                if(inc>27)
                {
                    if(!tmplg.isEmpty())
                    {
                        if(cpte.getLgncpte()==null)
                        {    
                            cpte.setLgncpte(new HashSet<Ligne>());    
                        }
                        
                        cpte.getLgncpte().addAll(tmplg);
                    }
                    if(!tmptr.isEmpty())
                    {
                        if(cpte.getCptetour()==null)
                        {
                            cpte.setCptetour(new HashSet<Tour>());    
                        }
                        
                        cpte.getCptetour().addAll(tmptr);
                    }
                    if(!tmppt.isEmpty())
                    {
                        if(cpte.getPoint()==null)
                        {
                            cpte.setPoint(new HashSet<Pt>());    
                        }
                        cpte.getPoint().addAll(tmppt);
                    }
                    if(!tmpctc.isEmpty())
                    {
                        if(cpte.getContact()==null)
                        {
                            cpte.setContact(new HashSet<Contact>());    
                        }
                        cpte.getContact().addAll(tmpctc);
                    }
                    if(!tmpevnmt.isEmpty())
                    {
                        if(cpte.getContact()==null)
                        {
                            cpte.setEvnmtcpte(new HashSet<Evnmt>());    
                        }
                        cpte.getEvnmtcpte().addAll(tmpevnmt);;
                    }
                   
                    gKml.boolcreateObj(false);
                    gKml.addBdd(cpte);
                   gKml.fEm();
                   
                   gKml.cEm();
                   refresh(cpte);
                   inc=0;


                }

            }


          }else
          {
              


                   


                     if(object instanceof Ligne)
                     {

                             cpte.getLgncpte().add((Ligne) object);




                     }else if(object instanceof Pt)
                     {

                            cpte.getPoint().add((Pt) object);


                     }else if(object instanceof Contact)
                     {

                             cpte.getContact().add((Contact) object);


                     }else if(object instanceof Evnmt)                
                    {
                                               
                            cpte.getEvnmtcpte().add((Evnmt) object);


                    }else if(object instanceof Tour)                
                    {


                            cpte.getCptetour().add((Tour) object);


                    }
                
          }
          gKml.boolcreateObj(false);
         gKml.addBdd(cpte);
      
        gKml.fEm();
        }
      
               
    } 
    private void insertBdd(Ligne lg, ArrayList<Coordinate> cods)
    {
        cfm=false;
        int inc=0;
        
        try{
           
            lg.setCoords(new ArrayList()); ;
            booladdCoord(true);
            lg.getCoords().addAll(cods.stream().map(this::addCoord).collect(Collectors.toList()));
                       /*for(Coordinate iter:cods)
                       {
                       
                             lg.getCoords().add(addCoord(iter,true));
                        
                       }*/
                    
                      
        gKml.fEm();  
        gKml.cEm();
        gKml.boolcreateObj(true);
        gKml.addBdd(lg);
        //
        gKml.fEm();
        
        }catch(NullPointerException npe)
        {
            gKml.setGereDonne(new run.ejb.ejbkml.GereDonne());
            insertBdd( lg,  cods);
        }catch(NoSuchElementException nseex)
        {
            Xcption.Xcption("ChargeKml insertBdd NoSuchElementException  ", nseex.getMessage());
        }/* catch (InterruptedException ex) {
            Logger.getLogger(ChargeKml.class.getName()).log(Level.SEVERE, null, ex);
        } */
        
        
    }
     private void insertTour(Tour tr, List<Prmtr> lprmtr)
    {
        cfm=false;
        int inc=0;
        try{
            ArrayList temp=null;
            tr.setParametreTour(new ArrayList<Prmtr>(lprmtr.size()));
            gKml.boolcreateObj(true);
       if(gKml.addBdd(tr)!=null)
       {
                 gKml.boolcreateObj(false);
                  if(lprmtr.size()>9)
                    {
                    temp=new ArrayList(28);
                        
                       Iterator<Prmtr> iter= lprmtr.iterator();
                       float nb= lprmtr.size();
                       for(int i=0; i<=nb; i++)
                       {
                           inc++;
                           if(iter.hasNext())
                                temp.add(addPrmtr(iter.next(),true));
                           /*if(i!=Math.floor(nb))
                           {
                            for(int j=1; j<=9; j++)
                            {
                                if(iter.hasNext())
                                     temp.add(addCoord(iter.next(),true));
                            }
                           }else
                           {
                               float t= cods.size()-(9*nb);
                               for(int j=1; j<=t; j++)
                                {
                                    if(iter.hasNext())
                                         temp.add(addCoord(iter.next(),true));
                                }
                           }*/
                          if(inc>27)
                          {
                             tr.getParametreTour().addAll(temp);
                             
                             gKml.addBdd(tr );
                             
                             temp=new ArrayList();
                             inc=0;
                          }
                           
                           
                       }
                       
                       tr.getParametreTour().addAll(temp);
                       
                        gKml.addBdd(tr);
                       
                    }else
                    {
                        tr.setParametreTour(new ArrayList<Prmtr>(lprmtr.size()));
                        for(Prmtr p:lprmtr)
                                tr.getParametreTour().add(
                                        (Prmtr) addPrmtr(p,true));
                      
                        
                        gKml.addBdd(tr);
                        
                    }
        
           }        
        }catch(NoSuchElementException nseex)
        {
            
            Xcption.Xcption("ChargeKml insertBdd NoSuchElementException  ", nseex.getMessage());
        }
    } 
     
     
    private Object addPrmtr(Prmtr next, boolean b)
    {
        gKml.boolcreateObj(b);
        next = (Prmtr) gKml.addBdd(next);
       
            return next;
        
    }
     private void booladdCoord(boolean bdd)
     {
          boolcoord=bdd;
     }
     public Coord addCoord(Coordinate codi)
    {
        
            Coord co = new Coord();
        
        
            try{
                
                int cpte=0;
                                
                                            
                                                String  precis= "MUST_MUST";
                                                List<String> fields=new ArrayList(2);
                                                fields.add("latitude");fields.add("longitude");
                    
                                         
                                          
                                            if( gKml.rchMulti(codi, codi,fields, precis, null).isEmpty())
                                            {
                                                
                                                
                                               co=new Coord();
                                                    co.setLongitude(codi.getLongitude());
                                                    co.setLatitude(codi.getLatitude());
                                                    if(codi.getAltitude()!=0)
                                                        co.setAlt(codi.getAltitude());

                                                    //co.setAdresse(entite.getAdresse()+" "+entite.getNom()+" "+cpte);
                                                    co.setAdresse("");
                                                        cpte++;   
                                                    //co.setEntite(entite);
                                                
                                                if(boolcoord)
                                                {
                                                    gKml.boolcreateObj(true);
                                                    co=(Coord) gKml.addBdd(co);
                                                }
                                                  
                                                   
                                                
                                            }
                                          else
                                            {
                                                
                                                 co=null;    
                                                try{
                                                 co=(Coord) gKml.rchMulti(codi, codi,fields, precis, null).get(0);
                                                }catch(Exception ex)
                                                {
                                                    
                                                    Xcption.Xcption("addCoord() co=(Coord) gKml.rchMulti(codi, codi,fields, precis).get(0); Exception", ex.getMessage());
                                                    co=new Coord();
                                                }
                                                    co.setLongitude(codi.getLongitude());
                                                    co.setLatitude(codi.getLatitude());
                                                        
                                                        if(codi.getAltitude()!=0)
                                                            co.setAlt(codi.getAltitude());


                                                    //co.setAdresse(entite.getAdresse()+" "+entite.getNom()+" "+cpte);
                                                        co.setAdresse("");
                                                        cpte++;
                                             
                                             if(boolcoord)
                                             {
                                                 gKml.boolcreateObj(false);
                                                 co=(Coord) gKml.addBdd(co);
                                             }
                                             
                                              //gKml.getGereDonne().finish();      
                                            
                                              Xcption.Xcption("","\n");
                                              Xcption.Xcption("",co.getAdresse()+" "+co.getNom()+" "+cpte+" existe deja");
                                              Xcption.Xcption("","\n");
                                            }
              
            }catch(PersistenceException pex)
            {
                Xcption.Xcption("ChargeKml addCoord PersistenceException ", pex.getMessage());
                co=null;
            }catch(Exception ex)
            {
               
                Xcption.Xcption("ChargeKml addCoord Exception ", ex.getMessage());
                gKml.fEm();
            }
          
            return co;
    }
    
   
    private void ajEvnmt(String nom,String ad,String desc, Date dt, Pt pt) {
        Evnmt evnmt = new Evnmt(nom, dt, ad, desc, pt.getNom());
        gKml.fEm();
                this.gKml=new GereKml();
                gKml.cEm();
                gKml.boolcreateObj(true);
                gKml.addBdd(evnmt);
    }

    public Pt addPoint(Coordinate cod, String nm, String ad, String desc, String styleUrl)
    {
        
        if(ad.contains("+"))
            ad.replace("+", " et ");
        
        boolean balt=false;
        gKml.fEm();
        Pt point=new Pt();
        try{
            if(!gKml.getGereDonne().isSynk())
                synchronized(this){
                                         wait(900);
                                   }
       /* System.runFinalization();
            System.gc();*/
         // this.gKml=new GereKml();
        gKml.cEm();
        cfm=false;
        ChrSpe chspe=new ChrSpe();
        
           
        
        
           // nm=chspe.chrchChIso(nm);
            //List<RsEntite> rchBdd = rchBdd(nm, "point", "fix", "nom");
         
             List<RsEntite> rchnmad =null;
            List<RsEntite> rchnmdesc =null;
            List<RsEntite> rchaddesc =null;
            try{
                List rch=new ArrayList(2);List field=new ArrayList(2);
                rch.add(nm);field.add("nom");
                rch.add(ad);field.add("adresse");
          if(nm!=null&&ad!=null)       
          {
              if(ad!=null)
              {
                  if(ad.contains("Waypoints"))
                        {
                            balt=true;
                           ad.replace("Waypoints", "");
                           nm=chspe.chrchChIso(nm);
                           ad=chspe.chrchChIso(ad);
                                rch=new ArrayList(2);
                                  rch.add(nm);
                                  rch.add(ad);
                           
                           rchnmad = gKml.rchMulti(rch, new Pt(), field,"MUST_SHOULD", null);
                           
                           rch=new ArrayList(2); field=new ArrayList(2);
                                    rch.add(ad);field.add("adresse");
                                    desc=chspe.chrchChIso(desc);
                                    rch.add(desc);field.add("description");
                              if(ad!=null&&desc!=null)       
                                rchaddesc = gKml.rchMulti(rch, new Pt(), field,"MUST_MUST", null);
                              else
                                rchaddesc =  new ArrayList(0);
                        }else
                        {
                            rch=new ArrayList(2); field=new ArrayList(2);
                                rch.add(nm);field.add("nom");
                                rch.add(desc);field.add("description");
                              rchnmad = gKml.rchMulti(rch, new Pt(), field,"MUST_MUST", null);
                              
                              if(desc!=null)        
                                {
                                    rch=new ArrayList(2); field=new ArrayList(2);
                                      rch.add(nm);field.add("nom");
                                      rch.add(desc);field.add("description");
                                    rchnmdesc = gKml.rchMulti(rch, new Pt(), field,"MUST_MUST", null);
                                }
                  else
                    rchnmdesc =  new ArrayList();
                        }
              }else
                  rchnmad = gKml.rchMulti(rch, new Pt(), field,"MUST_MUST", null);
          }else
             rchnmad =  new ArrayList();
          
            
                  
                    
            
                    
                    
               /* gKml.getGereDonne().finish();
               // this.gKml=new GereKml();
                gKml.getGereDonne().createEM();*/
            }catch(java.lang.ClassCastException ccex)
            {
                
            }
           Xcption.Xcption("","first***cod********* "+cod); Xcption.Xcption("",cod.getLatitude()+"   "+cod.getLongitude()+"     "+cod.getAltitude());
            
            
                 Double latitude = cod.getLatitude();Double longitude = cod.getLongitude();
                          // Short shfcolat = latitude.shortValue();Short shfcolng = longitude.shortValue();
                 boolean bdoub=false;
           if(balt)
           {
                List<RsEntite> rchbdd=rchnmad;
                    rchbdd=supDoublonsRs(rchbdd, rchnmdesc);
                   //rchbdd=supDoublonsRs(rchbdd, rchaddesc);
                   for(RsEntite rs:rchbdd)
                   {
                      
                       Pt pt=(Pt) rs;
                    Double dbfirstcolat = pt.getLatitude();Double dbfirstcolng = pt.getLongitude();
                    if(!bdoub)
                       if(latitude.equals(dbfirstcolat)&&
                            longitude.equals(dbfirstcolng)&&pt.getDescription().equals(desc))
                       {
                              gKml.fEm();
                               gKml.supBdd(pt);

                               gKml.cEm();
                                pt.setId(null);
                                pt.setAlt(cod.getAltitude());
                                gKml.boolcreateObj(true);
                              pt=(Pt) gKml.addBdd(pt);
                              bdoub=true;
                       }  
                   }
           }
           else 
            {
               
                if(nm==null) nm="";
                if(ad==null) ad="";    
                if(desc==null) desc="";
                
                
                boolean aj=true;
                 List<RsEntite> rchbdd=rchnmad;
                    rchbdd=supDoublonsRs(rchbdd, rchnmdesc);
                   rchbdd=supDoublonsRs(rchbdd, rchaddesc);
                Xcption.Xcption("","Verif entrer point  "+nm+ad+desc); Xcption.Xcption("",rchbdd);
                for(RsEntite rs:rchbdd)
                {
                  
                        Pt test=(Pt) gKml.find(rs.getClass(), rs.getId());

                    Double dblat = test.getLatitude();Double dblng = test.getLongitude();
                        Xcption.Xcption("","first***test********* "+test); Xcption.Xcption("",test.getLatitude()+"   "+test.getLongitude()+"     "+test.getAlt());
                        if(test.getLatitude()==null||test.getLongitude()==null)
                        {
                            gKml.fEm();
                            gKml.supBdd(rs);

                        }else if(dblng.equals(longitude)
                                    &&dblat.equals(latitude))
                         {
                             
                                if(!bdoub)
                                {
                                    
                                    
                                    if(test.getNom().equals(nm))
                                    {
                                        bdoub=true;
                                       
                                     if(!mevnmts.isEmpty() )   
                                     {
                                         
                                        if(!mevnmts.isEmpty())
                                        {
                                            System.err.println("*****************************"
                                                    + "********************************");System.err.println("*****************************"
                                                    + "********************************");System.err.println("*****************************"
                                                    + "********************************");System.err.println("*****************************"
                                                    + "**************test.getEvnmts()******************");
                                                    Xcption.Xcption("",test.getNom());

                                                    levnmts = mevnmts.get(nm);
                                                    gKml.fEm();
                                              
                                            gKml.cEm();
                                            
                                                    List ltmp=new ArrayList();
                                                   List rch=new ArrayList();List field=new ArrayList();
                                                        field.add("nom");
                                                        field.add("adresse");
                                                           for(Evnmt evnmt:levnmts)
                                                           {
                                                               aj=true;
                                                               rch=new ArrayList();
                                                               rch.add(evnmt.getNom());rch.add(evnmt.getAdresse());
                                                               ArrayList<Evnmt> rchM = gKml.rchMulti(rch, evnmt, field, "MUST_MUST",null);
                                                               if(!rchM.isEmpty())
                                                               {
                                                                   for(Evnmt rse:rchM)
                                                                   {
                                                                       if(rse.getDt().equals(evnmt.getDt()))
                                                                           aj=false;
                                                                   }
                                                               }
                                                               if(aj)
                                                               {
                                                                   evnmt.setLieux(nm);
                                                                    ltmp.add(evnmt);
                                                                    gKml.boolcreateObj(true);
                                                                   gKml.addBdd(evnmt);
                                                                   
                                                               }
                                                               
                                                               
                                                            }
                                                           aj=false;
                                                           
                                             point.setEvnmts(new HashSet(ltmp));
                                        }
                                        point.setNom(nm);
                                        point.setAdresse(ad);
                                        point.setDescription(desc);
                                        point.setLatitude(latitude);point.setLongitude(longitude);
                                        gKml.boolcreateObj(aj);
                                       gKml.addBdd(point);
                                        
                                     }
                                    }
                                   
                                 }else 
                                 {

                                    gKml.fEm();
                                     gKml.supBdd(test);
                                     Xcption.Xcption("","sup"+test);
                                     
                                }
                            bdoub=true;
                            aj=false;
                        }else if(nm.equals(test.getNom())&&ad.equals(test.getAdresse())&&desc.equals(test.getDescription()))
                            {
                                gKml.fEm();
                                        gKml.supBdd(test);
                                        Xcption.Xcption("","sup"+test);
                                        Xcption.Xcption("","autre coord************ "+test); Xcption.Xcption("",test.getNom());
                            }
                          
                        
                      
                }
                
               /* gKml.getGereDonne().finish();
                System.runFinalization();*/
               // System.gc();
               // gKml=new GereKml();
                //gKml.getGereDonne().createEM();
                
                if(aj)
                {
                     gKml.cEm();
                    if(nm!=null)
                       point.setNom(nm);
                    if(ad!=null)
                       point.setAdresse(ad);
                    if(desc!=null)
                       point.setDescription(desc);
                    if(styleUrl!=null)
                       point.setType(styleUrl);
                   point.setLatitude(cod.getLatitude());
                   point.setLongitude(cod.getLongitude());
                   if(String.valueOf(cod.getAltitude())!=null)
                     point.setAlt(cod.getAltitude());               
                 
                    insertPt(point, aj);
                     
                
                }
           
                 gKml.fEm();
           
            
            }/*else
            {
                 
               
                 point.setNom(nm);
                point.setAdresse(ad);
                point.setDescription(desc);
               
                point.setType(styleUrl);
            point.setLatitude(cod.getLatitude());
                   point.setLongitude(cod.getLongitude());
                   if(String.valueOf(cod.getAltitude())!=null)
                     point.setAlt(cod.getAltitude());  
                 gKml.addBdd(point, true);
               
                      
            }*/
        
            // gKml.getGereDonne().finish();
        }catch(NullPointerException npe)
        {
            gKml.setGereDonne(new run.ejb.ejbkml.GereDonne());
            insertPt(point, true);
        }catch(Exception ex)
        {
            
            Xcption.Xcption("addpoint Exception "+nm+ad+desc+cod, ex.getMessage());
        }
            
            return point;
    }
   
    public List<Placemark> lancenew()
    {
        List<Placemark> lplmk=new ArrayList<Placemark>();
        bvisualise=true;
        List<Feature> t = doc.getFeature();
        for(Object o : t)
                {
                    lplmk.add(defini(o));
                }
        return lplmk;
    }
    
    public void dcplance(TreeNode node)
    {
        
        chgCpte=new ArrayList<RsEntite>();
         bvisualise=false;
        Placemark defini = defini( 
                                    node
                                    );
        
       
       
       
    }
   
    public Placemark defini(Object o) throws NullPointerException
    {
       
                
        Folder fldr;
        Placemark plcmk = null;

         env=new ChrSpe();
         
            try{
                if(o instanceof TreeNode||o instanceof DefaultTreeNode)
                     {
                       
                         TreeNode node=(TreeNode) o;
                         
                          String data = (String)node.getData();
                          Object obj=null;
                          if(!node.getChildren().isEmpty())
                              obj =  mdchg.get(
                                  data
                                  );
                          else
                          {
                              obj =  mplcchg.get(
                                  data
                                  );
                          }

                         if(obj instanceof Folder)
                         {
                                fldr=(Folder) obj;  
                                //fldr.setAddress(ad);
                                  o=fldr;
                         }
                         else if(obj instanceof Placemark)
                         {
                             plcmk=(Placemark) obj;
                             //plcmk.setAddress(ad);
                                  o=plcmk;
                         }
                       
                     }
                 if(o instanceof Folder)
                 {
                     
                     
                     classement++;
                             f[classement] = (Folder)o;
                             if(f[classement].getFeature().isEmpty())
                             {
                                 f[classement]=null;
                             }else
                             {
                                  List<Feature> tg = f[classement].getFeature();
                                 if(bvisualise)
                                 {
                                     Xcption.Xcption("dossier", f[classement].getName());
                                    parent = new DefaultTreeNode(
                                            (String)f[classement].getName()
                                            , parent);  
                                    mdchg.put(f[classement].getName(), f[classement]);
                                    
                                    if(classement==0&&
                                            root==null)
                                     {
                                         root=(DefaultTreeNode) parent;
                                     }
                                 }

                                 if(tg!=null)        
                                 {
                                     if(tg.isEmpty())
                                         f[classement]=null;
                                     else
                                         for(Object ftg : tg)
                                          {   
                                                 defini(ftg);
                                          }
                                     if(bvisualise!=false)
                                        parent=parent.getParent();
                                 }
                             }

                           
                  
                  f[classement]=null;  
                           classement--;
                 }else if(o instanceof Placemark)
                 {

                   
                    if(classement==-1)
                    {
                        classement=0;
                    }
                    Placemark p = (Placemark) o; 

                        name = p.getName();
                     
                            try{
                                if(bvisualise)
                                {
                                    List<Folder> pere=new ArrayList<Folder>();
                                 if(classement>0)
                                 {
                                     Xcption.Xcption("fichier", f[classement].getName());
                                     
                                    
                                     parent.getChildren().add(new DefaultTreeNode(
                                            name
                                            , parent));
                                     for(int i=classement; i>=0; i--)
                                         pere.add(f[i]);
                                 }
                                 else
                                 {//dans dossier principale

                                     parent=root;
                                     if(f[0]!=null)
                                         pere.add(f[0]);
                                     // System.out.print("classement ==0"); System.out.println(name+p.getAddress());
                                 }
                                 
                           
                             
                         
                             String ad="";
                             
                                 for(int i=pere.size()-1;
                                      i>=0; i--)
                                 {
                                         ad+=
                                                 pere.get(i).getName()
                                                 +" ";
                                 }
                                 
                                 p.setAddress(ad);
                        //categerorie dans description
                       
                                 
                                  p.setAddress(
                                           definDesc(p)
                                                   +" "+ad);
                          if(mplcchg.containsKey(name))
                           while(mplcchg.containsKey(name))
                             name+="*";
                            
                           
                              p.setName(name);
                             mplcchg.put(name, p);
                             
                             Xcption.Xcption("entrer donner dans tree", "nom : "+name+" ad : "+p.getAddress()+" desc : "+p.getDescription());
                             
                         }else
                         {
                                    Placemark get = mplcchg.get(p.getName());
                                  p.setAddress(get.getAddress());
                                  p.setDescription(get.getDescription());
                            addLocalite( p.getAddress().split(Variable.getLieu())[1]);
                            
                            Xcption.Xcption("entrer donner dans BDD", "nom : "+name+" ad : "+p.getAddress()+" desc : "+p.getDescription());
                             
                            chgCpte.add(definPlcmk(p));
                            
                            
                            mplcchg.remove(p);
                         }

                         }catch(Exception ex)
                         {
                             
                             Xcption.Xcption("ChargeKml Placemark definPlcmk Exception",ex.getMessage());
                         }
                 
                 }
                 else
                 {/*
                     Placemark p = (Placemark) o; 
                     if(bvisualise)
                         {
                            parent = new DefaultTreeNode(env.chrchUTF8(
                                            (String)p.toString()), parent);
                            mfchg.put(name, p);
                         }else
                         {
                             definPlcmk(p);
                             System.err.print("ChargeKml Placemark definPlcmk else"); System.out.println(" aucun traitement");
                         }
                     System.err.print("ChargeKml defini");System.out.println("non defini, instance kml");
                   */  
                 }
            }catch(Exception ex)
                 {
                     Xcption.Xcption("ChargeKml defini Exception",ex);
                 }

            
        
         
         
   return plcmk;
    }
    public void closeEntityManager()
    {
       
             gKml.fEm();
    }
    public RsEntite definPlcmk( Placemark p) 
    {
        RsEntite rsentite = null;
        kml=new Kml();
        kml.setFeature(p);
        
       
        try{
         Geometry geo = new LineString();
        if(p.getGeometry() instanceof MultiGeometry)
        {
            MultiGeometry mlGeometry=(MultiGeometry) p.getGeometry();
            if(mlGeometry.getGeometry().size()>1)
            {
                Xcption.Xcption("", "*************TourRsns mlGeometry.getGeometry() trop********");
                
                for(Geometry trop:mlGeometry.getGeometry())
                    Xcption.Xcption("",trop);
            }
                
            geo=mlGeometry.getGeometry().get(0);
                
        }
        else if( p.getGeometry() instanceof Geometry)
             geo =p.getGeometry();
        
        if(geo instanceof LineString)
        {
            LineString ligne = (LineString) geo;
           
                                                
                                                    coordinates = (ArrayList<Coordinate>) ligne.getCoordinates();
                                                    /*if(name!=null)
                                                        if(!name.isEmpty())
                                                            name=env.chrchUTF8(name);
                                                    if(p.getAddress()!=null)
                                                        if(!p.getAddress().isEmpty())
                                                            ad=env.chrchUTF8(p.getAddress());
                                                    if(p.getDescription()!=null)
                                                        if(!p.getDescription().isEmpty())
                                                            desc=env.chrchUTF8(p.getDescription());*/
                                                    if(p.getStyleUrl()!=null)
                                                        if(!p.getStyleUrl().isEmpty())
                                                            p.setStyleUrl(
                                                                    p.getStyleUrl());
                                                    
                                                        rsentite= addLigne(coordinates,name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                                            
        }
        else if(geo instanceof LinearRing)
        {
           LinearRing points = (LinearRing) geo;
           
                                                
                                                    ArrayList<Coordinate> listCo = (ArrayList<Coordinate>) points.getCoordinates();
                                                coordinates=new ArrayList<Coordinate>(listCo);
                                                
                                                /*if(name!=null)
                                                    if(!name.isEmpty())
                                                            name=env.chrchUTF8(name);
                                                    if(p.getAddress()!=null)
                                                        if(!p.getAddress().isEmpty())
                                                            ad="lieudit_"+ env.chrchUTF8(p.getAddress());
                                                    if(p.getDescription()!=null)
                                                        if(!p.getDescription().isEmpty())
                                                            desc=env.chrchUTF8(p.getDescription());*/
                                                    if(p.getStyleUrl()!=null)
                                                        if(!p.getStyleUrl().isEmpty())
                                                            p.setStyleUrl(
                                                                    p.getStyleUrl());
            rsentite= addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                   
        }else if(geo instanceof Point)
        {
          Point point = (Point) geo; 
             
                  List<Coordinate> listCo = point.getCoordinates();
                  coordinates=new ArrayList<Coordinate>(
                          listCo);   
                  
                  /*if(name!=null)
                    if(!name.isEmpty())
                        name=env.chrchUTF8(name);
                  if(p.getAddress()!=null)
                     if(!p.getAddress().isEmpty())
                        ad="point_"+ env.chrchUTF8(p.getAddress());
                  if(p.getDescription()!=null)
                     if(!p.getDescription().isEmpty())
                        desc=env.chrchUTF8(p.getDescription());*/
                  if(p.getStyleUrl()!=null)
                     if(!p.getStyleUrl().isEmpty())
                        p.setStyleUrl(
                                p.getStyleUrl());
                                                    
                  rsentite=addPoint(point.getCoordinates().get(0), name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                           
        }else if(geo instanceof Polygon)
        {//LinearRing
             
            
            
         Polygon  pg = (Polygon)geo;
                
                      Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                      coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());
                  
                  
                  if(p.getStyleUrl()!=null)
                     if(!p.getStyleUrl().isEmpty())
                        p.setStyleUrl(
                                p.getStyleUrl());
                      
                  rsentite=addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl()); 
                     
        }else if(geo instanceof MultiGeometry)
        {
          MultiGeometry multiGe=  (MultiGeometry)geo;
                                                              
             name= p.getName();
             List<Geometry> lMGeo = multiGe.getGeometry();
             for(Iterator<Geometry> iter=lMGeo.iterator(); iter.hasNext();) 
             {
                 Geometry gNxt = iter.next(); 
                 Xcption.Xcption("","MultiGeometry ****//***  ChargeKml  defini  definPlcmk( Placemark p)  p.getGeometry() instanceof MultiGeometry");
                     Xcption.Xcption("",gNxt.toString());
                      if(gNxt instanceof LineString)
                        {
                            LineString ligne = (LineString) gNxt;
                                coordinates = (ArrayList<Coordinate>) ligne.getCoordinates();
                                   if(p.getStyleUrl()!=null)
                                      if(!p.getStyleUrl().isEmpty())
                                         p.setStyleUrl(
                                           p.getStyleUrl());

                            rsentite= addLigne(coordinates,name, p.getAddress(), p.getDescription(), p.getStyleUrl());

                        }
                        else if(gNxt instanceof LinearRing)
                        {
                           LinearRing points = (LinearRing) gNxt;


                                                                    ArrayList<Coordinate> listCo = (ArrayList<Coordinate>) points.getCoordinates();
                                                                coordinates=new ArrayList<Coordinate>(listCo);

                                                                    if(p.getStyleUrl()!=null)
                                                                        if(!p.getStyleUrl().isEmpty())
                                                                            p.setStyleUrl(
                                                                                    p.getStyleUrl());
                            rsentite= addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());

                        }else if(gNxt instanceof Point)
                        {
                          Point point = (Point) gNxt; 

                                  List<Coordinate> listCo = point.getCoordinates();
                                  coordinates=new ArrayList<Coordinate>(
                                          listCo);   

                                  if(p.getStyleUrl()!=null)
                                     if(!p.getStyleUrl().isEmpty())
                                        p.setStyleUrl(
                                                p.getStyleUrl());

                                  rsentite=addPoint(point.getCoordinates().get(0), name, p.getAddress(), p.getDescription(), p.getStyleUrl());

                           }else if(p.getGeometry() instanceof Polygon)
                            {//LinearRing



                             Polygon  pg = (Polygon)gNxt;

                                          Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                          coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());


                                      if(p.getStyleUrl()!=null)
                                         if(!p.getStyleUrl().isEmpty())
                                            p.setStyleUrl(
                                                    p.getStyleUrl());

                                      rsentite=addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl()); 

                            }
             }
                                                   
        }else
        {
            Xcption.Xcption("definPlcmk( Placemark p)  defini","non defini, instance kml");
           
            
        }
        }catch(Exception ex)
        {
            Xcption.Xcption("Exception definPlcmk( Placemark p)  Exception",ex.getMessage());
            Xcption.Xcption("","p.getGeometry()"+p.getGeometry()+"name "+name+"coordinates "+coordinates);
            gKml.setGereDonne(new run.ejb.ejbkml.GereDonne());
        }
        
        return rsentite;
    }
   /* public void lance()
    {System.out.println(doc.getName());
	        List<Feature> t = doc.getFeature();
    
                
	        for(Object o : t)
                {

                    //dossier principale
                    
                    Placemark p;
                    if(o instanceof Folder)
                    {
                            
                        Folder f = (Folder)o;

                        List<Feature> tg = f.getFeature();

if(bvisualise)
{
   root = new DefaultTreeNode(f.getName(), null);  
   parent=root;
}
                        for(Object ftg : tg)
                        {
                            Polygon pg;
                            Point pt; 
                            //traitement par ville
                            if(ftg instanceof Placemark)
                                    {
                                       p = (Placemark) ftg; 

                                        name = p.getName();
                                        p.setAddress(f.getName()+" "+name);

                                        LineString points;
                                        try{
                                         points = (LineString) p.getGeometry(); 
                                        if(bvisualise)
                                        {
                                            enfant = new DefaultTreeNode(p.getName(), parent);
                                        }else{
                                            coordinates = new ArrayList<Coordinate>( points.getCoordinates());

                                         addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                        }
                                         
                                        }catch(ClassCastException ex)
                                        {

                                           if(p.getGeometry() instanceof Polygon)
                                           {

                                                pg = (Polygon)p.getGeometry();
                                                                if(bvisualise)
                                                                {
                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                }else
                                                                {
                                                                   Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                                                   coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());
                                                                   addLigne(coordinates, name,"place "+ p.getAddress(), p.getDescription(), p.getStyleUrl()); 
                                                                }
                                           }else if(p.getGeometry() instanceof LineString)
                                           {

                                                points = (LineString) p.getGeometry(); 
                                                if(bvisualise)
                                                {
                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                }else
                                                {
                                                    ArrayList<Coordinate> listCo = (ArrayList<Coordinate>) points.getCoordinates();
                                                coordinates=new ArrayList<Coordinate>(listCo);
                                                         addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                                }               
                                           }else if(p.getGeometry() instanceof Point)
                                           {

                                                Point point = (Point) p.getGeometry(); 
                                                if(bvisualise)
                                                {
                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                }else
                                                {
                                                    List<Coordinate> listCo = point.getCoordinates();
                                                coordinates=new ArrayList<Coordinate>(listCo);        

                                                         addPoint(point.getCoordinates().get(0), name,"point"+" "+p.getAddress(), p.getDescription(), p.getStyleUrl());

                                                }   
                                           }else
                                           {    
                                                   MultiGeometry multiGe=  (MultiGeometry)p.getGeometry();
                                                if(bvisualise)
                                                {
                                                    parent = new DefaultTreeNode(p.getName(), parent);
                                                }                                                   
                                                  name= p.getName();
                                                List<Geometry> lMGeo = multiGe.getGeometry();
                                                    for(Iterator<Geometry> iter=lMGeo.iterator(); iter.hasNext();) 
                                                    {
                                                   Geometry gNxt = iter.next();
                                                       if(gNxt instanceof Polygon)
                                                        {

                                                             pg = (Polygon)gNxt;
                                                                if(bvisualise)
                                                                {
                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                }else
                                                                {
                                                                    Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                                                                coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());
                                                                                addLigne(coordinates, name,"place "+ p.getAddress(), p.getDescription(), p.getStyleUrl());
                       
                                                                }
                                                        }else if(gNxt instanceof LineString)
                                                        {

                                                             points = (LineString) gNxt;   
                                                            if(bvisualise)
                                                            {
                                                                enfant = new DefaultTreeNode(p.getName(), parent);
                                                            }else
                                                            {
                                                              coordinates = new ArrayList<Coordinate>( points.getCoordinates());
                                                              addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());
  
                                                            }
                                                                      
                                                        }else if(p.getGeometry() instanceof Point)
                                                        {

                                                             Point point = (Point) p.getGeometry();   
                                                                    if(bvisualise)
                                                                    {
                                                                        enfant = new DefaultTreeNode(p.getName(), parent);
                                                                    }else
                                                                    {
                                                                      coordinates = new ArrayList<Coordinate>( point.getCoordinates());
                                                                      addPoint(point.getCoordinates().get(0), name,"point"+" "+p.getAddress(), p.getDescription(), p.getStyleUrl());  
                                                                    }
                                                                      
                                                        }else 
                                                        {
                                                            System.err.println("multiGe.getGeometry()");System.out.println(multiGe.getGeometry().toString());
                                                        }
                                                    }                

                                           }





                                        }

                                    }
                            else
                            { //dossier zone

                            Folder g = (Folder) ftg; 
                            if(bvisualise)
                            {
                                parent = new DefaultTreeNode(g.getName(), root);
                            }

                            List<Feature> lOj = g.getFeature(); 

                            for(Object plc : lOj)
                            {
                    
                                    if(plc instanceof  Placemark)
                                    {
                                        p = (Placemark) plc; 
                                        
                                        name = p.getName();
                                        p.setAddress(f.getName()+" "+g.getName()+" "+name);
                                        try{
                                        LineString points = (LineString) p.getGeometry(); 
                                        if(bvisualise)
                                        {
                                            enfant = new DefaultTreeNode(p.getName(), parent);
                                        }else
                                        {
                                           coordinates = new ArrayList<Coordinate>( points.getCoordinates());

                                         addLigne(coordinates, name,"zone"+" "+ g.getName()+" "+name, p.getDescription(), g.getStyleUrl()); 
                                        }
                                        }catch(ClassCastException ex)
                                        {

                                           if(p.getGeometry() instanceof Polygon);
                                           {
                                               try
                                               {
                                                pg = (Polygon)p.getGeometry();
                                                                if(bvisualise)
                                                                {
                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                }else
                                                                {
                                                                  Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                                                   coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());
                                                                   addLigne(coordinates, name,"place "+p.getAddress(), g.getDescription(), g.getStyleUrl());  
                                                                }
                                               }catch(ClassCastException cce)
                                               {
                                                   //System.err.println(cce);
                                                    pt = (Point)p.getGeometry();
                                                    if(bvisualise)
                                                    {
                                                        enfant = new DefaultTreeNode(p.getName(), parent);
                                                    }else
                                                    {
                                                        coordinates = new ArrayList<Coordinate>( pt.getCoordinates());
                                                                      addPoint(pt.getCoordinates().get(0), name, "lieu"+" "+p.getAddress(), g.getDescription(), g.getStyleUrl());

                                                    }

                                               }

                                           }


                                        }
                                    }
                                    if(plc instanceof Folder)
                                    {//dossier ville


                                            Folder ville = (Folder) plc;
                                        if(bvisualise)
                                        {
                                            parent = new DefaultTreeNode(ville.getName(), parent);
                                        }
                                            List<Feature> lPl = ville.getFeature();

                                             for(Object plVl : lPl)
                                                {
                                                    if(plVl instanceof Placemark)
                                                    {
                                                        p = (Placemark) plVl;
                                                                                                               
                                                        name =p.getName();
                                                    try{

                                                        LineString points = (LineString) p.getGeometry(); 
                                                        if(bvisualise)
                                                        {
                                                            enfant = new DefaultTreeNode(p.getName(), parent);
                                                        } else
                                                        {
                                                           coordinates = new ArrayList<Coordinate>( points.getCoordinates());

                                                        addLigne(coordinates, name, g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl()); 
                                                        }
                                                         
                                                        }catch(ClassCastException cce)
                                                        {
                                                            if(p.getGeometry() instanceof Point)
                                                            {
                                                                pt = (Point) p.getGeometry();
                                                                if(bvisualise)
                                                                {
                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                }else
                                                                {
                                                                     coordinates = new ArrayList<Coordinate>( pt.getCoordinates());

                                                               addPoint(pt.getCoordinates().get(0), name, "point"+" "+g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl());
                                                                }
                                                               
                                                            }else
                                                            if(p.getGeometry() instanceof Polygon)
                                                            {
                                                               pg = (Polygon) p.getGeometry();
                                                                if(bvisualise)
                                                                {
                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                }else
                                                                {
                                                                    LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();
                                                                 coordinates = new ArrayList<Coordinate>( linearRing.getCoordinates());

                                                                addLigne(coordinates, name, "place "+g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl());
                                                                }

                                                                     
                                                            }


                                                        }
                                                    }else if(plVl instanceof Polygon)
                                                    {

                                                            pg = (Polygon) plVl;

                                                            name = pg.getId();
                                                            LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();
                                                            if(bvisualise)
                                                            {
                                                                enfant = new DefaultTreeNode(pg.getId(), parent);
                                                            }else
                                                            {
                                                               coordinates = new ArrayList<Coordinate>( linearRing.getCoordinates());

                                                            addLigne(coordinates, name, "place "+g.getName()+" "+ville.getName()+" "+name, ville.getDescription(), g.getStyleUrl());
 
                                                            }              
                                                    }else if(plVl instanceof Point)
                                                    {
                                                            pt = (Point) plVl;
                                                            if(bvisualise)
                                                            {
                                                                enfant = new DefaultTreeNode(name, parent);
                                                            }else
                                                            {
                                                                // name = pt.getId();

                                                             coordinates = new ArrayList<Coordinate>( pt.getCoordinates());

                                                            addPoint(pt.getCoordinates().get(0), name, "point"+" "+g.getName()+" "+ville.getName()+" "+name, ville.getDescription(), g.getStyleUrl());

                                                            }
                                                           
                                                    }
                                                    else if(plc instanceof Folder)
                                                    {//dossier lieu dit


                                                             ville = (Folder) plc;
                                                            if(bvisualise)
                                                            {
                                                                parent = new DefaultTreeNode(ville.getName(), parent);
                                                            }
                                                             lPl = ville.getFeature();

                                                             for(Object ssplVl : lPl)
                                                                {
                                                                    if(ssplVl instanceof Placemark)
                                                                    {
                                                                        p = (Placemark) ssplVl;
                                                                                                                                               
                                                                        name =p.getName();
                                                                    try{

                                                                        LineString points = (LineString) p.getGeometry();
                                                                        if(bvisualise)
                                                                        {
                                                                            enfant = new DefaultTreeNode(name, parent);
                                                                        }else
                                                                        {
                                                                            coordinates = new ArrayList<Coordinate>( points.getCoordinates());

                                                                            addLigne(coordinates, name, g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl());
                                                                        }
                                                                         
                                                                        }catch(ClassCastException cce)
                                                                        {
                                                                            if(p.getGeometry() instanceof Point)
                                                                            {
                                                                                pt = (Point) p.getGeometry();
                                                                                if(bvisualise)
                                                                                {
                                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                                }else
                                                                                {
                                                                                    coordinates = new ArrayList<Coordinate>( pt.getCoordinates());

                                                                               addPoint(pt.getCoordinates().get(0), name, "point"+" "+g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl());
                                                                                }   
                                                                            }else
                                                                                if(p.getGeometry() instanceof Polygon)
                                                                            {
                                                                               pg = (Polygon) p.getGeometry();
                                                                                if(bvisualise)
                                                                                {
                                                                                    enfant = new DefaultTreeNode(p.getName(), parent);
                                                                                }else
                                                                                {
                                                                                    LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();
                                                                                 coordinates = new ArrayList<Coordinate>( linearRing.getCoordinates());

                                                                                addLigne(coordinates, name, "place "+g.getName()+" "+ville.getName()+" "+name, p.getDescription(), g.getStyleUrl());
                                                                                }         
                                                                            }


                                                                        }
                                                                    }else if(ssplVl instanceof Polygon)
                                                                    {

                                                                            pg = (Polygon) ssplVl;

                                                                            name = pg.getId();
                                                                            if(bvisualise)
                                                                            {
                                                                                enfant = new DefaultTreeNode(name, parent);
                                                                            }else
                                                                            {
                                                                                LinearRing linearRing = pg.getOuterBoundaryIs().getLinearRing();

                                                                             coordinates = new ArrayList<Coordinate>( linearRing.getCoordinates());

                                                                            addLigne(coordinates, name, "place "+g.getName()+" "+ville.getName()+" "+name, ville.getDescription(), g.getStyleUrl());
                                                                            }    
                                                                    }else if(ssplVl instanceof Point)
                                                                    {
                                                                            pt = (Point) ssplVl;
                                                                            if(bvisualise)
                                                                            {
                                                                                enfant = new DefaultTreeNode(name, parent);
                                                                            }else
                                                                            {
                                                                               coordinates = new ArrayList<Coordinate>( pt.getCoordinates());

                                                                            addPoint(pt.getCoordinates().get(0), name, "point"+" "+g.getName()+" "+ville.getName()+" "+name, ville.getDescription(), g.getStyleUrl());
 
                                                                            }
                                                                             
                                                                    }else if(ssplVl instanceof Folder)
                                                                    {
                                                                        
                                                                    }

                                                                }

                                                    }

                                                }
                                             parent=parent.getParent();

                                    }

                                }

                            }//else traitement par ville

                        }

                    }else
                        if(o instanceof Placemark)
                        {
                            p = (Placemark) o; 
                                                     
                                    name = p.getName();
                                    p.setAddress(name);
                                   if(bvisualise)
                                    {
                                        parent = new DefaultTreeNode(p.getName(), root);
                                    }else
                                    {
                                       
                                    LineString points;
                                    Polygon pg;
                                    try{
                                     points = (LineString) p.getGeometry();   
                                     coordinates = new ArrayList<Coordinate>( points.getCoordinates());
                                                       
                                    }catch(ClassCastException ex)
                                    {
                                        
                                       if(p.getGeometry() instanceof Polygon)
                                       {
                                           
                                            pg = (Polygon)p.getGeometry();
                                             
                                                              Boundary outerBoundaryIs = pg.getOuterBoundaryIs();
                                                               coordinates = new ArrayList<Coordinate>( outerBoundaryIs.getLinearRing().getCoordinates());
                                                               addLigne(coordinates, name,"place "+ name, p.getDescription(), p.getStyleUrl());
                                                              
                                                              
                                       }else if(p.getGeometry() instanceof LineString)
                                       {
                                                
                                            points = (LineString) p.getGeometry();   
                                            ArrayList<Coordinate> listCo = (ArrayList<Coordinate>) points.getCoordinates();
                                            coordinates=new ArrayList<Coordinate>(listCo);
                                                     addLigne(coordinates, name, p.getAddress(), p.getDescription(), p.getStyleUrl());
                                           
                                       }else if(p.getGeometry() instanceof Point)
                                       {
                                                
                                            Point point = (Point) p.getGeometry();   
                                            List<Coordinate> listCo = point.getCoordinates();
                                            coordinates=new ArrayList<Coordinate>(listCo);        
                                                      
                                                     addPoint(point.getCoordinates().get(0), name, "point"+" "+p.getAddress(), p.getDescription(), p.getStyleUrl());
                                           
                                       }
                                    }
                               }
                        }
	        }
                
        
    }*/

     private Object refresh(Object o)
    {
        if(o!=null)
        {
            Xcption.Xcption("","GereCmpte refresh(Object o) o.toString()"+o.getClass());
                                Xcption.Xcption("",o.toString());
            ArrayList rch=null;
            if(o instanceof RsEntite)
            {
                RsEntite rs=(RsEntite) o;
                if(rs instanceof Ligne)
                    rch = gKml.getGereDonne().rch((String) rs.getNom(), new Ligne(), "nom");
                else if(rs instanceof Pt)
                    rch = gKml.getGereDonne().rch((String) rs.getNom(), new Pt(), "nom");
                
            }else if(o instanceof IntBaz)
            {
                IntBaz ibz=(IntBaz) o;
                if(ibz instanceof Tour)
                    rch = gKml.getGereDonne().rch((String) ibz.getNom(), new Tour(), "nmTour");
                else if(ibz instanceof Prmtr)
                {
                    Prmtr pr=(Prmtr) ibz;
                    
                    rch = gKml.getGereDonne().rch( pr.getIdPrmtr(), new Prmtr(), "idPrmtr");
                }
            }else if(o instanceof Compte)
            {
                Compte c=(Compte) o;
                 rch = gKml.getGereDonne().rch((String) c.getNom(), new Compte(), "nom");
            }else if(o instanceof Contact)
            {
                Contact c=(Contact) o;
                 rch = gKml.getGereDonne().rch((String) c.getObjet(), new Contact(), "objet");
            }

            if(rch!=null)
                for(Object tmp:rch)
                    if(tmp.equals(o))
                        o=tmp;                    
        }else
        {
            Xcption.Xcption("","GereCmpte refresh(Object o) o.toString()");
                                Xcption.Xcption("","o est null");
        }
        
        
        
        return o;
    }
    public DefaultTreeNode getRoot() {
        return root;
    }

    public void setRoot(DefaultTreeNode root) {
        this.root = root;
    }

    public Map<String, Placemark> getMplcchg() {
        return mplcchg;
    }

    public void setMplcchg(Map<String, Placemark> mplcchg) {
        this.mplcchg = mplcchg;
    }

    public HashMap<String, Folder> getMdchg() {
        return mdchg;
    }

    public void setMdchg(HashMap<String, Folder> mdchg) {
        this.mdchg = mdchg;
    }



    public List<RsEntite> getChgCpte() {
        return chgCpte;
    }

    public void setChgCpte(List<RsEntite> chgCpte) {
        this.chgCpte = chgCpte;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public List<String> getTbverif() {
        return tbverif;
    }

    public void setTbverif(List<String> tbverif) {
        this.tbverif = tbverif;
    }

    public Compte getCpte() {
        return cpte;
    }

    public void setCpte(Compte cpte) {
        this.cpte = cpte;
    }

    public boolean isBvisualise() {
        return bvisualise;
    }

    public void setBvisualise(boolean bvisualise) {
        this.bvisualise = bvisualise;
    }

    public boolean isCfm() {
        return cfm;
    }

    public void setCfm(boolean cfm) {
        this.cfm = cfm;
    }

    private void upBdd(Ligne lg, ArrayList<Coordinate> cods) {
        if(!lg.getCoords().isEmpty())
            {
                int i=0;
                for(Coord co:lg.getCoords())
                {
                    Coordinate get = cods.get(i);
                        if(get.getLatitude()==co.getLatitude()&&
                                get.getLongitude()==co.getLongitude())
                            if(get.getAltitude()!=co.getAlt())
                                co.setAlt(get.getAltitude());
                        
                        gKml.boolcreateObj(false);
                                gKml.addBdd(co);
                    i++;
                }
                lg.setId(null);
            }
    }
    private String perdanlPer(String nme,String perd, String ad)
    {
        Date ddeb;
        Date dfin;
        String hrs = null;
        String[] tbplsr=new String[1];
        
        String deb = perd.split("du")[1].split("au")[0];
                
            String fin =perd.split("au")[1].split("de")[0];
            if(perd.contains("de"))
            {
                hrs=perd.split("au")[1].split("de")[1];
                
                if(hrs.contains(","))
                {
                     tbplsr = hrs.split(",");

                }else
                    tbplsr[0]=hrs;
            }
            String[] tbf = fin.split("/");
            char[] aryfin = tbf[2].toCharArray();
            if(aryfin.length==2)
            {
                fin=fin.replace(""+aryfin[aryfin.length-2]+aryfin[aryfin.length-1],
                        "20"+aryfin[aryfin.length-2]+aryfin[aryfin.length-1]);
            }
            
            tbf = fin.split("/");
            if(!deb.contains(("/")))
            {
                ddeb=new Date();
                deb=deb.replace(" ", "");
                
                ddeb=new Date(Integer.valueOf(tbf[2].replace(" ", ""))-1900,
                        Integer.valueOf(tbf[1].replace(" ", "")),
                        Integer.valueOf(deb.replace(" ", "")));
                
                deb+="/"+tbf[1].replace(" ","")+"/"+tbf[2].replace(" ", "");
                
                
            }else
            {
                String[] tbd =deb.split("/");
                int years=Integer.valueOf(tbd[2].replace(" ", ""));
                if(years>100)
                    years=years-1900;
                ddeb=new Date(years,
                        Integer.valueOf(tbd[1].replace(" ", ""))-1,
                        2000+Integer.valueOf(tbd[0].replace(" ", "")));
            }
            int years=Integer.valueOf(tbf[2].replace(" ", ""));
                if(years>100)
                    years=years-1900;
            dfin=new Date(years-1900,
                        Integer.valueOf(tbf[1].replace(" ", ""))-1,
                        2000+Integer.valueOf(tbf[0].replace(" ", "")));
            
           perd="du"+deb+"au"+perd.split("au")[1];
           
           Evnmt evnmt = new Evnmt();
           String desc="musique";
           if(tbplsr[0]!=null)
            for(String dt:tbplsr)
            {
                String[] tbhm = dt.split("h");
                    ddeb.setHours(Integer.valueOf(tbhm[0].replace(" ", "")));
                    if(tbhm[1]!=null)
                        ddeb.setMinutes(Integer.valueOf(tbhm[1].replace(" ", "")));
                    
                
                 if(nme.contains("\\("))
                     desc=nme.split("\\(")[1].replace("\\)", "");

                 
                ddeb=new Date(ddeb.getYear(), ddeb.getMonth(), ddeb.getDay()+1);
                if(ddeb.after(dfin) )
                    ddeb=dfin;
                
                evnmt=new Evnmt( nme,  ddeb,dfin, ad, desc);
                levnmts.add(evnmt);
            }
           else
           {
               if(nme.contains("\\("))
                     desc=nme.split("\\(")[1].replace("\\)", "");
               evnmt=new Evnmt( nme,  ddeb,dfin, ad, desc);
                levnmts.add(evnmt);
           }
           
       
        
                perd=nme+" "+Variable.getLtitre().get(30)+" "+UtilRsns.datePourString((java.util.Date)evnmt.getDt() , DateTools.Resolution.MINUTE)+" au "+UtilRsns.datePourString((java.util.Date)evnmt.getFin(), DateTools.Resolution.MINUTE);
                
                
        return perd;
    }
    private String smpldanlPer(String nme,String trfdt, String ad)
    {
            String strdt=trfdt;
                trfdt=nme+" ";
                
                String dcpm="dim_lun_mar_mer_jeu_ven_sam";
                String[] tbdt = strdt.split(" ");
                int jr = 0;
      
                if(tbdt.length>1)
                    jr=Integer.valueOf(tbdt[2]);
                int mois = 0;
                dcpm="JANV_FEV_MARS_AVR_MAI_JUIN_JUIL_AOUT_SEPT_OCT_NOV_DEC";
                if(Arrays.asList(dcpm.split(("_"))).contains(tbdt[3]))
                    mois = Arrays.asList(dcpm.split(("_"))).indexOf(tbdt[3]);

                String heure=null;
                if(strdt.contains(Variable.getAnxdact()[0]))
                    heure = strdt.split(Variable.getAnxdact()[0])[0].split("-")[1].split(",")[0];
                else if(strdt.contains("-"))
                    heure = strdt.split("-")[1].split(",")[0];
                Date date;
                if(heure!=null)
                {
                    String[] tbh = heure.split(":");
                    date= new Date(Integer.valueOf(tbdt[4])-1900, mois, jr,Integer.valueOf(tbh[0].replace(" ", "")),Integer.valueOf(tbh[1].replace("\n", "")));
                }else
                    date= new Date(Integer.valueOf(tbdt[4])-1900, mois, jr);
                
                    
                String desc="musique";
                if(nme.contains("\\("))
                    desc=nme.split("\\(")[1].replace("\\)", "");
                Evnmt evnmt=new Evnmt( nme, date, ad, desc);
                levnmts.add(evnmt);
                
                trfdt=nme+" "+UtilRsns.datePourString((java.util.Date)evnmt.getDt(), DateTools.Resolution.MINUTE);
               /* if(tbevnmt[i].contains("("))
                    desc=tbevnmt[i].split("(")[1].split("]")[0];
                else
                    desc="musique";
              ajEvnmt(tbevnmt[i], null, desc, date, date, pt);*/
           
        return trfdt;
    }
    private String anlPer(String nm,String ad,String[] tb) 
    {
         
        String nme=tb[0];
        String perd=tb[1];
        if(!perd.contains(Variable.getLtitre().get(30)))
        {
            nme=null;
            perd=null;
            for(String str:tb)
            {
                if(str.contains(Variable.getLzact()[3]))
                    nme=str;
                else if(str.contains(Variable.getLtitre().get(30)))
                    perd=str;       
            }
            
            
        }
        if(!nme.contains(Variable.getLzact()[3]))
        {
            
            String[] split = nme.split(",");
            boolean baj=false;
            for(String str:split)
                if(str.contains(Variable.getLzact()[5]))
                {
                    nme=Variable.getLzact()[5];
                    if(nme.contains("("))
                        baj=true;
                }else if(baj)
                {
                    nme+=str;
                    if(nme.contains(")"))
                        baj=false;
                }

                        
        }
        String trfdt="";
            
           if(perd.contains(Variable.getLtitre().get(30))) 
                trfdt= perd.replace(Variable.getLtitre().get(30)+":","");
           
           String[] sptrfdt = trfdt.split(",");
           String[] tbnme = new String[sptrfdt.length];
           /* if(sptrfdt.length>1)
                if(sptrfdt[1].length()<7)
                {
                    String dt=sptrfdt[0].split("de")[0];
                    for(int i=1; i<sptrfdt.length; i++)
                        sptrfdt[i]=dt+" de "+sptrfdt[i];
                }*/
           if(nme!=null)
              tbnme = nme.split(",");
           else
               for(int i=0; i<sptrfdt.length;i++)
                   tbnme[i]=nm;
          
           perd=""; 
       try{
           levnmts=new ArrayList<Evnmt>();
            String str;
           for(int i=0; i<sptrfdt.length; i++)
            {
                if(tbnme[i]!=null)
                        str=tbnme[i];
                
                if(sptrfdt[i].contains("AOÃ›"))
                    sptrfdt[i]="AOUT";
                
                 if(sptrfdt[i].contains("du"))
                {
                    
                    
                        perd+=perdanlPer(tbnme[i],sptrfdt[i],ad)+",";
                   
                }else if(sptrfdt[i].contains("h")&&perd.length()<8)
                {
                    String[] splh = sptrfdt[i].split("h");
                    Evnmt père = levnmts.get(levnmts.size()-1);
                    Integer day =null;
                    Evnmt efnt;
                    if(père.getFin()!=null)
                    {
                        if(père.getFin().getDay()-1>=père.getDt().getDay())
                            day= père.getDt().getDay()+1;
                        else
                            day= père.getDt().getDay();
                        Date dt=new Date(père.getDt().getYear(), père.getDt().getMonth(),day ,
                                Integer.valueOf(splh[0].replace(" ","")),
                                Integer.valueOf(splh[1].replace(" ","")));
                     efnt=new Evnmt(père.getNom(), père.getDt(),père.getFin(), père.getAdresse(), père.getCateg());
                    }else
                    {
                       
                            day= père.getDt().getDay()+1;
                        
                        Date dt=new Date(père.getDt().getYear(), père.getDt().getMonth(),day ,
                                Integer.valueOf(splh[0].replace(" ","")),
                                Integer.valueOf(splh[1].replace(" ","")));
                     efnt=new Evnmt(père.getNom(), père.getDt(), père.getAdresse(), père.getCateg());
                    }
                        
                        levnmts.add(efnt);
                }else if(sptrfdt[i].contains("/"))
                {
                    String desc=nm;
                    if(nme.contains("\\("))
                        desc=nme.split("\\(")[1].replace("\\)", "");
                    
                    String[] split = sptrfdt[i].split("/");
                    Date date = new Date(Integer.getInteger(split[0].replace(" ", "")), Integer.getInteger(split[1]), Integer.getInteger(split[2].replace(" ", "")));
                    Evnmt evnmt=new Evnmt( tbnme[i], date, ad, desc);
                levnmts.add(evnmt);
                }
                else
                {
                    
                        perd+=smpldanlPer(tbnme[i],sptrfdt[i],ad)+",";
                    
                }
                 
            }
           mevnmts.put(nm, levnmts);
       }catch(Exception npex)
       {
           Xcption.Xcption("ChargeKml anlPer npex tbnme[i],sptrfdt[i],ad", nme+trfdt+ad+npex.getMessage());
           for(String str:sptrfdt)
               perd+=str;
       }
        return perd;
    }

    private void insertPt(Pt point, boolean fe) throws NullPointerException{
      
        if(!mevnmts.isEmpty())
           {
                Xcption.Xcption("*****************************"
                    + "**************test.getEvnmts()******************",point.getNom());
                levnmts = mevnmts.get(point.getNom());
                gKml.boolcreateObj(true);
                point.setEvnmts(
                        levnmts.stream().
                        peek(e->e.setLieux(point.getNom())).peek(gKml::addBdd).
                        collect(Collectors.toSet())
                );
                        
                   /* for(Evnmt evnmt:levnmts)
                    {
                                                               evnmt.setLieux(point.getNom());
                                                               
                                                               gKml.addBdd(evnmt);
                                                               
                     }
                point.setEvnmts(new HashSet(levnmts));*/
                                                   
        gKml.boolcreateObj(fe);
        gKml.fEm();
        gKml.cEm();  
           }
        gKml.addBdd(point); 
    }

    private String definDesc(Placemark p) 
    {
        String categ="";
         try{
                        
                       System.err.print("**********************************defini description ");System.err.println(p.getAddress());
                        if(p.getDescription()!=null)
                                 if(!p.getDescription().isEmpty())
                                 {
                                     //erreur fichier source
                                     if(p.getDescription().contains("festival"))
                                         p.setDescription(
                                                 p.getDescription().replace("festival", Variable.getLzact()[3]));
                                     if(p.getDescription().contains("activite"))
                                         p.setDescription(
                                                 p.getDescription().replace("activite", Variable.getPrmtr().get(5)));
                                     
                                     if(p.getDescription().contains(Variable.getPrmtr().get(5)+":")||
                                             p.getDescription().contains(Variable.getZact()+":"))
                                     {
                                         String[] split = p.getDescription().split("_");
                                         categ=split[0]+" ";
                                         
                                         if(categ.contains(Variable.getLzact()[5]))
                                         {
                                             String perd = null;
                                             String rplperd = null;
                                             
                                             for(String str:split)
                                                 if(str.contains(Variable.getLtitre().get(30)))
                                                 {
                                                     perd=str;
                                                     rplperd=anlPer(p.getName(),p.getAddress(),split);
                                                 }
                                             
                                              p.setDescription(
                                                    p.getDescription().replace(perd, rplperd));
                                               
                                         }
                                         
                                         p.setDescription(
                                                    p.getDescription().replace(categ+"_", " "));
                                         
                                     }
                                     if(p.getDescription().contains(Variable.getPrmtr().get(4))||
                                             p.getDescription().contains(Variable.getPrmtr().get(6)))
                                     {
                                         String[] split = p.getDescription().split("_");
                                         categ=split[0]+" ";
                                         if(split.length>=2)
                                             categ+=split[1];
                                         /*p.setDescription(
                                            p.getDescription().replace(categ, "").replace("_", "")
                                         );*/
                                     }
                                     if(p.getDescription().contains(Variable.getLzact()[3]+":"))
                                         {
                                             
                                             String[] split = p.getDescription().split(Variable.getLzact()[3]+":")[1].split("_");
                                            categ+=Variable.getLzact()[3];
                                          
                                            /*if(split[1].contains("_"))
                                                perd=split[1].split("_")[0];
                                            else*/
                                            String perd=split[1];
                                           
                                            Xcption.Xcption("perd********",perd);
                                            String rplperd=anlPer(p.getName(),p.getAddress(),split);
                                         String replace = p.getDescription().replace(split[0]+"_", " ");
                                            replace= replace.replace(perd, rplperd);
                                            
                                            p.setDescription(replace);
                                         }
                                     if(p.getDescription().contains("_")&&categ=="")
                                        {
                                           
                                            String[] split = p.getDescription().split("_");
                                            if(categ.equals(""))
                                                categ=split[0];
                                            if(split.length>=2)
                                                if(verifEntrer.contains(split[1]))
                                                    categ+=split[1]+" ";
                                           /* p.setDescription(
                                                    p.getDescription().replace(categ,"").replace("_"," "));*/
                                        }
                                       
                                                                    
                                 }
                        
                                 if(categ.equals(""))
                                 {
                                     categ=verifEntrer.stream().
                                             filter(c->name.contains(c)).
                                             findFirst().get();
                                     /*for(String c:verifEntrer)
                                                if(name.contains(c))
                                                categ=c;*/
                                      
                                 }
                     
                                       
                        }catch(Exception ex)
                        {
                            Xcption.Xcption("defini description Exception",ex.getMessage());
                        }
         return categ;
    }

    


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.kml;

import com.google.common.collect.Lists;
import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.gx.Tour;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import run.ejb.base.Classmt;

import run.ejb.base.Variable;
import run.ejb.entite.climat.Meteo;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.entite.util.bdd.ChrSpe;
import run.ejb.util.ex.Xcption;
import run.ejb.util.kml.schema.IconKml;
import run.ejb.util.kml.schema.LigneKml;
import run.ejb.util.kml.schema.ObjetKml;
import run.ejb.util.rsentite.schema.PrmtrSchm;
import run.ejb.util.kml.schema.Ref;
import run.ejb.util.kml.schema.RsnsObj;

/**
 *
 * @author Administrateur
 */
public class TracerRsns
{

    
    private Feature plan;
    private Folder dossier;
    private Placemark p=null;
    private Placemark retour=null;
    private PrmtrSchm schema;
    private Polygon po;
    private Folder d;
    private float taille;
    
    private List<Feature> lFeat;
   // private List<Feature> lFMeteo;
    
    private String couleur;
   
    private boolean bclim;
    
    private List<Long> lid;
    
    private final List<String> srdmCouleur;
    private final List<String> sprmtr;
    private final Map<String, String> smicone;
    private final List<String> sltitre ;
    private final Map<String, String> smIclim;
    public TracerRsns() 
    {
         srdmCouleur = Variable.getRdmCouleur();
         sprmtr = Variable.getPrmtr();
         smicone = Variable.getMicone();
         sltitre = Variable.getLtitre();
         smIclim = Variable.getmIclim();
         
        dossier=new Folder();
        lFeat=new ArrayList<Feature>();
        //lFMeteo=new ArrayList<Feature>();
        
    }
public TracerRsns(Placemark p) 
    {
        srdmCouleur = Variable.getRdmCouleur();
         sprmtr = Variable.getPrmtr();
         smicone = Variable.getMicone();
         sltitre = Variable.getLtitre();
         smIclim = Variable.getmIclim();
         
        this.p=p;
    }
    public TracerRsns(Polygon po) 
    {
        srdmCouleur = Variable.getRdmCouleur();
         sprmtr = Variable.getPrmtr();
         smicone = Variable.getMicone();
         sltitre = Variable.getLtitre();
         smIclim = Variable.getmIclim();
         
        this.po=po;
    }

    
    public TracerRsns(Feature plan) 
    {
        srdmCouleur = Variable.getRdmCouleur();
         sprmtr = Variable.getPrmtr();
         smicone = Variable.getMicone();
         sltitre = Variable.getLtitre();
         smIclim = Variable.getmIclim();
         
        this.plan = plan;
    }
    
    public TracerRsns(Feature plan, String couleur) 
    {
        srdmCouleur = Variable.getRdmCouleur();
         sprmtr = Variable.getPrmtr();
         smicone = Variable.getMicone();
         sltitre = Variable.getLtitre();
         smIclim = Variable.getmIclim();
         
        dossier=new Folder();
        lFeat=new ArrayList<Feature>();
        
        this.plan = plan;
        this.couleur=couleur;
        
    }
    public void createTracerbySchema(RsnsObj rsnsSchema)
    {
        lid=new ArrayList<Long>();
        
        boolean bmeteo=false;
        boolean rt=false;
        this.schema=schema;
        
            
                    if(rsnsSchema.getRef().equals("zone"))
                            rt=false;
                    else if(rsnsSchema.getRef().contains("tour"))
                        rt=true;
                    else if(rsnsSchema.getRef().contains("meteo"))
                        bmeteo=true;
                    else if(retour==null)
                        rt=true;
                     Meteo meteo = new Meteo();
                    Ref prmtrVue = rsnsSchema.getPrmtrVue();
                    int onf=0;
                  //GereDonne gd=new GereDonne();  
                 if(rsnsSchema.getPourKml()!=null)  
                  for(List list:rsnsSchema.getPourKml())   
                  {
                   try{
                     
                    for(Object o:list)
                    {
                       p=new Placemark();
                        RsEntite rs= null;
                        if(o instanceof RsEntite)
                        {
                            rs=(RsEntite) o;
                            p.setId(rs.getNom());
                            lid.add(rs.getId());
                        }
                        else if(o instanceof Meteo)
                            meteo=(Meteo) o;
                        
                   
                   Xcption.Xcption("prmtrVue",prmtrVue);
                  
                   Xcption.Xcption("o",rs);
                  
                   if(o!=null)
                        if(prmtrVue instanceof IconKml&&o.getClass()!=meteo.getClass())//bloque erreur info meteo
                        {
                            IconKml ik=(IconKml) prmtrVue;
                           Pt pt ; 
                            Point point = p.createAndSetPoint();
                            List<Coordinate> lc = point.createAndSetCoordinates();
                            Coord coord=null;
                           if(rs instanceof Pt&&rsnsSchema.getRef()!="meteo/simple")
                           {
                               
                               //gd.createEM();
                               pt = (Pt) rs;
                                
                                 //coord = gd.getEm().find(pt.getCoord().getClass(), pt.getCoord().getId());
                               
                                p.setName(pt.getNom());
                                    String adresse = pt.getAdresse();
                                
                                ChrSpe spe=new ChrSpe();
                                boolean fee=false;
                                List<String> subList  = sprmtr.subList(12, sprmtr.size());
                                if(ik.getImgref().equals("text"))
                                    fee=true;
                                List<String> collect = subList.stream().filter(s->
                                        adresse.contains(s)).
                                        collect(Collectors.toList());
                                 
                              /*if(!fee)  
                                for(String prmtr:subList)
                                    if(!fee)
                                    {
                                        
                                            ik.setImgref(
                                                           smicone.get(prmtr));
                                                    fee=true;
                                           
                                       
                                    }else
                                    {
                                        ik.setImgref("_"+
                                                           smicone.get(prmtr));
                                    }*/
                                if(collect.isEmpty())
                                {
                                    collect = subList.stream().filter(s->
                                        pt.getDescription().contains(s)).
                                        collect(Collectors.toList());
                                    if(!collect.isEmpty())  
                                        fee=true;
                                        /*for(String prmtr:subList)
                                            if(!fee)
                                            {
                                                ik.setImgref(
                                                               smicone.get(prmtr));
                                                        fee=true;

                                            }*/
                                }
                                if(!fee)
                                {
                                    if(pt.getNom().contains(sprmtr.get(10))) 
                                            {
                                                ik.setImgref(
                                                               smicone.get(sprmtr.get(10)));
                                                
                                                p.setDescription(rs.getDescription().replace("liens ", ""));
                                                        fee=true;
                                            }
                                }
                                
                                 Coordinate coordinate=new Coordinate(pt.getLongitude(),pt.getLatitude());
                                    lc.add(coordinate);
                                 point.setCoordinates(lc);
                                p.setAddress(adresse);
                               if(adresse.contains(sltitre.get(14)))
                               {
                                   p.setDescription(rs.getDescription().replace("liens ", ""));
                               }
                               
                                p.setStyleUrl(
                                        fontClick(ik,onf));
                               
                                
                                if(rt)
                                {
                                    retour=p;
                                    rt=false;
                                }
                           }
                           else if(rs instanceof Ligne)
                           {
                               Ligne l = (Ligne) rs;
                                List<Coord> coords = Lists.newArrayList(l.getCoords());
                                if(!coords.isEmpty())
                                {
                                    int abs = Math.abs(coords.size()/4);
                                    double lat =(coords.get(0).getLatitude()+coords.get(1*abs).getLatitude()+coords.get(2*abs).getLatitude()+coords.get(3*abs).getLatitude())
                                            /4;
                                    double lng =(coords.get(0).getLongitude()+coords.get(1*abs).getLongitude()+coords.get(2*abs).getLongitude()+coords.get(3*abs).getLongitude())
                                            /4;

                                  coord = new Coord();
                                    coord.setLatitude(lat);
                                    coord.setLongitude(lng);
                                
                               
                                  
                                        
                                if(rsnsSchema.getRef().equals("meteo/simple"))
                                {
                                    p.setId("Météo"+p.getName());
                                    p.setDescription(meteo.getDeg()+"°");
                                    coord.setAlt(1000d);
                                        
                                
                                }
                                ik.setImgref(
                                                meteo.getUrlimg());
                                p.setStyleUrl(
                                        fontClick(ik,onf));
                                
                           }
                           
                            Coordinate coordinate =null;
                                // coordinate = new Coordinate(coord.getNom());
                              if(coord!=null)   
                                if(coord.getAlt()!=null)
                                {
                                    coordinate=new Coordinate(coord.getLongitude(),coord.getLatitude(),coord.getAlt());
                                }
                                else
                                {
                                    coordinate=new Coordinate(coord.getLongitude(),coord.getLatitude());
                                }

                                    lc.add(coordinate);

                                point.setCoordinates(lc);
                            if(rs.getAdresse()!=null)
                                p.setAddress(rs.getAdresse());
                            else
                                p.setAddress("");
                           }
                          
                            
                        }else if(prmtrVue instanceof ObjetKml)
                        {
                            
                                
                            ObjetKml objkml=(ObjetKml) prmtrVue;
                            
                            Ligne lg = (Ligne) rs;
                            if(objkml.getHexapoly()==null)
                            {
                                LineString lineString = p.createAndSetLineString();
                                try{
                                    List<Coordinate> lcodi=new ArrayList<Coordinate>();      
                                    List<Object> lo= Arrays.asList(lg.getCoords().toArray());
                                    boolean balt=false;
                                    for(Object oc:lo)
                                    {
                                        Coordinate codi;
                                        Coord co=(Coord) oc;
                                        if(co.getAlt()!=null)
                                            {
                                                balt=true;
                                                codi=new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt());
                                                if(!lcodi.contains(codi))
                                                    lcodi.add(codi);
                                                
                                            }else
                                            {
                                                codi=new Coordinate(co.getLongitude(), co.getLatitude());
                                                if(!lcodi.contains(codi))
                                                    lcodi.add(codi);
                                                
                                            }
                                   
                                    }
                                         lineString.setCoordinates(lcodi);
                                         p.setName(lg.getNom());
                                 //   plmk.setDescription(fig.getDescription());
                                    p.setAddress(lg.getAdresse());

                                    p.setStyleUrl(
                                            fontClick(prmtrVue,onf));
                                    
                                    }catch(Exception ex)
                                    {
                                                
                                    }
                            }else
                            {
                                
                                   ObjetKml okml= (ObjetKml) prmtrVue;
                                   if(okml.getHexapoly()!=null)
                                   {
                                       Polygon polygon = p.createAndSetPolygon();
                                Boundary outer = polygon.createAndSetOuterBoundaryIs();
                                LinearRing lr =  outer.createAndSetLinearRing();
                                try{
                                           
                                           List<Object> lo= Arrays.asList(lg.getCoords().toArray());
                                    
                                     if(lo!=null&&!lo.isEmpty())       
                                     {

                                      List<Coordinate> lcodi=new ArrayList<Coordinate>();      
                                   
                                        boolean balt=false;
                                        for(Object oc:lo)
                                        {
                                            Coordinate codi;
                                            Coord co=(Coord) oc;
                                            if(co.getAlt()!=null)
                                                {
                                                    balt=true;
                                                    codi=new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt());
                                                    if(!lcodi.contains(codi)) 
                                                        lcodi.add(codi);

                                                }else
                                                {
                                                    codi=new Coordinate(co.getLongitude(), co.getLatitude());
                                                    if(!lcodi.contains(codi))
                                                        lcodi.add(codi);

                                                }

                                        }
                                        
                                        lr.setCoordinates(lcodi);
                                       /*  Iterator iter=lco.iterator();
                                         Coord co= (Coord)iter.next();
                                         if(balt)
                                                {

                                                    lr.addToCoordinates(co.getLongitude(), co.getLatitude(), co.getAlt());
                                                }else
                                                {

                                                    lr.addToCoordinates(co.getLongitude(), co.getLatitude());
                                                }
                                                */

                                     }
                                    }catch(ClassCastException ccex)
                                    {
                                          Xcption.Xcption("GereKml trCoFg ClassCastException", ccex.getMessage());;
                                    }
                                
                                   }else if(okml.getHexacont()!=null)
                                   {
                                       LineString lineString = p.createAndSetLineString();
                               
                                         List<Coordinate> lcodi=new ArrayList<Coordinate>();      
                                    List<Object> lo= Arrays.asList(lg.getCoords().toArray());
                                    boolean balt=false;
                                    for(Object oc:lo)
                                    {
                                        Coordinate codi;
                                        Coord co=(Coord) oc;
                                        if(co.getAlt()!=null)
                                            {
                                                balt=true;
                                                codi=new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt());
                                                if(!lcodi.contains(codi))
                                                    lcodi.add(codi);
                                                
                                            }else
                                            {
                                                codi=new Coordinate(co.getLongitude(), co.getLatitude());
                                                if(!lcodi.contains(codi))
                                                    lcodi.add(codi);
                                                
                                            }
                                   
                                    }
                                         lineString.setCoordinates(lcodi);
                                   }
                                
                                

                                    p.setName(lg.getNom());
                                 //   plmk.setDescription(fig.getDescription());
                                    p.setAddress(lg.getAdresse());

                                    p.setStyleUrl(
                                            fontClick(prmtrVue,onf));
                                  
                            }
                          if(rt)
                          {
                              retour=p;
                              rt=false;
                          }   
                                
                        }else if(prmtrVue instanceof LigneKml)
                        {
                            LigneKml lgnkml=(LigneKml) prmtrVue;
                            Ligne lg = (Ligne) rs;
                            
                                LineString lineString = p.createAndSetLineString();
                            try{
                                    List<Coord> lco= lg.getCoords();
                                    List<Coordinate> lcodi=new ArrayList<Coordinate>(lco.size());
                                    Coordinate coordinate;
                                        boolean balt=false;
                                        for(Coord co:lco)
                                            if(co.getAlt()!=null)
                                                {
                                                    balt=true;
                                                 coordinate = new Coordinate(co.getLongitude(), co.getLatitude(), co.getAlt());
                                                   if(!lcodi.contains(coordinate)) 
                                                       lcodi.add(coordinate);
                                                }else
                                                {
                                                    coordinate = new Coordinate(co.getLongitude(), co.getLatitude());
                                                   if(!lcodi.contains(coordinate)) 
                                                       lcodi.add(coordinate);
                                                    
                                                }
                                   
                                   lineString.setCoordinates(lcodi);
                                   
                                         
                                         p.setName(lg.getNom());
                                 //   plmk.setDescription(fig.getDescription());
                                    p.setAddress(lg.getAdresse());
                                    if(p.getAddress().contains("ONF"))
                                        onf=9;
                                    p.setStyleUrl(
                                            fontClick(prmtrVue,onf));
                                   
                                    }catch(Exception ex)
                                    {
                                             Xcption.Xcption("TracerRSNS createTracerbySchema  prmtrVue instanceof LigneKml","");  
                                             
                                    }
                            if(rt)
                          {
                              retour=p;
                              rt=false;
                          }
                        }
                       /* if(bmeteo)
                            lFMeteo.add(p);
                        else*/
                            lFeat.add(p);
                        //random++;
                        }
                   }catch(Exception ex)
                   {
                       Xcption.Xcption("TracerRSNS createTracerbySchema for(RsnsObjet) Exception",ex.getMessage());
                   }
                  }
               
    }
   /* private List<Coord> supDoublons(List<Coord> lco)
    {  
        ArrayList<Coord> ltmp=new ArrayList();
        for(Coord co:lco)
            if(!ltmp.contains(co))
                ltmp.add(co);
        return ltmp;
    }*/
    
    
    public void formeTracer()
    {

            if(plan instanceof Placemark)
            {
                p = (Placemark) plan;
                if(p.getGeometry() instanceof Point)
                {
                    fontPoint();
                }else
                {
                    fontTracer();
                }
                
                lFeat.add(p);
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
                            if(p.getGeometry() instanceof Point)
                            {
                                
                                fontPoint();
                            }else if(p.getGeometry() instanceof LinearRing)
                            {
                                 fontPlace();
                            }else if(p.getGeometry() instanceof LineString)
                            {
                                fontTracer();
                            }
                          
                          lFeat.add(p);

                      }
                      else if(tr instanceof Polygon)
                      {
                         po = (Polygon) tr;
                         fontPlace();
                            
                      }
            
                   }
            }
           
            
                
        
    }
    public void addlFeat(Feature feat)
    {
        lFeat.add(0,feat);
    }
    public Document finish()
    {
        Document doc=new Document();
            doc.setOpen(true);
            
            if(plan!=null)
            {
                doc.setName(plan.getName());
                if(plan instanceof Tour)
                    doc.addToFeature(plan);
            }else if(!lFeat.isEmpty())
            {
                doc.setName(lFeat.get(0).getName());
                dossier.setFeature(lFeat);
                doc.addToFeature(dossier);
            }
        
        return doc;
    }
    public void cplaceMark() throws NullPointerException
    {
        Geometry geo = p.getGeometry();
        LineString ls;
        LinearRing lr;
        HashMap<Integer, Coordinate> ptElv ;
    
        if(geo instanceof LineString)
        {
            double alti=1000;
            ls= (LineString) p.getGeometry();
            List<Coordinate> coors = ls.getCoordinates();
            Iterator<Integer> iterKey = null;
            try{
                 ptElv = chchElv(coors);
             iterKey =  ptElv.keySet().iterator();
            }catch(NullPointerException npe)
            {
                System.out.println("elevation"+npe.getMessage());
            }
            
                
            double fact=0;
            int p=0;
            if(iterKey.hasNext())
            {
                 p=iterKey.next();
            }
            
            int oldPt=0;int afterPt=0;
            int pt=Math.abs(p);
             double cherchHead = 0; 
             double cherchTilt = 0;
             
            for(int i=0; i<coors.size();i++)
            {
                if((i==0)||(i==afterPt))
                {
                   oldPt=pt;
                   if(iterKey.hasNext()) {
                        p=iterKey.next();
                    }
                   afterPt=Math.abs(p);
                   
                   
                }
                else if(i==oldPt)
                {
                    if(iterKey.hasNext()) {
                        p=iterKey.next();
                    }
                    
                }
                else if((oldPt<i)&&(i<afterPt))
                        {
                            oldPt=pt;
                            if(iterKey.hasNext()) {
                                p=iterKey.next();
                            }
                            afterPt=Math.abs(p);
                        }
                Coordinate co=(Coordinate)coors.get(i);
                 
            }
        }
        else if(geo instanceof LinearRing)
        {
            lr= (LinearRing) p.getGeometry();
            List<Coordinate> lco = lr.getCoordinates();
            List<Coordinate> lvue = new ArrayList<Coordinate>();
             
            Coordinate co=new Coordinate(0.00, 0.00, 0.00);
            
            
                
            int cpte;    
            for(int i=0; i<4; i++)
            {
                cpte=(int) ((lco.size()/4)*i);
                co.setLongitude(lco.get(cpte).getLongitude());
                co.setAltitude(lco.get(cpte).getAltitude());
                co.setLatitude(lco.get(cpte).getLatitude());
                lvue.add(co);
                                
            }
            
           
        }
            
        else
            System.out.println("no found this \n"+geo.toString());
        
    }            
    public HashMap<Integer, Coordinate> chchElv(List<Coordinate> coors)
    {
		boolean cste=true;
        double oldPente=0;double pente;
        HashMap<Integer, Coordinate> mapPente = new HashMap<Integer, Coordinate>();
        try{
		//pour tout point elevation
            for(int cpte=0; cpte<coors.size()-1; cpte++)
            {
                pente=coors.get(cpte+1).getAltitude()-coors.get(cpte).getAltitude();
                
                
                    if((pente*oldPente)>0)
                    {
			cste=true;
                        
                    }
                    else if((pente*oldPente)<0)
                    {
			cste=false;
                        mapPente.put(-cpte, coors.get(cpte));
                    } 
                    else if((pente*oldPente)==0)
                    {
                        mapPente.put(cpte, coors.get(cpte));
                    } 
                
                   
                oldPente=pente;
            }
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        
        
        return mapPente;
        
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

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public float getTaille() {
        return taille;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public Placemark getRetour() {
        return retour;
    }

    public void setRetour(Placemark retour) {
        this.retour = retour;
    }

    public boolean isBclim() {
        return bclim;
    }

    public void setBclim(boolean bclim) {
        this.bclim = bclim;
    }

    private void fontTracer() 
    {
        if(couleur!=null)
        {
            if(couleur.isEmpty())
            {
                couleur="FFFFFFFF";
            }
        }
        //Placemark p=(Placemark) p;
        Style style = p.createAndAddStyle();
        LineStyle lineStyle = style.createAndSetLineStyle();
        lineStyle.setColor(couleur);
        lineStyle.setWidth(taille);
        
    }
    public Placemark setFormClim(Placemark plc, YahooRep meteo)
    {
        plc.createAndSetPoint();
        //Placemark p=(Placemark) p;
        /* Style style = feat.createAndAddStyle();
               style.setId("stid_"+meteo.getJour());
                        IconStyle iconestyle = style.createAndSetIconStyle();
                        iconestyle.setScale(1.3);
                        Icon icon = iconestyle.createAndSetIcon();
                            icon.setHref(meteo.getUrl());
                            icon.setRefreshInterval(0.4d);
                            icon.setViewRefreshTime(0.2d);
                            icon.setViewBoundScale(1.0d);
        StyleMap styleMap = feat.createAndAddStyleMap();
                 styleMap.setId("stmpid"+meteo.getJour());
                 Pair pair = styleMap.createAndAddPair();
                            pair.setKey(StyleState.NORMAL);
                            pair.setStyleUrl("#"+style.getId());
                          
                    */     
      
        return plc;
    }
    public void fontPoint() 
    {
        
      List<String> lns = new ArrayList<String>();
        //Placemark p=(Placemark) p;
         Style style = p.createAndAddStyle();
                        
                        IconStyle iconestyle = style.createAndSetIconStyle();
                        iconestyle.setScale(1.3);
                        Icon icon = iconestyle.createAndSetIcon();
                        
     if(bclim) 
     { 
         p=(Placemark) plan;
         if(p.getName()==null)
             p.setName(" ");
         if(p.getDescription()==null)
             p.setDescription(" ");
         p.addToFeatureSimpleExtension(style);
         if(p.getDescription()!=null)
          {
             for(Iterator<String> iter=smIclim.keySet().iterator();
                         iter.hasNext();)
                   {
                       String next = iter.next();
            
                
                    if(p.getDescription().toLowerCase().contains(
                                               next
                                               )||
                            p.getName().toLowerCase().contains(next))
                    {
                      if(p.getStyleUrl()==null)  
                      {
                          p.withStyleUrl(smIclim.get(next));
                          style.setTargetId(p.getStyleUrl());
                      }
                        
        
                                    lns.add(smIclim.get(next));

                        
                        
                    }
                    
                }
             //suppr text de la vue
                    p.setDescription(null);
        }
     }
     else
     {
         String testic=null;
             if(p.getDescription()!=null)
                 testic=p.getDescription();
             else if(p.getAddress()!=null)
                 testic=p.getAddress();
             
        for(String next:smicone.keySet())
        {
             
            if(testic!=null)
                 {
                
                    if(testic.toLowerCase().contains(
                                               next
                                               )||
                            p.getName().toLowerCase().contains(next))
                    {
                      if(p.getStyleUrl()==null)  
                      {
                          p.withStyleUrl(smicone.get(next));
                          style.setTargetId(p.getStyleUrl());
                      }
                        
        
                                    lns.add(smicone.get(next));

                        
                        
                    }
                    //suppr text de la vue
                    p.setDescription(null);
                }
        }
        
    }
        if(!lns.isEmpty())
                        {
                                icon.setHref(lns.get(0));
                                
                                
           
                        }
        
        
        
        
    }
    public Feature setformePoint(Feature feat) 
    {
        List<String> lns = new ArrayList<String>();
        //Placemark p=(Placemark) p;
         Style style = feat.createAndAddStyle();
                        
                        IconStyle iconestyle = style.createAndSetIconStyle();
                        iconestyle.setScale(0.9);
                        Icon icon = iconestyle.createAndSetIcon();
                        
        for(Iterator<String> iter=smicone.keySet().iterator(); iter.hasNext();)
        {
            String next = iter.next();
            if(feat.getDescription()!=null)
                 {
                
                    if(feat.getDescription().toLowerCase().contains(
                                               next
                                               )||
                            feat.getName().toLowerCase().contains(next))
                    {
                        
                        feat.withStyleUrl(smicone.get(next));
                        
        
                                    lns.add(smicone.get(next));

                        
                        
                    }
                }
        }
        if(!lns.isEmpty())
                        {
                                icon.setHref(lns.get(0));
                                style.setTargetId(feat.getStyleUrl());
                                
           
                        }
        
        
        
        return feat;
    }

    private String fontClick(Ref ref,int onf)
    {
        
        Style style=null;
       StyleMap styleMap=new StyleMap();
       
       if(p.getName()!=null)
            styleMap.setId("style"+p.getName());
       else
           styleMap.setId("style"+p.getDescription());
       
            if(ref instanceof ObjetKml)
                        {
                           styleMap= p.createAndAddStyleMap();
                            ObjetKml obkml=(ObjetKml) ref;
                           if(obkml.getHexapoly()!=null)
                               { 
                               String hexapoly = obkml.getHexapoly();
                              Pair pair = styleMap.createAndAddPair();
                                        pair.setKey(StyleState.NORMAL);
                                         style = pair.createAndSetStyle();
                                            
                                            PolyStyle polyStyle = style.createAndSetPolyStyle();
                                            if(!hexapoly.equals(""))
                                            {
                                                if(onf!=0)
                                                    hexapoly=srdmCouleur.get(onf);
                                                else if(hexapoly.equals("hzd"))
                                                    hexapoly=srdmCouleur.get(6);
                                                polyStyle.setColor(hexapoly);
                                                polyStyle.setFill(true);
                                                
                                                
                                            }else
                                                polyStyle.setFill(false);
                                        
                               styleMap.addToPair(pair);
                             
                                pair = styleMap.createAndAddPair();
                                        pair.setKey(StyleState.HIGHLIGHT);
                                         style = pair.createAndSetStyle();
                                            
                                            polyStyle = style.createAndSetPolyStyle();
                                            
                                            if(obkml.getHglhexacont()!=null)
                                            {
                                                if(onf!=0)
                                                    hexapoly=srdmCouleur.get(onf);
                                                else if(hexapoly.equals("hzd"))
                                                    hexapoly=srdmCouleur.get(6);
                                                polyStyle.setColor(obkml.getHglhexacont());
                                              
                                                polyStyle.setOutline(true);
                                            }else
                                            {
                                                polyStyle.setOutline(false);
                                            }
                                           
                                     //styleMap.addToPair(pair);
                               }else if(obkml.getHexacont()!=null)
                                {
                               String hexacont = obkml.getHexacont();

                                    Pair pair = styleMap.createAndAddPair();
                                        pair.setKey(StyleState.NORMAL);
                                         style = pair.createAndSetStyle();
                                            
                                            PolyStyle polyStyle = style.createAndSetPolyStyle();
                                            if(!hexacont.equals(""))
                                            {
                                                if(onf!=0)
                                                    hexacont=srdmCouleur.get(onf);
                                                else if(hexacont.equals("hzd"))
                                                    hexacont=srdmCouleur.get(6);
                                                polyStyle.setColor(hexacont);
                                                polyStyle.setOutline(true);
                                                
                                                
                                            }else
                                                polyStyle.setOutline(false);
                                        
                               styleMap.addToPair(pair);
                             
                                pair = styleMap.createAndAddPair();
                                        pair.setKey(StyleState.HIGHLIGHT);
                                         style = pair.createAndSetStyle();
                                            
                                            polyStyle = style.createAndSetPolyStyle();
                                            
                                            if(obkml.getHglhexacont()!=null)
                                            {
                                                hexacont=obkml.getHglhexacont();
                                                if(onf!=0)
                                                    hexacont=srdmCouleur.get(onf);
                                                else if(hexacont.equals("hzd"))
                                                    hexacont=srdmCouleur.get(6);
                                                polyStyle.setColor(hexacont);
                                              
                                                polyStyle.setOutline(true);
                                            }else
                                            {
                                                polyStyle.setOutline(false);
                                            }
                                     

                                }
                                                
                        }else if(ref instanceof LigneKml)
                        {
                          
                            LigneKml lkml=(LigneKml) ref;
                                      
                            style = p.createAndAddStyle();
                                            LineStyle lineStyle = style.createAndSetLineStyle();
                                        
                                       String hexacont = lkml.getHexacont();
                                            if(onf!=0)
                                                    hexacont=srdmCouleur.get(onf);
                                                else if(hexacont.equals("hzd"))
                                                    hexacont=srdmCouleur.get(6);
                                            lineStyle.setColor(hexacont);
                                            lineStyle.setWidth(lkml.getWidth());
                           style.setId(styleMap.getId());

                        }
                        else if(ref instanceof IconKml)
                        {
            
                            boolean wthic=true;
                            IconKml ik=(IconKml) ref;
                                style = p.createAndAddStyle();
            String[] split = p.getAddress().split(Variable.getLieu());
                            LabelStyle labelStyle = style.createAndSetLabelStyle();
                            String color = Variable.getRdmCouleur().get(0);
                                double width = ik.getWidth();
                            if(split[0].contains(Variable.getPrmtr().get(6)))
                            {
                                
                                String specf=split[0].split(" ")[1];
                                
                                if(specf.contains(Variable.getLtitre().get(4).toLowerCase()))
                                {//ville
                                    wthic=false;
                                    width=2*width;
                                    color = Variable.getRdmCouleur().get(14);
                                }else if(specf.contains(Variable.getLinf()[3]))
                                {//quartier
                                    wthic=false;
                                    color = Variable.getRdmCouleur().get(13);
                                }
                                else if(specf.contains(Variable.getLinf()[0]))
                                {//eau
                                    
                                    color = Variable.getRdmCouleur().get(11);
                                }
                                else if(specf.contains(Variable.getLinf()[3]))
                                {//foret
                                    width=1.5*width;
                                    color = Variable.getRdmCouleur().get(12);
                                }
                                
                                    
                                
                            }else if(split[0].contains(Variable.getPrmtr().get(4)))
                            {
                                color = Variable.getRdmCouleur().get(17);
                               
                                String specf=split[0].split(" ")[1];
                                specf=specf.replace(" ", "");
                                if(specf.contains(Variable.getLinf()[2]))
                                {//mont
                                    
                                    width=0.9*width;
                                    color = Variable.getRdmCouleur().get(16);
                                }else if(specf.contains(Variable.getLinf()[3]))
                                {//quartier
                                    wthic=false;
                                    color = Variable.getRdmCouleur().get(13);
                                }
                                else if(specf.contains(Variable.getLinf()[0]))
                                {//eau
                                    
                                    color = Variable.getRdmCouleur().get(11);
                                }
                                else if(specf.contains(Variable.getLinf()[1]))
                                {//foret
                                    width=1.5*width;
                                    color = Variable.getRdmCouleur().get(12);
                                }
                                
                                    
                            }else if(split[0].contains(Variable.getPrmtr().get(5)))
                            {
                                color = Variable.getRdmCouleur().get(11);
                                String specf=split[0].split(":")[1];
                                specf=specf.replace(" ", "");
                                if(specf.contains("_"))
                                  {//+=2 icoes
                                     width=2*width;       
                                  }
                                int clr=12;
                                for(Classmt clmst:Variable.categActivite())
                                {
                                    if(specf.contains(clmst.getRef()))
                                    {
                                        color = Variable.getRdmCouleur().get(clr);
                                        width=1.5*width;
                                    }
                                    else if(clmst.getList().contains(specf))
                                        color = Variable.getRdmCouleur().get(clr+4);
                                    clr++;
                                    
                                }
                                
                            }else if(split[0].contains(Variable.getZact()))
                            {
                                String specf=split[0].split(":")[1];
                                if(specf.contains("_"))
                                  {//+=2 icoes
                                            
                                  }
                                for(String str:Variable.getLzact())
                                {
                                    if(specf.contains(str))
                                    {
                                        color = Variable.getRdmCouleur().get(10);
                                        
                                    }
                                   
                                }
                            }else if(Variable.categSentier().contains(split[0]
                                    .replace(" ", "")))
                            {
                                
                                String specf=split[0].split("_")[1];
                                if(!specf.contains(","))
                                  {//avec icones
                                      wthic=false;      
                                  }
                                int clr=16;
                                for(String str:Variable.categSentier())
                                {
                                    if(split[0].contains(str))
                                    {
                                        color = Variable.getRdmCouleur().get(clr);
                                        
                                    }
                                    clr++;
                                   
                                }
                            }
                            
                            labelStyle.setScale(width);
                                    labelStyle.setColor(color);
                            
                            IconStyle iconStyle = style.createAndSetIconStyle();        
                            if(wthic)
                            {
                               
                                   iconStyle.setScale(ik.getWidth());
                                   Icon icon = iconStyle.createAndSetIcon();
                                            icon.setHref(ik.getImgref()); 
                            }else
                            {
                                iconStyle.setScale(0);
                            }
                                
                                            
                                                         
                            if(p.getDescription()!=null)
                            {
                                BalloonStyle balloonStyle = style.createAndSetBalloonStyle();
                                    balloonStyle.addToBalloonStyleSimpleExtension("$[description]");
                                    
                            }
                                                             
                           style.setId(styleMap.getId());
                        }
            
              
       
       
        return styleMap.getId();
         
    }
    /* private String cherchIcon(Placemark plcmk)
    {
        String hrfic="";
        Set<String> skic = smicone.keySet();
      
                for(String kic:skic)
                    {
                        if(plcmk.getAddress().contains(kic))
                            hrfic= smicone.get(kic);
                    }
       return hrfic;
    }*/
    private void fontPlace() 
    {
        if(couleur.isEmpty())
        {
            couleur="FF00CCFF";
        }
        //Placemark p=(Placemark) planf;
        Style style = p.createAndAddStyle();
        PolyStyle polyStyle = style.createAndSetPolyStyle();
        polyStyle.setColor(couleur);
        
        polyStyle.setOutline(Boolean.TRUE);
        polyStyle.setFill(false);
    }

    public List<Long> getLid() {
        return lid;
    }

    public void setLid(List<Long> lid) {
        this.lid = lid;
    }

    public List<Feature> getlFeat() {
        return lFeat;
    }

    public void setlFeat(List<Feature> lFeat) {
        this.lFeat = lFeat;
    }
    
    public void setSchema(PrmtrSchm schema) {
        this.schema = schema;
    }

   /* public List<Feature> getlFMeteo() {
        return lFMeteo;
    }*/

   
}

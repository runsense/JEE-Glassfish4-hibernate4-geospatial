/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package run.ejb.entite.util.kml;

import com.google.common.collect.Lists;
import de.micromata.opengis.kml.v_2_2_0.*;
import de.micromata.opengis.kml.v_2_2_0.gx.FlyTo;
import de.micromata.opengis.kml.v_2_2_0.gx.FlyToMode;
import de.micromata.opengis.kml.v_2_2_0.gx.Playlist;
import de.micromata.opengis.kml.v_2_2_0.gx.SoundCue;
import de.micromata.opengis.kml.v_2_2_0.gx.TourPrimitive;
import de.micromata.opengis.kml.v_2_2_0.gx.Wait;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import run.ejb.base.Variable;
import run.ejb.ejbkml.GereKml;
import run.ejb.entite.geo.Coord;
import run.ejb.entite.geo.Ligne;
import run.ejb.entite.geo.Prmtr;
import run.ejb.entite.geo.Pt;
import run.ejb.entite.geo.Tour;
import run.ejb.entite.geo.interf.RsEntite;
import run.ejb.util.ex.Xcption;
import run.ejb.util.rsentite.schema.PrmtrSchm;

/**
 *
 * @author Administrateur
 */
public class TourRsns 
{
    private double oldHead;
    private  GereKml gKml;
    private PrmtrSchm schema;
    private String lieu="île de la Reunion";
    private Kml marshKml;
    private Feature feat ;
    
    private ArrayList<Double> lHead;

    private boolean biti;
    private Tour rTour;

    private ArrayList<run.ejb.entite.geo.Tour> lbddtour;
    private String[] latlngalt;
  
    private Ligne ligne;
    
    private  final List<String> lvprmtr ;
 
    public TourRsns() 
    {
        lvprmtr = Variable.getPrmtr();
        marshKml=new Kml();
        gKml=new GereKml();
    }

    @Override
    protected void finalize() throws Throwable {
        gKml.fEm();
        gKml=null;
    }

    public TourRsns(Kml marshKml, Feature feat, GereKml gKml) 
    {
        lvprmtr = Variable.getPrmtr();
        this.marshKml = marshKml;
        this.feat=feat;
        this.gKml=gKml;
    }
  
    
    public void addTour(Tour mTour)
    {
       /* ChargeKml chgKml=new ChargeKml(gKml);
            chgKml.addTour(mTour);*/
          
    }
    private String cAnim(Tour play)
    {
        
        String anmKml="<gx:AnimatedUpdate>" +
"          <gx:duration>6.5</gx:duration>" +
"          <Update>" +
"            <targetHref></targetHref>" +
"            <Change>" +
"                <LineStyle targetId=\""+play.getNmTour().replace("tour :", "style")+"\">" +
"                <color>5014B4FF</color>"+
"                  <width>3.6</width>" +
"                </LineStyle>" +
"            </Change>" +
"          </Update>" +
"        </gx:AnimatedUpdate>";
        
        return anmKml;
    }
    public String fdonneTour(Tour tour )
    {
        de.micromata.opengis.kml.v_2_2_0.gx.Tour kmltour = marshKml.createAndSetTour();
        Playlist playlist = kmltour.createAndSetPlaylist();
        ctourbybdd(tour, playlist);
        kmltour.setId(tour.getNom());
            kmltour.setName(
                    tour.getNom());
        kmltour.setPlaylist(playlist);
        
        marshKml.setFeature(kmltour);
                        StringWriter stringWriter = new StringWriter();
                            marshKml.marshal(stringWriter);
        String cAnim = cAnim(tour);
        String rtr=stringWriter.getBuffer().substring(0);
        /*String[] split = rtr.split("<gx:Tour");
        cAnim=split[0]+cAnim+"<gx:Tour"+split[1];*/
        return rtr;
    }
    private void donneTour(Object kml, Playlist playlist)
    {
        //GereKml gKml=new GereKml();
        Folder d;
        Placemark p;
        LineString ls;
        try{

            
            if(kml instanceof Placemark)
            {
                p = (Placemark) kml;
               
                ArrayList rchBdd = gKml.rchBdd("tour :"+p.getAddress()+" "+p.getName(), "tour", "fix", "nmTour");
                            if(rchBdd.isEmpty())
                            {
                                //ChargeKml chg=new ChargeKml(gKml);
                                
                                Tour ctour = cplaceMark(p,playlist);
                                //chg.addTour(ctour);
                                
                                lbddtour.add(ctour);
                            }
                            else
                            {
                                Tour tour=(Tour) rchBdd.get(0);
                                lbddtour.add(tour);
                                ctourbybdd(tour, playlist);
                            }
            }
            else if(kml instanceof Folder)
            {
                d=  (Folder) kml;
                
                List<Feature> lPl = d.getFeature();
                                      
                for(Object tr : lPl)
                   {
                       try{
                      if(tr instanceof Placemark)
                      {
                          p = (Placemark) tr;
                          ArrayList rchBdd = gKml.rchBdd("tour :"+p.getAddress()+" "+p.getName(), "tour", "fix", "nmTour");
                            if(rchBdd.isEmpty())
                                {
                                //ChargeKml chg=new ChargeKml(gKml);
                                
                                Tour ctour = cplaceMark(p,playlist);
                                lbddtour.add(ctour);
                                //chg.addTour(ctour);
                                
                                
                            }
                            else
                            {
                                Tour tour=(Tour) rchBdd.get(0);
                                lbddtour.add(tour);
                                ctourbybdd(tour, playlist);
                            }
                      }
                      else if(tr instanceof Polygon)
                      {
                         p = (Placemark) tr;
        
                            lbddtour.add(
                                    cplaceMark(p,playlist));
                      }else if(tr instanceof Folder)
                      {
                         d = (Folder) tr;
        
                            donneTour(d , playlist);
                      }
                      else
                      {
                          System.out.println("**************exception cast tour*********for******"+feat.toString()+"***************************");
                      }
                       }catch(Exception ex)
                       {
                           
                            Xcption.Xcption("formeTour Exception "+tr,ex.getMessage());
                       }
                   }
            }
            
            else 
                System.out.println("**************exception cast tour***************"+feat.toString()+"***************************");
        }catch(ClassCastException ex)
        {
            Xcption.Xcption("formeTour ClassCastException",ex.getMessage());
        }
       
    }
    public de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour(List<Tour> ltour)
    {
        lbddtour=(ArrayList<Tour>) ltour;
        String httpsound="";
        de.micromata.opengis.kml.v_2_2_0.gx.Tour tour = marshKml.createAndSetTour();
        Playlist playlist = tour.createAndSetPlaylist();
       
            
        if(httpsound!="")
        {
            SoundCue soundCue = playlist.createAndAddSoundCue();
            soundCue.setHref(httpsound);
        }
        for(Tour tr:ltour)
        {
            ctourbybdd(tr, playlist);
        }
        tour.setName(
                    lbddtour.get(0).getNom());
        tour.setPlaylist(playlist);  
        
        return tour;
    }
    public Document addFeat(List<Feature> lft)
    {
        Document createAndSetDocument = marshKml.createAndSetDocument();
        for(Feature f:lft)
            createAndSetDocument.addToFeature(f);
       
        return createAndSetDocument;
    }
    public de.micromata.opengis.kml.v_2_2_0.gx.Tour formeTour() throws NullPointerException
    {
        
        String httpsound="";
        
        if(lbddtour==null)
            lbddtour=new ArrayList<Tour>();
        //Document document = marshKml.createAndSetDocument();
        de.micromata.opengis.kml.v_2_2_0.gx.Tour tour = marshKml.createAndSetTour();
        Playlist playlist = tour.createAndSetPlaylist();
        
        if(httpsound!="")
        {
            SoundCue soundCue = playlist.createAndAddSoundCue();
            soundCue.setHref(httpsound);
        }
        donneTour(feat, playlist);
            tour.setId(lbddtour.get(0).getNom());
            tour.setName(
                    lbddtour.get(0).getNom());
        tour.setPlaylist(playlist);  
        
        
        return tour;
    }
    public HashMap<Integer, Double> cherchVue(HashMap<Integer, Coordinate> ptElv, ArrayList<Double> lHead)
    {
        boolean cfm=false;boolean op=false;
        int cpte=0;
        
        HashMap<Integer, Double> lVue=new HashMap<Integer, Double>();
        
        
        if(ptElv.isEmpty())
        {
            for(int i=0; i<=lHead.size(); i++)
            {
                Double cherchHead = lHead.get(i);
                 lVue.put(i, cherchHead);
                 
            }
        }
         
        else
        {
        
        try{
        for(int i=0; i<lHead.size(); i++)
        {
            Double cherchHead = lHead.get(i);
            Double nwsHead = null;
            if((i+1)<lHead.size())
            {
                nwsHead = lHead.get(i+1);
            }
            
             if((nwsHead>0&&cherchHead<0)||(nwsHead<0&&cherchHead>0))
                        {
                            if((Math.abs(nwsHead)>90&&Math.abs(cherchHead)>90)||(Math.abs(nwsHead)<90&&Math.abs(cherchHead)<90))
                            {
                               cfm=true;
                            }
                            else//opposé
                            {
                                cfm=true;
                            }
                            
                        }
                        else
                        {//oldHead et cherchhead ont le meme signe
                            if((Math.abs(nwsHead)>90&&Math.abs(cherchHead)>90)||(Math.abs(nwsHead)<90&&Math.abs(cherchHead)<90))
                            {
                                //meme direction
                            }
                            else//opposé
                            {
                                
                            }
                        }
            if((cfm)||ptElv.containsKey(cpte)||ptElv.containsKey(-cpte))
            {
               
                   lVue.put(cpte, cherchHead);
               
                
                 
            }
            
            
            
                    
                    cpte++;
        }
        }catch(Exception ex)
        {
            Xcption.Xcption("TourRsns cherchVue Exception",ex.getMessage());
        }
        }
        return lVue;
    }
    public String ctourbyRs(RsEntite rs) throws NullPointerException
    {
        String rtr=null;
        
        de.micromata.opengis.kml.v_2_2_0.gx.Tour kmltour = marshKml.createAndSetTour();
        Playlist playlist = kmltour.createAndSetPlaylist();
         List<TourPrimitive> tourPrimitive = playlist.getTourPrimitive();
         double tilt = 1-85;
         
         if(rs instanceof Ligne)
         {
             Ligne lgn=null;
             lgn=(Ligne) rs;
             double dist=0d;
             
            List<Coord> newAL = Lists.newArrayList(lgn.getCoords());
            int size = newAL.size();
            Coord prem = newAL.get(0);
            Coord mil = newAL.get(Math.toIntExact(size/2));
            Coord dern = newAL.get(size-1);
         
         double dblng=(prem.getLongitude()+dern.getLongitude())/2;
         double dblat=(prem.getLatitude()+dern.getLatitude())/2;
            Coordinate first=new Coordinate(prem.getLongitude(), prem.getLatitude());
            Coordinate milc = new Coordinate(mil.getLongitude(), mil.getLatitude());
            Coordinate middle=new Coordinate(dblng, dblat);
            Coordinate last=new Coordinate(dern.getLongitude(), dern.getLatitude());
            List<Double> lpente = chchElv(Lists.newArrayList(first,milc,middle,last));
            dist=calculDistance(prem.getLatitude(), dern.getLatitude(), prem.getLongitude(), dern.getLongitude(), 0d, 0d);
          
            
            Prmtr prmtr=new Prmtr();
            FlyTo flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(3);prmtr.setDura(3d);    
                        
             prmtr = cPos(flyTo, middle, lHead.get(0), dist*4.5,prmtr,tilt);
                     
                     
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                        if(lpente.get(0)!=0)
                            tilt=1-lpente.get(0)/dist;
                       prmtr= cPos(flyTo, first, lHead.get(0), dist*1.5,prmtr,tilt);
                   
                     
                     Wait wait = playlist.createAndAddWait();
                          wait.setDuration(2);
                          
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(4);prmtr.setDura(4d);
                        if(lpente.get(1)!=0)
                            tilt=1-lpente.get(1)/dist;
            prmtr= cPos(flyTo, middle, lHead.get(1), dist,prmtr, tilt);
                   
              
             prmtr=new Prmtr();      
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                        if(lpente.get(2)!=0)
                            tilt=1-lpente.get(2)/dist;
                        prmtr=cPos(flyTo, last, lHead.get(2), dist*1.5, prmtr, tilt);
                 
        
                     wait = playlist.createAndAddWait();
                          wait.setDuration(1);
                          
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                        tilt = 1-85;
                       prmtr= cPos(flyTo, last, -lHead.get(2), dist*2.5,prmtr,tilt);
                      
       
                StringWriter stringWriter = new StringWriter();
                                    marshKml.marshal(stringWriter);
                                    
             rtr = stringWriter.getBuffer().substring(0);
         }
         else if(rs instanceof Pt)
         {
             Pt pt=(Pt) rs;
             double dist=0d;
         
         Coordinate co=new Coordinate(pt.getLongitude(), pt.getLatitude(), 500);
            FlyTo flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);
                        flyTo.setDuration(5);
                        
                        cPos(flyTo, co, 180- (90), 18000,new Prmtr(),0.75);
            
                  flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);
                        flyTo.setDuration(2); 
                        cPos(flyTo, co, 180, 18000,new Prmtr(),0.75); 
                  flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);
                        flyTo.setDuration(5); 
                        cPos(flyTo, co,0, 18000,new Prmtr(), 0.75);
                  flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);
                        flyTo.setDuration(5); 
                        cPos(flyTo, co,270, 18000,new Prmtr(), 0.75);     
       
                StringWriter stringWriter = new StringWriter();
                                    marshKml.marshal(stringWriter);
                                    
             rtr = stringWriter.getBuffer().substring(0);
         }
        /*AnimatedUpdate animation=new AnimatedUpdate();
            animation.createAndSetUpdate(null, coTr);*/
         
                            
        return rtr;
    }
    public void ctourbybdd(Tour trbdd, Playlist playlist) throws NullPointerException
    {
    
         List<TourPrimitive> tourPrimitive = playlist.getTourPrimitive();
         
        /*AnimatedUpdate animation=new AnimatedUpdate();
            animation.createAndSetUpdate(null, coTr);*/
        for(Prmtr prmtr:trbdd.getParametreTour())
        {
            Coordinate co=new Coordinate(prmtr.getLongitude(), prmtr.getLatitude());
                
            FlyToMode mode=null;
            if(prmtr.getAltMode()=="smooth")
             mode=FlyToMode.SMOOTH;
            else
               mode=FlyToMode.BOUNCE; 
            FlyTo flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(mode);
                        flyTo.setDuration(prmtr.getDura()); 
                        cPos(flyTo, co, prmtr.getHead(), prmtr.getRange(),prmtr, 1);
        };
    }
   
    public run.ejb.entite.geo.Tour cplaceMark(final Placemark pl, Playlist playlist) throws NullPointerException
    {
        
        run.ejb.entite.geo.Tour tour=new Tour();
        
        latlngalt=new String[3];
        
       
        
        Geometry geo = new LineString();
        if(pl.getGeometry() instanceof MultiGeometry)
        {
            MultiGeometry mlGeometry=(MultiGeometry) pl.getGeometry();
            if(mlGeometry.getGeometry().size()>1)
            {
                System.err.print("*************TourRsns mlGeometry.getGeometry() trop********");
                for(Geometry trop:mlGeometry.getGeometry())
                    System.out.println(trop);
            }
                
            geo=mlGeometry.getGeometry().get(0);
                
        }
        else if( pl.getGeometry() instanceof Geometry)
             geo =pl.getGeometry();
        
        LineString ls;
        LinearRing lr;
        HashMap<Integer, Coordinate> hmptElv = null;
        HashMap<Integer, Double> cherchVue=null ;
    
        tour=new Tour();
            
            tour.setNmTour("tour :"+pl.getAddress()+" "+pl.getName());
            tour.setParametreTour(new ArrayList<Prmtr>());
         
         List<Prmtr> lPrmtr=new ArrayList();
         try{
      if(pl.getName()!=null)
      {
         if(pl.getName().contains("tour"))
         {
            
             
             if(geo instanceof LineString)             
             {
                 ls= (LineString) geo;
            ArrayList<Coordinate> coors = (ArrayList<Coordinate>) ls.getCoordinates();
             List<Double> lpente = chchElv(coors);
             
             Coordinate codepr=new Coordinate(55.546906, -21.13748);
               Prmtr prmtr=new Prmtr();
            FlyTo flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(0.5d);prmtr.setDura(0.5d);      
             prmtr = cPos(flyTo, codepr, 0, 150000,prmtr, 1);
                     lPrmtr.add(
                             prmtr
                             ); 
               Wait wait = playlist.createAndAddWait();
                          wait.setDuration(3);      
                     
               prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(1.5d);prmtr.setDura(1.5d);      
             prmtr = cPos(flyTo, coors.get(0), 0, 8000,prmtr, 1);
                     lPrmtr.add(
                             prmtr
                             ); 
                     
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(1.5);prmtr.setDura(1.5d);
                       prmtr= cPos(flyTo, coors.get(0), 180, 3000,prmtr, 0.2);
                   lPrmtr.add(
                           prmtr
                           );
                        
             for(int i=0;i<lHead.size()-2;i++ )
             {
                 
                 Coordinate c =coors.get(i);
                 prmtr=new Prmtr();
                  double calculDistance = calculDistance(c.getLatitude(),coors.get(i+1).getLatitude() , c.getLongitude(), coors.get(i+1).getLongitude(), c.getAltitude(), coors.get(i+1).getAltitude());
                  flyTo = playlist.createAndAddFlyTo();
                if(calculDistance!=0) 
                {
                  if( calculDistance<=1000)
                  {
                      flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                      flyTo.setDuration(0.8d);prmtr.setDura(0.8d);
                       prmtr= cPos(flyTo, c, lHead.get(i), 6*calculDistance,prmtr, 0.7d);
                  }else
                  {
                      flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                      flyTo.setDuration(0.8d*2);prmtr.setDura(0.8d*2);
                       prmtr= cPos(flyTo, c, lHead.get(i), 3*calculDistance,prmtr, 0.7d);
                  }
             
                        
                   lPrmtr.add(
                           prmtr
                           );
                }  
             }
              prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                       prmtr= cPos(flyTo, coors.get(coors.size()-1), -lHead.get(lHead.size()-1), 15000,prmtr, 0.6d);
                   lPrmtr.add(     
                           prmtr
                           );
             }else if(geo instanceof Point)
             {
                Point point= (Point) geo;
                    ArrayList<Coordinate> coors = (ArrayList<Coordinate>) point.getCoordinates();
                     

                     Coordinate codepr=coors.get(0);
                       Prmtr prmtr=new Prmtr();
                    FlyTo flyTo = playlist.createAndAddFlyTo();
                                flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                                flyTo.setDuration(0.5d);prmtr.setDura(0.5d);      
                     prmtr = cPos(flyTo, codepr, 0, 150000,prmtr, 1);
                             lPrmtr.add(
                                     prmtr
                                     ); 
                       Wait wait = playlist.createAndAddWait();
                                  wait.setDuration(3);      

                       prmtr=new Prmtr();
                     flyTo = playlist.createAndAddFlyTo();
                                flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                                flyTo.setDuration(1.5d);prmtr.setDura(1.5d);      
                     prmtr = cPos(flyTo, codepr, 0, 8000,prmtr, 1);
                             lPrmtr.add(
                                     prmtr
                                     ); 
                             wait.setDuration(1); 
                     prmtr=new Prmtr();
                     flyTo = playlist.createAndAddFlyTo();
                                flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                                flyTo.setDuration(1);prmtr.setDura(1.5d);
                               prmtr= cPos(flyTo, codepr, 180, 3000,prmtr, 0.2);
                           lPrmtr.add(
                                   prmtr
                                   );
                           
                     prmtr=new Prmtr();
                     flyTo = playlist.createAndAddFlyTo();
                                flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                                flyTo.setDuration(2);prmtr.setDura(1.5d);
                                codepr.setLatitude(30d);
                               prmtr= cPos(flyTo, codepr, 180, 30,prmtr, 0.2);
                           lPrmtr.add(
                                   prmtr
                                   );      
             }
         }else if((pl.getAddress().contains(Variable.getPrmtr().get(7))||
                 (pl.getAddress().contains(Variable.getPrmtr().get(8))))&&
                 geo instanceof LineString)
         {
             Prmtr prmtr=new Prmtr();
             
                        ls=(LineString) geo;
                       ArrayList<Coordinate> coors = (ArrayList<Coordinate>) ls.getCoordinates();
                        List<Double> lpente = chchElv(coors);
                            

                           List<Coordinate> lco = ls.getCoordinates();
                           List<Coordinate> lvue = new ArrayList<Coordinate>();

                           Coordinate co=new Coordinate((lco.get(0).getLongitude()+lco.get(lco.size()/2).getLongitude())/2, (lco.get(0).getLatitude()+lco.get(lco.size()/2).getLatitude())/2, 0.00);           
                           double range=200000;
                                FlyTo flyTo = playlist.createAndAddFlyTo();
                               flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                               flyTo.setDuration(2);prmtr.setDura(2d);

                                   
                                   lvue.add(co);
                                   prmtr=cPos(flyTo, co, lHead.get(0), range,prmtr, 1);
                               boolean add = lPrmtr.add(
                                                     prmtr
                                                     );

                               Wait createAndAddWait = playlist.createAndAddWait();
                                   createAndAddWait.setDuration(3);

                           int cpte;    
                           for(int i=3; i>=1; i--)
                           {

                               
                                flyTo = playlist.createAndAddFlyTo();
                               flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                               flyTo.setDuration(4);prmtr.setDura(4d);

                                   cpte=(int) ((lco.size()/4)*i);
                                   Coordinate rechNexCoord = lco.get(((lco.size()/4)*(i-1)));
                                       range = calculDistance(lco.get(cpte).getLatitude(), rechNexCoord.getLatitude(),
                                                               lco.get(cpte).getLongitude(), rechNexCoord.getLongitude(),
                                                               lco.get(cpte).getAltitude(), rechNexCoord.getAltitude())
                                               *3;
                                   co.setLongitude(lco.get(cpte).getLongitude());
                                   co.setAltitude(lco.get(cpte).getAltitude());
                                   co.setLatitude(lco.get(cpte).getLatitude());
                                   lvue.add(co);
                                   prmtr=cPos(flyTo, co, (90*i), range,prmtr, 1);
                                add = lPrmtr.add(
                                                     prmtr
                                                     );

                                createAndAddWait = playlist.createAndAddWait();
                                   createAndAddWait.setDuration(2);
                           }
                             prmtr=new Prmtr();
                                flyTo = playlist.createAndAddFlyTo();
                               flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                               flyTo.setDuration(4);prmtr.setDura(4d);

                                   cpte=0;
                                   Coordinate rechNexCoord = lco.get((lco.size()/4)*3);
                                       range = calculDistance(lco.get(cpte).getLatitude(), rechNexCoord.getLatitude(),
                                                               lco.get(cpte).getLongitude(), rechNexCoord.getLongitude(),
                                                               lco.get(cpte).getAltitude(), rechNexCoord.getAltitude())
                                               *3;
                                   co.setLongitude(lco.get(cpte).getLongitude());
                                   co.setAltitude(lco.get(cpte).getAltitude());
                                   co.setLatitude(lco.get(cpte).getLatitude());
                                   lvue.add(co);
                                   prmtr=cPos(flyTo, co, (90*3), range,prmtr, 1);
                                add = lPrmtr.add(
                                                     prmtr
                                                     );
                                
                                co=new Coordinate((lco.get(0).getLongitude()+lco.get(lco.size()/2).getLongitude())/2, (lco.get(0).getLatitude()+lco.get(lco.size()/2).getLatitude())/2, 0.00);           
                            range=1000;
                                 flyTo = playlist.createAndAddFlyTo();
                               flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                               flyTo.setDuration(2);prmtr.setDura(2d);

                                   
                                   lvue.add(co);
                                   prmtr=cPos(flyTo, co, lHead.get(0), range,prmtr, 1);
                                add = lPrmtr.add(
                                                     prmtr
                                                     );

                                
         }else 
        if(geo instanceof LineString)
        {
           
           
            ls= (LineString) geo;
            ArrayList<Coordinate> coors = (ArrayList<Coordinate>) ls.getCoordinates();
             List<Double> lpente = chchElv(coors);
            double range = calculDistance(coors.get(0).getLatitude(), coors.get(coors.size()-1).getLatitude(),
                                                coors.get(0).getLongitude(), coors.get(coors.size()-1).getLongitude(),
                                                coors.get(0).getAltitude(), coors.get(coors.size()-1).getAltitude());
            Prmtr prmtr=new Prmtr();
            FlyTo flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(3);prmtr.setDura(3d);      
             prmtr = cPos(flyTo, coors.get(0), lHead.get(0), range*4.5,prmtr, 1);
                     lPrmtr.add(
                             prmtr
                             ); 
                     
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                       prmtr= cPos(flyTo, coors.get(0), lHead.get(0), range*1.5,prmtr, 0.7);
                   lPrmtr.add(
                           prmtr
                           );
                     
                     Wait wait = playlist.createAndAddWait();
                          wait.setDuration(2);
                          
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(4);prmtr.setDura(4d);
            prmtr= cPos(flyTo, coors.get(Math.abs(coors.size()/2)), lHead.get(Math.abs(lHead.size()/2)), range,prmtr, 0.3);
                   lPrmtr.add(
                           prmtr
                           );
              
             prmtr=new Prmtr();      
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                        prmtr=cPos(flyTo, coors.get(coors.size()-1), lHead.get(lHead.size()-1), range*1.5, prmtr, 1);
                   lPrmtr.add(
                            prmtr
                           );
        
                     wait = playlist.createAndAddWait();
                          wait.setDuration(1);
                          
             prmtr=new Prmtr();
             flyTo = playlist.createAndAddFlyTo();
                        flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                        flyTo.setDuration(2);prmtr.setDura(2d);
                       prmtr= cPos(flyTo, coors.get(coors.size()-1), -lHead.get(lHead.size()-1), range*2.5,prmtr, 0.6);
                   lPrmtr.add(     
                           prmtr
                           );
   
            
        }
        else if(geo instanceof Polygon ||geo instanceof LinearRing)
        {
            Polygon pg=(Polygon) geo;
            lr=pg.getOuterBoundaryIs().getLinearRing();
            List<Coordinate> lco = lr.getCoordinates();
            List<Coordinate> lvue = new ArrayList<Coordinate>();
             
            Coordinate co=new Coordinate(0.00, 0.00, 0.00);
            
            double range=0;
                
            int cpte;    
            for(int i=2; i>=0; i--)
            {
                
                Prmtr prmtr=new Prmtr();
                FlyTo flyTo = playlist.createAndAddFlyTo();
                flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                flyTo.setDuration(4);prmtr.setDura(4d);
                
                    cpte=(int) ((lco.size()/4)*i);
                    
                  try{
                      int nextcpte=Math.abs((lco.size()/4)*(i-1));
                      
                        range = calculDistance(lco.get(cpte).getLatitude(), lco.get((nextcpte)).getLatitude(),
                                                lco.get(cpte).getLongitude(), lco.get((nextcpte)).getLongitude(),
                                                lco.get(cpte).getAltitude(), lco.get((nextcpte)).getAltitude())
                                *3;
                  }catch(Exception ex)
                   {
                       range=1000;     
                   }
                    co.setLongitude(lco.get(cpte).getLongitude());
                    co.setAltitude(lco.get(cpte).getAltitude());
                    co.setLatitude(lco.get(cpte).getLatitude());
                    lvue.add(co);
                    prmtr=cPos(flyTo, co, (90*i), range,prmtr, 1);
                boolean add = lPrmtr.add(
                                      prmtr
                                      );
               
                Wait createAndAddWait = playlist.createAndAddWait();
                    createAndAddWait.setDuration(3);
            }
             Prmtr prmtr=new Prmtr();
                FlyTo flyTo = playlist.createAndAddFlyTo();
                flyTo.setFlyToMode(FlyToMode.SMOOTH);prmtr.setFlyMode(FlyToMode.SMOOTH.value());
                flyTo.setDuration(4);prmtr.setDura(4d);
                
                    cpte=0;
                    int nextcpte = (lco.size()/4);
                    try{
                        range = calculDistance(lco.get(cpte).getLatitude(), lco.get(nextcpte).getLatitude(),
                                                lco.get(cpte).getLongitude(), lco.get(nextcpte).getLongitude(),
                                                lco.get(cpte).getAltitude(), lco.get(nextcpte).getAltitude())
                                *3;
                        }catch(Exception ex)
                   {
                       range=1000;     
                   }
                    co.setLongitude(lco.get(cpte).getLongitude());
                    co.setAltitude(lco.get(cpte).getAltitude());
                    co.setLatitude(lco.get(cpte).getLatitude());
                    lvue.add(co);
                    prmtr=cPos(flyTo, co, (90*3), range,prmtr, 1);
                boolean add = lPrmtr.add(
                                      prmtr
                                      );
               
                Wait createAndAddWait = playlist.createAndAddWait();
                    createAndAddWait.setDuration(3);
     
        }else if(geo instanceof Point)
        {
            Point point=(Point) geo;
            Prmtr prmtr=new Prmtr();
             FlyTo flyTo = playlist.createAndAddFlyTo();
             flyTo.setFlyToMode(FlyToMode.BOUNCE);prmtr.setFlyMode(FlyToMode.BOUNCE.value());
             flyTo.setDuration(4);prmtr.setDura(4d);
                
            List<Coordinate> lco = point.getCoordinates();
            //si climat
          if(pl.getDescription()!=null)
            if(pl.getDescription().contains(lvprmtr.get(5)))
                {
                    lco.get(0).setAltitude(1500);
                }
            else if(pl.getDescription().contains(lvprmtr.get(4)))
                {
                    lco.get(0).setAltitude(3500);
                }
          else 
                {
                    lco.get(0).setAltitude(500);
                }
            
            Coordinate co=new Coordinate(lco.get(0).getLongitude(),
                    lco.get(0).getLatitude());
                    if(lco.get(0).getAltitude()!=0)
                    {
                        co.setAltitude(lco.get(0).getAltitude());
                    }
                    
                    
                latlngalt=new String[3];
                latlngalt[0]=String.valueOf(co.getLatitude());
                latlngalt[1]=String.valueOf(co.getLongitude());
                latlngalt[2]=String.valueOf(9000);
                prmtr=cPos(flyTo, co,180- (90), 18000,prmtr, 0.75);
            lPrmtr.add(    
                prmtr
                );
                Wait createAndAddWait = playlist.createAndAddWait();
                    createAndAddWait.setDuration(3);
        }else
            System.out.println("no found this \n"+geo.toString());
         }else
            System.out.println("no found this \n"+geo.toString()); 
         }catch(Exception ex)
         {
             Xcption.Xcption("TourRsns cplaceMark() Exception",ex.getMessage());
         }
    
        tour.setParametreTour(lPrmtr);
       
        return tour;
    }
    private Coordinate rechNexCoord(List<Coordinate> lco, int cpte)
    {
        int rcnext=1;
        Coordinate cob=lco.get(cpte);
        Coordinate coap=lco.get(cpte+rcnext);
       
        try{
            while(rcnext!=0)
                if(cob.getLatitude()==coap.getLatitude()
                        ||cob.getLongitude()==coap.getLongitude())
                {
                    rcnext++;
                    coap=lco.get(cpte+rcnext);
                }else
                {
                    rcnext=0;
                }
        }catch(Exception ex)
        {
            
        }
            
        return coap;
    }
    private double calculDistance(double lat1, double lat2, double lng1, double lng2, double alt1, double alt2)
    {
        //rayon de la terre
	int r = 6366;
        double dist=0;
         lat1= Math.toRadians(lat1);
         lat2= Math.toRadians(lat2);
         lng1= Math.toRadians(lng1);
         lng2= Math.toRadians(lng2);
        
            //calcul précis
		dist= 2 * Math.asin(Math.sqrt(Math.pow (Math.sin((lat1-lat2)/2) , 2) + Math.cos(lat1)*Math.cos(lat2)* Math.pow( Math.sin((lng1-lng2)/2) , 2)));
 
		//sortie en m
		dist = dist * r*1000;
                
                if(alt1!=0)
                {
                       
                    dist= Math.sqrt(Math.pow(dist,2)+Math.pow(alt2-alt1,2));
                }
               /* System.err.print("calcul distance lat1:"+lat1+"lat2:"+lat2+" lng1:"+lng1+" lng2:"+lng2);
                System.out.println("dist :"+dist);*/
        return dist;
    }
    public Prmtr cPos(FlyTo flyTo,Coordinate co, double head, double range, Prmtr prmtr, double tilt)
    {
        Coord coord=new Coord();
        LookAt lookAt = flyTo.createAndSetLookAt();
        
                            lookAt.setTilt(100-85*tilt);prmtr.setTilt(100-85*tilt);
                            lookAt.setLatitude(co.getLatitude());prmtr.setLatitude(co.getLatitude());
                            lookAt.setLongitude(co.getLongitude());prmtr.setLongitude(co.getLongitude());
                            
                  
                            Optional.ofNullable(co.getAltitude()).map(alti->{lookAt.setAltitude(alti);coord.setAlt(alti);return alti;});
                                
                 
                  
                            
                            lookAt.setRange(range);prmtr.setRange(range);
                            lookAt.setHeading(head);prmtr.setHead(head);
                            lookAt.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);prmtr.setAltMode(AltitudeMode.RELATIVE_TO_GROUND.value());
        
        return prmtr;
    }
    private Coordinate bfr;
    public Double cherchHead( Coordinate aft)
    {
        double hd=0; 
        if(bfr==null)
            bfr=aft;
        else
        {
            double base=90;
        
        double dflg=aft.getLongitude()-bfr.getLongitude();
        double dflat=aft.getLatitude()-bfr.getLatitude();
       // System.err.print("*****cherchHead******");System.out.print(bfr);System.err.println("*****cherchHead*******");
        if(dflg>0)  
        {
           
            //vers 90
            if(dflat>0)
            {
                //baisse
                
                    hd=base-Math.atan(dflg/dflat);
                
            }
            else if(dflat<0)
            {
                //baisse
                
                    hd=base+Math.atan(dflg/dflat);
                
            }
            else
            {
                hd=base;
            }
        }
        else if(dflg<0)      
        {
            //vers -90
            if(dflat>0)
            {
                
                    hd=-base-Math.atan(dflg/dflat);
                
            }
            else if(dflat<0)
            {
                
                    hd=-base+Math.atan(dflg/dflat);;
                
            }
            else
            {
                hd=-base;
            }
        }
        else 
        {
            hd=0;
        }
        bfr=aft;
        }
        
      
           return hd;
      
    }
    
    public List<Double> chchElv(List<Coordinate> coors)
    {
        List<Double> lpente=new ArrayList();

        try{
		//liste de rotation camera
             lHead=new ArrayList();
             
             //pour tout point elevation
             double cherchHead;
         
            lHead.addAll(
                    coors.stream().map(this::cherchHead).
                            collect(Collectors.toList()));
            if(coors.get(0).getAltitude()!=0.0d)
                for(int cpte=0; cpte<coors.size()-2; cpte++)
                {
                    /*cherchHead=cherchHead(coors.get(cpte),coors.get(cpte+1));
                    lHead.add(cherchHead);*/
                    //if( coors.get(cpte).getAltitude()!=0.0d)
                        lpente.add(coors.get(cpte+1).getAltitude()-coors.get(cpte).getAltitude());




                }
          
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }finally{
            
        }
        
        
        return lpente;
        
    }

    public Kml getMarshKml() {
        return marshKml;
    }

    public void setMarshKml(Kml marshKml) {
        this.marshKml = marshKml;
    }

    public Feature getFeat() {
        return feat;
    }

    public void setFeat(Feature feat) {
        this.feat = feat;
    }

    public ArrayList getlHead() {
        return lHead;
    }

    public void setlHead(ArrayList lHead) {
        this.lHead = lHead;
    }

    public boolean isBiti() {
        return biti;
    }

    public void setBiti(boolean biti) {
        this.biti = biti;
    }

    public Tour getrTour() {
        return rTour;
    }

    public void setrTour(Tour rTour) {
        this.rTour = rTour;
    }

    public String[] getLatlngalt() {
        return latlngalt;
    }

    public void setLatlngalt(String[] latlngalt) {
        this.latlngalt = latlngalt;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public ArrayList<Tour> getLbddtour() {
        return lbddtour;
    }

    public void setLbddtour(ArrayList<Tour> lbddtour) {
        this.lbddtour = lbddtour;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public void setSchema(PrmtrSchm schema) {
        this.schema = schema;
    }

    
}
